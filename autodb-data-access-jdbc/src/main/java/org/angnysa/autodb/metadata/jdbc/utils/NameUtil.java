package org.angnysa.autodb.metadata.jdbc.utils;


import lombok.NonNull;

public class NameUtil {

    public static String generateFullyQualifiedName(String... parts) {
        return generateFullyQualifiedName('.', parts);
    }

    public static String generateFullyQualifiedName(char separator, @NonNull String... parts) {

        StringBuilder sb = new StringBuilder();
        for (String part : parts) {

            if (! isEmpty(part)) {
                if (sb.length() > 0) {
                    sb.append(separator);
                }
                sb.append(part);
            }
        }

        return sb.toString();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
