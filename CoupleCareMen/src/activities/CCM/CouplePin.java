package activities.CCM;


import java.util.HashMap;
import java.util.List;

import org.apache.http.protocol.HTTP;


import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import AlertDialogManager.AlertDialogManager;
import Gson.Men;
import Gson.Period;
import Gson.Women;
import SessionManager.SessionManager;
import activities.CCM.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class CouplePin extends ActionBarActivity{
	
	TextView txtFecha, txtMsg, txtNext, txtNextLabel,txtrequest;
	String fecha2, msg, msgNext;
	
	AlertDialogManager alert = new AlertDialogManager();
		
	AsyncHttpClient client = new AsyncHttpClient();
	Gson g = new Gson();
	Gson gmen = new Gson();
	Gson gwomen = new Gson();
	
	TextView txtResult;
	EditText etPin;
	
	MenuItem item;
	
	String SPin="";
	
	Button btnSync;
	Button btnRequest;
	
	SessionManager session;
		
	private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    
    public final static String EXTRA_MESSAGE = "";
    
    private CharSequence tituloSeccion;  
    private CharSequence tituloApp;

	
	String id="";
	String CouplePin = "";
	String UserName = "";
	String WomenName = "";
	
	ShareActionProvider myShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_couple_pin);
		
		btnSync = (Button)findViewById(R.id.btnSync);
		txtrequest = (TextView)findViewById(R.id.txtrequest);
		
		btnRequest = (Button)findViewById(R.id.btnRequest);
		
		btnSync.setVisibility(View.INVISIBLE);
		
		btnRequest.setVisibility(View.VISIBLE);
		
		txtrequest.setVisibility(View.VISIBLE);
		
		txtResult = (TextView)findViewById(R.id.textResult);
		etPin = (EditText)findViewById(R.id.etePin);

		session = new SessionManager(getApplicationContext());
		
		if(isOnline() == false){
			Intent i = new Intent(this, NoConnection.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	    startActivity(i);
    	    finish();
		}

		
		if(session.isLoggedIn()){
        	
        	// get user data from session
            HashMap<String, String> user = session.getUserDetails();
            
            // name
            id = user.get(SessionManager.KEY_PASS);
            
            
            // email
            String email = user.get(SessionManager.KEY_EMAIL);
            
            
			
			btnRequest.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showDialogRequest(R.layout.promptrequest);
				}
			});
            
        }
        else{
        }
		
		//session.checkLogin();
		
		if(!session.isLoggedIn()){
			Intent i = new Intent(getApplicationContext(), GuideBegin.class);
			// Closing all the Activities

			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			startActivity(i);
			finish();
		}
		
		opcionesMenu = new String[] {"Home", "Settings", "Log out"};
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(
        		getSupportActionBar().getThemedContext(),
                android.R.layout.simple_list_item_1, opcionesMenu));
        
		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case 0:
					 Intent home = new Intent(getApplicationContext(),DrawerHomeCal.class); 
					 
						// Closing all the Activities
							home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							
						// Add new Flag to start new Activity
						home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						startActivity(home);
							
						finish();
					break;
				case 1:
					 Intent toDrawerSettings = new Intent(getApplicationContext(), DrawerSettings.class); 
					 
						// Closing all the Activities
					 	toDrawerSettings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							
						// Add new Flag to start new Activity
						toDrawerSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						startActivity(toDrawerSettings);
							
						finish();
					break;
				case 2:				
					session.logoutUser();
					
					// Closing all the Activities
					Intent login = new Intent(getApplicationContext(),GuideBegin.class); 
					 
					// Closing all the Activities
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						
					// Add new Flag to start new Activity
					login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					startActivity(login);
						
					finish();
					break;

				}

				drawerList.setItemChecked(position, true);

				tituloSeccion = opcionesMenu[position];
				getSupportActionBar().setTitle("");

				drawerLayout.closeDrawer(drawerList);
			}
		});
		
		tituloSeccion = getTitle();
		tituloApp = getTitle();
		
		drawerToggle = new ActionBarDrawerToggle(this, 
				drawerLayout,
				R.drawable.ic_navigation_drawer, 
				R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(CouplePin.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(UserName);
				ActivityCompat.invalidateOptionsMenu(CouplePin.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		item = menu.findItem(R.id.action_share);
		myShareActionProvider = (ShareActionProvider) MenuItemCompat
				.getActionProvider(item);
		
		//Cambiar string.
		AnalyticsBoton("Request_PIN");
		String shareBody = "Hey there, I using CoupleCare for Men, download it at http://www.couplecare.us/index.php/download and use it :D";
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				i.setType(HTTP.PLAIN_TEXT_TYPE); // "text/plain" MIME type
				myShareActionProvider.setShareIntent(i);
				
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent toDrawerSettings = new Intent(getApplicationContext(),
					DrawerSettings.class);
			startActivity(toDrawerSettings);
			finish();
			break;
		case R.id.home:

			break;
		case R.id.action_logout:

			break;
		case R.id.action_status:
			
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean menuAbierto = drawerLayout.isDrawerOpen(drawerList);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	public void SearchPin(View view){
		SPin = etPin.getText().toString();
		btnRequest.setVisibility(View.VISIBLE);
		txtrequest.setVisibility(View.VISIBLE);
		client.get("http://couplecare.us/backendcouple/SearchPin.php?PIN="+SPin, new AsyncHttpResponseHandler() {
    	    //En caso de que haya una respuesta
			@Override
    	    public void onSuccess(String response) {
				//Se usa el GSON pasandole los paremetros recibido a la clase Women en donde tendremos nuestras 
				//variables en donde los valores serán pasados desde el JSON.
				response = response.replace(" ", "");
				if(!response.equals("0")){
					
					
					if(response.equals("00")){
						txtResult.setText(Html.fromHtml("<b>Sorry :(</b><br>The owner of this PIN is already synchronized with another account."));
						btnSync.setVisibility(View.INVISIBLE);
						btnRequest.setVisibility(View.VISIBLE);
						
					}
					else{
						client.get("http://couplecare.us/backendcouple/JSON/women/"+response+".json", new AsyncHttpResponseHandler(){
							@Override
				    	    public void onSuccess(String response) {
								String WomenEmail;
								Women jsonWomen = gwomen.fromJson(response, Women.class); 
				    	        CouplePin = jsonWomen.CouplePin.toString();
				    	        WomenEmail = jsonWomen.Email.toString();
				    	        WomenName = jsonWomen.Name.toString();
				    	        
				    	        txtResult.setText(Html.fromHtml("Is <b>"+WomenName+"</b> your partner?<br>"+WomenEmail));
				    	        btnSync.setVisibility(View.VISIBLE);
				    	        btnRequest.setVisibility(View.INVISIBLE);
				    	        txtrequest.setVisibility(View.INVISIBLE);
				    	    	
								
							}
			    	        
						});
					}
				}
				else{
					txtResult.setText(Html.fromHtml("<b>Not found :(</b><br> Are you sure that this is the PIN of your partner?"));
					btnSync.setVisibility(View.INVISIBLE);
					btnRequest.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	public void InsertPinMen(View view){
		client.get("http://couplecare.us/backendcouple/InsertPinMen.php?ID="+id+"&PIN="+SPin, new AsyncHttpResponseHandler(){
			@Override
    	    public void onSuccess(String response) {
				
				response = response.replace(" ", "");
				Intent i = new Intent(getApplicationContext(),
						DrawerHomeCal.class);
				// Delete backStack
				startActivity(i);
				finish();
			}
	        
		});
	}
	
	
	public void showDialogRequest(int la) {
		LayoutInflater li = LayoutInflater.from(CouplePin.this);
		final View promptsView = li.inflate(la, null);

		AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(
				CouplePin.this);

		alerDialogBuilder.setView(promptsView);

		alerDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Send",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int idw) {

								// Create the text message with a string
								Intent sendIntent = new Intent();
								sendIntent.setAction(Intent.ACTION_SEND);
								sendIntent
										.putExtra(
												Intent.EXTRA_TEXT,
									"Hey there, I'm using CoupleCare for Men it's great, download CoupleCare at https://play.google.com/store/apps/details?id=activities.couple and use it :D, don't forget to give me your PIN :D.");
								sendIntent.setType(HTTP.PLAIN_TEXT_TYPE); // "text/plain"
																			// MIME
																			// type
								String title = getResources().getString(
										R.string.chooser_title);
								// Create intent to show chooser
								Intent chooser = Intent.createChooser(
										sendIntent, title);

								// Verify the intent will resolve to at least
								// one activity
								if (sendIntent
										.resolveActivity(getPackageManager()) != null) {
									startActivity(sendIntent);
								}

							}
						})
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();

							}
						});

		AlertDialog alertDialog = alerDialogBuilder.create();

		alertDialog.show();

	}

	// Función booleana para detectar el estado de la conexión del dispositivo.
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		// Recibir información de la red conectada.
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
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

	public void AnalyticsBoton(String action){
		// May return null if a EasyTracker has not yet been initialized with a
		  // property ID.
		  EasyTracker easyTracker = EasyTracker.getInstance(getApplicationContext());

		  // MapBuilder.createEvent().build() returns a Map of event fields and values
		  // that are set and sent with the hit.
		  easyTracker.send(MapBuilder
		      .createEvent("ui_action",     // Event category (required)
		                   action,  // Event action (required)
		                   "play_button",   // Event label
		                   null)            // Event value
		      .build()
		  );
	 }
	
}