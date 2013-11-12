package ak.expensemanager.ui;

import ak.expensemanager.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity {

	Button  btn_addBank = null;
	Button  btn_addcategory = null;
	Button  btn_addTransaction = null;
	Button  btn_viewExp = null;
	Button  btn_updateLimit = null;
	Button  btn_exit = null;
	
	ClickListner listner = null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridlayout_mainmenu);
		btn_addBank = (Button)findViewById(R.id.btn_addBank);
		btn_addcategory = (Button)findViewById(R.id.btn_addCategory);
		btn_addTransaction = (Button)findViewById(R.id.btn_addTransaction);
		btn_viewExp = (Button)findViewById(R.id.btn_showExp);
		btn_updateLimit = (Button)findViewById(R.id.btn_updateLimit);
		btn_exit = (Button)findViewById(R.id.btn_exit);
		
		listner = new ClickListner();
	
		
	}



	@Override
	protected void onResume() {
		super.onResume();
		if(listner == null)
			listner = new ClickListner();
		btn_addBank.setOnClickListener(listner);
		btn_addcategory.setOnClickListener(listner);
		btn_addTransaction.setOnClickListener(listner);
		btn_viewExp.setOnClickListener(listner);
		btn_updateLimit.setOnClickListener(listner);
		btn_exit.setOnClickListener(listner);
		
	}
	

	
	@Override
	protected void onPause() {
		super.onPause();
		btn_addBank.setOnClickListener(null);
		btn_addcategory.setOnClickListener(null);
		btn_addTransaction.setOnClickListener(null);
		btn_viewExp.setOnClickListener(null);
		btn_updateLimit.setOnClickListener(null);
		btn_exit.setOnClickListener(null);
	}



	class ClickListner implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			case R.id.btn_addBank:
				startActivity(new Intent(MainMenu.this,MainActivity.class));
				break;


			case R.id.btn_addCategory:
			case R.id.btn_updateLimit:
				startActivity(new Intent(MainMenu.this,EditCategoryActivity.class));
				
				break;


			case R.id.btn_addTransaction:
				startActivity(new Intent(MainMenu.this,FloatingActivity.class));
				break;


			case R.id.btn_showExp:
				startActivity(new Intent(MainMenu.this,DisplayExpenses.class));
				break;
			
			case R.id.btn_exit:
				finish();
				break;


			}

	
		}

}
}
