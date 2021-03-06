package activities.couple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.google.analytics.tracking.android.EasyTracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import sharedpreferences.ManageSharedPreferences;
import AlertDialogManager.AlertDialogManager;
import Gson.Period;
import SessionManager.SessionManager;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DrawerNavSignDate extends FragmentActivity {

	SessionManager session;

	AsyncHttpClient client = new AsyncHttpClient();

	private static final int DATE_DIALOG_ID = 0;
	private static final int DATE_DIALOG_ID1 = 1;
	int item = 0;
	ViewPager vp;
	private vpAdapter myAdapter;
	String getname;
	String getemail;
	String getpass;
	String getpassconf;
	private int SmYear;
	private int SmMonth;
	private int SmDay;
	private int EmYear;
	private int EmMonth;
	private int EmDay;

	TextView mDateUpStart;
	TextView mDateUpEnd;
	String id = "";

	Spinner sp1;
	public final static String EXTRA_MESSAGE = "";

	EditText etuname;
	EditText etemail;
	EditText etpass;
	EditText etpassconf;
	
	String uname;
	String mail;
	String pass;
	String passv;

	// Preferences of data and cycle
	ManageSharedPreferences prefSignUp;
	ManageSharedPreferences prefCycle;

	// Object of alertDialog
	AlertDialogManager alert = new AlertDialogManager();

	// Manager of DatePickerDialog ID=0;
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			SmYear = year;
			SmMonth = monthOfYear;
			SmDay = dayOfMonth;
			updateDisplayStart(mDateUpStart);
		}
	};

	// Manager of DatePickerDialog ID=1;
	private DatePickerDialog.OnDateSetListener mDateSetListend = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			EmYear = year;
			EmMonth = monthOfYear;
			EmDay = dayOfMonth;
			updateDisplayEnd(mDateUpEnd);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signupdate);
		
		if(isOnline() == false){
			Intent i = new Intent(this, NoConnection.class);
    	    startActivity(i);
    	    finish();
		}

		vp = (ViewPager) findViewById(R.id.viewpager);

		myAdapter = new vpAdapter();
		vp.setAdapter(myAdapter);

		session = new SessionManager(getApplicationContext());
	}

	private class vpAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((LinearLayout) object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((LinearLayout) object);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			final LayoutInflater inflater = (LayoutInflater) container
					.getContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			View v = null;
			switch (position) {
			case 0:
				v = inflater.inflate(R.layout.activity_signup, null);
				ImageButton btnnext = (ImageButton) v
						.findViewById(R.id.btnnext);
				

				etuname = (EditText) v.findViewById(R.id.etname);
				etemail = (EditText) v.findViewById(R.id.etemail);
				etpass = (EditText) v.findViewById(R.id.etpass);
				etpassconf = (EditText) v.findViewById(R.id.etpassconf);

			
				btnnext.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
				break;

			case 1:
				v = inflater.inflate(R.layout.activity_dateregister, null);
				Button btnRegister = (Button) v.findViewById(R.id.btnRegister);
				Button btnStart = (Button) v.findViewById(R.id.btnStart);
				Button btnEnd = (Button) v.findViewById(R.id.btnEnd);
				mDateUpStart = (TextView) v.findViewById(R.id.mdatedisstart);
				mDateUpEnd = (TextView) v.findViewById(R.id.mdatedisend);

				btnStart.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						showDialog(DATE_DIALOG_ID);
					}
				});

				// get the current date
				Calendar c = Calendar.getInstance();
				SmYear = c.get(Calendar.YEAR);
				SmMonth = c.get(Calendar.MONTH);
				SmDay = c.get(Calendar.DAY_OF_MONTH);
				// display the current date (this method is below)
				updateDisplayStart(mDateUpStart);

				btnEnd.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						showDialog(DATE_DIALOG_ID1);
					}
				});

				// get the current date
				Calendar ce = Calendar.getInstance();
				EmYear = ce.get(Calendar.YEAR);
				EmMonth = ce.get(Calendar.MONTH);
				EmDay = ce.get(Calendar.DAY_OF_MONTH);
				// display the current date (this method is below)
				updateDisplayEnd(mDateUpEnd);

				sp1 = (Spinner) v.findViewById(R.id.spinner1);
				setdaysspinner(sp1);

				sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parentview,
							View view, int position, long id) {

						String items = parentview.getItemAtPosition(position)
								.toString();
						item = Integer.parseInt(items);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

						loaddataspinner(sp1);
					}
				});

				btnRegister.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						saveddataspinner(item);
						// GetTextDateSaved(mDateUpStart, mDateUpEnd);
						

						
						uname = etuname.getText().toString();
						mail = etemail.getText().toString();
						pass = etpass.getText().toString();
						passv = etpassconf.getText().toString();

						if (uname.matches("")) {
							Toast.makeText(getApplicationContext(), "You did not enter a username",
									Toast.LENGTH_SHORT).show();
							return;
						}

						if (mail.matches("")) {
							Toast.makeText(getApplicationContext(), "You did not enter a email",
									Toast.LENGTH_SHORT).show();
							return;
						}

						if (pass.matches("")) {
							Toast.makeText(getApplicationContext(), "You did not enter a password",
									Toast.LENGTH_SHORT).show();
							return;
						}

						if (passv.matches("")) {
							Toast.makeText(getApplicationContext(), "Please confirm your password",
									Toast.LENGTH_SHORT).show();
							return;
						}
						
						if (pass.equals(passv)) {

							String name = etuname.getText().toString();
							name = name.replace(" ", "%20");
							String email = etemail.getText().toString();
							email = email.replace(" ", "%20");
							String password = etpass.getText().toString();
							password = password.replace(" ", "%20");
							
							// Se asigna la URL del php para registrar
							client.get(
									"http://couplecare.us/backendcouple/registerwomen.php?Email="
											+ email + "&Password=" + password
											+ "&Name=" + name + "&PhoneID=",
									new AsyncHttpResponseHandler() {
										// En caso de que haya una respuesta
										@Override
										public void onSuccess(String response) {
											id = response;
											id = id.replace(" ", "");

											Log.d("item", "" + mDateUpStart
													+ mDateUpEnd);

											if (id.equals("") || id.equals("0")) {
												
												alert.showAlertDialog(DrawerNavSignDate.this, "Register Failed", "This email already exist", false);

											} else {
												// Se asigna la URL del php para
												// registrar
												client.get(
														"http://couplecare.us/backendcouple/registerperiod.php?PeriodStart="
																+ mDateUpStart
																		.getText()
																		.toString()
																+ "&PeriodEnd="
																+ mDateUpEnd
																		.getText()
																		.toString()
																+ "&DurationCycle="
																+ item + "&id="
																+ id + "",
														new AsyncHttpResponseHandler() {
															// En caso de que haya
															// una respuesta
															@Override
															public void onSuccess(
																	String response) {
																session = new SessionManager(
																		getApplicationContext());
																session.createLoginSession(
																		"", id);
																
																intentHome();

															}
														});
											}
										}
									});
							
							
								
						}
						else{
					     alert.showAlertDialog(DrawerNavSignDate.this, "Unmatch password", "Passwords are diferents", false);
						}

					}
				});

				break;

			}

			((ViewPager) container).addView(v, 0);
			return v;
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
		}
	}
	
	//SDK para Facebook
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		com.facebook.Settings.publishInstallAsync(getApplicationContext(),
				"230341937134870");
	}

	public void validatedit(EditText username, EditText email,
			EditText password, EditText confirmpass) {
		// Variables for getText of EditText
		prefSignUp = new ManageSharedPreferences(getApplicationContext());

		uname = username.getText().toString();
		mail = email.getText().toString();
		pass = password.getText().toString();
		passv = confirmpass.getText().toString();

		if (uname.matches("")) {
			Toast.makeText(this, "You did not enter a username",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (mail.matches("")) {
			Toast.makeText(this, "You did not enter a email",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (pass.matches("")) {
			Toast.makeText(this, "You did not enter a password",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (passv.matches("")) {
			Toast.makeText(this, "You did not enter a pass verify",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (pass.equals(passv)) {
			prefSignUp.createDataWoman(uname, mail, pass);
			intentHome();
				
		} else {
			Toast.makeText(getApplicationContext(),
					"Please confirm your password", Toast.LENGTH_SHORT).show();
		}
	}

	public void validatedata(String name, String email, String pass,
			String passconf) {

	}

	public void updateDisplayEnd(TextView txt1) {
		txt1.setText(new StringBuilder().append(EmYear).append("/")
				.append(EmMonth + 1).append("/").append(EmDay));
	}

	public void GetTextDateSaved(TextView txt1, TextView txt2) {
		prefCycle = new ManageSharedPreferences(getApplicationContext());
		String textstart = txt1.getText().toString();
		String textend = txt2.getText().toString();
		prefCycle.createDataCycle(textstart, textend);
		Toast.makeText(getApplicationContext(),
				textstart + " " + textend + " " + "Data Saved",
				Toast.LENGTH_SHORT).show();
	}

	public void updateDisplayStart(TextView txt) {
		txt.setText(new StringBuilder().append(SmYear).append("/")
				.append(SmMonth + 1).append("/").append(SmDay));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, SmYear,
					SmMonth, SmDay);

		case DATE_DIALOG_ID1:
			return new DatePickerDialog(this, mDateSetListend, EmYear, EmMonth,
					EmDay);
		}
		return null;
	}

	public void setdaysspinner(Spinner sp1) {
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.cycle_days, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sp1.setAdapter(adapter);
	}

	public void saveddataspinner(int position) {
		prefCycle = new ManageSharedPreferences(getApplicationContext());
		prefCycle.createCycleShared(position);
	}

	public void loaddataspinner(Spinner sp1) {
		sp1.setSelection(prefCycle.getCycle());
	}
	
	public void intentHome(){
		// Closing all the Activities
		Intent home = new Intent(getApplicationContext(),DrawerHomeCal.class); 
		 
		// Closing all the Activities
		home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
		// Add new Flag to start new Activity
		home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String message = "guia";
		home.putExtra(EXTRA_MESSAGE, message);
		
		startActivity(home);
			
		finish();
	}
	
	//Funci�n booleana para detectar el estado de la conexi�n del dispositivo.
    public boolean isOnline() {
    	ConnectivityManager cm =
    			(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	
    	//Recibir informaci�n de la red conectada.
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
    
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this); // Add this method.
	    
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this); // Add this method.
	  }

}