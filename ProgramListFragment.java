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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.daily.workout.app.R;

import mobile.amikom.id.ac.workout.activity.HomeActivity;
import mobile.amikom.id.ac.workout.adapter.ProgramListAdapter;

public class ProgramListFragment extends SherlockFragment {
	OnProgramSelectedListener mCallback;
	
	// create object of listview
	ListView list;
	
	// create object of custom adapter class
	ProgramListAdapter wla;
	
	// create arraylist variable to store object data from database
	ArrayList<ArrayList<Object>> data;

	// create variables to store selected value
	int SelectedDayID = 0, SelectedPosition;

	// create arraylist variables to store data from database
	public static ArrayList<Integer> ProgramID = new ArrayList<Integer>();
	static ArrayList<Integer> WorkoutID = new ArrayList<Integer>();
	public static ArrayList<String> Name = new ArrayList<String>();
	public static ArrayList<String> Image = new ArrayList<String>();
	public static ArrayList<String> Time = new ArrayList<String>();
	
	// create interface for listener method
	public interface OnProgramSelectedListener{
		public void onProgramSelected(int selectedID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_list, container, false);
		
		// connect object of listview and listview id on xml
		list = (ListView) v.findViewById(R.id.list);
		
		wla = new ProgramListAdapter(getActivity());
		
		// get value that passed from previous page
		Bundle bundle = this.getArguments();
		SelectedDayID = bundle.getInt("selectedDayID", 0);
		
		// call asynctask class to get data from database
		new getWorkoutList().execute();
		
		// listener to get selected id when list item clicked
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				mCallback.onProgramSelected(ProgramID.get(position));

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
				optionsDialog();
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
            mCallback = (OnProgramSelectedListener) activity;
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
			if(ProgramID.size() != 0){
				list.setAdapter(wla);
			}else{
				Toast.makeText(getActivity(), getString(R.string.no_data_program), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	// method to create arraylist variables before used
	void clearData(){
		ProgramID.clear();
		WorkoutID.clear();
		Name.clear();
		Image.clear();
		Time.clear();
	}
	
	// method to fetch data from database
    public void getDataFromDatabase(){
    	data = HomeActivity.dbPrograms.getWorkoutListByDay(SelectedDayID);
    	
    	clearData();
    	
    	// store data to arraylist variables
    	for(int i=0;i<data.size();i++){
    		ArrayList<Object> row = data.get(i);
    		
    		ProgramID.add(Integer.parseInt(row.get(0).toString()));
    		WorkoutID.add(Integer.parseInt(row.get(1).toString()));
    		Name.add(row.get(2).toString());
    		Image.add(row.get(3).toString());
    		Time.add(row.get(4).toString());
    	}
    	
    }
    
    // method to create option dialog
    void optionsDialog(){
		String title = getString(R.string.select_option);
		String[] options = getResources().getStringArray(R.array.options);
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    
    	// set the dialog title
	    builder.setTitle(title);
	    
	    // specify the list array
	    builder.setItems(options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch(which){
				case 0:
					// show update dialog
					updateDialog();
					dialog.dismiss();
					break;
				case 1:
					// show delete dialog
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

	        	// if data is not available update data to this day program, otherwise, show toast message
	        	if(!isAvailable){
		        	HomeActivity.dbPrograms.updateData(SelectedDayID, ProgramID.get(SelectedPosition));
		        	Toast.makeText(getActivity(), getString(R.string.success_update), Toast.LENGTH_SHORT).show();
		        	getActivity().getSupportFragmentManager().popBackStack();
	        	}else{
	        		Toast.makeText(getActivity(), getString(R.string.failed_add)+" "+day_name[SelectedDayID], Toast.LENGTH_SHORT).show();
	        	}
	        }
	    });
	    
	    // set negative button
	    builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int id) {
	        	// close update dialog when cancel button clicked
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
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	
	    // set the dialog title
	    builder.setTitle(title);
	    // set the dialog message
	    builder.setMessage(message);
		
	    // set positive button
	    builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int id) {
	        	
	        	// delete data when delete button clicked
	        	HomeActivity.dbPrograms.deleteData(ProgramID.get(SelectedPosition));
	        	Toast.makeText(getActivity(), getString(R.string.success_delete), Toast.LENGTH_SHORT).show();
	        	getActivity().getSupportFragmentManager().popBackStack();
	        }
	    });
	    
	    // set negative button
	    builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int id) {
	        	
	        	// close delete dialog
	        	dialog.dismiss();
	        }
	    });

	    // show dialog
		AlertDialog alert = builder.create();
		alert.show();
	}
}
