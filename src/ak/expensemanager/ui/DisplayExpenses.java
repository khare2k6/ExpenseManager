package ak.expensemanager.ui;

import ak.expensemanager.R;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.debug.IDebugTag.Months;
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

	final static String TAG = "ankitkhare" + DisplayExpenses.class.getSimpleName();
	FragmentManager frgManager = null;
	 ActionBar actionBar = null;
		
	Fragment frg_yearly,frg_monthly,frg_daily,frg_all_categories,frg_category;
	
	IFragmentTime frg_type_time = null;
	IFragmentCategory frg_type_cat = null;
	

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

		frg_type_time = (IFragmentTime) frg_yearly;
		frg_type_cat = (IFragmentCategory) frg_all_categories;

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		Tab tab = actionBar.newTab().setText(IDebugTag.YEARLY_VIEW_TITLE).setTabListener(this);

		actionBar.addTab(tab);

		tab = actionBar.newTab().setText(IDebugTag.CATEGORY_VIEW_TITLE).setTabListener(this);
		actionBar.addTab(tab);

		   
	}
	




	
	void replaceFragment(Fragment fragment/*,FragmentOperation state2*/){
		
		if(fragment instanceof IFragmentTime)
			frg_type_time = (IFragmentTime) fragment;
		else
			frg_type_cat = (IFragmentCategory) fragment;
			
		FragmentTransaction transaction = frgManager.beginTransaction();
	
		transaction.replace(R.id.fragment_container, fragment);
		Log.d(TAG,"adding to back stack:"+fragment);
		transaction.addToBackStack(IDebugTag.FRAGMENT);
		transaction.commit();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
	
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
		Log.d(TAG,"switching to monthly view");
		((FragmentListMonthly)frg_monthly).setMonth(getMonthFromPosition(position));
		replaceFragment(frg_monthly/*,FragmentOperation.REPLACE*/);

		
	}

	 Months getMonthFromPosition(int pos){
		switch(pos){
		case 0: return Months.JANUARY;
		case 1: return Months.FEBUARY;
		case 2: return Months.MARCH;
		case 3: return Months.APRIL;
		case 4: return Months.MAY;
		case 5: return Months.JUNE;
		case 6: return Months.JULY;
		case 7: return Months.AUGUST;
		case 8: return Months.SEPTEMBER;
		case 9: return Months.OCTOBER;
		case 10: return Months.NOVEMBER;
		case 11: return Months.DECEMBER;
		default:
			return null;
		
		
		}
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG,"Back pressed..");
		super.onBackPressed();
	
	}


	@Override
	public void OnDateSelected(String date) {
		//Toast.makeText(this, date , Toast.LENGTH_SHORT).show();
		Log.d(TAG,"switching to daily view");
		((FragmentListDaily)frg_daily).setDate(date);
		replaceFragment(frg_daily/*,FragmentOperation.REPLACE*/);
	
	}
	
	@Override
	public void OnCategorySelected(String category,Months month) {
		Log.d(TAG,"switching to category view specific");
	((FragmentCategory)frg_category).setCategory(new Category(category, 0),month);
	replaceFragment(frg_category/*,FragmentOperation.REPLACE*/);

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
		frgManager.popBackStackImmediate( IDebugTag.FRAGMENT,FragmentManager.POP_BACK_STACK_INCLUSIVE);
		
		if(tab.getText().toString().equals(IDebugTag.YEARLY_VIEW_TITLE)){
			Log.d(TAG,"switching to yearly tab");
			replaceFragment((Fragment)frg_type_time/*,FragmentOperation.REPLACE*/);
		}	else{
			Log.d(TAG,"switching to category");
			replaceFragment((Fragment)frg_type_cat/*,FragmentOperation.REPLACE*/);
			
		}
	}


	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	

}
