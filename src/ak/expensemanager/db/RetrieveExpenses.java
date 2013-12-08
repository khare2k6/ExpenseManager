package ak.expensemanager.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.model.Category;
import ak.expensemanager.model.Expense;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.method.MovementMethod;
import android.util.Log;

public class RetrieveExpenses implements IRetrieveExpenses {

	DbHelper dbHelper = null;
	Context context = null;
	final String TAG = IDebugTag.ankitTag
			+ RetrieveExpenses.class.getSimpleName();

	public RetrieveExpenses(Context context) {
		this.context = context;
		dbHelper = new DbHelper(context, DbHelper.DB_NAME, null,
				DbHelper.DB_VERSION);
	}

	@Override
	public ArrayList<MonthlyExpense> getAllMonthExpenses() {
		ArrayList<MonthlyExpense> list = new ArrayList<MonthlyExpense>();

		for (IDebugTag.Months month : IDebugTag.Months.values()) {
			MonthlyExpense exp = new MonthlyExpense(month.toString());
			exp.setTotalExpense(getMonthlyExpense(month.toString()));
			Log.d(TAG,
					"exp for month:" + exp.getMonth() + " = "
							+ exp.getTotalExpense());
			list.add(exp);
		}
		return list;
	}

	@Override
	public void addExpense(Expense expense) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.C_DATE, expense.getCalendar().getTimeInMillis());
		values.put(DbHelper.C_AMOUNT, expense.getAmount());
		values.put(DbHelper.C_CATEGORY, expense.getCategory().getName());
		values.put(DbHelper.C_NOTES, expense.getNote());

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long ret = db.insert(DbHelper.TABLE_NAME, null, values);
		if (ret == -1)
			Log.d(TAG, "Some error occured in inserting");
	}

	@Override
	public Cursor getCursorToDb() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db
				.query(DbHelper.TABLE_NAME, null, null, null, null, null, null);
	}
	
	public int getTotalExpenseForCategory(Category category){
		Cursor cursor = getEntriesForCategory(category);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			int totalExp =0;
			do{
				int expForThisEntry = cursor.getInt(cursor.getColumnIndex(DbHelper.C_AMOUNT));
				totalExp += expForThisEntry; 
			}while(cursor.moveToNext());
			Log.d(TAG,"Total exp for category :"+category.getName()+ "exp = "+totalExp);
			return totalExp;
		}else{
			Log.d(TAG,"No records found  for category :"+category.getName());
			return 0;
		}
			
	}
	@Override
	public Cursor getExpenseForCategories(Set<String> catSet) {
		MatrixCursor matCategoryCursor = new MatrixCursor(new String[] {DbHelper.C_ID,DbHelper.C_CATEGORY,DbHelper.C_AMOUNT});
		int entry =1;
		for(String cat : catSet){
			Cursor catCursor = getEntriesForCategory(new Category(cat, 0));
			int totalExp = 0;
			if(catCursor.getCount() >0){
			catCursor.moveToFirst();
			
			do{
				int thisRowExp = catCursor.getInt(catCursor.getColumnIndex(DbHelper.C_AMOUNT));
				totalExp += thisRowExp;
				
			}while(catCursor.moveToNext());
			}else{
				Log.d(TAG,"No record for cat"+cat);
			}
			matCategoryCursor.addRow(new Object[]{entry++,cat,totalExp});
			
		}
		return matCategoryCursor;
	}

	public Cursor getEntriesForCategory(Category category) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Log.d(TAG,"querying for "+category.getName());
	
		return db.query(DbHelper.TABLE_NAME,/*Table Name*/
				new String[] {DbHelper.C_ID,DbHelper.C_CATEGORY,DbHelper.C_AMOUNT},/*Coloum name*/
				DbHelper.C_CATEGORY+" = ? ",/*selection*/
				new String[]{category.getName()},/*selection arg*/
				null, null, null);
		
	}

	@Override
	public int getMonthlyExpense(String month) {
		Log.d(TAG, "getMonthlyExpense for " + month);
		int totalExp = 0;

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(DbHelper.TABLE_NAME, null, null, null, null,
				null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				long dateInMs = cursor.getLong(cursor
						.getColumnIndex(DbHelper.C_DATE));
				String thisRecordsMonth = getMonth(dateInMs);
				Log.d(TAG, "this entry's month=" + thisRecordsMonth);
				if (month.equalsIgnoreCase(thisRecordsMonth)) {
					int thisRecordExpense = cursor.getInt(cursor
							.getColumnIndex(DbHelper.C_AMOUNT));
					/**if its ATM withdrawal then ignore*/
					if(isAtmTransaction(cursor)){
						Log.d(TAG,"SKipping entry for amt:"+thisRecordExpense);
						continue;
					}
					totalExp = totalExp + thisRecordExpense;
				}

			} while (cursor.moveToNext());
			Log.d(TAG, "total exp for " + month + "= " + totalExp);
			return totalExp;
		}

		return 0;
	}

	String getMonth(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
		return sdf.format(new Date(date));
	}

	String getDate(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd,MMM");
		return sdf.format(new Date(date));
	}

	/**
	 * DB helper class
	 * */
	public class DbHelper extends SQLiteOpenHelper {

		public static final String DB_NAME = "expenseManager";
		public static final String TABLE_NAME = "expense";
		public static final int DB_VERSION = 2;

		// Column details
		public static final String C_ID = BaseColumns._ID;
		public static final String C_DATE = "date";
		public static final String C_AMOUNT = "amount";
		public static final String C_CATEGORY = "category";
		public static final String C_NOTES = "notes";

		public final String TAG = IDebugTag.ankitTag
				+ DbHelper.class.getSimpleName();

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			Log.d(TAG, "DB helper constructor");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "onCreate");
			String sql = "create table " + TABLE_NAME + " ( " + C_ID
					+ " integer primary key autoincrement, " + C_DATE + " int,"
					+ C_AMOUNT + ", int, " + C_CATEGORY + " text, " + C_NOTES
					+ " text)";
			db.execSQL(sql);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String sql = "drop table if exists " + TABLE_NAME;
			db.execSQL(sql);
			onCreate(db);
		}

	}
	
	/**
	 * Returns if category of current transaction is ATM 
	 * withdrawl
	 * */
	boolean isAtmTransaction(Cursor cursor){
		String category = cursor.getString(cursor.getColumnIndex(DbHelper.C_CATEGORY));
		return category.equalsIgnoreCase(IDebugTag.ATM_TRANS);
	}


	public Cursor getMonthlyExp(int month) {
		// SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = geMontlyExpenseCursor(month);
		MatrixCursor matCursor = new MatrixCursor(new String[] { DbHelper.C_ID,
				DbHelper.C_DATE, DbHelper.C_AMOUNT });
		int dailExp = 0;
		int totalEntries = 1;
		int previousDate = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			long previousDateLong = 0;
			long startDateLong = cursor.getLong(cursor
					.getColumnIndex(DbHelper.C_DATE));
			int startDateInt = Integer.parseInt((new SimpleDateFormat("dd"))
					.format(new Date(startDateLong)));
			previousDate = startDateInt;
			previousDateLong = startDateLong;
			// sum up expense of single days
			do {
				if(isAtmTransaction(cursor)){
					Log.d(TAG,"skipping daily entry for:"+cursor.getInt(cursor
						.getColumnIndex(DbHelper.C_AMOUNT)));
					continue;
				}
				startDateLong = cursor.getLong(cursor
						.getColumnIndex(DbHelper.C_DATE));
				startDateInt = Integer.parseInt((new SimpleDateFormat("dd"))
						.format(new Date(startDateLong)));
				Log.d(TAG, "sDate =" + startDateInt);

				if (startDateInt == previousDate) {
					
					dailExp += cursor.getInt(cursor
							.getColumnIndex(DbHelper.C_AMOUNT));
				} else {
					// enter into cursor
					matCursor.addRow(new Object[] { totalEntries++,
							getDate(previousDateLong), dailExp });
					previousDate = startDateInt;
					previousDateLong = startDateLong;

					Log.d(TAG, "entry added  =" + dailExp);
				
					dailExp = cursor.getInt(cursor
							.getColumnIndex(DbHelper.C_AMOUNT));
				}

			} while (cursor.moveToNext());
			matCursor.addRow(new Object[] { totalEntries++,
					getDate(startDateLong), dailExp });
		}

		return matCursor;

	}

	@Override
	public Cursor geMontlyExpenseCursor(int month) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Calendar cal = Calendar.getInstance();
		Calendar startDateCalender = new GregorianCalendar(
				cal.get(Calendar.YEAR), month, 1);
		Calendar endDateCalender = new GregorianCalendar(
				(cal.get(Calendar.YEAR)), month,

				startDateCalender.getActualMaximum(Calendar.DAY_OF_MONTH));

		// start and end date in ms
		long startDate = startDateCalender.getTimeInMillis();
		long endDate = endDateCalender.getTimeInMillis();

		String selection = DbHelper.C_DATE + " BETWEEN " + startDate + " AND "
				+ endDate;
		Cursor cursor = db.query(DbHelper.TABLE_NAME, null, selection, null,
				null, null, null);
		Log.d(TAG,
				"geMontlyExpenseCursor reutning cursor.getcoutn="
						+ cursor.getCount());
		return cursor;
	}

	@Override
	public Cursor getExpenseBetweenDates(long startDate, long endDate) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Calendar cal = Calendar.getInstance();
		Log.d(TAG, "start date= " + getDate(startDate) + "end date = "
				+ getDate(endDate));
		String selection = DbHelper.C_DATE + " BETWEEN " + startDate + " AND "
				+ endDate;
		Cursor cursor = db.query(DbHelper.TABLE_NAME, null, selection, null,
				null, null, null);
		Log.d(TAG, "getDaily reutning cursor.getcoutn=" + cursor.getCount());
		return cursor;

	}

	@Override
	public void delete(long id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String whereClause = DbHelper.C_ID + " = " + id;
		db.delete(DbHelper.TABLE_NAME, whereClause, null);
	}

	@Override
	public Cursor getExpenseForCategory(Category category) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.query(DbHelper.TABLE_NAME,
				new String[]{DbHelper.C_ID, DbHelper.C_DATE,DbHelper.C_NOTES,DbHelper.C_AMOUNT},
				DbHelper.C_CATEGORY +" = ?",
				new String[]{category.getName()},
				null, null, null);
	
	}

	

	

}
