package nu.annat.simplesql;

import java.sql.SQLException;

public abstract class ObjectMapper<T> {

	private static final String TAG = ObjectMapper.class.getSimpleName();
	protected int initializedFieldCount = -1;

	public final void init(HelperResultSet rs) throws SQLException {
		if (initializedFieldCount == rs.getColumnCount()) {
			//if (BuildConfig.DEBUG || true) Log.d(TAG, "early reject");
			return;
		}
		initializedFieldCount = prepareIndex(rs);
//		if (BuildConfig.DEBUG || true) {
			if (BuildConfig.DEBUG && initializedFieldCount != rs.getColumnCount())
				throw new RuntimeException("Not all fields was initialized");

	}

	protected abstract int prepareIndex(HelperResultSet rs) throws SQLException;

	public T doMapObject(HelperResultSet rs) throws SQLException {
		return mapObject(rs);
	}

	public abstract T mapObject(HelperResultSet rs) throws SQLException;

}
