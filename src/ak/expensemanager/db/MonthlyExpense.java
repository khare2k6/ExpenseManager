package ak.expensemanager.db;


public class MonthlyExpense {

	String month;
	int    totalExpense;
	
	public MonthlyExpense(String month){
		this.month = month;
		totalExpense = 0;
	}
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
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
