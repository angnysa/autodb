package org.angnysa.autodb.metadata.jdbc;

import lombok.*;
import org.angnysa.autodb.metadata.api.Column;
import org.angnysa.autodb.metadata.api.Index;
import org.angnysa.autodb.metadata.api.utils.Dumper;
import org.angnysa.autodb.metadata.jdbc.utils.ListIndexerUtil;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(exclude = "table")
public class JdbcIndex implements Index {
	private JdbcTable table;
	private final String name;
	private final List<JdbcColumn> columns;
	@Getter(AccessLevel.PRIVATE)
	private final Map<String, JdbcColumn> columnsByName;

	public JdbcIndex(String name, List<JdbcColumn> columns) {
		this.name = name;
		this.columns = List.copyOf(columns);
		columnsByName = ListIndexerUtil.index(columns, JdbcColumn::getName);
	}

	public void setTable(@NonNull JdbcTable table) {
		if (this.table == null) {
			for (JdbcColumn column : columns) {
				if (column.getTable() != table) {
					throw new IllegalStateException(String.format("Column '%s' in index '%s' is linked to table '%s', expected '%s'",
							column.getName(), getName(), column.getTable().getFullyQualifiedName(), table.getFullyQualifiedName()));
				}
			}
			this.table = table;
		} else if (this.table != table) {
			throw new IllegalStateException(String.format("Index '%s' is already linked to table '%s'", getName(), getTable().getFullyQualifiedName()));
		}
	}

	@Override
	public List<String> getColumnsName() {
		return columns.stream().map(Column::getName).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public JdbcColumn getColumn(int index) {
		return columns.get(index);
	}

	@Override
	public JdbcColumn getColumn(String columnName) {
		columnName = columnName.toLowerCase();
		if (columnsByName.containsKey(columnName)) {
			return columnsByName.get(columnName);
		} else {
			throw new IllegalArgumentException(String.format("No column '%s' in index '%s'", columnName, getName()));
		}
	}

	@Override
    public boolean covers(Coverage coverage, List<String> columnNames) {
		if (coverage == Coverage.CanCoverLess) {
			// if the index can cover less than the requested columns, it just needs to cover the first one
			return columnNames.get(0).equals(columns.get(0).getName());
		} else if (columns.size() < columnNames.size()) {
			// the first condition takes care of 'less'. The index must now cover at least the given columns.
			// the index has less columns than requested, therefore it cannot match.
			return false;
		} else if (columns.size() > columnNames.size() && coverage == Coverage.MustCoverExact) {
			// cannot match if an exact match is requested and both lists are not the same size
			return false;
		} else {
			for (int i = 0; i < columnNames.size() && i < columns.size(); i++) {
				if (!columnNames.get(i).equals(this.columns.get(i).getName())) {
					// end of match, columns remaining in both, accept only if index can cover less than requested
					return false;
				}
			}

			if (columns.size() == columnNames.size()) {
				// full match
				return true;
			} else if (columns.size() > columnNames.size()) {
				// index covers more
				return coverage == Coverage.CanCoverMore;
			} else {
				// index covers less
				return false;
			}
		}
	}

	@SneakyThrows
	@Override
	public String toString() {
		return Dumper.dumpIndex(this);
	}
}
