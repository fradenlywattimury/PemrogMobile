package mobile.amikom.id.ac.workout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.workout.app.R;

import mobile.amikom.id.ac.workout.fragment.WorkoutListFragment;


public class WorkoutListAdapter extends BaseAdapter {

		private Context mContext;
		
		
		public WorkoutListAdapter(Context context) {
			mContext = context;

		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return WorkoutListFragment.WorkoutID.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_workout_list, null);
				holder = new ViewHolder();
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// connect views object and views id on xml
			holder.imgWorkout = (ImageView) convertView.findViewById(R.id.imgWorkout);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			
			// set data to textview and imageview
			holder.txtTitle.setText(WorkoutListFragment.Name.get(position));
			holder.txtTime.setText(WorkoutListFragment.Time.get(position));
			int image = mContext.getResources().getIdentifier(WorkoutListFragment.Image.get(position), "drawable", mContext.getPackageName());
			holder.imgWorkout.setImageResource(image);
			
			return convertView;
		}

		// method to create object of views
		static class ViewHolder {
			ImageView imgWorkout;
			TextView txtTitle, txtTime;
		}
		
		
	}