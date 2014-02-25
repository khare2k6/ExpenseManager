package ak.expensemanager.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ak.expensemanager.R;
import ak.expensemanager.category.ILimitUpdate;
import ak.expensemanager.category.SettingsSharedPref;
import ak.expensemanager.db.CustomAdapter;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses.DbHelper;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.model.Category;
import ak.expensemanager.model.Expense;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentListDaily extends Fragment implements IFragmentTime {

	final static String TAG = IDebugTag.ankitTag
			+ FragmentListDaily.class.getSimpleName();
	ListView lv_yearlyExp = null;
	TextView tv_title = null;
	CustomAdapter adapter = null;
	final String[] FROM = {DbHelper.C_CATEGORY, DbHelper.C_NOTES, DbHelper.C_AMOUNT };
	final int[] TO = {R.id.tv_row_category, R.id.tv_row_notes ,R.id.tv_row_amount,
			};
	Activity activity = null;
	IRetrieveExpenses expenses = null;
	long entry_id = 0;
	Date date;
	String selectedDate;
	Expense editExpense;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "onAttach");
		this.activity = activity;
		expenses = new RetrieveExpenses(this.activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_listexpenses, null);
		View header = inflater.inflate(R.layout.header_category_notes_amt, null);
		lv_yearlyExp = (ListView) v.findViewById(R.id.lv_listexpenses);
		lv_yearlyExp.addHeaderView(header);
		tv_title = (TextView) v.findViewById(R.id.tv_title);
		tv_title.setText(selectedDate);
		return v;
	}

	public void setDate(String sdate,Context context) {
		SimpleDateFormat sdf = new SimpleDateFormat(IDebugTag.DATE_FORMAT_DD_MMM_WEEKDAY);
		Calendar cal = Calendar.getInstance();
		selectedDate = sdate;
		Log.d(TAG, "setdate :" + sdate);
		try {
			this.date = sdf.parse(sdate);
			cal.setTime(this.date);
			cal.set(Calendar.YEAR, UtilityExp.getSelectedYear(context));
			this.date = cal.getTime();
			Log.d(TAG, "date setted = " + date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
		lv_yearlyExp.setAdapter(null);
		adapter = null;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d(TAG, "onContextItemSelected.." + item.getItemId());
		switch (item.getItemId()) {
		case 1:
			Log.d(TAG, "delete pressed.." + item.getItemId());
			expenses.delete(entry_id);
			adapter.swapCursor(expenses.getExpenseBetweenDates(
					this.date.getTime(), nextDay(this.date).getTime()));
			adapter.notifyDataSetChanged();
			break;
		case 2:
			Intent intent = new Intent(activity,FloatingActivity.class);
			 Bundle bundle = new Bundle();
			 bundle.putLong("editId", entry_id);
			 bundle.putParcelable(IDebugTag.PARCEL, editExpense);
			 intent.putExtra(IDebugTag.EDIT_ENTRY_BUNDLE, bundle);
			 startActivity(intent);
			break;
		}
	
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "Delete");
		menu.add(0,2,0,"Edit");
	}

	public Date nextDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int nextDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
		Log.d(TAG, "next day=" + nextDay);
		cal.set(Calendar.DAY_OF_MONTH, nextDay);
		cal.set(Calendar.YEAR, UtilityExp.getSelectedYear(activity));
		Log.d(TAG, "next set date=" + cal.getTime());

		return cal.getTime();
	}

	@Override
	public void onResume() {
		super.onResume();
		ILimitUpdate limitUpdate = new SettingsSharedPref(this.activity);
		final int limit = limitUpdate.getLimit();
		Log.d(TAG, "onResume="+expenses);
		adapter = new CustomAdapter(activity, R.layout.row_daily, expenses.getExpenseBetweenDates(this.date.getTime(),
				nextDay(this.date).getTime()), FROM, TO, 0);
		lv_yearlyExp.setAdapter(adapter);
		lv_yearlyExp.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View arg1,
					int pos, long id) {
				entry_id = id;
				Calendar cal = Calendar.getInstance();
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				cal.setTimeInMillis(c.getLong(c.getColumnIndex(DbHelper.C_DATE)));
				
				editExpense = new Expense(c.getInt(c.getColumnIndex(DbHelper.C_AMOUNT)),
						new Category(c.getString(c.getColumnIndex(DbHelper.C_CATEGORY)),1050), 
						c.getString(c.getColumnIndex(DbHelper.C_NOTES)),
						cal);
				return false;
			}
		});
		registerForContextMenu(lv_yearlyExp);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		lv_yearlyExp.setAdapter(null);
		adapter = null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
}
