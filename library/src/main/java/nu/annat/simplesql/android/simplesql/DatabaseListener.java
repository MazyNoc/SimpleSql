package nu.annat.simplesql.android.simplesql;

import java.sql.SQLException;

public interface DatabaseListener {

	public void createDatabase(HelperConnection conn);
	
	public void updateDatabase(HelperConnection conn, int oldversion, int newversion) throws SQLException;
	
}
