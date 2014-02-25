package ak.expensemanager.ui;

import java.util.ArrayList;

import ak.expensemanager.R;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.MonthlyExpense;
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

public class FragmentListYearly extends Fragment implements IFragmentTime{

	final String TAG = IDebugTag.ankitTag + FragmentListYearly.class.getSimpleName();
	MatrixCursor matrixCursor ;
	IRetrieveExpenses expenses;
	final String [] FROM = {BaseColumns._ID,DbHelper.C_DATE,DbHelper.C_AMOUNT};
	final int [] TO ={R.id.tv_row_id,R.id.tv_row_monthDate,R.id.tv_row_amount};
	Activity parentActivity;
	TextView tv_title;
	ListView lv_expenses;
	SimpleCursorAdapter adapter;
	IOnMonthSelectedListener mActivityListner = null;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		//if(matrixCursor == null)
			//matrixCursor = new MatrixCursor(FROM);
		expenses = new RetrieveExpenses(parentActivity);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View v = inflater.inflate(R.layout.fragment_listexpenses, container);
		View v = inflater.inflate(R.layout.fragment_listexpenses, null);
		tv_title = (TextView)v.findViewById(R.id.tv_title);
		//tv_title.setText(IDebugTag.YEARLY_VIEW);
		tv_title.setText(UtilityExp.getSelectedYear(parentActivity)+IDebugTag.EXPENSES);
		
		lv_expenses = (ListView)v.findViewById(R.id.lv_listexpenses);
		return v;
		
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
		lv_expenses.setAdapter(null);
		adapter = null;
		matrixCursor = null;
		// add matrixCursor = null;
		
		
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
			addDataToCursor();
		adapter = new SimpleCursorAdapter(parentActivity, R.layout.row_yearly, matrixCursor, FROM, TO,0);
	
		lv_expenses.setAdapter(adapter);
		lv_expenses.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String tv = ((TextView)view.findViewById(R.id.tv_row_monthDate)).getText().toString();
				mActivityListner.OnMonthSelected(tv,position);
				
			}
		});
	}

	private void addDataToCursor() {
		ArrayList<MonthlyExpense> expList = expenses.getAllMonthExpenses();
		matrixCursor = new MatrixCursor(FROM);
		int i =1;
		for(MonthlyExpense exp :expList){
			matrixCursor.addRow(new Object[]{i++,exp.getMonth().toString(),exp.getTotalExpense()});
		}
	}

	@Override
	public void onAttach(Activity activity) {
			super.onAttach(activity);
			this.parentActivity =activity; 
			mActivityListner = (IOnMonthSelectedListener) this.parentActivity;
	}

	@Override
	public void onStop() {
		super.onStop();
		lv_expenses.setAdapter(null);
		adapter = null;
	}

	public interface IOnMonthSelectedListener {
		public void OnMonthSelected(String month, int position);

	}
	}
