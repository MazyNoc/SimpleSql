package nu.annat.simplesql;

import java.util.ArrayList;
import java.util.HashMap;


public class HelperColumnMapping extends ArrayList<HelperColumnMapping.ColumnMap> {

	private static final long serialVersionUID = -8942553122546520217L;

	public static class ColumnMap {
		public String name;
		public Integer index;
	}

	private HashMap<String, Integer> stoi = new HashMap<>();

	@Override
	public boolean add(ColumnMap e) {
		stoi.put(e.name, e.index);
		return super.add(e);
	}

	public Integer getIndexByName(String name) {
		return stoi.get(name);
	}

	public int getIdxByName(String name) {
		Integer integer = stoi.get(name);
		if (integer == null) {
			return -1;
		} else {
			return integer.intValue();
		}

	}

}
