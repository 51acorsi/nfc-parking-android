package app.parking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.robobinding.viewattribute.compoundbutton.OnCheckedChangeAttribute;

import parking.protocol.Protocol;
import parking.protocol.Protocol.PaymentMethod;
import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.ReaderCallback;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import app.parking.db.ParkingEntry;
import app.parking.nfc.hce.IsoDepAdapter;
import app.parking.nfc.hce.IsoDepTransceiver;
import app.parking.nfc.hce.IsoDepTransceiver.OnMessageReceived;
import app.parking.nfc.hce.NFCHostApduService;
import app.parking.R;
import app.parking.UserAccount.OnAmountChangeListener;
import app.parking.UserAccount.OnAutoPaymentChangeListener;
import app.parking.UserAccount.OnEntryListChangeListener;

;

public class MainActivity extends Activity implements OnMessageReceived,
		ReaderCallback {

	// private List<ParkingEntry> entries;
	private ParkinEntryListAdapter entryListAdapter;

	private NfcAdapter nfcAdapter;
	// private ListView listView;
	private IsoDepAdapter isoDepAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		isoDepAdapter = new IsoDepAdapter(getLayoutInflater());
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		this.initializeScreen();
		this.createEvents();
		this.loadParkingEntryList();
		this.loadUserAccount();

		// this.runSimulation();
		// this.createFakeEntries();
	}

	private void initializeScreen() {
		// Locale.setDefault(new Locale("en", "US"));
		NumberPicker numPicker = (NumberPicker) findViewById(R.id.npAmount);
		numPicker.setMinValue(0);
		numPicker.setMaxValue(100);
	}

	private void createEvents() {

		// Number Picker Amount Listener
		NumberPicker numPicker = (NumberPicker) findViewById(R.id.npAmount);
		numPicker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				updateUserAmount(picker, oldVal, newVal);
			}
		});

		// UserAccount Amount Listener
		UserAccount.setOnAmountChangedListener(new OnAmountChangeListener() {
			@Override
			public void onAmountChange(float oldAmount, float newAmount) {
				userAmountUpdated(oldAmount, newAmount);
			}
		});

		// Auto Payment CheckBox Change Listener
		CheckBox cbAutoPayment = (CheckBox) findViewById(R.id.cbAutoPayment);
		cbAutoPayment.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				updateAutoPaymentUpdated(isChecked);
			}
		});

		// UserAccount AutoPayment Listener
		UserAccount
				.setOnAutoPaymentChangedListener(new OnAutoPaymentChangeListener() {

					@Override
					public void onStateChange(boolean newValue) {
						userAutoPaymentUpdated(newValue);
					}
				});

		// UserAccount List Change Listener
		UserAccount.setOnEntryListChanged(new OnEntryListChangeListener() {

			@Override
			public void onListChange(List<ParkingEntry> parkingEntries) {
				parkingEntryListUpdated(parkingEntries);
			}
		});
	}

	private void loadUserAccount() {
		if (UserAccount.getUserName() == null) {
			UserAccount.setAmount(25);
			UserAccount.setUserName("felipe.01");
			UserAccount.setAutoPayment(true);

			TextView tView = (TextView) findViewById(R.id.txtViewUser);
			tView.setText(UserAccount.getUserName());
		}
	}

	private void loadParkingEntryList() {

		// this.entries = new ArrayList<ParkingEntry>();

		// Create fake data for testing purpose
		// this.createFakeEntries();

		// Find the ListView resource.
		ListView entriesView = (ListView) findViewById(R.id.lViewEntries);

		// Create ArrayAdapter for task list.
		this.entryListAdapter = new ParkinEntryListAdapter(this,
				UserAccount.getEntries());

		// Set the ArrayAdapter as the ListView's adapter.
		entriesView.setAdapter(this.entryListAdapter);

		// // Defining the item click listener for listView
		// OnItemClickListener itemClickListener = new OnItemClickListener() {
		//
		// public void onItemClick(AdapterView<?> arg0, View view, int position,
		// long id) {
		// //Toast.makeText(getBaseContext(), "You selected :" +
		// task_list.get(position).getName(), Toast.LENGTH_SHORT).show();
		// Task task = task_list.get(position);
		//
		// Intent intent = new Intent(getApplicationContext(),
		// TaskUpdater.class);
		// intent.putExtra("TASK_ID", task.getId());
		// intent.putExtra("TASK_NAME", task.getName());
		// intent.putExtra("TASK_LAT", task.getLocation().getLatitudeE6());
		// intent.putExtra("TASK_LON", task.getLocation().getLongitudeE6());
		// intent.putExtra("TASK_DATE", task.getDate().getTime());
		// startActivityForResult(intent, RequestCodes.TASK_UPDATE);
		// }
		//
		// };
		//
		// // Setting the item click listener for listView
		// mainListView.setOnItemClickListener(itemClickListener);
	}

	private void updateUserAmount(NumberPicker picker, int oldVal, int newVal) {
		// UserAccount.setAmount(picker.getValue());
	}

	private void userAmountUpdated(float oldAmount, float newAmount) {
		NumberPicker numPicker = (NumberPicker) findViewById(R.id.npAmount);
		numPicker.setValue((int) UserAccount.getAmount());
		TextView txtAmount = (TextView) findViewById(R.id.txtAmount);
		txtAmount.setText(UserAccount.getDisplayAmount());
	}

	private void updateAutoPaymentUpdated(boolean isChecked) {
		UserAccount.setAutoPayment(isChecked);
	}

	private void userAutoPaymentUpdated(boolean autoPayment) {
		CheckBox cbAutoPayment = (CheckBox) findViewById(R.id.cbAutoPayment);
		cbAutoPayment.setChecked(UserAccount.getAutoPayment());
	}

	private void parkingEntryListUpdated(List<ParkingEntry> entries) {
		this.entryListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		UserAccount.forceNotification();
		// nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A
		// | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
	}

	@Override
	public void onPause() {
		super.onPause();
		// nfcAdapter.disableReaderMode(this);
	}

	@Override
	public void onTagDiscovered(Tag tag) {
		IsoDep isoDep = IsoDep.get(tag);
		IsoDepTransceiver transceiver = new IsoDepTransceiver(isoDep, this);
		Thread thread = new Thread(transceiver);
		thread.start();
	}

	@Override
	public void onMessage(final byte[] message) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				isoDepAdapter.addMessage(new String(message));
			}
		});
	}

	@Override
	public void onError(Exception exception) {
		onMessage(exception.getMessage().getBytes());
	}

	// Test Methods
	private void createFakeEntries() {
		UserAccount.registerEntry(1, "Parking 1", 11, new Date(),
				PaymentMethod.BY_ENTRY, 3);
		UserAccount.registerEntry(1, "Parking 1", 12, new Date(114, 5, 1),
				PaymentMethod.BY_ENTRY, 5);
		UserAccount.registerEntry(1, "Parking 1", 13, new Date(114, 4, 6, 10,
				0, 0), PaymentMethod.BY_ENTRY, 5);
		// UserAccount.registerEntry(1, "Parking 1", 14, new Date(),
		// PaymentMethod.BY_ENTRY, 5);
		// UserAccount.registerEntry(1, "Parking 1", 15, new Date(),
		// PaymentMethod.BY_ENTRY, 5);
		// UserAccount.registerEntry(1, "Parking 1", 16, new Date(),
		// PaymentMethod.BY_ENTRY, 5);
		// UserAccount.registerEntry(1, "Parking 1", 17, new Date(),
		// PaymentMethod.BY_ENTRY, 5);
		// UserAccount.registerEntry(1, "Parking 1", 18, new Date(),
		// PaymentMethod.BY_ENTRY, 5);
		// UserAccount.registerEntry(1, "Parking 1", 19, new Date(),
		// PaymentMethod.BY_ENTRY, 5);
		// UserAccount.registerEntry(2, "Parking 1", 20, new Date(),
		// PaymentMethod.BY_ENTRY, 15);
	}

	private void runSimulation() {
		NFCHostApduService hceDemo = new NFCHostApduService();
		byte[] command;

		// ------------------------
		// Entry
		// ------------------------
		// Get User ID
		command = Protocol.getUserIDCommand();
		hceDemo.processCommandApdu(command, null);

		// Register Entry
		command = Protocol.getSetNewRegistryCommand(1, "Parking Test1", 1,
				new Date(), PaymentMethod.BY_ENTRY, 5);
		hceDemo.processCommandApdu(command, null);

		// ------------------------
		// Exit
		// ------------------------
		// Get User ID
		command = Protocol.getUserIDCommand();
		hceDemo.processCommandApdu(command, null);

		// Request Entry Payment
		command = Protocol.getReqPaymentCommand(1, 1);
		hceDemo.processCommandApdu(command, null);

		// command = Protocol.getSetNewRegistryCommand(1, "Parking Test2", 1,
		// new Date(), PaymentMethod.BY_ENTRY, 5);
		// hceDemo.processCommandApdu(command, null);
	}
}
