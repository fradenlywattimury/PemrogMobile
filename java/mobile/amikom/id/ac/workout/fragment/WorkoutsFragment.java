package mobile.amikom.id.ac.workout.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.daily.workout.app.R;

import mobile.amikom.id.ac.workout.activity.HomeActivity;
import mobile.amikom.id.ac.workout.adapter.WorkoutsAdapter;

public class WorkoutsFragment extends SherlockFragment {
	OnCategorySelectedListener mCallback;
	
	// create object of listview
	ListView list;
	
	// create object of custom adapter
	WorkoutsAdapter wa;
	
	// create arraylist variables to store data
	public static ArrayList<Integer> WorkoutID = new ArrayList<Integer>();
	public static ArrayList<String> Category_name = new ArrayList<String>();
	public static ArrayList<String> Category_image = new ArrayList<String>();
	public static ArrayList<String> Total = new ArrayList<String>();

	// create arraylist variable to store object data from database
	ArrayList<ArrayList<Object>> data;

	// create interface for listener method
	public interface OnCategorySelectedListener{
		public void onCategorySelected(int selectedID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_list, container, false);

		// connect object of listview and listview id on xml
		list = (ListView) v.findViewById(R.id.list);
		
		wa = new WorkoutsAdapter(getActivity());

		// call asynctask class to get data from database
		new getCategoryList().execute();

		// listener to get selected id when list item clicked
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				mCallback.onCategorySelected(WorkoutID.get(position));

		        // Set the item as checked to be highlighted when in two-pane layout
		        list.setItemChecked(position, true);
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
            mCallback = (OnCategorySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
	
	// asynctask class that is used to fetch data from database in background
	public class getCategoryList extends AsyncTask<Void, Void, Void>{
		
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
				list.setAdapter(wa);
			}else{
				Toast.makeText(getActivity(), getString(R.string.no_data_workout), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	// method to create arraylist variables before used
	void clearData(){
		WorkoutID.clear();
		Category_name.clear();
		Category_image.clear();
		Total.clear();
	}
	
	// method to fetch data from database
    public void getDataFromDatabase(){
    	data = HomeActivity.dbWorkouts.getAllCategories();
    	
    	clearData();
    	
    	for(int i=0;i<data.size();i++){
    		ArrayList<Object> row = data.get(i);
    		
    		WorkoutID.add(Integer.parseInt(row.get(0).toString()));
    		Category_name.add(row.get(1).toString());
    		Category_image.add(row.get(2).toString());
    		Total.add(row.get(3).toString());
    	}
    	
    }
}
