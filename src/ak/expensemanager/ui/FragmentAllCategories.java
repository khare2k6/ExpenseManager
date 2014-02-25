
package ak.expensemanager.ui;

import ak.expensemanager.R;
import ak.expensemanager.category.CategoryInfoSharedPref;
import ak.expensemanager.category.ICategory;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses.DbHelper;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.debug.IDebugTag.Months;
import ak.expensemanager.model.Month;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentAllCategories extends Fragment implements IFragmentCategory, AdapterView.OnItemSelectedListener{
	
   
    
	final String TAG = IDebugTag.ankitTag + FragmentAllCategories.class.getSimpleName();
	ICategory category;
	Months selectedMonth ;//= Months.YEARLY;
	IRetrieveExpenses expenses;
	final String [] FROM = {DbHelper.C_CATEGORY,DbHelper.C_AMOUNT};
	final int [] TO ={R.id.tv_row_category,R.id.tv_row_amount};
	Activity parentActivity;
	TextView tv_title;
	ListView lv_expenses;
	SimpleCursorAdapter adapter;
	Spinner spinnerMonths;
	ArrayAdapter<Months> arrayAdapter ;
	IOnCategorySelectedListener mActivityListner = null;

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		expenses = new RetrieveExpenses(parentActivity);
		category = new CategoryInfoSharedPref(parentActivity);
		 arrayAdapter = new ArrayAdapter<Months>(parentActivity, R.layout.spinner_item, Months.values());
		 arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 selectedMonth = Month.getSelectedMonth();
			
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"OnCreateView");
		View v = inflater.inflate(R.layout.fragment_listexpenses_all_categories, null);
		View header_month = inflater.inflate(R.layout.header_category_amt, null);
		header_month.setClickable(false);
		
		tv_title = (TextView)v.findViewById(R.id.tv_title);
		tv_title.setText(IDebugTag.CATEGORY_VIEW_TITLE);
		
		lv_expenses = (ListView)v.findViewById(R.id.lv_listexpenses);
		lv_expenses.addHeaderView(header_month);
		// adapter = new ArrayAdapter<Months>(parentActivity, android.R.layout.simple_spinner_item, Months.values());
		 spinnerMonths =(Spinner) v.findViewById(R.id.spinner_months);			
		spinnerMonths.setAdapter(arrayAdapter);
		
		
		return v;
		
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
		lv_expenses.setAdapter(null);
		spinnerMonths.setOnItemSelectedListener(null);
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
		selectedMonth = Month.getSelectedMonth();
		Log.d("ankitKhare_cat",selectedMonth.name());
		/*adapter = new SimpleCursorAdapter(parentActivity, R.layout.row_all_months_category, 
				expenses.getExpenseForCategories(category.getAllCategory().keySet(),
						UtilityExp.getMonthStartEndDate(Months.YEARLY, IDebugTag.FIRST_DATE_OF_MONTH),
						UtilityExp.getMonthStartEndDate(Months.YEARLY, IDebugTag.LAST_DATE_OF_MONTH)), FROM, TO,0);
	
		lv_expenses.setAdapter(adapter);*/
		lv_expenses.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				TextView tv = (TextView)view.findViewById(R.id.tv_row_category);
				if(tv == null) return ;
				String strtv = tv.getText().toString();
				mActivityListner.OnCategorySelected(strtv,selectedMonth);
			}
		});
		
		spinnerMonths.setOnItemSelectedListener(this);
		spinnerMonths.setSelection(Month.getSelectedMonth().ordinal());
		
	}


	
	@Override
	public void onAttach(Activity activity) {
			super.onAttach(activity);
			Log.d(TAG,"onAttach");
			this.parentActivity = activity; 
			mActivityListner = (IOnCategorySelectedListener) this.parentActivity;
	}

	@Override
	public void onStop() {
		super.onStop();
		lv_expenses.setAdapter(null);
		adapter = null;
	}

	/**
	 * to allow selection of month from
	 * Yearly view/monthly view itself
	 * */
	/*public void setMonth(Months month){
		selectedMonth = month;
	}*/
	public interface IOnCategorySelectedListener {
		public void OnCategorySelected(String category,Months month);

	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Log.d("ankitKhare_cat","onItemSelected:"+((Months) parent.getItemAtPosition(pos)).name());
		 Month.setSelectedMonth((Months) parent.getItemAtPosition(pos));
		 selectedMonth = Month.getSelectedMonth();
		
		lv_expenses.setAdapter(null);
		
		long []startEndDate = UtilityExp.getMonthStartEndDate(parentActivity,selectedMonth);
		Cursor cursor = expenses.getExpenseForCategories(category.getAllCategory().keySet(),
				startEndDate[IDebugTag.FIRST_DATE_OF_MONTH],startEndDate[IDebugTag.LAST_DATE_OF_MONTH]);
		adapter = new SimpleCursorAdapter(parentActivity, R.layout.row_all_months_category,
				cursor, FROM, TO,0);
		lv_expenses.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
	
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
