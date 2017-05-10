package org.diff;

public class Context {
	private static final String DEFAULT_START_DELETE = "<d>";
	private static final String DEFAULT_END_DELETE = "</d>";
	
	private static final String DEFAULT_START_INSERT = "<i>";
	private static final String DEFAULT_END_INSERT = "</i>";
	
	private static final String DEFAULT_START_EQUAL = "";
	private static final String DEFAULT_END_EQUAL = "";
	
	private final DiffTypeOfDetails type;
	
	private final String startDelete;
	private final String endDelete;
	
	private final String startInsert;
	private final String endInsert;
	
	private final String startEqual;
	private final String endEqual;
	
	protected Context(DiffTypeOfDetails type, String startDelete,
			String endDelete, String startInsert, String endInsert,
			String startEqual, String endEqual) {
		
		this.type = type;
		this.startDelete = startDelete;
		this.endDelete = endDelete;
		this.startInsert = startInsert;
		this.endInsert = endInsert;
		this.startEqual = startEqual;
		this.endEqual = endEqual;
	}
	
	public static Context makeDefaultCharContext() {
		return makeNewCharContext(DEFAULT_START_DELETE, DEFAULT_END_DELETE, DEFAULT_START_INSERT, DEFAULT_END_INSERT, 
				DEFAULT_START_EQUAL, DEFAULT_END_EQUAL);
	}
	
	public static Context makeDefaultWordContext() {
		return makeNewWordContext(DEFAULT_START_DELETE, DEFAULT_END_DELETE, DEFAULT_START_INSERT, DEFAULT_END_INSERT, 
				DEFAULT_START_EQUAL, DEFAULT_END_EQUAL);
	}
	
	public DiffTypeOfDetails getType() {
		return type;
	}

	public String getStartDelete() {
		return startDelete;
	}

	public String getEndDelete() {
		return endDelete;
	}

	public String getStartInsert() {
		return startInsert;
	}

	public String getEndInsert() {
		return endInsert;
	}

	public String getStartEqual() {
		return startEqual;
	}

	public String getEndEqual() {
		return endEqual;
	}

	public static Context makeNewCharContext(String startDelete,
			String endDelete, String startInsert, String endInsert,
			String startEqual, String endEqual) {
		return makeNewContext(DiffTypeOfDetails.CHAR, startDelete, endDelete, startInsert, endInsert, startEqual, endEqual);
	}
	
	public static Context makeNewWordContext(String startDelete,
			String endDelete, String startInsert, String endInsert,
			String startEqual, String endEqual) {
		return makeNewContext(DiffTypeOfDetails.WORD, startDelete, endDelete, startInsert, endInsert, startEqual, endEqual);
	}
	
	public static Context makeNewContext(DiffTypeOfDetails type, String startDelete,
			String endDelete, String startInsert, String endInsert,
			String startEqual, String endEqual) {
		return new Context(type, startDelete, endDelete, startInsert, endInsert, startEqual, endEqual);
	}
	
	
}
