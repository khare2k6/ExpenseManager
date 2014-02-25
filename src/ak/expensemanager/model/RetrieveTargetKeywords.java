package ak.expensemanager.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class RetrieveTargetKeywords {

	final String WORDS="words.txt";
	final String TAG = RetrieveTargetKeywords.class.getSimpleName();
	

	ArrayList<String> listOfTargetWords = null;
	ArrayList<String> currencyList = new ArrayList<String>();
	AssetManager assetManager = null;
	Scanner scanner = null;
	static RetrieveTargetKeywords instance = null;
	
	private RetrieveTargetKeywords(Context context){
		assetManager = context.getAssets();
		try {
			scanner = new Scanner(assetManager.open(WORDS));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(scanner != null){
			listOfTargetWords = new ArrayList<String>();
			while(scanner.hasNext()){
				listOfTargetWords.add(scanner.nextLine());
			}
			populateCurrencyList();
		}
		
	}
	
	
	public static RetrieveTargetKeywords getInstance(Context context){
		if(instance == null){
			instance = new RetrieveTargetKeywords(context);
		}
		return instance;
	}
	
	/**
	 * Add currency words like Rs/INR
	 * */
	public void populateCurrencyList() {
		currencyList.add("rs.");
		currencyList.add("inr.");
		currencyList.add("rs");
		currencyList.add("inr");
	}

	/**
	 * Get the target word list
	 * from resource file
	 * */
	public ArrayList<String> getTargetWordsList() throws Exception{
		if(listOfTargetWords == null){
			Log.d(TAG,"target word list is null!");
			throw new Exception("Target word list is empty");
		}
		return listOfTargetWords;
	}
	
	/**
	 * Matches target keywords in given msg
	 * and returns true if any keyword matches.
	 * */
	public boolean isExpense(String msg){
		for(String targetWord: listOfTargetWords){
			if(msg.contains(targetWord)){
				for(String currency: currencyList){
					if(msg.contains(currency)){
						Log.d(TAG,"start the activity!");
						return true;
					}
				}
					
				
			}
		}
		return false;
	}
	/**
	 * To get the currency list like Rs/INR
	 * */
	public ArrayList<String> getCurrencyList(){
		return currencyList;
	}
}
