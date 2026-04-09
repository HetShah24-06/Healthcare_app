package project1;

/**
 * SQL utility class.
 * Note: escapeSQL is retained for compatibility but SQL injection prevention
 * is now handled via PreparedStatements throughout the data manager classes.
 */
class DBUtils {
    /** @deprecated Use PreparedStatement parameters instead */
    @Deprecated
    public static String escapeSQL(String input) {
        return input.replace("'", "''");
    }
}
