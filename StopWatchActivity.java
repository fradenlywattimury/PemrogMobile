package mobile.amikom.id.ac.workout.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.daily.workout.app.R;

public class StopWatchActivity extends SherlockActivity{
	
	// create object of ActionBar;
	ActionBar actionbar;
	
	// create object of views
	TextView txtTitle, txtTimer;
	Button btnStart, btnReset;
	ImageView flipper;
	
	// create object of WakeLock class
	WakeLock wl;
	
	// set variables as FLAGs
	boolean FLAG = false;
	boolean STOP = false;
	boolean START = true;
	
	// create object of handler class
	// and set variables for time
	private Handler myHandler = new Handler(); 
	private long startTime = 0L; 
	long timeInMillies = 0L; 
	long timeSwap = 0L; 
	long finalTime = 0L;
	
	// create variable to store data
	int WorkoutID;
	String Name, Time;
	ArrayList<String> Images = new ArrayList<String>();
	
	// create variable to store screen width and height
	int ScreenWidth = 0;
	int ScreenHeight = 0;

	ArrayList<ArrayList<Object>> data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stop_watch);
		
		// change actionbar title
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");    
		if ( 0 == titleId ) 
		        titleId = com.actionbarsherlock.R.id.abs__action_bar_title;
		
		// and change the title color to white
		TextView txtActionbarTitle = (TextView)findViewById(titleId);
		txtActionbarTitle.setTextColor(getResources().getColor(R.color.actionbar_title_color));
		
		// get values that passed from previous page
		Intent i = getIntent();
		WorkoutID = i.getIntExtra("workout_id", 0);
		Name = i.getStringExtra("name");
		Time = i.getStringExtra("time");
		
		// get actionbar and set navigation back on actionbar
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		// connect views object and views id on xml
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTimer = (TextView) findViewById(R.id.txtTimer);
		btnStart = (Button) findViewById(R.id.btnStart);
		btnReset = (Button) findViewById(R.id.btnReset);
		flipper = (ImageView) findViewById(R.id.flipper);
		
		// get PowerManager to keep screen on
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "SCREEN ON");
		
		
		//set the animation of the slideshow
		//flipper.setInAnimation (AnimationUtils.loadAnimation (this,
		//	            R.anim.fade_in_out));
		//flipper.setFlipInterval(2000);
		//flipper.startFlipping ();
		
		// call synctask to get images
		new getImages().execute();
		
		// listener for btnStart to start stopwatch
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!FLAG){
					wl.acquire();
					FLAG = START;
					btnStart.setText(getString(R.string.stop));
					btnReset.setEnabled(false);
					startTime = SystemClock.uptimeMillis(); 
					myHandler.postDelayed(updateTimerMethod, 0);
				}else{
					wl.release();
					FLAG = STOP;
					btnStart.setText(getString(R.string.start));
					btnReset.setEnabled(true);
					timeSwap += timeInMillies; 
					myHandler.removeCallbacks(updateTimerMethod);
				}
			}
		});
		
		// listener form btnReset to reset stopwatch
		btnReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startTime = 0L; 
				
				timeInMillies = 0L; 
				timeSwap = 0L; 
				finalTime = 0L;
				txtTimer.setText(getString(R.string.initial_time)); 
			}
		});
		
	}
	
	// asynctask class to fetch data from database in background
	public class getImages extends AsyncTask<Void, Void, Void>{
		ProgressDialog progress;
    	
    	@Override
		 protected void onPreExecute() {
		  // TODO Auto-generated method stub
    		
    		// show progress dialog when fetching data from database
    		progress= ProgressDialog.show(
    				StopWatchActivity.this, 
    				"", 
    				getString(R.string.loading_data), 
    				true);
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			getDataFromDatabase();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			// when finishing fetching data close progress dialog and show data to views
			progress.dismiss();
			
			txtTitle.setText(Name);
			
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			ScreenWidth = dm.widthPixels;
			ScreenHeight = ScreenWidth / 2 + 50;
			
			for(int i=0;i<Images.size();i++){
				FrameLayout fl = new FrameLayout(StopWatchActivity.this);
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ScreenWidth, ScreenHeight);
				fl.setLayoutParams(lp);
				
				ImageView imgWorkout = new ImageView(StopWatchActivity.this);
				imgWorkout.setScaleType(ScaleType.FIT_CENTER);
				int imagedata = getResources().getIdentifier(Images.get(i), "drawable", getPackageName());
				imgWorkout.setImageResource(imagedata);
				
			    fl.addView(imgWorkout, new ViewGroup.LayoutParams(
			    		ViewGroup.LayoutParams.MATCH_PARENT, 
			    		ViewGroup.LayoutParams.MATCH_PARENT));

			    flipper.setImageResource(imagedata);
			    
			}
			
			
		}
		
		// method to clear arraylist variable before used
		void clearData(){
			Images.clear();
		}
		
		// method to get data from database
	    public void getDataFromDatabase(){
	    	data = HomeActivity.dbWorkouts.getImages(WorkoutID);
	    	
	    	clearData();
	    	
	    	// store data to arraylist variable
	    	for(int i=0;i<data.size();i++){
	    		ArrayList<Object> row = data.get(i);
	    		
	    		Images.add(row.get(0).toString());
	    	}
	    	
	    }
	}
	
	// thread to run stopwatch
	private Runnable updateTimerMethod = new Runnable() {

		public void run() { 
			String timer = "";
			timeInMillies = SystemClock.uptimeMillis() - startTime; 
			finalTime = timeSwap + timeInMillies;
	
			int seconds = (int) (finalTime / 1000); 
			int minutes = seconds / 60; 
			seconds = seconds % 60; 
			int milliseconds = (int) (finalTime % 1000); 
			
			timer = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
			txtTimer.setText(timer);
			if(timer.equals(Time)){
				FLAG = STOP;
				btnStart.setText(getString(R.string.start));
				btnReset.setEnabled(true);
				myHandler.removeCallbacks(this);
			}else{
				myHandler.postDelayed(this, 0); 
			}
		}

		};

	
	// create listener when option menu clicked
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(wl.isHeld()){
			wl.release();
		}
	}
	
}