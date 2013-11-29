package ak.expensemanager.model;

public class Category {
	private String mName;
	private int mLimit;
	
	public Category(String name, int limit) {
		mName = name;
		mLimit = limit;
	}
	
	public String getName() {
		return mName;
	}
	
	public int getLimit() {
		return mLimit;
	}
	
	// TODO: write equals and hashcode
	
}
