package ak.expensemanager.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ak.expensemanager.R;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.MonthlyExpense;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses.DbHelper;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.debug.IDebugTag.Months;
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

public class FragmentListMonthly extends Fragment implements IFragmentTime{
	
    Months month;
	final String TAG = IDebugTag.ankitTag + FragmentListMonthly.class.getSimpleName();
	MatrixCursor matrixCursor ;
	IRetrieveExpenses expenses;
	final String [] FROM = {BaseColumns._ID,DbHelper.C_DATE,DbHelper.C_AMOUNT};
	final int [] TO ={R.id.tv_row_id,R.id.tv_row_monthDate,R.id.tv_row_amount};
	Activity parentActivity;
	TextView tv_title;
	ListView lv_expenses;
	SimpleCursorAdapter adapter;
	IOnDateSelectedListener mActivityListner = null;

	
	public void setMonth(Months selectedMonth){
		this.month = selectedMonth;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		if(matrixCursor == null)
			matrixCursor = new MatrixCursor(FROM);
		expenses = new RetrieveExpenses(parentActivity);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View v = inflater.inflate(R.layout.fragment_listexpenses, container);
		View v = inflater.inflate(R.layout.fragment_listexpenses, null);
		View header_month = inflater.inflate(R.layout.header_date_amt, null);
		
		tv_title = (TextView)v.findViewById(R.id.tv_title);
		tv_title.setText(UtilityExp.getMonthName(month));
		
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
		if(matrixCursor.getCount() == 0)
			//addDataToCursor();
		adapter = new SimpleCursorAdapter(parentActivity, R.layout.row_yearly, expenses.getMonthlyExp(month)
				, FROM, TO,0);
	
		lv_expenses.setAdapter(adapter);
		lv_expenses.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				TextView tv = (TextView)view.findViewById(R.id.tv_row_monthDate);
				if(tv == null) return ;
				String strtv = tv.getText().toString();
				mActivityListner.OnDateSelected(strtv);
			}
		});
	}

	private void addDataToCursor() {
		ArrayList<MonthlyExpense> expList = expenses.getAllMonthExpenses();
		int i =1;
		for(MonthlyExpense exp :expList){
			matrixCursor.addRow(new Object[]{i++,exp.getMonth().toString(),exp.getTotalExpense()});
		}
	}

	@Override
	public void onAttach(Activity activity) {
			super.onAttach(activity);
			Log.d(TAG,"onAttach");
			this.parentActivity = activity; 
			mActivityListner = (IOnDateSelectedListener) this.parentActivity;
	}

	@Override
	public void onStop() {
		super.onStop();
		lv_expenses.setAdapter(null);
		adapter = null;
	}

	
	public interface IOnDateSelectedListener {
		public void OnDateSelected(String date);

	}
	
	
	
}
