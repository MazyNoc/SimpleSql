package nu.annat.android.simplesql.mysql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nu.annat.android.simplesql.HelperResultSet;
import nu.annat.android.simplesql.HelperStatement;

public class MySqlHelperStatement extends HelperStatement {


    private final Connection connection;
    private String[] params = null;
    private int[] types = null;
    private PreparedStatement statement;

    public MySqlHelperStatement(Connection connection, String sql, int totalNumberOfParameters) throws SQLException {
        super(sql, totalNumberOfParameters);

        this.connection = connection;

        statement = connection.prepareStatement(sql);


//
//        if (totalNumberOfParameters > 0) {
//            params = new String[totalNumberOfParameters];
//            types = new int[totalNumberOfParameters];
//        }
    }

    @Override
    public void setObject(int position, Object object, int type) throws SQLException {

        statement.setObject(position, object, type);
    }

    @Override
    public void execute() throws SQLException {
        statement.executeQuery();
        super.execute();
    }

    @Override
    public Long executeInsertAutoKey() throws SQLException {

        statement.executeQuery();
        ResultSet tableKeys = statement.getGeneratedKeys();
        tableKeys.next();
        return tableKeys.getLong(1);

    }

    @Override
    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }

    @Override
    public HelperResultSet executeQuery() throws SQLException {
        ResultSet resultSet = statement.executeQuery();
//        log.info("resultset " + resultSet.toString());
//        resultSet.last();
//        log.info("resultset size " + resultSet.getRow());
//
//
//        ResultSetMetaData metaData = resultSet.getMetaData();
//
//        for (int i = 1; i < metaData.getColumnCount(); i++) {
//            log.info("column " + i + " = " + metaData.getColumnName(i));
//        }
//
//        resultSet.beforeFirst();
        return new MySqlResultSet(resultSet);
    }

    @Override
    public HelperResultSet executeQuery(String string) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery(string);
        return new MySqlResultSet(resultSet);
    }

    @Override
    public void close() throws SQLException {
        // void..
    }

}
