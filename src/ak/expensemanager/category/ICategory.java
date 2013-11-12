package ak.expensemanager.category;

import java.util.Map;

public interface ICategory {
	/**
	 * Add this Category name and number in db.
	  * */
	boolean addCategory(String name);
	
	
	
	boolean searchCatetory(String name);
	
	/**
	 * Remove this category info from
	 * contact list
	 * */
	boolean removeCategory(String number);
	
	/**
	 * Get all the category stored in the db
	 * */
	Map<String , ? > getAllCategory();
	


}
