package ak.expensemanager.category;

import ak.expensemanager.contacts.ContactInfoSharedPref;
import ak.expensemanager.debug.IDebugTag;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UpdateLimitSharedPref implements ILimitUpdate{

	
	final static String TAG =  IDebugTag.ankitTag + ContactInfoSharedPref.class.getSimpleName();
	final static String SHARED_PREFS = "LimitInfo";
	SharedPreferences prefs =null;
	Context context;
	
	public UpdateLimitSharedPref(Context context){
		this.context = context;
		prefs = context.getSharedPreferences(SHARED_PREFS, 0);
	}
	@Override
	public int getLimit() {
		return prefs.getInt(IDebugTag.DEFINED_LIMIT, IDebugTag.LIMIT);
	}

	@Override
	public boolean setLimit(int limit) {
		Editor edit = prefs.edit();
		edit.putInt(IDebugTag.DEFINED_LIMIT, limit);
		return edit.commit();
	}

	

}
