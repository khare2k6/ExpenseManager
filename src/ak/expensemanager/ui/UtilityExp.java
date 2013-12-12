package ak.expensemanager.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.debug.IDebugTag.Months;

abstract public class UtilityExp {
	
	/**
	 * Returns the start OR last day 
	 * of a month
	 * */
	public static long[] getMonthStartEndDate(Months month){
			if(month.equals(Months.YEARLY)) return new long[]{0,0};
			
			Calendar cal = Calendar.getInstance();
			Calendar startDateCalender = new GregorianCalendar(
					cal.get(Calendar.YEAR), month.ordinal(), 1);
			Calendar endDateCalender = new GregorianCalendar(
					(cal.get(Calendar.YEAR)), month.ordinal(),

					startDateCalender.getActualMaximum(Calendar.DAY_OF_MONTH));

			// start and end date in ms
			long startDate = startDateCalender.getTimeInMillis();
			long endDate = endDateCalender.getTimeInMillis();
			long [] monthStartAndEnd ={startDate,endDate};
			
				return monthStartAndEnd;
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
}
