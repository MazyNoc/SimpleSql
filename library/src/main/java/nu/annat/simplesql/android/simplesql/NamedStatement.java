package nu.annat.simplesql.android.simplesql;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NamedStatement {
	static long cnt = 0;
	Map<String, List<Integer>> paramList = new HashMap<String, List<Integer>>();
	int totalNumberOfParameters=0;
	HelperStatement stmt = null;
	MappedParameters params = null;
	
	public NamedStatement(HelperConnection conn, String statement){
		String sql = parseSqlNames(statement);
		try {
			stmt = conn.prepareStatement(sql, totalNumberOfParameters);
			cnt++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setParameters(MappedParameters params){
		if(params==null) return;
		this.params = params;
		try {
			for(Parameter param : params){
				Collection<Integer> list = paramList.get(param.getName());
				if (null != list) {
					for (Integer i : paramList.get(param.getName())) {
						if(param.getObject()==null)
							stmt.setObject(i.intValue(), param.getObject(), Types.NULL);
						else
							stmt.setObject(i.intValue(), param.getObject(), param.getType());
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private String parseSqlNames(String statement){
		int readpos = 0;
		int index = 0;
		StringBuffer parsed = new StringBuffer();
		boolean inQuotes = false;
		while(readpos<statement.length()){
			char c = statement.charAt(readpos);
			if(c=='\'' || c=='"'){
				inQuotes = !inQuotes;
			} else if(!inQuotes){
				if(c==':' && readpos+1<statement.length() && 
						  Character.isJavaIdentifierStart(statement.charAt(readpos+1))){
					int len = readpos+2;
					while(len<statement.length() && Character.isJavaIdentifierPart(statement.charAt(len))){
						len++;
					}
					String parameter = statement.substring(readpos+1,len);
					c='?';
					readpos = len-1;
					
					List<Integer> indexList = paramList.get(parameter);
					if(indexList==null){
						indexList = new LinkedList<Integer>();
						paramList.put(parameter, indexList);
					}
					indexList.add(Integer.valueOf(++index));
					
				}
				parsed.append(c);
				readpos++;
			}
			
		}
		totalNumberOfParameters = index;
		return parsed.toString();
	}

	public void execute(){
		if(paramList.size()>0 && params==null)
			throw new RuntimeException("You must set params");
			
		try {
			getStmt().execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//close();
		}
	}

	public int executeUpdate(){
		if(paramList.size()>0 && params==null)
			throw new RuntimeException("You must set params");
		
		try {
			return getStmt().executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			//close();
		}
	}
	
	public Long executeInsertAutoKey() throws SQLException{
		if(paramList.size()>0 && params==null)
			throw new RuntimeException("You must set params");
		
		return getStmt().executeInsertAutoKey();

	}

	
	public void close(){
		try {
			getStmt().close();
			cnt--;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		stmt = null;
		super.finalize();
	}
	
	static long getcnt(){
		return cnt;
	}
	
	public HelperStatement getStmt() {
		return stmt;
	}
}
