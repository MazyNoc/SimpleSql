package nu.annat.simplesql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class MapMapper<K,V> {
	private Map<K,V> map;
	private boolean stopped=false;

	public boolean isStopped() {
		return stopped;
	}
	public void stop() {
		this.stopped = true;
	}

	public void initResult(){
		map = new HashMap<K,V>();
	}

	public void init(HelperResultSet rs) throws SQLException{};

	public void doRow(HelperResultSet rs) throws SQLException{
		row(rs, map);
	}
	public abstract void row(HelperResultSet rs, Map<K,V> map) throws SQLException;

	public Map<K,V> getResult(){
		return map;
	}
}
