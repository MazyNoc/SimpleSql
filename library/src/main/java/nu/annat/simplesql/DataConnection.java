package nu.annat.simplesql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataConnection {

	private MySqlConnection conn;

	private List<DatabaseListener> listeners = new ArrayList<DatabaseListener>();

	Map<String, Property> properties = null;

	public DataConnection(DatabaseListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public boolean openDatabase(String connectionString, String prefix, String username, String password, int version) {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			conn = new MySqlConnection( DriverManager.getConnection(connectionString, username, password) );

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		updatePropertyList(prefix);

		// get the property

		if (properties == null || properties.get("version") == null) {
			createDefaultProperties(version, prefix);
			fireCreateDatabase();

		}

		Property prop = properties.get("version");

		if (prop.asInt() < version) {
			fireUpdateDatabase(prefix, prop.asInt(), version);
		}

		return true;

	}

	private void createDefaultProperties(int version, String prefix) {

		try {
			String sql = "CREATE TABLE " + prefix + "baseproperties (name VARCHAR(50) NOT NULL ,value VARCHAR(200) NULL , PRIMARY KEY (name) );";
			conn.prepareStatement(sql,0).execute();
		} catch (SQLException e) {
			// silent exception
		}

		DataHelper dh = new DataHelper(conn);

		String sql2 = "INSERT INTO " + prefix + "baseproperties values( :name, :value )";
		MappedParameters mappedParameters = new MappedParameters();
		mappedParameters.add("name", "version", Types.VARCHAR);
		mappedParameters.add("value", String.valueOf(version), Types.VARCHAR);
		dh.updateQuery(sql2, mappedParameters);

	}

	private void updatePropertyList(String prefix) {
		// get the versionnumber
		String sql = "Select * from " + prefix + "baseproperties;";

		// properties = dataHelper.queryArrayListAuto(sql, Property.class,
		// null);

		DataHelper dataHelper = new DataHelper(conn);
		
		properties = dataHelper.queryMap(sql, new MapMapper<String, Property>() {

			@Override
			public void row(HelperResultSet rs, Map<String, Property> map) throws SQLException {
				Property property = new Property();
				property.setName(rs.getString(rs.getIndexOf( "name")));
				property.setValue(rs.getString(rs.getIndexOf("value")));
				map.put(property.getName(), property);
			}

		}, null);

	}

	private void updateProperty(String prefix, String name, String value){
		String sql =  "update " + prefix + "baseproperties set value = :value where name = :name";
		DataHelper dataHelper = new DataHelper(conn);

		MappedParameters params = new MappedParameters();
		params.add("name", name, Types.VARCHAR);
		params.add("value", value, Types.VARCHAR);

		dataHelper.updateQuery(sql, params);
	}
	
	private void fireUpdateDatabase(String prefix, int oldversion, int newversion) {
		try {
			for (DatabaseListener listener : listeners) {
				listener.updateDatabase(getConnection(), oldversion, newversion);
			}
			
			updateProperty(prefix, "version", String.valueOf(newversion));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void fireCreateDatabase() {
		for (DatabaseListener listener : listeners) {
			listener.createDatabase(getConnection());
		}
	}

	public HelperConnection getConnection() {
		return conn;
	}

}
