package ak.expensemanager.category;


/**
 * To mark some of the entries in different colour in 
 * Daily view. This limit is configurable from Category menu
 * */
public interface ILimitUpdate {
	/**
	 * returns default limit if not set by user
	 * */
	int getLimit();
	
	/**
	 * sets limit defined by user
	 * */
	boolean setLimit(int limit);
}
