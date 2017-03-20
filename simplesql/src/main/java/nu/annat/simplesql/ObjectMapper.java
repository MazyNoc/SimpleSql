package nu.annat.simplesql;

import java.sql.SQLException;

public abstract class ObjectMapper<T> {

    private static final String TAG = ObjectMapper.class.getSimpleName();
    protected int initializedFieldCount = -1;
    public static boolean DEBUG = false;

    public final void init(HelperResultSet rs) throws SQLException {
        if (initializedFieldCount == rs.getColumnCount()) {
            return;
        }
        initializedFieldCount = prepareIndex(rs);
        if (DEBUG && initializedFieldCount != rs.getColumnCount())
            throw new RuntimeException("Not all fields was initialized");

    }

    protected abstract int prepareIndex(HelperResultSet rs) throws SQLException;

    public T doMapObject(HelperResultSet rs) throws SQLException {
        return mapObject(rs);
    }

    public abstract T mapObject(HelperResultSet rs) throws SQLException;

}
