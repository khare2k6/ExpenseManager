package ak.expensemanager.ui;

import java.util.Arrays;
import java.util.Map;

import ak.expensemanager.R;
import ak.expensemanager.contacts.ContactInfoSharedPref;
import ak.expensemanager.contacts.IContactInfo;
import ak.expensemanager.debug.IDebugTag;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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

public class MainActivity extends Activity {

	final static String TAG = IDebugTag.ankitTag + MainActivity.class.getSimpleName();
	Button btn_addContact = null;
	EditText et_addContact = null;
	View.OnClickListener listner = null;
	IContactInfo contactInfo = null;
	ListView lv_contatcsList = null;
	ArrayAdapter<String>adapter = null;
	 String delete_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_addContact = (Button)findViewById(R.id.btn_addContacts);
		et_addContact = (EditText)findViewById(R.id.et_addCotnact);
		lv_contatcsList = (ListView)findViewById(R.id.lv_contacts);
		init();
	}
	
	/**
	 * Factory types.
	 * DB might be shared pref or sql lite or anything else
	 * */
	void init(){
		contactInfo = new ContactInfoSharedPref(this);
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
				case R.id.btn_addContacts:
					Log.d(TAG,"add contact clicked");
					if(contactInfo == null)
						init();
					
					if(et_addContact.getText().toString().isEmpty()){
						Toast.makeText(MainActivity.this, IDebugTag.FILL_CONTACT, Toast.LENGTH_SHORT).show();
						return;
					}else{
						contactInfo.addContact(IDebugTag.BANK, et_addContact.getText().toString());
					}
					break;
				}
			}
		};
		
		btn_addContact.setOnClickListener(listner);
		showContactList();
		lv_contatcsList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				TextView tv = (TextView)view;
				Toast.makeText(MainActivity.this, tv.getText(), Toast.LENGTH_SHORT).show();
				delete_number = (String) tv.getText();
				//categoryInfo.removeCategory(delete_category);
			
				return false;
			}
		});
		registerForContextMenu(lv_contatcsList);
		
		
	}

	
	

	

	@Override
	protected void onStop() {
		super.onStop();
		btn_addContact.setOnClickListener(null);
		lv_contatcsList.setAdapter(null);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			contactInfo.removeContact(delete_number);
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
	
	void showContactList(){
		Map contactsMap = contactInfo.getAllContacts();
		Object [] contactsObjArr =  contactsMap.keySet().toArray();
		String [] contacts = Arrays.copyOf(contactsObjArr, contactsObjArr.length,String[].class);
		adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,contacts);
		lv_contatcsList.setAdapter(adapter);
	}
	

}
