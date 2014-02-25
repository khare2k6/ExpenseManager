package ak.expensemanager.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Expense implements Parcelable{

	private int mAmount;
	private Category mCategory;
	private String mNote;
	private Calendar mCalendar= Calendar.getInstance();
	
	public static final String TAG = "ankit"+Expense.class.getSimpleName();
	public Expense(int amount, Category category, String note, Calendar cal) {
		mAmount = amount;
		mCategory = category;
		mNote = note;
		mCalendar = cal; 
	}
	
	public Expense(Parcel in){
		mAmount = in.readInt();
		mCategory = in.readParcelable(Category.class.getClassLoader());
		mNote = in.readString();
		mCalendar.setTimeInMillis(in.readLong());
	}
	public int getAmount() {
		return mAmount;
	}

	public Category getCategory() {
		return mCategory;
	}

	public String getNote() {
		return mNote;
	}

	public Calendar getCalendar() {
		return mCalendar;
	}

	public static Creator<Expense>CREATOR = new Creator<Expense>() {
		
		@Override
		public Expense[] newArray(int size) {
			return new Expense[size];
		}
		
		@Override
		public Expense createFromParcel(Parcel source) {
			return new Expense(source);
		}
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub`
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mAmount);
		dest.writeParcelable(mCategory, 0);
		dest.writeString(mNote);
		dest.writeLong(mCalendar.getTimeInMillis());
		
	}
	
	// TODO: write equals() and hashcode();
}
