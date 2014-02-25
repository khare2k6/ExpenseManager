
package ak.expensemanager.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ak.expensemanager.R;
import ak.expensemanager.category.CategoryInfoSharedPref;
import ak.expensemanager.category.ICategory;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses.DbHelper;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.debug.IDebugTag.Months;
import ak.expensemanager.model.Category;
import ak.expensemanager.model.Expense;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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

public class FragmentCategory extends Fragment implements IFragmentCategory{
	
   
    Category category;
    Months month;
	final String TAG = IDebugTag.ankitTag + FragmentCategory.class.getSimpleName();
	ICategory categoryManager;
	IRetrieveExpenses expenses;
	final String [] FROM = {DbHelper.C_DATE,DbHelper.C_NOTES,DbHelper.C_AMOUNT};
	final int [] TO ={R.id.tv_row_monthDate,R.id.tv_row_notes,R.id.tv_row_amount};
	Activity parentActivity;
	TextView tv_title;
	ListView lv_expenses;
	SimpleCursorAdapter adapter;
	private long entry_id;
	long[] startEndDate ;
	Bundle editEntry;
	Expense editExpense;

	//IOnCategorySelectedListener mActivityListner = null;

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		expenses = new RetrieveExpenses(parentActivity);
		categoryManager = new CategoryInfoSharedPref(parentActivity);
		
	}

	
	public void setCategory(Category category,Months month){
		this.category = category;
		this.month = month;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_listexpenses, null);
		View header_month = inflater.inflate(R.layout.header_date_notes_amt, null);
		
		tv_title = (TextView)v.findViewById(R.id.tv_title);
		tv_title.setText(category.getName());
		
		lv_expenses = (ListView)v.findViewById(R.id.lv_listexpenses);
		lv_expenses.addHeaderView(header_month);
		
		
		return v;
		
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
		lv_expenses.setAdapter(null);
		lv_expenses.setOnItemLongClickListener(null);
		adapter = null;
		
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG,"onDetach");
		lv_expenses.setAdapter(null);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
		startEndDate = UtilityExp.getMonthStartEndDate(parentActivity,month);
		adapter = new SimpleCursorAdapter(parentActivity, R.layout.row_date_notes_amt, 
				expenses.getExpenseForCategory(category,startEndDate[IDebugTag.FIRST_DATE_OF_MONTH],
					startEndDate[IDebugTag.LAST_DATE_OF_MONTH])
				, FROM, TO,0);
	
		lv_expenses.setAdapter(adapter);
		lv_expenses.setOnItemLongClickListener(new OnItemLongClickListener() {

			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				entry_id = id;
		
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				
				editEntry = new Bundle();
				editEntry.putString("amount",(String) ((TextView)view.findViewById(R.id.tv_row_amount)).getText());
				editEntry.putString("notes",(String)((TextView)view.findViewById(R.id.tv_row_notes)).getText());
				editEntry.putLong("id", entry_id);
				
				Log.d(TAG,"amt "+c.getInt(c.getColumnIndex(DbHelper.C_AMOUNT)));
				Log.d(TAG,"cat "+category);
				Log.d(TAG,"notes "+c.getString(c.getColumnIndex(DbHelper.C_NOTES)));
				Log.d(TAG,"date "+c.getLong(c.getColumnIndex(DbHelper.C_DATE)));
				
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(c.getLong(c.getColumnIndex(DbHelper.C_DATE)));
				
				editExpense = new Expense(c.getInt(c.getColumnIndex(DbHelper.C_AMOUNT)),
						category, 
						c.getString(c.getColumnIndex(DbHelper.C_NOTES)),
						cal);
				return false;

			}
		});
		registerForContextMenu(lv_expenses);
		adapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int arg2) {
				//set Date
				if(view.getId() == R.id.tv_row_monthDate){
					Long date = cursor.getLong(cursor.getColumnIndex(DbHelper.C_DATE));
					SimpleDateFormat sdf = new SimpleDateFormat(IDebugTag.DATE_FORMAT_DD_MMM_WEEKDAY);
					String sDate = sdf.format(new Date(date));
					TextView tv_date = (TextView)view;
					tv_date.setText(sDate);
					return true;
				}
				return false;
			}
		});
		/*lv_expenses.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String tv = ((TextView)view.findViewById(R.id.tv_row_category)).getText().toString();
				mActivityListner.OnCategorySelected(tv);
			}
		});*/
	}


	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		 if(item.getTitle().equals(IDebugTag.CONTEXT_MENU_ITEM_DELETE)){
			 expenses.delete(entry_id);
			 adapter.swapCursor(expenses.getExpenseForCategory(category,startEndDate[IDebugTag.FIRST_DATE_OF_MONTH],
						startEndDate[IDebugTag.LAST_DATE_OF_MONTH]));
			 adapter.notifyDataSetChanged();
			 return true;
		 }else if(item.getTitle().equals(IDebugTag.CONTEXT_MENU_ITEM_EDIT)){
			 Intent intent = new Intent(parentActivity,FloatingActivity.class);
			 Bundle bundle = new Bundle();
			 bundle.putLong("editId", entry_id);
			 bundle.putParcelable(IDebugTag.PARCEL, editExpense);
			 intent.putExtra(IDebugTag.EDIT_ENTRY_BUNDLE, bundle);
			 startActivity(intent);
			 
			 
			 
			// expenses.edit(new Expense(300, new Category("Fuel",0), "SomeTest", Calendar.getInstance()), entry_id);
			 return true;
		 }else{
			 return super.onContextItemSelected(item);
				
		 }
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(IDebugTag.CONTEXT_MENU_ITEM_DELETE);
		menu.add(IDebugTag.CONTEXT_MENU_ITEM_EDIT);
		
	}


	@Override
	public void onAttach(Activity activity) {
			super.onAttach(activity);
			Log.d(TAG,"onAttach");
			this.parentActivity = activity; 
			//mActivityListner = (IOnCategorySelectedListener) this.parentActivity;
	}

	@Override
	public void onStop() {
		super.onStop();
		lv_expenses.setAdapter(null);
		adapter = null;
	}

	

	
	
	
}
