package ak.expensemanager.db;

import java.util.ArrayList;

import android.database.Cursor;



public interface IRetrieveExpenses {

	/**
	 * Returns array list expenses of all the 
	 * Months
	 * */
	ArrayList<MonthlyExpense> getAllMonthExpenses();
	
	/**
	 * Get total expense for specific months
	 * */
	int getMonthlyExpense(String month);
	
	/**
	 * Add expense to db
	 * */
	public void addExpense(long date,int amount,String category,String notes);
	
	/**
	 * returns cursor to Db
	 * Needed for adapter creation
	 * */
	public Cursor getCursorToDb();
	
	/**
	 * Returns cursor for rows belonging to 
	 * some Month's specific entries
	 * */
	public Cursor getMonthlyExp(int month);
	public Cursor geMontlyExpenseCursor(int month);
	
	/**
	 * returns cursor for expenses for that particular date
	 * */
	public Cursor getExpenseBetweenDates(long startDate,long endDate);
	
	/**
	 * To delete a particular entry
	 * */
	public void delete(long id);
	
}
