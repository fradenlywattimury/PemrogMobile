package mobile.amikom.id.ac.workout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daily.workout.app.R;

import mobile.amikom.id.ac.workout.fragment.ProgramsFragment;


public class ProgramsAdapter extends BaseAdapter {

		private Context mContext;
		
		
		public ProgramsAdapter(Context context) {
			mContext = context;

		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return ProgramsFragment.DayID.size();
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
				convertView = inflater.inflate(R.layout.row_programs, null);
				holder = new ViewHolder();
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// connect views object and views id on xml
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
			
			// if day is Sunday and Saturday change color to red
			int id = ProgramsFragment.DayID.get(position);
			if((id == 0) || id == 6){
				holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.title_holiday_color));
			}
			
			// set data to textview
			holder.txtTitle.setText(ProgramsFragment.Day_name.get(position));
			
			int count = Integer.parseInt(ProgramsFragment.Total.get(position));
			
			// if workout more than 1 add 's
			if(count > 1){
				holder.txtTotal.setText(count+" "+mContext.getResources().getString(R.string.workouts));
			}else{
				holder.txtTotal.setText(count+" "+mContext.getResources().getString(R.string.workout));
			}
			
			return convertView;
		}
		
		// method to create object of views
		static class ViewHolder {
			TextView txtTitle, txtTotal;
		}
		
		
	}