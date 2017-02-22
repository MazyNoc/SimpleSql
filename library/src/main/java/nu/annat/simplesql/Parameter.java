package nu.annat.simplesql;


public class Parameter {
	private final String name;
	private int type;
	private Object object;

	public Parameter(String name, Object object, int type) {
		this.name = name;
		this.object = object;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return name + ":" + object;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Parameter parameter = (Parameter) o;

		if (!name.equals(parameter.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
