package org.angnysa.autodb.query.jdbc.types.jdbc41;

import org.angnysa.autodb.query.jdbc.types.TypeAccessor;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.time.*;

public class Jdbc41Accessors {
    // ARRAY
    private static final TypeAccessor<Long>           ACCESSOR_BIGINT               = new Jdbc41TypeAccessor<>(JDBCType.BIGINT                 , null, Long.class          );
    private static final TypeAccessor<byte[]>         ACCESSOR_BINARY               = new Jdbc41TypeAccessor<>(JDBCType.BINARY                 , null, byte[].class        );
    private static final TypeAccessor<Boolean>        ACCESSOR_BIT                  = new Jdbc41TypeAccessor<>(JDBCType.BIT                    , null, Boolean.class       );
    private static final TypeAccessor<InputStream>    ACCESSOR_BLOB                 = new Jdbc41TypeAccessor<>(JDBCType.BLOB                   , null, InputStream.class   );
    private static final TypeAccessor<Boolean>        ACCESSOR_BOOLEAN              = new Jdbc41TypeAccessor<>(JDBCType.BOOLEAN                , null, Boolean.class       );
    private static final TypeAccessor<String>         ACCESSOR_CHAR                 = new Jdbc41TypeAccessor<>(JDBCType.CHAR                   , null, String.class        );
    private static final TypeAccessor<Reader>         ACCESSOR_CLOB                 = new Jdbc41TypeAccessor<>(JDBCType.CLOB                   , null, Reader.class        );
    // DATALINK
    private static final TypeAccessor<LocalDate>      ACCESSOR_DATE                 = new Jdbc41TypeAccessor<>(JDBCType.DATE                   , null, LocalDate.class     );
    private static final TypeAccessor<BigDecimal>     ACCESSOR_DECIMAL              = new Jdbc41TypeAccessor<>(JDBCType.DECIMAL                , null, BigDecimal.class    );
    // DISTINCT
    private static final TypeAccessor<Double>         ACCESSOR_DOUBLE               = new Jdbc41TypeAccessor<>(JDBCType.DOUBLE                 , null, Double.class        );
    private static final TypeAccessor<Float>          ACCESSOR_FLOAT                = new Jdbc41TypeAccessor<>(JDBCType.FLOAT                  , null, Float.class         );
    private static final TypeAccessor<Integer>        ACCESSOR_INTEGER              = new Jdbc41TypeAccessor<>(JDBCType.INTEGER                , null, Integer.class       );
    // JAVA_OBJECT
    private static final TypeAccessor<String>         ACCESSOR_LONGNVARCHAR_STRING  = new Jdbc41TypeAccessor<>(JDBCType.LONGNVARCHAR           , null, String.class        );
    private static final TypeAccessor<Reader>         ACCESSOR_LONGNVARCHAR_READER  = new Jdbc41TypeAccessor<>(JDBCType.LONGNVARCHAR           , null, Reader.class        );
    private static final TypeAccessor<byte[]>         ACCESSOR_LONGVARBINARY_BYTE   = new Jdbc41TypeAccessor<>(JDBCType.LONGVARBINARY          , null, byte[].class        );
    private static final TypeAccessor<InputStream>    ACCESSOR_LONGVARBINARY_STREAM = new Jdbc41TypeAccessor<>(JDBCType.LONGVARBINARY          , null, InputStream.class   );
    private static final TypeAccessor<String>         ACCESSOR_LONGVARCHAR_STRING   = new Jdbc41TypeAccessor<>(JDBCType.LONGVARCHAR            , null, String.class        );
    private static final TypeAccessor<Reader>         ACCESSOR_LONGVARCHAR_READER   = new Jdbc41TypeAccessor<>(JDBCType.LONGVARCHAR            , null, Reader.class        );
    private static final TypeAccessor<String>         ACCESSOR_NCHAR                = new Jdbc41TypeAccessor<>(JDBCType.NCHAR                  , null, String.class        );
    private static final TypeAccessor<Reader>         ACCESSOR_NCLOB                = new Jdbc41TypeAccessor<>(JDBCType.NCLOB                  , null, Reader.class        );
    // NULL
    private static final TypeAccessor<BigDecimal>     ACCESSOR_NUMERIC              = new Jdbc41TypeAccessor<>(JDBCType.NUMERIC                , null, BigDecimal.class    );
    private static final TypeAccessor<String>         ACCESSOR_NVARCHAR             = new Jdbc41TypeAccessor<>(JDBCType.NVARCHAR               , null, String.class        );
    // OTHER
    private static final TypeAccessor<Double>         ACCESSOR_REAL                 = new Jdbc41TypeAccessor<>(JDBCType.REAL                   , null, Double.class        );
    // REF
    // REF_CURSOR
    // ROWID
    private static final TypeAccessor<Short>          ACCESSOR_SMALLINT             = new Jdbc41TypeAccessor<>(JDBCType.SMALLINT               , null, Short.class         );
    // SQLXML
    // STRUCT
    private static final TypeAccessor<LocalTime>      ACCESSOR_TIME                 = new Jdbc41TypeAccessor<>(JDBCType.TIME                   , null, LocalTime.class     );
    private static final TypeAccessor<OffsetTime>     ACCESSOR_TIME_WITH_TIMEZONE   = new Jdbc41TypeAccessor<>(JDBCType.TIME_WITH_TIMEZONE     , null, OffsetTime.class    );
    private static final TypeAccessor<LocalDateTime>  ACCESSOR_TIMESTAMP            = new Jdbc41TypeAccessor<>(JDBCType.TIMESTAMP              , null, LocalDateTime.class );
    private static final TypeAccessor<OffsetDateTime> ACCESSOR_TIMESTAMP_TZ_OFFSET  = new Jdbc41TypeAccessor<>(JDBCType.TIMESTAMP_WITH_TIMEZONE, null, OffsetDateTime.class);
    private static final TypeAccessor<ZonedDateTime>  ACCESSOR_TIMESTAMP_TZ_ZONED   = new Jdbc41TypeAccessor<>(JDBCType.TIMESTAMP_WITH_TIMEZONE, null, ZonedDateTime.class );
    private static final TypeAccessor<Instant>        ACCESSOR_TIMESTAMP_TZ_INSTANT = new Jdbc41TypeAccessor<>(JDBCType.TIMESTAMP_WITH_TIMEZONE, null, Instant.class       );
    private static final TypeAccessor<Byte>           ACCESSOR_TINYINT              = new Jdbc41TypeAccessor<>(JDBCType.TINYINT                , null, Byte.class          );
    private static final TypeAccessor<byte[]>         ACCESSOR_VARBINARY            = new Jdbc41TypeAccessor<>(JDBCType.VARBINARY              , null, byte[].class        );
    private static final TypeAccessor<String>         ACCESSOR_VARCHAR              = new Jdbc41TypeAccessor<>(JDBCType.VARCHAR                , null, String.class        );
}
