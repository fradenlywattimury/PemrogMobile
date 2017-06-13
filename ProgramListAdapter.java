package mobile.amikom.id.ac.workout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.workout.app.R;

import mobile.amikom.id.ac.workout.fragment.ProgramListFragment;

// adapter class to create custom listview
public class ProgramListAdapter extends BaseAdapter {

		private Context mContext;
		
		
		public ProgramListAdapter(Context context) {
			mContext = context;

		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return ProgramListFragment.ProgramID.size();
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
			
			// connect views object with views id on xml 
			holder.imgWorkout = (ImageView) convertView.findViewById(R.id.imgWorkout);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			
			// set data to text view and image view
			holder.txtTitle.setText(ProgramListFragment.Name.get(position));
			holder.txtTime.setText(ProgramListFragment.Time.get(position));
			int image = mContext.getResources().getIdentifier(ProgramListFragment.Image.get(position),
					"drawable", mContext.getPackageName());
			holder.imgWorkout.setImageResource(image);
			
			return convertView;
		}
		
		// create objects of views
		static class ViewHolder {
			ImageView imgWorkout;
			TextView txtTitle, txtTime;
		}
		
		
	}