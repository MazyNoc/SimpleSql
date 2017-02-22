package nu.annat.simplesql.android.simplesql;

import java.sql.SQLException;

public abstract class PlainMapper {
	private boolean stopped=false;

	public boolean isStopped() {
		return stopped;
	}
	public void stop() {
		this.stopped = true;
	}
	
	public void init(HelperResultSet rs) throws SQLException{};
	
	public void doRow(HelperResultSet rs) throws SQLException{
		row(rs);
	}
	public abstract void row(HelperResultSet rs) throws SQLException;

}
