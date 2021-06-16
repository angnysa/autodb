package org.angnysa.autodb.metadata.api;

import java.util.List;
import java.util.Map;

public interface Index {
    enum Coverage {
        /**
         * The index must begin with a sub-array of the given columns.
         */
        CanCoverLess,

        /**
         * The index must cover exactly the given columns in the given order.
         */
        MustCoverExact,

        /**
         * The index must begin with the requested columns, but can cover more.
         */
        CanCoverMore
    }

    Table getTable();
    String getName();
    List<? extends Column> getColumns();
    List<String> getColumnsName();
    Column getColumn(int index);
    Column getColumn(String columnName);
    boolean covers(Coverage coverage, List<String> columnNames);
}
