package ak.expensemanager.category;

import java.util.Map;

import ak.expensemanager.contacts.ContactInfoSharedPref;
import ak.expensemanager.debug.IDebugTag;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class CategoryInfoSharedPref implements ICategory{
	final static String TAG =  IDebugTag.ankitTag + ContactInfoSharedPref.class.getSimpleName();
	final static String SHARED_PREFS = "CategoryInfo";
	SharedPreferences prefs =null;
	Context context;
	
	public CategoryInfoSharedPref(Context context){
		this.context = context;
		prefs = context.getSharedPreferences(SHARED_PREFS, 0);
	} 
	
	
	@Override
	public synchronized boolean  addCategory(String name) {
		Log.d(TAG,"add category called with name :"+name);
		
		if(!searchCatetory(name)){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(name, 1);
			if(editor.commit()){
				Toast.makeText(context, IDebugTag.CATEGORY_ADDED, Toast.LENGTH_SHORT).show();
				return true;
			}
		}else{
			Toast.makeText(context, IDebugTag.CATEGORY_EXISTS, Toast.LENGTH_SHORT).show();
			return false;
		}
		return false;
	}

	@Override
	public boolean searchCatetory(String number) {
		return prefs.contains(number);
	}

	@Override
	public synchronized boolean removeCategory(String name) {
		Log.d(TAG,"remove "+name );
		
		if(searchCatetory(name)){
			SharedPreferences.Editor editor = prefs.edit();
			editor.remove(name);
			if(editor.commit()){
				Toast.makeText(context, IDebugTag.CATEGORY_REMOVED, Toast.LENGTH_SHORT).show();
				return true;
			}
			 
		}
		return false;
	}

	@Override
	public synchronized Map<String, ?> getAllCategory() {
		Log.d(TAG, "getAllCategory");
		Map<String, ?> map = (Map<String, ?>) prefs.getAll();
		
		for(Map.Entry<String, ?> entry :map.entrySet()){
			Log.d(TAG,"Name="+entry.getKey()+" value="+entry.getValue());
		}
			return map;
	}






}
