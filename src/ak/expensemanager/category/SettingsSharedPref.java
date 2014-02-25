package ak.expensemanager.category;

import java.util.Calendar;

import ak.expensemanager.contacts.ContactInfoSharedPref;
import ak.expensemanager.debug.IDebugTag;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsSharedPref implements ILimitUpdate{

	
	final static String TAG =  IDebugTag.ankitTag + ContactInfoSharedPref.class.getSimpleName();
	final static String SHARED_PREFS = "LimitInfo";
	SharedPreferences prefs =null;
	Context context;
	
	public SettingsSharedPref(Context context){
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
	public int getSelectedYear(){
		return prefs.getInt(IDebugTag.SELECTED_YEAR,  Calendar.getInstance().get(Calendar.YEAR));
	}

	public boolean setSelectedYear(int selectedYear){
		Editor edit = prefs.edit();
		edit.putInt(IDebugTag.SELECTED_YEAR, selectedYear);
		return edit.commit();
	}
	public boolean getAtmTxEnabledMode(){
		return prefs.getBoolean(IDebugTag.Atm_Tx_mode,  false);
	}
	public boolean setAtmTxEnabledMode(boolean enabled){
		Editor edit = prefs.edit();
		edit.putBoolean(IDebugTag.Atm_Tx_mode, enabled);
		return edit.commit();
	}

}
