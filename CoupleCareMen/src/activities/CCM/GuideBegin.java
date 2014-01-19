package activities.CCM;

import com.google.analytics.tracking.android.EasyTracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import sharedpreferences.ManageSharedPreferences;
import AlertDialogManager.AlertDialogManager;
import SessionManager.SessionManager;
import activities.CCM.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GuideBegin extends Activity {

	AsyncHttpClient client = new AsyncHttpClient();
	
	ViewPager vp;
	
	private vpAdapter myAdapter;
	SessionManager session;
	
	ManageSharedPreferences sessionw;
	
	SharedPreferences prefwoman;
	SharedPreferences preflogin;
	
	String emaildw;
	String passdw;
	String putemail = "";
	String putpass = "";
	String emaillog;
	String passlog;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signupdate);
		
		if(isOnline() == false){
			Intent i = new Intent(this, NoConnection.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
			return 3;
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
			LayoutInflater inflater = (LayoutInflater) container.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = null;
			switch (position) {
			case 0:
				v = inflater.inflate(R.layout.activity_step1, null);

				break;
			case 1:
				v = inflater.inflate(R.layout.activity_step2, null);

				break;

			case 2:
				v = inflater.inflate(R.layout.activity_login, null);
				// Add the id of edittext and the code about each widget.
				
				final EditText email;
				final EditText pass;
				
				email = (EditText) v.findViewById(R.id.etemail);
				pass = (EditText) v.findViewById(R.id.etpass);

				putemail = email.getText().toString();
				putpass = pass.getText().toString();

				Button login = (Button) v.findViewById(R.id.btnLogin);

				login.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						check2data(email.getText().toString(), pass.getText().toString());
					}
				});

				Button start = (Button) v.findViewById(R.id.btnsignup);

				start.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						 Intent signup = new Intent(getApplicationContext(), DrawerNavSignDate.class); 
						 
							// Closing all the Activities
							signup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								
							// Add new Flag to start new Activity
							signup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							
							startActivity(signup);
								
							finish();

					}
				});
				
				TextView forgetpass = (TextView) v.findViewById(R.id.forgotpass);
				
				forgetpass.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showDialogPass(R.layout.prompfpass);
						
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

	/*public void getDatawoman() {
		emaildw = sessionw.getEmail();
		passdw = sessionw.getpass();
	}*/
	
	String access= "";
	String id = "";

	public void check2data(String email, String pass) {

	

		// Check if username, password is filled
		//	getDatawoman();
			//Se asigna la URL del php para registrar
			client.get("http://couplecare.us/backendcouple/login.php?T=M&Email="+email+"&Password="+pass+"", new AsyncHttpResponseHandler() {
	    	    //En caso de que haya una respuesta
				@Override
				public void onSuccess(String response) {
					id = response;
					id = id.replace(" ", "");
					
					if(!id.equals("0")){
						access = "yes";
					}
					else{
						alert.showAlertDialog(GuideBegin.this, "Login failed..",
								"Username/Password is incorrect", false);
					}
	    	    }
			});
			
			if(access == "yes"){
				// Starting MainActivity
				
				 Intent home = new Intent(getApplicationContext(),DrawerHomeCal.class); 
				 
					// Closing all the Activities
						home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						
					// Add new Flag to start new Activity
					home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
					session = new SessionManager(getApplicationContext());
					
					session.createLoginSession(email, id);
					
					startActivity(home);
					
					finish();
			}
			
		/*} else {
			// user didn't entered user name or password
			// Show alert asking him to enter the details
			alert.showAlertDialog(GuideBegin.this, "Login failed..",
					"Please enter username and password", false);
		}*/
	}

	public void getDataSharedLogin() {
		preflogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

		emaillog = preflogin.getString("email", "");

		passlog = preflogin.getString("pass", "");

	}
	
	public void showDialogPass(int la) {
		LayoutInflater li = LayoutInflater.from(GuideBegin.this);
		final View promptsView = li.inflate(la, null);

		AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(
				GuideBegin.this);

		alerDialogBuilder.setView(promptsView);

		final EditText useremail = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		alerDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int idw) {
					
						String email = useremail.getText().toString();

						client.get(
								"http://couplecare.us/backendcouple/sendpassmen.php?Email="+email,
								new AsyncHttpResponseHandler() {

									// En caso de que haya una respuesta
									@Override
									public void onSuccess(String response) {
										response = response.replace(" ", "");
										if (!response.equals("0")
												&& !response.equals("")
												&& !response.equals(null)) {
											alert.showAlertDialog(
													GuideBegin.this,
													"Check Password.",
													"An email has been send to your mailbox. Could be on SPAM or Promotions.",
													true);
								
										} else {
											alert.showAlertDialog(
													GuideBegin.this,
													"Fail check password",
													"This user is not registered.",
													false);
										}
									}
								});

					}
				}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();

							}
						});

		AlertDialog alertDialog = alerDialogBuilder.create();

		alertDialog.show();

	}
	
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		com.facebook.Settings.publishInstallAsync(getApplicationContext(),
				"163407503868589");
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