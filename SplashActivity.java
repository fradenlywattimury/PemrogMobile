/**
 * Application Name			: Daily Workout App
 * Author					: Taufan Erfiyanto
 * Website					: http://pongodev.com
 * Date						: June 28th, 2013
 */

package mobile.amikom.id.ac.workout.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.daily.workout.app.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends SherlockActivity {
	
	// create object of progressbar
	ProgressBar prgLoading;
	
	// set variable for progress bar to 0
	int progress = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // connect view object with view id on xml
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
    	prgLoading.setProgress(progress);

    	// call asynctask class
		new Loading().execute();
    }
    
    
    // asynctask class to run progress bar
    public class Loading extends AsyncTask<Void, Void, Void>{
    	
    	@Override
		 protected void onPreExecute() {
		  // TODO Auto-generated method stub
    		
    	}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			while(progress < 100){
				try {
					Thread.sleep(1000);
					progress += 30;
					prgLoading.setProgress(progress);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			// call Home page when progress bar finish
			Intent i = new Intent(SplashActivity.this, HomeActivity.class);
			startActivity(i);
		}
    }
}