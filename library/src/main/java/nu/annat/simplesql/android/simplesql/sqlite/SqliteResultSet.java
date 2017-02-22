package nu.annat.simplesql.android.simplesql.sqlite;

import android.database.Cursor;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import nu.annat.simplesql.android.simplesql.HelperColumnMapping;
import nu.annat.simplesql.android.simplesql.HelperColumnMapping.ColumnMap;
import nu.annat.simplesql.android.simplesql.HelperResultSet;

/**
 * Sql lite implementation of the HelperResultSet.
 * This converts the sqlite and adroid specific calls to be used in a common way by the framework.
 */
public class SqliteResultSet implements HelperResultSet {

    private final Cursor cursor;
    HelperColumnMapping mapping = null;
    private boolean wasNull = false;

    public SqliteResultSet(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public boolean next() {
        return cursor.moveToNext();
    }

    @Override
    public void close() {
        cursor.close();
    }

    @Override
    public HelperColumnMapping getColumnMapping() throws SQLException {
        if (mapping == null) {
            mapping = new HelperColumnMapping();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                ColumnMap map = new ColumnMap();
                map.index = i;
                map.name = cursor.getColumnName(i);
                mapping.add(map);
            }
        }
        return mapping;
    }

    @Override
    public int getIndexOf(String columnName) {
        return cursor.getColumnIndex(columnName);

    }

    @Override
    public int getFirstColumn() {
        return 0;
    }

	@Override
	public int getColumnCount() {
		return cursor.getColumnCount();
	}

	@Override
    public String getString(int i) {
        if (cursor.isNull(i)) {
            wasNull = true;
            return null;
        } else {
            wasNull = false;
            return cursor.getString(i);
        }
    }

    @Override
    public Integer getIntObject(int i) {
        if (cursor.isNull(i)) {
            wasNull = true;
            return null;
        } else {
            wasNull = false;
            return Integer.valueOf(cursor.getInt(i));
        }
    }

    @Override
    public int getInt(int i) {
        return getInt(i,0);
    }

    @Override
    public int getInt(int i, int whenNull) {
        wasNull = false;
        try {
            return cursor.getInt(i);
        } catch (Exception e) {
            if (cursor.isNull(i)) {
                wasNull = true;
                return whenNull;
            } else {
                throw e;
            }
        }
    }

    @Override
    public Long getLongObject(int i) {
        if (cursor.isNull(i)) {
            wasNull = true;
            return null;
        } else {
            wasNull = false;
            return Long.valueOf(cursor.getLong(i));
        }
    }

    @Override
    public long getLong(int i) {
        return getLong(i, 0);
    }

    @Override
    public long getLong(int i, long whenNull) {
        wasNull = false;
        try {
            return cursor.getLong(i);
        } catch (Exception e) {
            if (cursor.isNull(i)) {
                wasNull = true;
                return whenNull;
            } else {
                throw e;
            }
        }
    }

    @Override
    public Timestamp getTimestamp(int i) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Double getDoubleObj(int i) {
        if (cursor.isNull(i)) {
            wasNull = true;
            return null;
        } else {
            wasNull = false;
            return Double.valueOf(cursor.getDouble(i));
        }
    }

    @Override
    public double getDouble(int i) {
        return getDouble(i, 0);
    }

    @Override
    public double getDouble(int i, double whenNull) {
        wasNull = false;
        try {
            return cursor.getDouble(i);
        } catch (Exception e) {
            if (cursor.isNull(i)) {
                wasNull = true;
                return whenNull;
            } else {
                throw e;
            }
        }
    }

    @Override
    public Float getFloatObj(int i) {
        if (cursor.isNull(i)) {
            wasNull = true;
            return null;
        } else {
            wasNull = false;
            return Float.valueOf(cursor.getFloat(i));
        }
    }

    @Override
    public float getFloat(int i) {
        return getFloat(i, 0);
    }

    @Override
    public float getFloat(int i, float whenNull) {
        wasNull = false;
        try {
            return cursor.getFloat(i);
        } catch (Exception e) {
            if (cursor.isNull(i)) {
                wasNull = true;
                return whenNull;
            } else {
                throw e;
            }
        }
    }

    @Override
    public Boolean getBooleanObj(int i) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean getBoolean(int i, boolean whenNull) {
        wasNull = false;
        try {
            return cursor.getString(i).startsWith("t");
        } catch (Exception e) {
            if (cursor.isNull(i)) {
                wasNull = true;
                return whenNull;
            } else {
                throw e;
            }
        }
    }

	/**
	 * 	@Override
	public boolean getBoolean(int i, boolean whenNull) {
	wasNull = false;
	if (cursor.isNull(i)) {
	wasNull = true;
	return whenNull;
	} else {
	return cursor.getString(i).startsWith("t");
	}
	}*/

    @Override
    public Date getDate(int i) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean wasNull() {
        return wasNull;
    }

}
