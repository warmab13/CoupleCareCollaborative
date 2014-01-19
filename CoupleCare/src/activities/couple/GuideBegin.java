package activities.couple;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import sharedpreferences.ManageSharedPreferences;
import AlertDialogManager.AlertDialogManager;
import SessionManager.SessionManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

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

						Intent i = new Intent(getApplicationContext(),
								DrawerNavSignDate.class); // Agregar actividad
															// siguiente
						startActivity(i);

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
			client.get("http://couplecare.us/backendcouple/login.php?T=W&Email="+email+"&Password="+pass+"", new AsyncHttpResponseHandler() {
	    	    //En caso de que haya una respuesta
				@Override
				public void onSuccess(String response) {
					id = response;
					id = id.replace(" ", "");
					int idi = Integer.parseInt(id);
					if(idi < 1){
						alert.showAlertDialog(GuideBegin.this, "Login failed..",
								"Username/Password is incorrect", false);
					}
					else{
						access = "yes";
					}
	    	    }
			});
			
			if(access == "yes"){
				// Starting MainActivity
				Intent i = new Intent(getApplicationContext(),
						DrawerHomeCal.class);
				// Delete backStack
				session = new SessionManager(getApplicationContext());
				session.createLoginSession(email, id);
				startActivity(i);
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

}
