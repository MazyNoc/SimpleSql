package nu.annat.simplesql;

import java.sql.Types;
import java.util.LinkedList;

public class MappedParameters extends LinkedList<Parameter> {

	private static final long serialVersionUID = 1L;

	public MappedParameters() {
		super();
	}

	public void add(String name, Object object, int type) {
		if (object == null) {
			add(new Parameter(name, object, Types.NULL));
		} else {
			add(new Parameter(name, object, type));
		}
	}

	public boolean hasParam(String name) {
		for (Parameter parameter : this) {
			if (parameter.getName().equals(name))
				return true;
		}
		return false;
	}
}
