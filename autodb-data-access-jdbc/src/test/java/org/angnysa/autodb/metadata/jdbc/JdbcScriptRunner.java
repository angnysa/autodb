package org.angnysa.autodb.metadata.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stolen from org.apache.ibatis.jdbc.ScriptRunner
 */
@Log4j2
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JdbcScriptRunner {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final String DEFAULT_DELIMITER = ";";

    private static final Pattern DELIMITER_PATTERN = Pattern.compile("^\\s*((--)|(//))?\\s*(//)?\\s*@DELIMITER\\s+([^\\s]+)", Pattern.CASE_INSENSITIVE);

    private final Connection connection;

    private boolean stopOnError = true;
    private boolean throwWarning = false;
    private boolean sendFullScript = false;
    private boolean removeCRs = false;
    private boolean escapeProcessing = true;
    private String delimiter = DEFAULT_DELIMITER;
    private boolean fullLineDelimiter;

    public boolean runScript(Reader reader) throws SQLException, IOException {
        log.debug("Using database: {} {}, driver: {} {}, default catalog: '{}', default schema: '{}'",
                connection.getMetaData().getDatabaseProductName(),
                connection.getMetaData().getDatabaseProductVersion(),
                connection.getMetaData().getDriverName(),
                connection.getMetaData().getDriverVersion(),
                connection.getCatalog(),
                connection.getSchema());

        if (sendFullScript) {
            return executeFullScript(reader);
        } else {
            return executeLineByLine(reader);
        }
    }

    private boolean executeFullScript(Reader reader) throws SQLException, IOException {
        StringBuilder script = new StringBuilder();
        BufferedReader lineReader = new BufferedReader(reader);
        String line;
        while ((line = lineReader.readLine()) != null) {
            script.append(line);
            script.append(LINE_SEPARATOR);
        }
        String command = script.toString();
        log.debug(command);
        return executeStatement(command);
    }

    private boolean executeLineByLine(Reader reader) throws SQLException, IOException {
        boolean ret = true;

        StringBuilder command = new StringBuilder();
        BufferedReader lineReader = new BufferedReader(reader);
        String line;
        while ((line = lineReader.readLine()) != null) {
            Boolean lret = handleLine(command, line);
            if (lret != null) {
                ret = ret && lret;
            }
        }

        checkForMissingLineTerminator(command);

        return ret;
    }

    private void checkForMissingLineTerminator(StringBuilder command) throws SQLException {
        if (command != null && command.toString().trim().length() > 0) {
            throw new SQLException("Line missing end-of-line terminator (" + delimiter + ") => " + command);
        }
    }

    private Boolean handleLine(StringBuilder command, String line) throws SQLException {
        Boolean ret = null;

        String trimmedLine = line.trim();
        if (lineIsComment(trimmedLine)) {
            Matcher matcher = DELIMITER_PATTERN.matcher(trimmedLine);
            if (matcher.find()) {
                delimiter = matcher.group(5);
            }
            log.debug(trimmedLine);
        } else if (commandReadyToExecute(trimmedLine)) {
            command.append(line, 0, line.lastIndexOf(delimiter));
            command.append(LINE_SEPARATOR);
            log.debug(command);
            ret = executeStatement(command.toString());
            command.setLength(0);
        } else if (trimmedLine.length() > 0) {
            command.append(line);
            command.append(LINE_SEPARATOR);
        }

        return ret;
    }

    private boolean lineIsComment(String trimmedLine) {
        return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
    }

    private boolean commandReadyToExecute(String trimmedLine) {
        // issue #561 remove anything after the delimiter
        return !fullLineDelimiter && trimmedLine.contains(delimiter) || fullLineDelimiter && trimmedLine.equals(delimiter);
    }

    private boolean executeStatement(String command) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.setEscapeProcessing(escapeProcessing);
            String sql = command;
            if (removeCRs) {
                sql = sql.replace("\r\n", "\n");
            }
            try {
                boolean hasResults = statement.execute(sql);
                while (!(!hasResults && statement.getUpdateCount() == -1)) {
                    checkWarnings(statement);
                    debugResults(statement, hasResults);
                    hasResults = statement.getMoreResults();
                }

                return true;
            } catch (SQLException e) {
                log.error(String.format("Error executing: %s", command), e);
                if (stopOnError) {
                    throw e;
                }

                return false;
            }
        }
    }

    private void checkWarnings(Statement statement) throws SQLException {
        if (!throwWarning) {
            return;
        }
        // In Oracle, CREATE PROCEDURE, FUNCTION, etc. returns warning
        // instead of throwing exception if there is compilation error.
        SQLWarning warning = statement.getWarnings();
        if (warning != null) {
            throw warning;
        }
    }

    private void debugResults(Statement statement, boolean hasResults) throws SQLException {

        if (log.isDebugEnabled()) {
            if (hasResults) {

                StringBuilder msg = new StringBuilder("Results:\n");

                try (ResultSet rs = statement.getResultSet()) {
                    ResultSetMetaData md = rs.getMetaData();
                    int cols = md.getColumnCount();
                    for (int i = 1; i <= cols; i++) {
                        String name = md.getColumnLabel(i);
                        msg.append(name).append("\t");
                    }

                    msg.append("\n");

                    while (rs.next()) {
                        for (int i = 1; i <= cols; i++) {
                            String value = rs.getString(i);
                            msg.append(value).append("\t");
                        }
                        msg.append("\n");
                    }
                }

                log.debug(msg.toString());
            }
        }
    }
}
