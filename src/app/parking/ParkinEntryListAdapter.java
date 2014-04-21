package app.parking;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.parking.db.ParkingEntry;
	
	public class ParkinEntryListAdapter extends ArrayAdapter<ParkingEntry> {

		private List<ParkingEntry> list;
		private final Activity context;

		public ParkinEntryListAdapter(Activity context, List<ParkingEntry> list) {
			super(context, R.layout.parkingentry_row, list);
			this.context = context;
			this.list = list;
		}

		static class ViewHolder {
			protected TextView text;
		}
		
		public void setList(List<ParkingEntry> list) {
			this.list = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LinearLayout listView;
			// Get the current task
			ParkingEntry pEntry = list.get(position);

			// Inflate the view
			if (convertView == null) {
				listView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi;
				vi = (LayoutInflater) getContext().getSystemService(inflater);
				vi.inflate(R.layout.parkingentry_row, listView, true);
			} else {
				listView = (LinearLayout) convertView;
			}
			// Get the text boxes from the row .xml file
			TextView parkingName = (TextView) listView
					.findViewById(R.id.parkingName);
			TextView paymentStatus = (TextView) listView
					.findViewById(R.id.paymentStatus);

			// Insert real dates to textView
			parkingName.setText(pEntry.getParkingName());
			paymentStatus.setText(pEntry.getPaymentStatus().toString());

			return listView;
		}
		
	}
