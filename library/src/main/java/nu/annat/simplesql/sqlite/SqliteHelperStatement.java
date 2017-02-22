package nu.annat.simplesql.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import nu.annat.simplesql.HelperResultSet;
import nu.annat.simplesql.HelperStatement;

public class SqliteHelperStatement extends HelperStatement {

    private final SQLiteDatabase connection;
    private String[] params = null;
    private int[] types = null;

    public SqliteHelperStatement(SQLiteDatabase connection, String sql, int totalNumberOfParameters) throws SQLException {
        super(sql, totalNumberOfParameters);

        this.connection = connection;

        if (totalNumberOfParameters > 0) {
            params = new String[totalNumberOfParameters];
            types = new int[totalNumberOfParameters];
        }
    }

    @Override
    public void setObject(int position, Object object, int type) throws SQLException {
        int realPos = position - 1;
        params[realPos] = object == null ? null : object.toString();
        types[realPos] = type;
    }

    @Override
    public void execute() throws SQLException {
        connection.execSQL(sql, params);
        super.execute();
    }

    @Override
    public Long executeInsertAutoKey() throws SQLException {
        executeUpdate();
        Cursor rs = connection.rawQuery("SELECT last_insert_rowid()", null);
        try {
            if (rs.moveToFirst()) {
                return Long.valueOf(rs.getLong(0));
            } else {
                throw new RuntimeException("cant get key");
            }
        } finally {
            rs.close();
        }

    }

    @Override
    public int executeUpdate() throws SQLException {
        if (params == null) {
            connection.execSQL(sql);
        } else {
            connection.execSQL(sql, params);
        }
        return 0;
    }

    @Override
    public HelperResultSet executeQuery() throws SQLException {
        Cursor cursor = connection.rawQuery(sql, params);
        return new SqliteResultSet(cursor);
    }

    @Override
    public HelperResultSet executeQuery(String string) throws SQLException {
        Cursor cursor = connection.rawQuery(sql, null);
        return new SqliteResultSet(cursor);
    }

    @Override
    public void close() throws SQLException {
        // void..
    }

}
