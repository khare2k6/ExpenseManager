
package ak.expensemanager.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import ak.expensemanager.R;
import ak.expensemanager.category.CategoryInfoSharedPref;
import ak.expensemanager.category.ICategory;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses.DbHelper;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.model.Category;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentCategory extends Fragment{
	
   
    Category category;
	final String TAG = IDebugTag.ankitTag + FragmentCategory.class.getSimpleName();
	ICategory categoryManager;
	IRetrieveExpenses expenses;
	final String [] FROM = {DbHelper.C_DATE,DbHelper.C_NOTES,DbHelper.C_AMOUNT};
	final int [] TO ={R.id.tv_row_monthDate,R.id.tv_row_notes,R.id.tv_row_amount};
	Activity parentActivity;
	TextView tv_title;
	ListView lv_expenses;
	SimpleCursorAdapter adapter;
	//IOnCategorySelectedListener mActivityListner = null;

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		expenses = new RetrieveExpenses(parentActivity);
		categoryManager = new CategoryInfoSharedPref(parentActivity);
		
	}

	
	public void setCategory(Category category){
		this.category = category;
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
		adapter = new SimpleCursorAdapter(parentActivity, R.layout.row_date_notes_amt, 
				expenses.getExpenseForCategory(category)
				, FROM, TO,0);
	
		lv_expenses.setAdapter(adapter);
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
