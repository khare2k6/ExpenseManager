package ak.expensemanager.ui;

import ak.expensemanager.R;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.ui.FragmentListMonthly.IOnDateSelectedListener;
import ak.expensemanager.ui.FragmentListYearly.IOnMonthSelectedListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DisplayExpenses extends Activity implements IOnMonthSelectedListener, IOnDateSelectedListener{

	final static String TAG = IDebugTag.ankitTag + DisplayExpenses.class.getSimpleName();
	FragmentManager frgManager = null;
	Fragment frg_yearly,frg_monthly,frg_daily;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expenses);
		frgManager = getFragmentManager();
		frg_yearly = new FragmentListYearly();
		frg_monthly = new FragmentListMonthly();
		frg_daily = new FragmentListDaily();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_settings:
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
			
		case R.id.action_category:
			Intent intent2 = new Intent(this,EditCategoryActivity.class);
			startActivity(intent2);
						return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onResume() {
		super.onResume();
		
		FragmentTransaction transaction = frgManager.beginTransaction();
		transaction.replace(R.id.fragment_container, frg_yearly);
		//transaction.addToBackStack(null);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	
		switch(item.getItemId()){
		
		
		}
		return super.onContextItemSelected(item);
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		
	}

	/**
	 * Test function
	 * */
	void pushData(){
		IRetrieveExpenses exp = new RetrieveExpenses(this);
		int cout = 1000;
		for(int i =0;i<10;i++){
			cout = cout+10;
			exp.addExpense(System.currentTimeMillis(), cout,"Fuel","Blha hjafh haf a");
		}
	}

	@Override
	public void OnMonthSelected(String month,int position) {
		//Toast.makeText(this, month +" position "+position, Toast.LENGTH_SHORT).show();
		((FragmentListMonthly)frg_monthly).setMonth(position);
		FragmentTransaction transaction = frgManager.beginTransaction();
		transaction.replace(R.id.fragment_container, frg_monthly);
		transaction.addToBackStack(null);
		transaction.commit();
		
	}


	@Override
	public void OnDateSelected(String date) {
		//Toast.makeText(this, date , Toast.LENGTH_SHORT).show();
		((FragmentListDaily)frg_daily).setDate(date);
		FragmentTransaction transaction = frgManager.beginTransaction();
		transaction.replace(R.id.fragment_container, frg_daily);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	

}
