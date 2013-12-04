package ak.expensemanager.model;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class SmsDecipher {
	public static final String TAG = "SmsDecipher";

	int mAmount = -1;
	final int SPACE = 32;
	final int COMMA = 44;
	final int DOT = 46;
	final int ZERO = 48;
	final int ONE = 49;
	final int TWO = 50;
	final int THREE = 51;
	final int FOUR = 52;
	final int FIVE = 53;
	final int SIX = 54;
	final int SEVEN = 55;
	final int EIGHT = 56;
	final int NINE = 57;

	ArrayList<String> targetWordList = null;
	ArrayList<String> currencyList = null;
	RetrieveTargetKeywords retrieveWords = null;

	public SmsDecipher(String sms, Context context) {
		retrieveWords = RetrieveTargetKeywords.getInstance(context);
		init(context);
		DecipherAmountFromMessage(sms);
		
		
	}

	/**
	 * Initialize currency and target word list
	 * */
	public void init(Context context) {
		
		try {
			Log.d(TAG,"retrieveWords="+retrieveWords);
			targetWordList = 
					retrieveWords.getTargetWordsList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		currencyList = retrieveWords.getCurrencyList();
	}


	private void DecipherAmountFromMessage(String sms) {
		if (sms == null) {
			return;
		}
		mAmount = getAmtFromMsg(sms);

	}

	public int getAmount() {
		return mAmount;
	}

	/**
	 * Retrive amount from text msg
	 * */
	int getAmtFromMsg(String msg) {
		String lowerCaseMsg = msg.toLowerCase();

		/** list has target words like debited,purchased */
		for (String word : targetWordList) {

			/** search for debited,purchased such words in msg */
			if (lowerCaseMsg.contains(word)) {
				// int index = lowerCaseMsg.indexOf(word);
				for (String curr : currencyList) {

					/** search for Rs , INR now */
					if (lowerCaseMsg.contains(curr)) {
						int indexOfRsOrInr = lowerCaseMsg.indexOf(curr, 0);

						/** move the index so index is pointing at digits now */
						indexOfRsOrInr = indexOfRsOrInr + curr.length();
						return calculateAmountFromString(lowerCaseMsg,
								indexOfRsOrInr);
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Finds amount from msg where index is pointing to * Rs/INR
	 * */
	int calculateAmountFromString(String msg, int index) {
		StringBuilder sb = new StringBuilder();

		do {
			char a = msg.charAt(index++);
			switch (a) {
			case SPACE:
			case COMMA:
				continue;

			case ZERO:
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
			case FIVE:
			case SIX:
			case SEVEN:
			case EIGHT:
			case NINE:
				sb.append(a);
				continue;

			default:
				break;
			}

			break;

		} while (index < msg.length());

		int amt = Integer.parseInt(sb.toString());
		return amt;
	}

}
