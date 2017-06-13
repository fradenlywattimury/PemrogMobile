package mobile.amikom.id.ac.workout.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.daily.workout.app.R;

public class DetailProgramActivity extends SherlockFragmentActivity{
	
	// create object of ActionBar;
	ActionBar actionbar;
	
	// create object of views
	FrameLayout lytImage;
	TextView txtTitle, txtCaption, txtSteps;
	Button btnStart, btnOptions;
	
	// create variables to store workout data
	int SelectedID, SelectedDayID = 0;
	int WorkoutID, Day_ID, ProgramID;
	String Image, Name, Time, Steps;
	
	// create variable to store screen width and height
	int ScreenWidth = 0;
	int ScreenHeight = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_program);
		
		// change actionbar title
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");    
		if ( 0 == titleId ) 
		        titleId = com.actionbarsherlock.R.id.abs__action_bar_title;
		
		// and change the title color to white
		TextView txtActionbarTitle = (TextView)findViewById(titleId);
		txtActionbarTitle.setTextColor(getResources().getColor(R.color.actionbar_title_color));
		
		// get actionbar
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		// connect views object with views id on xml
		lytImage = (FrameLayout) findViewById(R.id.lytImage);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtCaption = (TextView) findViewById(R.id.txtCaption);
		txtSteps = (TextView) findViewById(R.id.txtSteps);
		btnStart = (Button) findViewById(R.id.btnStart);
		btnOptions = (Button) findViewById(R.id.btnOptions);
		
		// get id value that is passed from previous page
		Intent i = getIntent();
		SelectedID = i.getIntExtra("selectedID", 0);
		
		// get screen width and height
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		ScreenWidth = dm.widthPixels;
		ScreenHeight = ScreenWidth / 2 + 50;
		
		// call asynctask to get data from database
		new getWorkoutDetail().execute();
		
		// listener for btnStart, open stop watch page when clicked
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DetailProgramActivity.this, StopWatchActivity.class);
				i.putExtra("workout_id", WorkoutID);
				i.putExtra("name", Name);
				i.putExtra("time", Time);
				startActivity(i);
			}
		});

		// listener for btnOptions, open option dialog when clicked
		btnOptions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				optionsDialog();
			}
		});
		
	}

	// listener for option menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	// back to previous page
	        case android.R.id.home:
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	// asynctask class to handle fetching data from database in background
	public class getWorkoutDetail extends AsyncTask<Void, Void, Void>{
		ProgressDialog progress;
    	
    	@Override
		 protected void onPreExecute() {
		  // TODO Auto-generated method stub
    		
    		// show progress dialog during fecthing data
    		progress= ProgressDialog.show(
    				DetailProgramActivity.this, 
    				"", 
    				getString(R.string.loading_data), 
    				true);
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			// call this method to fetch data from database
			getDataFromDatabase();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			// when finish fetching data, close progress dialog and show data
			progress.dismiss();
			
			FrameLayout fl = new FrameLayout(DetailProgramActivity.this);
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ScreenWidth, ScreenHeight);
			fl.setLayoutParams(lp);
			
			ImageView imgWorkout = new ImageView(DetailProgramActivity.this);
			imgWorkout.setScaleType(ScaleType.FIT_CENTER);
			int image = getResources().getIdentifier(Image, "drawable", getPackageName());
			imgWorkout.setImageResource(image);
			txtTitle.setText(Name);
			txtCaption.setText(Time);
			txtSteps.setText(Steps);
			
			fl.addView(imgWorkout, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.MATCH_PARENT));
			
			lytImage.addView(fl);
			
			
		}
		
		// method to get data from database
	    public void getDataFromDatabase(){
	    	ArrayList<Object> row = HomeActivity.dbPrograms.getDetail(SelectedID);
	    	
			// store data to variables
	    	ProgramID = Integer.parseInt(row.get(0).toString());
	    	WorkoutID = Integer.parseInt(row.get(1).toString());
	    	Name = row.get(2).toString();
	    	SelectedDayID = Integer.parseInt(row.get(3).toString());
	    	Image = row.get(4).toString();
	    	Time = row.get(5).toString().trim();
	    	Steps = row.get(6).toString();
	    	
	    }
	}
	
	// method to create options dialog
	void optionsDialog(){
		String title = getString(R.string.select_option);
		String[] options = getResources().getStringArray(R.array.options);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
		// set the dialog title
	    builder.setTitle(title);
	    
	    // specify the list array
	    builder.setItems(options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch(which){
				case 0:
					// open update dialog
					updateDialog();
					dialog.dismiss();
					break;
				case 1:
					// open delete dialog
					deleteDialog();
					dialog.dismiss();
					break;
				}
			}
		});
	    

	    // show dialog
		AlertDialog alert = builder.create();
		alert.show();
	}

	// method to create update dialog
	void updateDialog(){
		String title = getString(R.string.pick_day);
		String[] day_name = getResources().getStringArray(R.array.day_name);
		String positive = getString(R.string.update); 
		String negative = getString(R.string.cancel);
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
    	// set the dialog title
	    builder.setTitle(title);
	   
	    // specify the list array
		builder.setSingleChoiceItems(day_name, SelectedDayID, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				// get selected day
				SelectedDayID = which;
			}
		});
		
	    // set positive button
	    builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int id) {
	        	
	        	String[] day_name = getResources().getStringArray(R.array.day_name);
	        	
	        	// check if data already available in this day program
	        	boolean isAvailable = HomeActivity.dbPrograms.isDataAvailable(SelectedDayID, WorkoutID);

	        	// if data is not available update data to this day program, otherwise, show toast message
	        	if(!isAvailable){
		        	HomeActivity.dbPrograms.updateData(SelectedDayID, ProgramID);
		        	Toast.makeText(DetailProgramActivity.this, getString(R.string.success_update), Toast.LENGTH_SHORT).show();
		        	getSupportFragmentManager()
					.beginTransaction().
					remove(HomeActivity.programListFrag)
					.commit();
		        	finish();
	        	}else{
	        		Toast.makeText(DetailProgramActivity.this, getString(R.string.failed_add)+" "+day_name[SelectedDayID], Toast.LENGTH_SHORT).show();
	        	}
	        }
	    });
	    
	    // set negative button
	    builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int id) {
	        	// close update dialog if cancel button clicked
	        	dialog.dismiss();
	        }
	    });

	    // show dialog
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	// method to create delete dialog
	void deleteDialog(){
		String title = getString(R.string.confirm);
		String message = getString(R.string.delete_message);
		String positive = getString(R.string.delete); 
		String negative = getString(R.string.cancel);
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
    	// set the dialog title
	    builder.setTitle(title);
	    // set the dialog message
	    builder.setMessage(message);
		
	    // set positive button
	    builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int id) {
	        	
	        	// delete data from database if delete button clicked
	        	HomeActivity.dbPrograms.deleteData(ProgramID);
	        	Toast.makeText(DetailProgramActivity.this, getString(R.string.success_delete), Toast.LENGTH_SHORT).show();
	        	getSupportFragmentManager()
				.beginTransaction().
				remove(HomeActivity.programListFrag)
				.commit();
	        	finish();
	        }
	    });
	    
	    // set negative button
	    builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int id) {
	        	// close dialog if cancel button clicked
	        	dialog.dismiss();
	        }
	    });

	    // show dialog
		AlertDialog alert = builder.create();
		alert.show();
	}
}

