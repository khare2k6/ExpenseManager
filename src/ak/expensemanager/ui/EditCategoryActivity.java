package ak.expensemanager.ui;



import java.util.Arrays;
import java.util.Map;

import ak.expensemanager.R;
import ak.expensemanager.category.CategoryInfoSharedPref;
import ak.expensemanager.category.ICategory;
import ak.expensemanager.category.ILimitUpdate;
import ak.expensemanager.category.UpdateLimitSharedPref;
import ak.expensemanager.debug.IDebugTag;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditCategoryActivity extends Activity {

	final static String TAG = IDebugTag.ankitTag + MainActivity.class.getSimpleName();
	Button btn_addCategory = null;
	Button btn_updateLimit = null;
	EditText et_addCategory = null;
	EditText et_updateLimit = null;
	View.OnClickListener listner = null;
	ICategory categoryInfo = null;
	ListView lv_categoryList = null;
	ArrayAdapter<String>adapter = null;
	String delete_category = null;
	ILimitUpdate updatelimit = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_category);
		
		btn_addCategory = (Button)findViewById(R.id.btn_addCategory);
		et_addCategory = (EditText)findViewById(R.id.et_addCategory);
		et_updateLimit = (EditText)findViewById(R.id.et_updateLimit);
		lv_categoryList = (ListView)findViewById(R.id.lv_category);
		btn_updateLimit = (Button)findViewById(R.id.btn_updateLimit);
		init();
	}
	
	/**
	 * Factory types.
	 * DB might be shared pref or sql lite or anything else
	 * */
	void init(){
		categoryInfo = new CategoryInfoSharedPref(this);
		updatelimit =  new UpdateLimitSharedPref(this);
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		Log.d(TAG,"onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
		
		listner = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				switch(v.getId()){
				case R.id.btn_addCategory:
					Log.d(TAG,"add category clicked");
					if(categoryInfo == null)
						init();
					
					if(et_addCategory.getText().toString().isEmpty()){
						Toast.makeText(EditCategoryActivity.this, IDebugTag.FILL_CATEGORY, Toast.LENGTH_SHORT).show();
						return;
					}else{
						categoryInfo.addCategory(et_addCategory.getText().toString());
					}
					break;
				case R.id.btn_updateLimit:
					
					if(et_updateLimit.getText().toString().isEmpty()){
						Toast.makeText(EditCategoryActivity.this, "Empty!", Toast.LENGTH_SHORT).show();
						return;
					}else{
						
						if(updatelimit.setLimit(Integer.parseInt(et_updateLimit.getText().toString())))
							Toast.makeText(EditCategoryActivity.this, "Updated", Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(EditCategoryActivity.this, "Limit not set!", Toast.LENGTH_SHORT).show();
					}
					break;
				}
			}
		};
		
		btn_addCategory.setOnClickListener(listner);
		btn_updateLimit.setOnClickListener(listner);
		showCategoryList();
		lv_categoryList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				TextView tv = (TextView)view;
				//Toast.makeText(EditCategoryActivity.this, tv.getText(), Toast.LENGTH_SHORT).show();
				delete_category = (String) tv.getText();
				//categoryInfo.removeCategory(delete_category);
			
				return false;
			}
		});
		registerForContextMenu(lv_categoryList);
		et_updateLimit.setHint(((Integer)updatelimit.getLimit()).toString());
		
	}

	
	



	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			categoryInfo.removeCategory(delete_category);
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "Delete");
	}

	@Override
	protected void onStop() {
		super.onStop();
		btn_addCategory.setOnClickListener(null);
		lv_categoryList.setAdapter(null);
	}


	void showCategoryList(){
		Map contactsMap = categoryInfo.getAllCategory();
		Object [] contactsObjArr =  contactsMap.keySet().toArray();
		String [] contacts = Arrays.copyOf(contactsObjArr, contactsObjArr.length,String[].class);
		adapter = new ArrayAdapter<String>(EditCategoryActivity.this, android.R.layout.simple_list_item_1,contacts);
		lv_categoryList.setAdapter(adapter);
	}
	

}
