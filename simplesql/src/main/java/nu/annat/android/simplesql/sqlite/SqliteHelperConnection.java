package nu.annat.android.simplesql.sqlite;

import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import nu.annat.android.simplesql.HelperConnection;
import nu.annat.android.simplesql.HelperStatement;

public class SqliteHelperConnection implements HelperConnection {

    private final SQLiteDatabase database;

    public SqliteHelperConnection(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public HelperStatement prepareStatement(String sql, int totalNumberOfParameters) throws SQLException {
        return new SqliteHelperStatement(database, sql, totalNumberOfParameters);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

}