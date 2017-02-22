package nu.annat.simplesql;

import java.util.ArrayList;
import java.util.HashMap;

public class HelperColumnMapping extends ArrayList<HelperColumnMapping.ColumnMap> {

	private static final long serialVersionUID = -8942553122546520217L;

	public static class ColumnMap {
		public String name;
		public Integer index;
	}

	// private HashMap<Integer, String> itos = new HashMap<Integer, String>();
	private HashMap<String, Integer> stoi = new HashMap<String, Integer>();

	@Override
	public boolean add(ColumnMap e) {
		// itos.put(e.index, e.name);
		stoi.put(e.name, e.index);
		return super.add(e);
	}

	public Integer getIndexByName(String name) {
		return stoi.get(name);
	}

	public int getIdxByName(String name) {
		Integer integer = stoi.get(name);
		if (integer == null) {
            if(BuildConfig.DEBUG || true)
                throw new RuntimeException("Cant find " + name);
            else
			    return -1;
		} else {
			return integer.intValue();
		}

	}

}
