package ak.expensemanager.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ak.expensemanager.R;
import ak.expensemanager.category.ILimitUpdate;
import ak.expensemanager.category.UpdateLimitSharedPref;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses.DbHelper;
import ak.expensemanager.debug.IDebugTag;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
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

public class FragmentListDaily extends Fragment {

	final static String TAG = IDebugTag.ankitTag
			+ FragmentListDaily.class.getSimpleName();
	ListView lv_yearlyExp = null;
	TextView tv_title = null;
	SimpleCursorAdapter adapter = null;
	final String[] FROM = { DbHelper.C_DATE, DbHelper.C_AMOUNT,
			DbHelper.C_CATEGORY, DbHelper.C_NOTES };
	final int[] TO = { R.id.tv_row_monthDate, R.id.tv_row_amount,
			R.id.tv_row_category, R.id.tv_row_notes };
	Activity activity = null;
	IRetrieveExpenses expenses = null;
	long entry_id = 0;
	// int date ;
	// int month ;
	Date date;

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

		// View v= inflater.inflate(R.layout.fragment_listexpenses, container);
		View v = inflater.inflate(R.layout.fragment_listexpenses, null);
		lv_yearlyExp = (ListView) v.findViewById(R.id.lv_listexpenses);
		tv_title = (TextView) v.findViewById(R.id.tv_title);
		tv_title.setText(IDebugTag.DAILY_VIEW);

		return v;

	}

	public void setDate(String sdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd,MMM");
		Calendar cal = Calendar.getInstance();
		Log.d(TAG, "setdate :" + sdate);
		try {
			this.date = sdf.parse(sdate);
			cal.setTime(this.date);
			cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
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
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "Delete");
	}

	public Date nextDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int nextDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
		Log.d(TAG, "next day=" + nextDay);
		cal.set(Calendar.DAY_OF_MONTH, nextDay);
		cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		Log.d(TAG, "next set date=" + cal.getTime());

		return cal.getTime();
	}

	@Override
	public void onResume() {
		super.onResume();
		ILimitUpdate limitUpdate = new UpdateLimitSharedPref(this.activity);
		final int limit = limitUpdate.getLimit();
		Log.d(TAG, "onResume");
		adapter = new SimpleCursorAdapter(activity, R.layout.row_monthly,
				expenses.getExpenseBetweenDates(this.date.getTime(),
						nextDay(this.date).getTime()), FROM, TO, 0);
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int arg2) {

				switch (view.getId()) {
				case R.id.tv_row_monthDate:

					long date = cursor.getLong(cursor
							.getColumnIndex(DbHelper.C_DATE));
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM:EEE");
					String strdate = sdf.format(new Date(date));
					TextView tv_date = (TextView) view;
					tv_date.setText(strdate.toString());
					break;

				case R.id.tv_row_amount:
					Integer amount = (int) cursor.getInt(cursor
							.getColumnIndex(DbHelper.C_AMOUNT));
					TextView tv_amt = (TextView) view;
					tv_amt.setText(amount.toString());
					tv_amt.setTextColor(android.graphics.Color.BLACK);
					if (amount > limit) {
						Log.d(TAG, "amount = " + amount);
						tv_amt.setTextColor(android.graphics.Color.RED);
					}
					break;
				default:
					return false;
				}
				return true;
			}
		});
		lv_yearlyExp.setAdapter(adapter);
		lv_yearlyExp.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long id) {
				entry_id = id;
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
