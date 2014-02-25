package ak.expensemanager.receiver;

import java.util.Calendar;

import ak.expensemanager.contacts.ContactInfoSharedPref;
import ak.expensemanager.contacts.IContactInfo;
import ak.expensemanager.debug.IDebugTag;
import ak.expensemanager.model.Expense;
import ak.expensemanager.model.RetrieveTargetKeywords;
import ak.expensemanager.model.SmsDecipher;
import ak.expensemanager.ui.FloatingActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	final static String TAG = IDebugTag.ankitTag +SmsReceiver.class.getSimpleName();
	Bundle bundle = null;
	IContactInfo contactInfo = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG,"new sms received..");
		bundle = intent.getExtras();
		
		if(bundle !=null){
			Object [] pduObjects = (Object[]) bundle.get("pdus");
			for(int i =0;i<pduObjects.length;i++){
				SmsMessage message = SmsMessage.createFromPdu((byte[])pduObjects[i]);
				String msg = message.getMessageBody();
				String num = message.getDisplayOriginatingAddress();
				Log.d(TAG,"msg:"+msg+" number = "+num);
				//Toast.makeText(context, "Message:"+msg+"  Num:"+num, Toast.LENGTH_SHORT).show();
				if(searchSender(msg,num,context)){
					Log.d(TAG,"sender :"+num+" found,starting activity");
				
					if(RetrieveTargetKeywords.getInstance(context).isExpense(msg.toLowerCase())){
						Intent intentAct = new Intent(context,FloatingActivity.class);
						intentAct.putExtra(IDebugTag.MESSAGE, msg);
						intentAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Expense exp = new Expense(new SmsDecipher(msg, context).getAmount(),
								null, "", Calendar.getInstance());
						Bundle bundle = new Bundle();
						bundle.putParcelable(IDebugTag.PARCEL, exp);
						intentAct.putExtra(IDebugTag.NEW_ENTRY_BUNDLE, bundle);
						
						context.startActivity(intentAct);
					}
				}		
			}
		}
		
	}
	
	private boolean searchSender(String msg, String num,Context context) {
			Log.d(TAG,"search sender..");
			createObjects(context);
			//return contactInfo.searchContact(num);// temp testing
			return contactInfo.searchSubStringContact(num);
			
	}

	private void createObjects(Context context){
		contactInfo = new ContactInfoSharedPref(context);
	}
	
	
}
