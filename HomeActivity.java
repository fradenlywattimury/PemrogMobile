package mobile.amikom.id.ac.workout.activity;

import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.daily.workout.app.R;

import mobile.amikom.id.ac.workout.fragment.ProgramListFragment;
import mobile.amikom.id.ac.workout.fragment.ProgramsFragment;
import mobile.amikom.id.ac.workout.fragment.WorkoutListFragment;
import mobile.amikom.id.ac.workout.fragment.WorkoutsFragment;
import mobile.amikom.id.ac.workout.database.DBHelperPrograms;
import mobile.amikom.id.ac.workout.database.DBHelperWorkouts;

public class HomeActivity extends SherlockFragmentActivity
	implements WorkoutsFragment.OnCategorySelectedListener, WorkoutListFragment.OnWorkoutSelectedListener,
        ProgramsFragment.OnDaySelectedListener, ProgramListFragment.OnProgramSelectedListener {
	
	// create object of ActionBar
	ActionBar actionbar;
	
	// create DBHelper objects for workout and program database
	public static DBHelperWorkouts dbWorkouts;
	public static DBHelperPrograms dbPrograms;
	
	// create objects of fragments
	WorkoutsFragment workoutsFrag = new WorkoutsFragment();
	WorkoutListFragment workoutListFrag;
	
	ProgramsFragment programsFrag = new ProgramsFragment();
	static ProgramListFragment programListFrag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		// change actionbar title
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");    
		if ( 0 == titleId ) 
		        titleId = com.actionbarsherlock.R.id.abs__action_bar_title;
		
		// and change the title color to white
		TextView txtActionbarTitle = (TextView)findViewById(titleId);
		txtActionbarTitle.setTextColor(getResources().getColor(R.color.actionbar_title_color));
		
		dbWorkouts = new DBHelperWorkouts(this);
		dbPrograms = new DBHelperPrograms(this);
		
		/**
         * when this app's installed at the first time, code below will
         * copy database stored in assets to
         * /data/data/com.daily.workout.app/databases/
         */
        try {
        	dbWorkouts.createDataBase();
		}catch(IOException ioe){
			throw new Error("Unable to create database");
		}
		
        // then, the database will be open to use
		try{
			dbWorkouts.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
		
		/**
         * when this app's installed at the first time, code below will
         * copy database stored in assets to
         * /data/data/com.daily.workout.app/databases/
         */
        try {
        	dbPrograms.createDataBase();
		}catch(IOException ioe){
			throw new Error("Unable to create database");
		}
		
        // then, the database will be open to use
		try{
			dbPrograms.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
		
		// get ActionBar and set tab mode
		actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		// create tab for workouts
		Tab tabWorkouts = actionbar.newTab();
		tabWorkouts.setText(R.string.workouts);
		tabWorkouts.setTabListener(new CustomTabListener());
		actionbar.addTab(tabWorkouts);
		
		// create tab for programs
		Tab tabPrograms = actionbar.newTab();
		tabPrograms.setText(R.string.programs);
		tabPrograms.setTabListener(new CustomTabListener());
		actionbar.addTab(tabPrograms);
		
	}
	
	// create option menu
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// listener for option menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	// close application
	            finish();
	            return true;
	        case R.id.menuShare:
	        	// share google play link of this app to other app such as email, facebook, etc
	        	Intent iShare = new Intent(Intent.ACTION_SEND);
				iShare.setType("text/plain");
				iShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
				iShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.message)+" "+getString(R.string.gplay_web_url));
				startActivity(iShare.createChooser(iShare, getString(R.string.share_via)));
				return true;
	        case R.id.menuRate:
	        	// open google play app to ask user to rate & review this app
	        	Intent iRate = new Intent(Intent.ACTION_VIEW);
				iRate.setData(Uri.parse(getString(R.string.gplay_url)));
				startActivity(iRate);
				return true;
	        case R.id.menuAbout:
	        	// open About app page
	        	Intent iAbout = new Intent(this, AboutActivity.class);
				startActivity(iAbout);
				return true;
			default:
				return super.onOptionsItemSelected(item);
	    }
	}
	
	// create custom class for tab navigation
	private class CustomTabListener implements ActionBar.TabListener{

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
			if(tab.getPosition() == 0){	
				// set first tab with workout fragment
				getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, workoutsFrag, "WORKOUTSFRAG")
				.commit();
			}else{
				// set second tab with program fragment
				getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, programsFrag, "PROGRAMSFRAG")
				.commit();
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
		
	}

	
	// method from WorkoutsFragment to open workout list page
	@Override
	public void onCategorySelected(int selectedID) {
		// TODO Auto-generated method stub
		
		workoutListFrag = new WorkoutListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("selectedID", selectedID);
		workoutListFrag.setArguments(bundle);
		
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, workoutListFrag, "WORKOUTLISTFRAG")
		.addToBackStack(null)
		.commit();
	}

	// method from WorkoutlistFragment to open detail workout page
	@Override
	public void onWorkoutSelected(int selectedID) {
		// TODO Auto-generated method stub
		Intent i = new Intent(HomeActivity.this, DetailWorkoutActivity.class);
		i.putExtra("selectedID", selectedID);
		startActivity(i);
	}

	// method from ProgramsFragment to open program list page
	@Override
	public void onDaySelected(int selectedID) {
		// TODO Auto-generated method stub
		
		programListFrag = new ProgramListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("selectedDayID", selectedID);
		programListFrag.setArguments(bundle);
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, programListFrag, "PROGRAMLISTFRAG")
		.addToBackStack(null)
		.commit();
	}
	
	// close database before app destroy
	@Override
	protected void onDestroy() {
	   	// TODO Auto-generated method stub
		dbWorkouts.close();
		dbPrograms.close();
	   	super.onDestroy();
	}

	// method from ProgramListFragment to open detail program page
	@Override
	public void onProgramSelected(int selectedID) {
		// TODO Auto-generated method stub
		Intent i = new Intent(HomeActivity.this, DetailProgramActivity.class);
		i.putExtra("selectedID", selectedID);
		startActivity(i);
	}
	
	// method to handle fragment when back key pressed
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
	    if (keyCode == KeyEvent.KEYCODE_BACK){
	        if (getSupportFragmentManager().getBackStackEntryCount() == 0){
	            this.finish();
	            return false;
	        }else{
	            getSupportFragmentManager().popBackStack();
	            if(programsFrag.isVisible()){
	            	removeProgramsFragment();
		        	actionbar.setSelectedNavigationItem(0);
	            }
	            
	            if(workoutsFrag.isVisible()){
	            	removeWorkoutsFragment();
		        	actionbar.setSelectedNavigationItem(1);
	            }

	            return false;
	        }

	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	// method to remove ProgramsFragment if available
	public void removeProgramsFragment(){
	    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    Fragment currentProgramsFrag = getSupportFragmentManager().findFragmentByTag("PROGRAMSFRAG");

	    String fragName = "NONE";

	    if (currentProgramsFrag!=null)
	        fragName = currentProgramsFrag.getClass().getSimpleName();

	    if (currentProgramsFrag != null)
	        transaction.remove(currentProgramsFrag);
	    
	    transaction.commit();
	}
	
	// method to remove WorkoutsFragment if available
	public void removeWorkoutsFragment(){
	    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    Fragment currentWorkoutsFrag = getSupportFragmentManager().findFragmentByTag("WORKOUTSFRAG");

	    String fragName = "NONE";

	    if (currentWorkoutsFrag!=null)
	        fragName = currentWorkoutsFrag.getClass().getSimpleName();

	    if (currentWorkoutsFrag != null)
	        transaction.remove(currentWorkoutsFrag);

	    transaction.commit();
	}

}