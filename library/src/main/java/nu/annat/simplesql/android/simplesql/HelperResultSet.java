package nu.annat.simplesql.android.simplesql;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public interface HelperResultSet {

	boolean next();

	void close();

	HelperColumnMapping getColumnMapping() throws SQLException;

	int getIndexOf(String columnName);

    /**
     * Different cursor implementations uses different base number for their columns.
     * this function makes sure we can handle both of them.
     * @return 0 for zero based cursors, or 1 for one based cursors
     */
	int getFirstColumn();

	int getColumnCount();
	
	String getString(int i);

	Integer getIntObject(int i);

	int getInt(int i);
	
	int getInt(int i, int whenNull);
	
	Long getLongObject(int i);

	long getLong(int i);

	long getLong(int i, long whenNull);
	
	Timestamp getTimestamp(int i);

	Double getDoubleObj(int i);
	
	double getDouble(int i);
	
	double getDouble(int i, double whenNull);
	
	Float getFloatObj(int i);
	
	float getFloat(int i);
	
	float getFloat(int i, float whenNull);

	Boolean getBooleanObj(int i);
	
	boolean getBoolean(int i, boolean whenNull);

	Date getDate(int i);

	boolean wasNull();

	
}
