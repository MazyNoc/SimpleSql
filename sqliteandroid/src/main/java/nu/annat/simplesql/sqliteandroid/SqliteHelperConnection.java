package nu.annat.simplesql.sqliteandroid;

import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import nu.annat.simplesql.HelperConnection;
import nu.annat.simplesql.HelperStatement;

public class SqliteHelperConnection implements HelperConnection {

    private final SQLiteDatabase database;

    public SqliteHelperConnection(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public HelperStatement prepareStatement(String sql, int totalNumberOfParameters) throws SQLException {
        return new SqliteHelperStatement(database, sql, totalNumberOfParameters);
    }

    @Override
    public void close() {

    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

}
