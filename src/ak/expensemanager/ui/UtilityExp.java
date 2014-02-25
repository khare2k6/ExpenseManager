package ak.expensemanager.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import ak.expensemanager.category.SettingsSharedPref;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.debug.IDebugTag.Months;
import android.content.Context;

 public class UtilityExp {
	
	/**
	 * Returns the start OR last day 
	 * of a month
	 * */
	
	public static long[] getMonthStartEndDate(Context context,Months month){
		Calendar cal = Calendar.getInstance();

		if(month.equals(Months.YEARLY)){ /*return new long[]{0,0};*/
			Calendar startDate = new GregorianCalendar(getSelectedYear(context),0,1);
			Calendar endDate = new GregorianCalendar(getSelectedYear(context),11,31);
			return new long[]{startDate.getTimeInMillis(),endDate.getTimeInMillis()};
		}
		Calendar startDateCalender = new GregorianCalendar(
				getSelectedYear(context), month.ordinal(), 1);
		Calendar endDateCalender = new GregorianCalendar(
				getSelectedYear(context), month.ordinal(),

				startDateCalender.getActualMaximum(Calendar.DAY_OF_MONTH));

		// start and end date in ms
		long startDate = startDateCalender.getTimeInMillis();
		long endDate = endDateCalender.getTimeInMillis();
		long [] monthStartAndEnd ={startDate,endDate};

		return monthStartAndEnd;
	}

	public static Integer getSelectedYear(Context context){
		//return 2014;
		SettingsSharedPref pref = new SettingsSharedPref(context);
		return pref.getSelectedYear();
	}
	/**Returns the name of the month selected*/
	public static String getMonthName(Months month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month.ordinal());
		return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
	}

	public static String  getMonth(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
		return sdf.format(new Date(date));
	}

	public static String  getDate(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat(IDebugTag.DATE_FORMAT_DD_MMM_WEEKDAY);
		return sdf.format(new Date(date));
	}
	public static Integer getCurrentYear(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.YEAR);
	}
}

