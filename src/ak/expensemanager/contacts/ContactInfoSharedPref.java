package ak.expensemanager.contacts;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ak.expensemanager.debug.IDebugTag;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class ContactInfoSharedPref implements IContactInfo{

	final static String TAG =  IDebugTag.ankitTag + ContactInfoSharedPref.class.getSimpleName();
	final static String SHARED_PREFS = "ContactInfo";
	SharedPreferences prefs =null;
	Context context;
	
	public ContactInfoSharedPref(Context context){
		this.context = context;
		prefs = context.getSharedPreferences(SHARED_PREFS, 0);
	} 
	
	
	
	@Override
	public synchronized boolean  addContact(String name, String number) {
		Log.d(TAG,"add contact called with name :"+name+" number = "+number);
		
		if(!searchContact(number)){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(number, name);
			if(editor.commit()){
				Toast.makeText(context, IDebugTag.CONTACT_ADDED, Toast.LENGTH_SHORT).show();
				return true;
			}
		}else{
			Toast.makeText(context, IDebugTag.CONTACT_EXISTS, Toast.LENGTH_SHORT).show();
			return false;
		}
		return false;
	}

	@Override
	public boolean searchContact(String number) {
		return prefs.contains(number);
	}
	
	@Override
	public boolean searchSubStringContact(String number){
		Log.d(TAG,"number = "+number);
		String num = number.toLowerCase();
		Map<String, String> map = (Map<String, String>) getAllContacts();
		for(Map.Entry<String, ?> entry :map.entrySet()){
			Log.d(TAG,"Number="+entry.getKey()+" Name="+entry.getValue());
			String contact = entry.getKey().toLowerCase();
			int foundBank = num.indexOf(contact);//entry.getKey().indexOf(number);
			if(foundBank != -1){
				Log.d(TAG,"bank found");
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized boolean removeContact(String number) {
		Log.d(TAG,"remove "+number );
		
		if(searchContact(number)){
			SharedPreferences.Editor editor = prefs.edit();
			editor.remove(number);
			if(editor.commit()){
				Toast.makeText(context, IDebugTag.CONTACT_REMOVED, Toast.LENGTH_SHORT).show();
				return true;
			}
			 
		}
		return false;
	}

	@Override
	public synchronized Map<String, ?> getAllContacts() {
		Log.d(TAG, "getAllContacts");
		Map<String, ?> map = (Map<String, ?>) prefs.getAll();
		
		for(Map.Entry<String, ?> entry :map.entrySet()){
			Log.d(TAG,"Number="+entry.getKey()+" Name="+entry.getValue());
		}
			return map;
	}

	
	
}
