package ak.expensemanager.model;

import java.util.Calendar;
import java.util.Date;

public class Expense {

	private int mAmount;
	private Category mCategory;
	private String mNote;
	private Calendar mCalendar;
	
	public Expense(int amount, Category category, String note, Calendar cal) {
		mAmount = amount;
		mCategory = category;
		mNote = note;
		mCalendar = cal; 
	}
	
	public int getAmount() {
		return mAmount;
	}

	public Category getCategory() {
		return mCategory;
	}

	public String getNote() {
		return mNote;
	}

	public Calendar getCalendar() {
		return mCalendar;
	}
	
	// TODO: write equals() and hashcode();
}
