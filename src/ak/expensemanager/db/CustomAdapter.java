package ak.expensemanager.db;

import ak.expensemanager.R;
import ak.expensemanager.category.ILimitUpdate;
import ak.expensemanager.category.UpdateLimitSharedPref;
import ak.expensemanager.db.RetrieveExpenses.DbHelper;
import ak.expensemanager.debug.IDebugTag;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CustomAdapter extends SimpleCursorAdapter{

	ILimitUpdate limitUpdate= null;
	final int limit;
	final String TAG = CustomAdapter.class.getSimpleName();
	
	public CustomAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		limitUpdate = new UpdateLimitSharedPref(context);
		 limit = limitUpdate.getLimit();
	}

@Override
public void bindView(View view, Context context, Cursor cursor) {
	
	// set amount
	Integer amount = (int) cursor.getInt(cursor
			.getColumnIndex(DbHelper.C_AMOUNT));
	TextView tv_amt = (TextView) view.findViewById(R.id.tv_row_amount);
	tv_amt.setText(amount.toString());
	tv_amt.setTextColor(android.graphics.Color.BLACK);
	if (amount > limit) {
		Log.d(TAG, "amount = " + amount);
		tv_amt.setTextColor(android.graphics.Color.RED);
	}
	
	//set Category
	String col = cursor.getString(cursor.getColumnIndex(DbHelper.C_CATEGORY));
	TextView tv_catagory = (TextView) view.findViewById(R.id.tv_row_category);
	tv_catagory.setText(col);
	if(col.equalsIgnoreCase(IDebugTag.ATM_TRANS)){
		view.setBackgroundColor(Color.GREEN);
		Log.d(TAG,"setting color to background");
	}
	
	//set Notes
	String notes = cursor.getString(cursor.getColumnIndex(DbHelper.C_NOTES));
	TextView tv_notes = (TextView) view.findViewById(R.id.tv_row_notes);
	tv_notes.setText(notes);

	super.bindView(view, context, cursor);
}

}
