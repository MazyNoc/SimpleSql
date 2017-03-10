package nu.annat.android.simplesql;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Reflectionbased mapper for pojos
 */
public class AutoMapper {

	Map<String, Method> methods= new HashMap<String, Method>();
	HelperColumnMapping mapping = null;
	
	public AutoMapper(Class<?> cls, HelperResultSet rs){
		
		Method[] methodArr = cls.getMethods();
		for(Method m : methodArr)
			if(m.getName().startsWith("set"))
				methods.put(m.getName().toLowerCase(Locale.getDefault()), m);
		
		try {
			mapping = rs.getColumnMapping();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void map(HelperResultSet rs, Object o){
		try{
			for(HelperColumnMapping.ColumnMap map: mapping){
				String s = "set"+map.name.toLowerCase(Locale.getDefault());
				int i = map.index.intValue();
				Method m = methods.get(s);
				if(m==null) continue;
				Class<?>[] mcl =  m.getParameterTypes();
				if(mcl.length==1){
					Class<?> mcls = mcl[0];
					if(mcls==String.class){
						m.invoke(o, rs.getString(i));
					} else if (mcls == Integer.class){
						m.invoke(o, rs.getIntObject(i));
					} else if (mcls == Long.class){
						m.invoke(o, rs.getLongObject(i));
					} else if (mcls == long.class){
						m.invoke(o, rs.getLongObject(i));
					} else if (mcls == Date.class){
						m.invoke(o, (Date)rs.getTimestamp(i));
					} else if (mcls == int.class){
						m.invoke(o, rs.getIntObject(i));
					} else if (mcls == float.class){
						m.invoke(o, rs.getFloatObj(i));
					} else if (mcls == Float.class){
						m.invoke(o, rs.getFloatObj(i));
					} else if (mcls == boolean.class){
						m.invoke(o, rs.getBooleanObj(i));
					} else if (mcls.getSuperclass() == Enum.class){
						m.invoke(o, Enum.valueOf((Class)mcls,rs.getString(i)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
