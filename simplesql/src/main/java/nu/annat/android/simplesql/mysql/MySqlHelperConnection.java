package nu.annat.android.simplesql.mysql;


import java.sql.Connection;
import java.sql.SQLException;

import nu.annat.android.simplesql.HelperConnection;
import nu.annat.android.simplesql.HelperStatement;

public class MySqlHelperConnection implements HelperConnection {

    private final Connection database;
    private final ConnectionChange listener;

    public MySqlHelperConnection(Connection database) {
        this(database, null);
    }

    public MySqlHelperConnection(Connection database, ConnectionChange listener) {
        this.database = database;
        this.listener = listener;
    }

    @Override
    public HelperStatement prepareStatement(String sql, int totalNumberOfParameters) throws SQLException {
        return new nu.annat.android.simplesql.mysql.MySqlHelperStatement(database, sql, totalNumberOfParameters);
    }

    public void close() {

        if (listener != null) {
            listener.onClose(this, database);
        } else {
            try {
                database.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getDatabase() {
        return database;
    }

    public interface ConnectionChange {
        void onClose(HelperConnection Helperconnection, Connection connection);
    }


}
