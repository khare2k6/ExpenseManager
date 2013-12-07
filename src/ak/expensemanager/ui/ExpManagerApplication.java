package ak.expensemanager.ui;

import java.util.ArrayList;

import ak.expensemanager.category.CategoryInfoSharedPref;
import ak.expensemanager.category.ICategory;
import ak.expensemanager.contacts.ContactInfoSharedPref;
import ak.expensemanager.contacts.IContactInfo;
import ak.expensemanager.debug.IDebugTag;
import android.app.Application;
import android.util.Log;

public class ExpManagerApplication extends Application{

	ICategory category = null;
	IContactInfo contactInfo = null;
	final static String TAG = IDebugTag.ankitTag +ExpManagerApplication.class.getSimpleName();
			
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG,"OnCreate");
    	
		category = new CategoryInfoSharedPref(this);
		contactInfo = new ContactInfoSharedPref(this);
		
	    ArrayList<String>listCategory = new ArrayList<String>(category.getAllCategory().keySet());
	    /*nothing in category,default category should be there*/
	    if(listCategory.size() == 0){
	    	Log.d(TAG,"Category Empty..Entering default!");
	    	for(int i = 0;i<IDebugTag.DEFAULT_CATEGORY.length ; i++){
	    	category.addCategory(IDebugTag.DEFAULT_CATEGORY[i]);
	    	}
	    }
	    ArrayList<String>listContact = new ArrayList<String>(contactInfo.getAllContacts().keySet());
	    if(listContact.size() == 0){
	    	Log.d(TAG,"Contact Empty..Entering default!");
	    	for(int i = 0;i<IDebugTag.DEFAULT_BANKS.length ; i++){
	    		contactInfo.addContact(IDebugTag.BANK,IDebugTag.DEFAULT_BANKS[i]);
	    	}
	    }
	}
	



}
