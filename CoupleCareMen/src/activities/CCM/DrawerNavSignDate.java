package activities.CCM;

import com.google.analytics.tracking.android.EasyTracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import sharedpreferences.ManageSharedPreferences;
import AlertDialogManager.AlertDialogManager;
import SessionManager.SessionManager;
import activities.CCM.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
	
	String email;
	
	TextView mDateUpStart;
	TextView mDateUpEnd;
	String id = "";
	
	Spinner sp1;
	
	EditText etuname; 
	EditText etemail;
	EditText etpass;
	EditText etpassconf;

	//Preferences of data and cycle
	ManageSharedPreferences prefSignUp;
	ManageSharedPreferences prefCycle;
	
	//Object of alertDialog
	AlertDialogManager alert = new AlertDialogManager();
	
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

	}

	private class vpAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 1;
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
				Button btnregister = (Button) v.findViewById(R.id.btnRegister);

				 etuname = (EditText) v.findViewById(R.id.etname);
				 etemail = (EditText) v.findViewById(R.id.etemail);
				 etpass = (EditText) v.findViewById(R.id.etpass);
				 etpassconf = (EditText) v.findViewById(R.id.etpassconf);

				getname = etuname.getText().toString();
				getemail = etemail.getText().toString();
				getpass = etpass.getText().toString();
				getpassconf = etpassconf.getText().toString();

				btnregister.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						validatedit(etuname,etemail,etpass,etpass);
						validatedata(getname, getemail, getpass, getpassconf);
						
						String name = etuname.getText().toString();
						name = name.replace(" ", "%20");
						
						email = etemail.getText().toString();
						String password = etpass.getText().toString();
						String confirmpass = etpassconf.getText().toString();
						
						
						//Se asigna la URL del php para registrar
						client.get("http://couplecare.us/backendcouple/registermen.php?Email="+email+"&Password="+password+"&Name="+name, new AsyncHttpResponseHandler() {
				    	    //En caso de que haya una respuesta
							@Override
							public void onSuccess(String response) {
								response = response.replace(" ", "");
								if(!response.equals("0")){
									session = new SessionManager(getApplicationContext());
									session.createLoginSession(email, response);
									
									Intent i = new Intent(getApplicationContext(),
				    						DrawerHomeCal.class);
				    				// Delete backStack
				    				startActivity(i);
				    				finish();
								}
				    	    }
						});
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
	
	
	public void validatedit(EditText username,EditText email,EditText password,EditText confirmpass){
		// Variables for getText of EditText
		prefSignUp = new ManageSharedPreferences(getApplicationContext());
		
				String  uname = username.getText().toString();
				String mail = email.getText().toString();
				String pass = password.getText().toString();
				String passv = confirmpass.getText().toString();
				
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
					Toast.makeText(getApplicationContext(),
							"Thank's your information was saved", Toast.LENGTH_SHORT)
							.show();
					
				} else {
					Toast.makeText(getApplicationContext(),
							"Please confirm your password", Toast.LENGTH_SHORT).show();
				}
	}
	
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		com.facebook.Settings.publishInstallAsync(getApplicationContext(),
				"163407503868589");
	}

	public void validatedata(String name, String email, String pass,
			String passconf) {
	
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
	  
		//Función booleana para detectar el estado de la conexión del dispositivo.
	    public boolean isOnline() {
	    	ConnectivityManager cm =
	    			(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    	
	    	//Recibir información de la red conectada.
	        NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnected()) {
	            return true;
	        }
	        return false;
	    }
	    



}