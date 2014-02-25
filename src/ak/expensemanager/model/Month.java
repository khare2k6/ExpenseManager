package ak.expensemanager.model;

import ak.expensemanager.debug.IDebugTag.Months;

public class Month {
	static Months selectedMonth = Months.YEARLY;
	
	private Month(){}
	/**
	 * Sets currently selected month by user
	 * Can be called from category view or yearly
	 * view to set selected month.
	 * */
	public static void setSelectedMonth(Months month){
		selectedMonth = month;
	}
	
	/**
	 * Gets the selected month earlier set by user.
	 * Returns Yearly otherwise.
	 * */
	public static Months getSelectedMonth(){
		return selectedMonth;
	}

}
