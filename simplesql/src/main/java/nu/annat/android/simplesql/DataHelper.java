package nu.annat.android.simplesql;

import android.annotation.SuppressLint;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataHelper {

	private class Cache extends LinkedHashMap<String, NamedStatement> {
		private static final long serialVersionUID = -3074676770510323482L;

		public Cache() {
			super(21, 0.75f, true);
		}

		@Override
		protected boolean removeEldestEntry(Entry<String, NamedStatement> eldest) {
			return size() > 20;
		}
	}

	private final HelperConnection connection;
	Cache cachedStatements = new Cache();

	public DataHelper(HelperConnection connection) {
		this.connection = connection;
	}

	public static void autoMapper(HelperResultSet rs, Object o) {
		Class<?> cls = o.getClass();

		AutoMapper automapper = new AutoMapper(cls, rs);
		automapper.map(rs, o);
	}

	@SuppressLint("DefaultLocale")
	public static MappedParameters automap(Object o) {
		MappedParameters params = new MappedParameters();
		Class<?> cls = o.getClass();
		Method[] methods = cls.getMethods();

		try {
			for (Method m : methods) {
				if (m.getName().startsWith("get")) {
					String name = m.getName().substring(3);
					name = name.substring(0, 1).toUpperCase() + name.substring(1);
					Class<?> param = m.getReturnType();
					int t = -1;
					if (param == Integer.class) {
						t = Types.INTEGER;
					} else if (param == Long.class) {
						t = Types.NUMERIC;
					} else if (param == String.class) {
						t = Types.VARCHAR;
					} else if (param == Float.class) {
						t = Types.FLOAT;
					} else if (param == Date.class) {
						t = Types.TIMESTAMP;
					}
					if (t >= 0) {
						params.add(name, m.invoke(o), t);
					}
				}

			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return params;
	}

	public HelperConnection getConnection() {
		return connection;
	}

	public synchronized NamedStatement getNamedStatement(String sql) {
		NamedStatement namedStatement = cachedStatements.get(sql);
		if (namedStatement == null) {
			namedStatement = new NamedStatement(connection, sql);
			cachedStatements.put(sql, namedStatement);
		}
		return namedStatement;

	}

	public <K, V> Map<K, V> queryMap(String sql, MapMapper<K, V> mapper, MappedParameters params) {
		NamedStatement stmt = getNamedStatement(sql);
		stmt.setParameters(params);
		try {
			HelperResultSet rs = execStatement(stmt.getStmt());
			mapper.initResult();
			mapper.init(rs);
			while (rs.next()) {
				try {
					mapper.doRow(rs);
					if (mapper.isStopped()) {
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
		return mapper.getResult();
	}

	public void queryRaw(String sql, RawMapper mapper, MappedParameters params) {
		NamedStatement stmt = getNamedStatement(sql);
		stmt.setParameters(params);
		try {
			HelperResultSet rs = execStatement(stmt.getStmt());
			mapper.init(rs);
			while (rs.next()) {
				try {
					mapper.doRow(rs);
					if (mapper.isStopped()) {
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}

	@SuppressWarnings("unchecked")
	public <E> List<E> queryArrayListAuto(String sql, Class<?> cls, MappedParameters params) {
		NamedStatement stmt = getNamedStatement(sql);
		stmt.setParameters(params);
		List<E> list = new ArrayList<E>();
		try {
			HelperResultSet rs = execStatement(stmt.getStmt());
			AutoMapper automapper = new AutoMapper(cls, rs);
			while (rs.next()) {
				Object obj = cls.newInstance();
				automapper.map(rs, obj);
				list.add((E) obj);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
		return list;
	}

	public <E> List<E> queryList(NamedStatement statement, final ObjectMapper<E> objectMapper, MappedParameters params) {

		ListMapper<E> mapper = new ListMapper<E>() {
			@Override
			protected ObjectMapper<E> getObjectMapper() {
				return objectMapper;
			}
		};

		return queryList(statement, mapper, params);
	}

	public <E> List<E> queryList(NamedStatement statement, ListMapper<E> mapper, MappedParameters params) {
		statement.setParameters(params);
		try {
			HelperResultSet rs = execStatement(statement.getStmt());
			mapper.initResult();
			ObjectMapper<E> objectMapper = mapper.getObjectMapper();
			if (objectMapper != null) {
				objectMapper.init(rs);
			}
			try {
				while (rs.next()) {
					mapper.doRow(rs, objectMapper);
					if (mapper.isStopped()) {
						break;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			statement.close();
		}
		return mapper.getResult();
	}

	public <E> List<E> queryList(String sql, final ObjectMapper<E> objectMapper, MappedParameters params) {
		ListMapper<E> mapper = new ListMapper<E>() {
			@Override
			protected ObjectMapper<E> getObjectMapper() {
				return objectMapper;
			}
		};
		return queryList(sql, mapper, params);
	}

	public <E> List<E> queryList(String sql, ListMapper<E> mapper, MappedParameters params) {
		NamedStatement stmt = getNamedStatement(sql);
		return queryList(stmt, mapper, params);
	}

	// public HelperResultSet queryResultSetEx(String sql, MappedParameters
	// params){
	// return new ResultSetEx(queryResultSet(sql, params));
	// }

	public void queryPlain(String sql, PlainMapper mapper, MappedParameters params) {
		NamedStatement stmt = getNamedStatement(sql);
		stmt.setParameters(params);
		try {
			HelperResultSet rs = execStatement(stmt.getStmt());
			mapper.init(rs);
			while (rs.next()) {
				try {
					mapper.doRow(rs);
					if (mapper.isStopped()) {
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}

	public HelperResultSet queryResultSet(String sql, MappedParameters params) {
		NamedStatement stmt = getNamedStatement(sql);
		stmt.setParameters(params);
		try {
			HelperResultSet rs = execStatement(stmt.getStmt());
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
		return null;
	}

	public <E> E queryForObject(String sql, ListMapper<E> mapper, MappedParameters params) {
		List<E> list = queryList(sql, mapper, params);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public <E> E queryForObject(String sql, ObjectMapper<E> mapper, MappedParameters params) {
		NamedStatement stmt = getNamedStatement(sql);
		stmt.setParameters(params);
		E result = null;
		try {
			HelperResultSet rs = execStatement(stmt.getStmt());
			mapper.init(rs);
			try {
				if (rs.next()) {
					try {
						result = mapper.doMapObject(rs);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <E> E queryForObject(String sql, Class<?> cls, MappedParameters params) {
		NamedStatement stmt = getNamedStatement(sql);
		stmt.setParameters(params);
		try {
			HelperResultSet rs = execStatement(stmt.getStmt());
			if (rs.next()) {

				if (cls == String.class) {
					return (E) rs.getString(rs.getFirstColumn());
				}

				if (cls == Long.class) {
					return (E) rs.getLongObject(rs.getFirstColumn());
				}

				Object obj = cls.newInstance();
				DataHelper.autoMapper(rs, obj);
				return (E) obj;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
		return null;
	}

	public <K, V> V queryForObject(String sql, MapMapper<K, V> mapper, K returnId, MappedParameters params) {
		Map<K, V> map = queryMap(sql, mapper, params);
		if (map.size() > 0) {
			if (null != returnId) {
				return map.get(returnId);
			}
			Iterator<V> iter = map.values().iterator();
			return iter.next();
		} else {
			return null;
		}
	}

	public Integer queryForInteger(String sql, MappedParameters params) {
		List<Integer> list = queryList(sql, new ListMapper<Integer>() {

			@Override
			protected ObjectMapper<Integer> getObjectMapper() {
				return new ObjectMapper<Integer>() {

					@Override
					protected int prepareIndex(HelperResultSet rs) throws SQLException {
						return 1;
					}

					@Override
					public Integer mapObject(HelperResultSet rs) throws SQLException {
						return rs.getIntObject(rs.getFirstColumn());
					}
				};
			}

		}, params);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	public Long queryForLong(String sql, MappedParameters params) {
		List<Long> list = queryList(sql, new ListMapper<Long>() {

			@Override
			protected ObjectMapper<Long> getObjectMapper() {
				return new ObjectMapper<Long>() {
					@Override
					protected int prepareIndex(HelperResultSet rs) throws SQLException {
						return 1;
					}

					@Override
					public Long mapObject(HelperResultSet rs) throws SQLException {
						return rs.getLongObject(rs.getFirstColumn());
					}
				};
			}


		}, params);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	public Date queryForDate(String sql, MappedParameters params) {
		List<Date> list = queryList(sql, new ListMapper<Date>() {

			@Override
			protected ObjectMapper<Date> getObjectMapper() {
				return new ObjectMapper<Date>() {
					@Override
					protected int prepareIndex(HelperResultSet rs) throws SQLException {
						return 1;
					}

					@Override
					public Date mapObject(HelperResultSet rs) throws SQLException {
						return rs.getDate(rs.getFirstColumn());
					}
				};
			}


		}, params);

		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	public int updateQuery(String sql, MappedParameters params) {
		NamedStatement ns = getNamedStatement(sql);
		try {
			ns.setParameters(params);
			return ns.executeUpdate();
		} finally {
			ns.close();
		}
	}

	public int updateQuery(NamedStatement ns, MappedParameters params) {
		try {
			ns.setParameters(params);
			return ns.executeUpdate();
		} finally {
			ns.close();
		}
	}

	public Long insertAutoKey(String sql, MappedParameters params) {
		NamedStatement ns = getNamedStatement(sql);
		try {
			ns.setParameters(params);
			return ns.executeInsertAutoKey();
		} catch (Exception e) {
			return null;
		} finally {
			ns.close();
		}
	}

	private HelperResultSet execStatement(HelperStatement stmt) {
		try {
			return stmt.executeQuery();
		} catch (Exception e) {
			// try again
			try {
				return stmt.executeQuery();
			} catch (Exception f) {
				System.out.println(f);
				return null;
			}
		}
	}

	public void taskQuery(String sql, Object object) {

	}

	public void dumpSql(String sql, MappedParameters params) {
//        if (!BuildConfig.DEBUG) {
//            return;
//        }
		List<Integer> columnWidths = new ArrayList<>();
		List<List<String>> data = new ArrayList<>();
		List<String> row;
		HelperResultSet rs = queryResultSet(sql, params);

		try {

			HelperColumnMapping columnMapping = rs.getColumnMapping();

			data.add(row = new ArrayList<>());
			for (HelperColumnMapping.ColumnMap c : columnMapping) {
				String colName = c.name;
				columnWidths.add(colName.length());
				row.add(colName);
			}

			while (rs.next()) {
				data.add(row = new ArrayList<>());
				for (HelperColumnMapping.ColumnMap c : columnMapping) {
					String colValue = rs.getString(c.index);
					if (colValue == null) colValue = "null";

					if (colValue.length() > columnWidths.get(c.index)) {
						columnWidths.set(c.index, colValue.length());
					}
					row.add(colValue);
				}
			}

			for (int i = 0; i < columnWidths.size(); i++) {
				columnWidths.set(i, columnWidths.get(i) + 4);
			}

			for (List<String> rowData : data) {
				String printRow = "";
				int i = 0;
				for (String s : rowData) {
					printRow += String.format("%1$-" + columnWidths.get(i++) + "s", s);
				}
				Log.d("sqldump", printRow);
			}

		} catch (SQLException ignore) {

		}
	}

}
