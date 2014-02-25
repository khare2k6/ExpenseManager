package ak.expensemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
	private String mName;
	private int mLimit;
	
	public Category(String name, int limit) {
		mName = name;
		mLimit = limit;
	}
	
	public Category(Parcel in){
		mName = in.readString();
		mLimit = in.readInt();
	}
	
	public String getName() {
		return mName;
	}
	
	public int getLimit() {
		return mLimit;
	}

	public static Creator<Category> CREATOR = new Creator<Category>() {
		
		@Override
		public Category[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Category[size];
		}
		
		@Override
		public Category createFromParcel(Parcel source) {
			return new Category(source);
		}
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeInt(mLimit);
	}
	
	// TODO: write equals and hashcode
	
}
