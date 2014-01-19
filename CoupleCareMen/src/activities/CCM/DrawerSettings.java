package activities.CCM;

import java.util.HashMap;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import AlertDialogManager.AlertDialogManager;
import Gson.Men;
import Gson.Women;
import SessionManager.SessionManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class DrawerSettings extends ActionBarActivity {

	TextView txtFecha, txtMsg, txtNext, txtNextLabel;
	String fecha2, msg, msgNext;
	final Context context = this;

	AlertDialogManager alert = new AlertDialogManager();

	AsyncHttpClient client = new AsyncHttpClient();
	Gson g = new Gson();
	Gson gwomen = new Gson();

	SessionManager session;

	int DurationCycle;
	int PeriodDays;

	private String[] opcionesMenu;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;

	public final static String EXTRA_MESSAGE = "";

	private CharSequence tituloSeccion;
	private CharSequence tituloApp;

	ViewPager vp;
	private vpAdapter myAdapter;

	String id = "";
	String CouplePin = "";
	String UserName = "";

	boolean sdDisponible = false;
	boolean sdAccesoEscritura = false;


	Button btnname;
	Button btnpass;
	Button btncpin;
	Button btncupdate;
	Button btnexit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homecal);
		
		
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

		if (session.isLoggedIn()) {

			// get user data from session
			HashMap<String, String> user = session.getUserDetails();

			// name
			id = user.get(SessionManager.KEY_PASS);

			// email
			String email = user.get(SessionManager.KEY_EMAIL);

		} else {
		}
		
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
		

		// Se asigna la URL del JSON alojado en Internet
		client.get("http://couplecare.us/backendcouple/JSON/men/" + id
				+ ".json", new AsyncHttpResponseHandler() {
			// En caso de que haya una respuesta
			@Override
			public void onSuccess(String response) {
				// Se usa el GSON pasandole los paremetros recibido a la clase
				// Women en donde tendremos nuestras
				// variables en donde los valores serán pasados desde el JSON.
				Men jsonmen = gwomen.fromJson(response, Men.class);
				CouplePin = jsonmen.CouplePin.toString();
				UserName = jsonmen.Name.toString();
			}
		});

		// ViewPager
		vp = (ViewPager) findViewById(R.id.viewpager);

		myAdapter = new vpAdapter();
		vp.setAdapter(myAdapter);

		opcionesMenu = new String[] { "Home", "Settings",
				"Log out" };
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case 0:
					Intent toDrawerHome = new Intent(getApplicationContext(), DrawerHomeCal.class);
					startActivity(toDrawerHome);
					finish();
					break;
				case 1:
					Intent toDrawerSettings = new Intent(getApplicationContext(), DrawerSettings.class);
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

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(DrawerSettings.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(UserName);
				ActivityCompat.invalidateOptionsMenu(DrawerSettings.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
				v = inflater.inflate(R.layout.activity_changedata, null);
				btnname = (Button) v.findViewById(R.id.btncname);

				btnname.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						showDialogNamew(R.layout.prompts);
					}
				});

				btnpass = (Button) v.findViewById(R.id.btncpass);

				btnpass.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						showDialogPass(R.layout.promptspass);

					}
				});

				
				
				btnexit = (Button) v.findViewById(R.id.btnexit);

				btnexit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 Intent home = new Intent(getApplicationContext(),DrawerHomeCal.class); 
						 
							// Closing all the Activities
							home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								
							// Add new Flag to start new Activity
							home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							
							startActivity(home);
								
							finish();
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

	public void showDialogNamew(int la) {
		LayoutInflater li = LayoutInflater.from(DrawerSettings.this);
		View promptsView = li.inflate(la, null);

		AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(
				DrawerSettings.this);

		alerDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		alerDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int idw) {
						
						String text = userInput.getText().toString();
						text = text.replace(" ", "%20");
						
						

						client.get(
								"http://couplecare.us/backendcouple/settingsmen/changename.php?Id="+id+"&Name="+text,
								new AsyncHttpResponseHandler() {
									// En caso de que haya una respuesta
									@Override
									public void onSuccess(String response) {
										response = response.replace(" ", "");
										if (!response.equals("0")
												&& !response.equals("")
												&& !response.equals(null)) {
											alert.showAlertDialog(
													DrawerSettings.this,
													"Success Change UserName",
													"Your UserName has been updated",
													true);
										} else {
											alert.showAlertDialog(
													DrawerSettings.this,
													"Fail update User Name",
													"Please try again",
													false);
											}
									}
								});
						}
					})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();

							}
						});

		AlertDialog alertDialog = alerDialogBuilder.create();

		alertDialog.show();

	}

	public void showDialogPass(int la) {
		LayoutInflater li = LayoutInflater.from(DrawerSettings.this);
		final View promptsView = li.inflate(la, null);

		AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(
				DrawerSettings.this);

		alerDialogBuilder.setView(promptsView);

		final EditText useractpass = (EditText) promptsView
				.findViewById(R.id.etdialogActPass);

		final EditText usernewpass = (EditText) promptsView
				.findViewById(R.id.etdialogNewPass);

		alerDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int idw) {
						
						
						String actpass = useractpass.getText().toString();
						actpass = actpass.replace(" ", "%20");
						String newpass = usernewpass.getText().toString();
						newpass = newpass.replace(" ", "%20");
					

						client.get("http://couplecare.us/backendcouple/settingsmen/changepass.php?Id="+id+"&OldPass="+actpass+"&NewPass="+newpass,
								new AsyncHttpResponseHandler() {

									// En caso de que haya una respuesta
									@Override
									public void onSuccess(String response) {
										response = response.replace(" ", "");
										if (!response.equals("0")
												&& !response.equals("")
												&& !response.equals(null)) {
											alert.showAlertDialog(
													DrawerSettings.this,
													"Success Change Password",
													"Your password has been updated",
													true);
								
										} else {
											alert.showAlertDialog(
													DrawerSettings.this,
													"Fail update password",
													"The actual password is wrong",
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