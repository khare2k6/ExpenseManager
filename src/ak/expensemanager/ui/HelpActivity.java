package ak.expensemanager.ui;


import ak.expensemanager.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends Activity {

	ImageButton img_left,img_right;
	OnClickListener btn_click_listener;
	ImageView imgMain;
	int page = 0;
	TextView tvTitle;
	TextView tvDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ankit","onCreate");
		setContentView(R.layout.layout_help);
		img_left = (ImageButton)findViewById(R.id.imgbtnleftbutton);
		img_right = (ImageButton)findViewById(R.id.imgbtnrightbutton);
		tvTitle = (TextView)findViewById(R.id.tv_title);
		tvDescription =(TextView)findViewById(R.id.tv_maintext);
		imgMain = (ImageView)findViewById(R.id.img);
		btn_click_listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.imgbtnleftbutton:
					page --;
					if(page < 0)
						page = 0;
					break;

				case R.id.imgbtnrightbutton:
					page++;
					if(page > 6 )
						page = 6;
					break;
				}
				changePage(page);
			}
		};
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	void changePage(int temppage){
		switch(temppage){
		case 0:
			tvTitle.setText(R.string.title_sms_transaction);
			imgMain.setImageResource(R.drawable.smstransaction);
			tvDescription.setText(R.string.desription_sms_transaction);
			break;
		case 1:
			tvTitle.setText(R.string.title_manual_transaction);
			imgMain.setImageResource(R.drawable.manualtransaction);
			tvDescription.setText(R.string.desription_manual_transaction);
			break;
		case 2:
			tvTitle.setText(R.string.title_menu_options);
			imgMain.setImageResource(R.drawable.menuoptions_1);
			tvDescription.setText(R.string.desription_menu_options);
			break;
		case 3:
			tvTitle.setText(R.string.title_add_category);
			imgMain.setImageResource(R.drawable.addcategory);
			tvDescription.setText(R.string.desription_add_category);
			break;
		case 4:
			tvTitle.setText(R.string.title_add_bank);
			imgMain.setImageResource(R.drawable.addbank);
			tvDescription.setText(R.string.desription_add_bank);
			break;
		case 5:
			tvTitle.setText(R.string.title_delete_entry);
			imgMain.setImageResource(R.drawable.deleterecord);
			tvDescription.setText(R.string.desription_delete_record);
			break;
		case 6:
			tvTitle.setText(R.string.title_atm_tx_enable);
			imgMain.setImageResource(R.drawable.atmmode);
			tvDescription.setText(R.string.desription_atm_tx_mode);
			break;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		Log.d("ankit","onResume");
		super.onResume();
		img_left.setOnClickListener(btn_click_listener);
		img_right.setOnClickListener(btn_click_listener);
	}

}
