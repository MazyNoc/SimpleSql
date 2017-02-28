package nu.annat.android.simplesql.mysql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import nu.annat.android.simplesql.HelperColumnMapping;
import nu.annat.android.simplesql.HelperColumnMapping.ColumnMap;
import nu.annat.android.simplesql.HelperResultSet;

public class MySqlResultSet implements HelperResultSet {

    private final ResultSet resultSet;
    HelperColumnMapping mapping = null;
    private boolean wasNull = false;

    public MySqlResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public int getFirstColumn() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        try {
            return resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            return 0;
        }
    }


    @Override
    public boolean next() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HelperColumnMapping getColumnMapping() throws SQLException {
        if (mapping == null) {
            mapping = new HelperColumnMapping();

            ResultSetMetaData metaData = resultSet.getMetaData();

            for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                ColumnMap map = new ColumnMap();
                map.index = Integer.valueOf(i);
                map.name = metaData.getColumnName(i);
                mapping.add(map);
            }
        }
        return mapping;
    }

    @Override
    public int getIndexOf(String columnName) {
        try {
            return resultSet.findColumn(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public String getString(int i) {
        try {
            return resultSet.getString(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer getIntObject(int i) {
        try {
            Integer integer = Integer.valueOf(resultSet.getInt(i));
            if (!resultSet.wasNull()) return integer;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public int getInt(int i) {
        return getInt(i, 0);
    }

    @Override
    public int getInt(int i, int whenNull) {
        try {
            int result = resultSet.getInt(i);
            if (!resultSet.wasNull()) return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return whenNull;
    }

    @Override
    public Long getLongObject(int i) {
        try {
            long longObj = resultSet.getLong(i);
            if (!resultSet.wasNull()) return longObj;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public long getLong(int i) {
        return getLong(i, 0);
    }

    @Override
    public long getLong(int i, long whenNull) {
        try {
            long result = resultSet.getLong(i);
            if (!resultSet.wasNull()) return result;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return whenNull;
    }

    @Override
    public Timestamp getTimestamp(int i) {
        try {
            return resultSet.getTimestamp(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Double getDoubleObj(int i) {
        try {
            double result = resultSet.getDouble(i);
            if (!resultSet.wasNull()) return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double getDouble(int i) {
        return getDouble(i, 0);
    }

    @Override
    public double getDouble(int i, double whenNull) {
        try {
            double result = resultSet.getDouble(i);
            if (!resultSet.wasNull()) return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return whenNull;
    }

    @Override
    public Float getFloatObj(int i) {
        try {
            float result = resultSet.getFloat(i);
            if (!resultSet.wasNull()) return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public float getFloat(int i) {
        return getFloat(i, 0);
    }

    @Override
    public float getFloat(int i, float whenNull) {
        try {
            float result = resultSet.getFloat(i);
            if (!resultSet.wasNull()) return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return whenNull;
    }

    @Override
    public Boolean getBooleanObj(int i) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean getBoolean(int i, boolean whenNull) {
        try {
            boolean result = resultSet.getBoolean(i);
            if (!resultSet.wasNull()) return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return whenNull;
    }

    @Override
    public Date getDate(int i) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean wasNull() {
        return wasNull;
    }

}
