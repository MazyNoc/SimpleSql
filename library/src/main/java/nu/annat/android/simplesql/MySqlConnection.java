package nu.annat.android.simplesql;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnection implements HelperConnection{

	private final Connection connection;

	public MySqlConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public HelperStatement prepareStatement(String sql, int totalNumberOfParams) throws SQLException{
		return null;
		//return connection.prepareStatement(sql);
	}

}
