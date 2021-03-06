package nu.annat.simplesql;

import java.sql.SQLException;

public abstract class RawMapper {
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
