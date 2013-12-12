package ak.expensemanager.db;

import ak.expensemanager.debug.IDebugTag.Months;


public class MonthlyExpense {

	Months month;
	int    totalExpense;
	
	public MonthlyExpense(Months month){
		this.month = month;
		totalExpense = 0;
	}
	
	public Months getMonth() {
		return month;
	}
	public void setMonth(Months month) {
		this.month = month;
	}
	public int getTotalExpense() {
		return totalExpense;
	}
	public void setTotalExpense(int totalExpense) {
		this.totalExpense = totalExpense;
	}
	
	public void addTotal(int exp){
		totalExpense = totalExpense +exp;
	}
	

}
