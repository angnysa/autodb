package org.angnysa.autodb.query.jdbc.types.jdbc40;

import org.angnysa.autodb.query.jdbc.types.JdbcTypeAccessor;
import org.angnysa.autodb.query.jdbc.types.TypeAccessor;
import org.angnysa.autodb.query.jdbc.types.jdbc40.javatime.*;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.*;

public class Jdbc40Accessors {
    // ARRAY
    private static final TypeAccessor<Long>           ACCESSOR_BIGINT               = new JdbcTypeAccessor<>(JDBCType.BIGINT        , null, Long.class        , ResultSet::getLong            , PreparedStatement::setLong            );
    private static final TypeAccessor<byte[]>         ACCESSOR_BINARY               = new JdbcTypeAccessor<>(JDBCType.BINARY        , null, byte[].class      , ResultSet::getBytes           , PreparedStatement::setBytes           );
    private static final TypeAccessor<Boolean>        ACCESSOR_BIT                  = new JdbcTypeAccessor<>(JDBCType.BIT           , null, Boolean.class     , ResultSet::getBoolean         , PreparedStatement::setBoolean         );
    private static final TypeAccessor<InputStream>    ACCESSOR_BLOB                 = new JdbcTypeAccessor<>(JDBCType.BLOB          , null, InputStream.class , ResultSet::getBinaryStream    , PreparedStatement::setBinaryStream    );
    private static final TypeAccessor<Boolean>        ACCESSOR_BOOLEAN              = new JdbcTypeAccessor<>(JDBCType.BOOLEAN       , null, Boolean.class     , ResultSet::getBoolean         , PreparedStatement::setBoolean         );
    private static final TypeAccessor<String>         ACCESSOR_CHAR                 = new JdbcTypeAccessor<>(JDBCType.CHAR          , null, String.class      , ResultSet::getString          , PreparedStatement::setString          );
    private static final TypeAccessor<Reader>         ACCESSOR_CLOB                 = new JdbcTypeAccessor<>(JDBCType.CLOB          , null, Reader.class      , ResultSet::getCharacterStream , PreparedStatement::setCharacterStream );
    // DATALINK
    private static final TypeAccessor<LocalDate>      ACCESSOR_DATE                 = new LocalDateAccessor();
    private static final TypeAccessor<BigDecimal>     ACCESSOR_DECIMAL              = new JdbcTypeAccessor<>(JDBCType.DECIMAL       , null, BigDecimal.class  , ResultSet::getBigDecimal      , PreparedStatement::setBigDecimal      );
    // DISTINCT
    private static final TypeAccessor<Double>         ACCESSOR_DOUBLE               = new JdbcTypeAccessor<>(JDBCType.DOUBLE        , null, Double.class      , ResultSet::getDouble          , PreparedStatement::setDouble          );
    private static final TypeAccessor<Float>          ACCESSOR_FLOAT                = new JdbcTypeAccessor<>(JDBCType.FLOAT         , null, Float.class       , ResultSet::getFloat           , PreparedStatement::setFloat           );
    private static final TypeAccessor<Integer>        ACCESSOR_INTEGER              = new JdbcTypeAccessor<>(JDBCType.INTEGER       , null, Integer.class     , ResultSet::getInt             , PreparedStatement::setInt             );
    // JAVA_OBJECT
    private static final TypeAccessor<String>         ACCESSOR_LONGNVARCHAR_STRING  = new JdbcTypeAccessor<>(JDBCType.LONGNVARCHAR  , null, String.class      , ResultSet::getNString         , PreparedStatement::setNString         );
    private static final TypeAccessor<Reader>         ACCESSOR_LONGNVARCHAR_READER  = new JdbcTypeAccessor<>(JDBCType.LONGNVARCHAR  , null, Reader.class      , ResultSet::getNCharacterStream, PreparedStatement::setNCharacterStream);
    private static final TypeAccessor<byte[]>         ACCESSOR_LONGVARBINARY_BYTE   = new JdbcTypeAccessor<>(JDBCType.LONGVARBINARY , null, byte[].class      , ResultSet::getBytes           , PreparedStatement::setBytes           );
    private static final TypeAccessor<InputStream>    ACCESSOR_LONGVARBINARY_STREAM = new JdbcTypeAccessor<>(JDBCType.LONGVARBINARY , null, InputStream.class , ResultSet::getBinaryStream    , PreparedStatement::setBinaryStream    );
    private static final TypeAccessor<String>         ACCESSOR_LONGVARCHAR_STRING   = new JdbcTypeAccessor<>(JDBCType.LONGVARCHAR   , null, String.class      , ResultSet::getString          , PreparedStatement::setString          );
    private static final TypeAccessor<Reader>         ACCESSOR_LONGVARCHAR_READER   = new JdbcTypeAccessor<>(JDBCType.LONGVARCHAR   , null, Reader.class      , ResultSet::getCharacterStream , PreparedStatement::setCharacterStream );
    private static final TypeAccessor<String>         ACCESSOR_NCHAR                = new JdbcTypeAccessor<>(JDBCType.NCHAR         , null, String.class      , ResultSet::getNString         , PreparedStatement::setNString         );
    private static final TypeAccessor<Reader>         ACCESSOR_NCLOB                = new JdbcTypeAccessor<>(JDBCType.NCLOB         , null, Reader.class      , ResultSet::getNCharacterStream, PreparedStatement::setNCharacterStream);
    // NULL
    private static final TypeAccessor<BigDecimal>     ACCESSOR_NUMERIC              = new JdbcTypeAccessor<>(JDBCType.NUMERIC       , null, BigDecimal.class  , ResultSet::getBigDecimal      , PreparedStatement::setBigDecimal      );
    private static final TypeAccessor<String>         ACCESSOR_NVARCHAR             = new JdbcTypeAccessor<>(JDBCType.NVARCHAR      , null, String.class      , ResultSet::getNString         , PreparedStatement::setNString         );
    // OTHER
    private static final TypeAccessor<Double>         ACCESSOR_REAL                 = new JdbcTypeAccessor<>(JDBCType.REAL          , null, Double.class      , ResultSet::getDouble          , PreparedStatement::setDouble          );
    // REF
    // REF_CURSOR
    // ROWID
    private static final TypeAccessor<Short>          ACCESSOR_SMALLINT             = new JdbcTypeAccessor<>(JDBCType.SMALLINT      , null, Short.class       , ResultSet::getShort           , PreparedStatement::setShort           );
    // SQLXML
    // STRUCT
    private static final TypeAccessor<LocalTime>      ACCESSOR_TIME                 = new LocalTimeAccessor();
    private static final TypeAccessor<OffsetTime>     ACCESSOR_TIME_WITH_TIMEZONE   = new OffsetTimeAccessor();
    private static final TypeAccessor<LocalDateTime>  ACCESSOR_TIMESTAMP            = new LocalDateTimeAccessor();
    private static final TypeAccessor<OffsetDateTime> ACCESSOR_TIMESTAMP_TZ_OFFSET  = new OffsetDateTimeAccessor();
    private static final TypeAccessor<ZonedDateTime>  ACCESSOR_TIMESTAMP_TZ_ZONED   = new ZonedDateTimeAccessor();
    private static final TypeAccessor<Instant>        ACCESSOR_TIMESTAMP_TZ_INSTANT = new InstantAccessor();
    private static final TypeAccessor<Byte>           ACCESSOR_TINYINT              = new JdbcTypeAccessor<>(JDBCType.TINYINT       , null, Byte.class        , ResultSet::getByte            , PreparedStatement::setByte            );
    private static final TypeAccessor<byte[]>         ACCESSOR_VARBINARY            = new JdbcTypeAccessor<>(JDBCType.VARBINARY     , null, byte[].class      , ResultSet::getBytes           , PreparedStatement::setBytes           );
    private static final TypeAccessor<String>         ACCESSOR_VARCHAR              = new JdbcTypeAccessor<>(JDBCType.VARCHAR       , null, String.class      , ResultSet::getString          , PreparedStatement::setString          );
}
