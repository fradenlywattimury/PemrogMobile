package mobile.amikom.id.ac.workout.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.daily.workout.app.R;

public class AboutActivity extends SherlockActivity{
	
	// create object of ActionBar;
	ActionBar actionbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		// change actionbar title
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");    
		if ( 0 == titleId ) 
		        titleId = com.actionbarsherlock.R.id.abs__action_bar_title;
		
		// and change the title color to white
		TextView txtActionbarTitle = (TextView)findViewById(titleId);
		txtActionbarTitle.setTextColor(getResources().getColor(R.color.actionbar_title_color));
		
		// get actionbar and set navigation back on actionbar
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		
	}

	// create listener when option menu clicked
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	// back to previous page
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	

}