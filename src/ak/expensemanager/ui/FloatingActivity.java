package ak.expensemanager.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import ak.expensemanager.R;
import ak.expensemanager.category.CategoryInfoSharedPref;
import ak.expensemanager.category.ICategory;
import ak.expensemanager.db.IRetrieveExpenses;
import ak.expensemanager.db.RetrieveExpenses;
import ak.expensemanager.debug.IDebugTag;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class FloatingActivity extends Activity{

	final static String TAG = IDebugTag.ankitTag + FloatingActivity.class.getSimpleName();
	String msg = null;
	EditText et_amount = null;
	Spinner spinner = null;
	ArrayAdapter<String> adapter = null;
	String [] arrCategory = null ;
	int amt;
	EditText et_notes = null;
	Button btn_submit = null;
	IRetrieveExpenses expenses = null;
	ICategory category = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		setContentView(R.layout.activity_floating);
		spinner = (Spinner)findViewById(R.id.spn_category);
		et_amount = (EditText)findViewById(R.id.et_amount);
		et_notes = (EditText)findViewById(R.id.et_notes);
		btn_submit = (Button)findViewById(R.id.btn_submitExp);
		expenses = new RetrieveExpenses(this);
		category = new CategoryInfoSharedPref(this);

        Window window = getWindow();
        //window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER_HORIZONTAL);
    
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG,"onNewintent");
		msg = intent.getStringExtra(IDebugTag.MESSAGE);
		Log.d(TAG,"Msg rx:"+msg);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		adapter = null;
		spinner.setAdapter(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		onNewIntent(getIntent());
	
		if(msg != null)
			et_amount.setText(((Integer)getAmountFromMessage()).toString());
		else
			et_amount.setHint("100");
		ArrayList<String> list = getCategoryList();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		spinner.setAdapter(adapter);
		if(spinner.getCount() == 0){
			Toast.makeText(this, "Adding Default Category!",Toast.LENGTH_LONG).show();
			category.addCategory(IDebugTag.DEFAULT_CATEGORY);
			finish();

		}
		
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG,"click: "+spinner.getSelectedItem().toString()+" and "+et_notes.getText().toString());
				if(et_amount.getText().toString().isEmpty()){
					Toast.makeText(FloatingActivity.this,"Please Enter Amount spent",
							Toast.LENGTH_SHORT).show();
					return;
				}
				expenses.addExpense(System.currentTimeMillis(), getAmountFromEditText(), spinner.getSelectedItem().toString(),
						et_notes.getText().toString());
				finish();
			}
		});
	}

	private ArrayList<String> getCategoryList() {
		ArrayList<String> list = new ArrayList<String>();
		list = new ArrayList<String>(category.getAllCategory().keySet());
		/*list.add("Fuel");
		list.add("Eating Out");
		list.add("Misc");*/
		
		return list;
	}

	int getAmountFromEditText(){
		String amt = et_amount.getText().toString();
		if(amt.isEmpty()){
			Toast.makeText(this, "No Amount entered", Toast.LENGTH_SHORT).show();
			return 0;
		}else{
			return Integer.parseInt(amt);
		}
		
	}
	
	
	int getAmountFromMessage(){
		int startIndex = msg.indexOf("INR");
		int endIndex = msg.indexOf(".");
		int credited =msg.indexOf("credited");
		if(credited != -1 || startIndex == -1 || endIndex == -1){
			Log.d(TAG,"credited msg..No action OR no debited msg");
			finish();
			return 0;
		}
		String amount = msg.substring(startIndex+3, endIndex);
		Log.d(TAG,"amount in String ="+amount);
		Number number= null;
		DecimalFormat format = new DecimalFormat("##,##,###");
		try {
			 number = format.parse(amount);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		amt =  number.intValue();
		return amt;
	}
	


}
