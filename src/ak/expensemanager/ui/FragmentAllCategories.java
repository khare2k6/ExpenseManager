
package ak.expensemanager.ui;

import ak.expensemanager.R;
import ak.expensemanager.category.CategoryInfoSharedPref;
import ak.expensemanager.category.ICategory;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses.DbHelper;
import ak.expensemanager.debug.IDebugTag;
import android.app.Activity;
import android.app.Fragment;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentAllCategories extends Fragment{
	
   
    
	final String TAG = IDebugTag.ankitTag + FragmentAllCategories.class.getSimpleName();
	ICategory category;
	IRetrieveExpenses expenses;
	final String [] FROM = {DbHelper.C_CATEGORY,DbHelper.C_AMOUNT};
	final int [] TO ={R.id.tv_row_category,R.id.tv_row_amount};
	Activity parentActivity;
	TextView tv_title;
	ListView lv_expenses;
	SimpleCursorAdapter adapter;
	IOnCategorySelectedListener mActivityListner = null;

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		expenses = new RetrieveExpenses(parentActivity);
		category = new CategoryInfoSharedPref(parentActivity);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_listexpenses, null);
		View header_month = inflater.inflate(R.layout.header_category_amt, null);
		
		tv_title = (TextView)v.findViewById(R.id.tv_title);
		tv_title.setText(IDebugTag.CATEGORY_VIEW_TITLE);
		
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
		adapter = new SimpleCursorAdapter(parentActivity, R.layout.row_all_months_category, 
				expenses.getExpenseForCategories(category.getAllCategory().keySet())
				, FROM, TO,0);
	
		lv_expenses.setAdapter(adapter);
		lv_expenses.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String tv = ((TextView)view.findViewById(R.id.tv_row_category)).getText().toString();
				mActivityListner.OnCategorySelected(tv);
			}
		});
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

	
	public interface IOnCategorySelectedListener {
		public void OnCategorySelected(String category);

	}
	
	
	
}
