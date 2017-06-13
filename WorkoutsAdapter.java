package mobile.amikom.id.ac.workout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.workout.app.R;

import mobile.amikom.id.ac.workout.fragment.WorkoutsFragment;


public class WorkoutsAdapter extends BaseAdapter {

		private Context mContext;
		
		
		public WorkoutsAdapter(Context context) {
			mContext = context;

		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return WorkoutsFragment.WorkoutID.size();
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
				convertView = inflater.inflate(R.layout.row_workouts, null);
				holder = new ViewHolder();
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// connect views object with views id on xml 
			holder.imgCategory = (ImageView) convertView.findViewById(R.id.imgCategory);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
			
			// set data to text view
			holder.txtTitle.setText(WorkoutsFragment.Category_name.get(position));
			int count = Integer.parseInt(WorkoutsFragment.Total.get(position));
			
			// if workout is more than one then add 's
			if(count > 1){
				holder.txtTotal.setText(count+" "+mContext.getResources().getString(R.string.workouts));
			}else{
				holder.txtTotal.setText(count+" "+mContext.getResources().getString(R.string.workout));
			}
			
			// set data to image view
			int image = mContext.getResources().getIdentifier(WorkoutsFragment.Category_image.get(position), "drawable", mContext.getPackageName());
			holder.imgCategory.setImageResource(image);
			
			return convertView;
		}

		// method to create object of views
		static class ViewHolder {
			ImageView imgCategory;
			TextView txtTitle, txtTotal;
		}
		
		
	}