package nu.annat.simplesql.android.simplesql;

import java.sql.SQLException;

public class HelperStatement {


	protected final String sql;
	protected final int totalNumberOfParameters;

	
	public HelperStatement(String sql, int totalNumberOfParameters) throws SQLException {
		this.sql = sql;
		this.totalNumberOfParameters = totalNumberOfParameters;
	}
	
	public void setObject(int position, Object object, int type) throws SQLException{
		throw new SQLException("Not implemented");
	}

	public void execute() throws SQLException{
		throw new SQLException("Not implemented");
	}

	public Long executeInsertAutoKey() throws SQLException{
		throw new SQLException("Not implemented");
	}

	public int executeUpdate() throws SQLException{
		throw new SQLException("Not implemented");
	}

	public HelperResultSet executeQuery() throws SQLException{
		throw new SQLException("Not implemented");
	}

	public HelperResultSet executeQuery(String string) throws SQLException{
		throw new SQLException("Not implemented");
	}

	public void close() throws SQLException{
		throw new SQLException("Not implemented");
	}


}
