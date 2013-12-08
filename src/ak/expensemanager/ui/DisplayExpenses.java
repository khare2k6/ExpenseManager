package ak.expensemanager.ui;

import ak.expensemanager.R;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.model.Category;
import ak.expensemanager.ui.FragmentAllCategories.IOnCategorySelectedListener;
import ak.expensemanager.ui.FragmentListMonthly.IOnDateSelectedListener;
import ak.expensemanager.ui.FragmentListYearly.IOnMonthSelectedListener;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DisplayExpenses extends Activity implements IOnMonthSelectedListener, IOnDateSelectedListener,
IOnCategorySelectedListener,TabListener{

	final static String TAG = IDebugTag.ankitTag + DisplayExpenses.class.getSimpleName();
	FragmentManager frgManager = null;
	 ActionBar actionBar = null;
	Fragment frg_yearly,frg_monthly,frg_daily,frg_all_categories,frg_category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_expenses);
		
		frgManager = getFragmentManager();
		frg_yearly = new FragmentListYearly();
		frg_monthly = new FragmentListMonthly();
		frg_daily = new FragmentListDaily();
		frg_all_categories = new FragmentAllCategories();
		frg_category = new FragmentCategory();
		
		  actionBar = getActionBar();
		  actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			 actionBar.setDisplayShowTitleEnabled(false);
			 Tab tab = actionBar.newTab().setText(IDebugTag.YEARLY_VIEW_TITLE).setTabListener(this);
			 actionBar.addTab(tab);
			 
			 tab = actionBar.newTab().setText(IDebugTag.CATEGORY_VIEW_TITLE).setTabListener(this);
			 actionBar.addTab(tab);
			
		   
	}
	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}


//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch(item.getItemId()){
//		case R.id.action_settings:
//			Intent intent = new Intent(this,MainActivity.class);
//			startActivity(intent);
//			return true;
//			
//		case R.id.action_category:
//			Intent intent2 = new Intent(this,EditCategoryActivity.class);
//			startActivity(intent2);
//						return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}


	@Override
	protected void onResume() {
		super.onResume();
		 
		FragmentTransaction transaction = frgManager.beginTransaction();
		transaction.replace(R.id.fragment_container, frg_yearly);
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
//	void pushData(){
//		IRetrieveExpenses exp = new RetrieveExpenses(this);
//		int cout = 1000;
//		for(int i =0;i<10;i++){
//			cout = cout+10;
//			exp.addExpense(System.currentTimeMillis(), cout,"Fuel","Blha hjafh haf a");
//		}
//	}

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
	
	@Override
	public void OnCategorySelected(String category) {
	Log.d(TAG,"Category selected:"+category);
	((FragmentCategory)frg_category).setCategory(new Category(category, 0));
	
	FragmentTransaction transaction = frgManager.beginTransaction();
	transaction.replace(R.id.fragment_container, frg_category);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	transaction.addToBackStack(null);
	transaction.commit();
	}
	
	// Thote Added
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_bank:
			startActivity(new Intent(this, MainActivity.class));
			break;
		case R.id.add_category:
			startActivity(new Intent(this, EditCategoryActivity.class));
			break;
		case R.id.add_transaction:
			startActivity(new Intent(this, FloatingActivity.class));
			break;
		}
		return true;
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if(tab.getText().toString().equals(IDebugTag.YEARLY_VIEW_TITLE)){
			ft.replace(R.id.fragment_container, frg_yearly);
		}	else{
			ft.replace(R.id.fragment_container, frg_all_categories);
			
		}
	}


	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	

}
