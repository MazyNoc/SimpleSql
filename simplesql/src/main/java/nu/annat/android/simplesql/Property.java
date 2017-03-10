package nu.annat.android.simplesql;

public class Property {

	private String name = "";
	private String value = "";
	
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String asString(){
		return value;
	}
	
	public boolean asBoolean(){
		return Boolean.parseBoolean(value);
	}

	public int asInt(){
		return Integer.parseInt(value);
	}

}
