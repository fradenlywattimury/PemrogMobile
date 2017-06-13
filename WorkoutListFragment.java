package mobile.amikom.id.ac.workout.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.daily.workout.app.R;

import mobile.amikom.id.ac.workout.activity.HomeActivity;
import mobile.amikom.id.ac.workout.adapter.WorkoutListAdapter;

public class WorkoutListFragment extends SherlockFragment {
	OnWorkoutSelectedListener mCallback;

	// create object of listview
	ListView list;
	
	// create object of custom adapter class
	WorkoutListAdapter wla;
	
	// create arraylist variable to store object data from database
	ArrayList<ArrayList<Object>> data;
	
	// create variables to store selected value
	int SelectedID, SelectedDayID = 0, SelectedPosition;
	
	// create arraylist variables to store data from database
	public static ArrayList<Integer> WorkoutID = new ArrayList<Integer>();
	public static ArrayList<String> Name = new ArrayList<String>();
	public static ArrayList<String> Image = new ArrayList<String>();
	public static ArrayList<String> Time = new ArrayList<String>();
	static ArrayList<String> Steps = new ArrayList<String>();
	
	// create interface for listener method
	public interface OnWorkoutSelectedListener{
		public void onWorkoutSelected(int selectedID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_list, container, false);
		
		// connect object of listview and listview id on xml
		list = (ListView) v.findViewById(R.id.list);
		
		wla = new WorkoutListAdapter(getActivity());
		
		// get value that passed from previous page
		Bundle bundle = this.getArguments();
		SelectedID = bundle.getInt("selectedID", 0);
		
		
		// call asynctask class to get data from database
		new getWorkoutList().execute();
		
		// listener to get selected id when list item clicked
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				mCallback.onWorkoutSelected(WorkoutID.get(position));

		        // Set the item as checked to be highlighted when in two-pane layout
		        list.setItemChecked(position, true);
			}
		});
		
		// listener to get selected id when list item long clicked
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				SelectedPosition = position;
				addDialog();
				return false;
			}
		});
		return v;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnWorkoutSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
	
	// asynctask class that is used to fetch data from database in background
	public class getWorkoutList extends AsyncTask<Void, Void, Void>{
		
		ProgressDialog progress;
    	
    	@Override
		 protected void onPreExecute() {
		  // TODO Auto-generated method stub
    		
    		// show progress dialog when fetching data from database
    		progress= ProgressDialog.show(
    				getActivity(), 
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
			
			// when finishing fetching data, close progress dialog and show data on listview
			// if available, otherwise show toast message
			progress.dismiss();
			if(WorkoutID.size() != 0){
				list.setAdapter(wla);
			}else{
				Toast.makeText(getActivity(), getString(R.string.no_data_workout), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	// method to create arraylist variables before used
	void clearData(){
		WorkoutID.clear();
		Name.clear();
		Image.clear();
		Time.clear();
		Steps.clear();
	}
	
	// method to fetch data from database
    public void getDataFromDatabase(){
    	data = HomeActivity.dbWorkouts.getWorkoutListByCategory(SelectedID);
    	
    	clearData();
    	
    	// store data to arraylist variables
    	for(int i=0;i<data.size();i++){
    		ArrayList<Object> row = data.get(i);
    		
    		WorkoutID.add(Integer.parseInt(row.get(0).toString()));
    		Name.add(row.get(1).toString());
    		Image.add(row.get(2).toString());
    		Time.add(row.get(3).toString());
    		Steps.add(row.get(4).toString());
    	}
    	
    }
    
    // method to create add dialog
	void addDialog(){
		String title = getString(R.string.pick_day);
		String[] day_name = getResources().getStringArray(R.array.day_name);
		String positive = getString(R.string.add);
		String negative = getString(R.string.cancel);
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	
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
	        	boolean isAvailable = HomeActivity.dbPrograms.isDataAvailable(SelectedDayID, WorkoutID.get(SelectedPosition));
	        	
	        	// if data is not available add data to this day program, otherwise, show toast message
	        	if(!isAvailable){
	        		HomeActivity.dbPrograms.addData(WorkoutID.get(SelectedPosition), Name.get(SelectedPosition), 
	        								SelectedDayID, Image.get(SelectedPosition), 
	        				Time.get(SelectedPosition), Steps.get(SelectedPosition));
	        		Toast.makeText(getActivity(), getString(R.string.success_add)+" "+day_name[SelectedDayID], Toast.LENGTH_SHORT).show();
		        }else{
		        	Toast.makeText(getActivity(), getString(R.string.failed_add)+" "+day_name[SelectedDayID], Toast.LENGTH_SHORT).show();
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

}
