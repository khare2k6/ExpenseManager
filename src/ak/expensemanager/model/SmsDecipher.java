package ak.expensemanager.model;

import java.text.DecimalFormat;
import java.text.ParseException;

import android.util.Log;

public class SmsDecipher {
	public static final String TAG = "SmsDecipher";
	
	int mAmount = -1;
	
	public SmsDecipher(String sms) {
		DecipherAmountFromMessage(sms);
	}
	
	private void DecipherAmountFromMessage(String sms) {
		if (sms == null) {
			return;
		}
		
		int startIndex = sms.indexOf("INR");
		int endIndex = sms.indexOf(".");
		int credited = sms.indexOf("credited");
		if (credited != -1 || startIndex == -1 || endIndex == -1) {
			Log.d(TAG, "credited msg..No action OR no debited msg");
			mAmount = -1;
		} else {
			String amount = sms.substring(startIndex + 3, endIndex);
			Log.d(TAG, "amount in String =" + amount);
			Number number = null;
			DecimalFormat format = new DecimalFormat("##,##,###");
			try {
				number = format.parse(amount);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mAmount = number.intValue();
		}
	}
	
	public int getAmount() {
		return mAmount;
	}
}
