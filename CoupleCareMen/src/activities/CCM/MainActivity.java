package activities.CCM;
import java.util.HashMap;

import AlertDialogManager.AlertDialogManager;
import SessionManager.SessionManager;
import activities.CCM.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	// Navigation Drawer
	private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    
    private CharSequence tituloSeccion;  
    private CharSequence tituloApp;
    // End Navigation Drawer

    //Session
    AlertDialogManager alert = new AlertDialogManager();
	public final static String EXTRA_MESSAGE = "";
	AlertDialog.Builder builderInternetState;

	String email;
	
	SessionManager session;
	
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AlertDialog.Builder builder;
		
		opcionesMenu = new String[] {"Home", "Settings", "Exit"};
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
						Intent Main = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(Main);
						finish();
						break;
					case 1:
						Intent Settings = new Intent(getApplicationContext(), NoConnection.class);
						startActivity(Settings);
						finish();
						break;
					case 2:
						session.logoutUser();
						finish();
						break;
				}

				drawerList.setItemChecked(position, true);

				tituloSeccion = opcionesMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);

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
				getSupportActionBar().setTitle(tituloSeccion);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		// Session class instance
        session = new SessionManager(getApplicationContext());
        
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        String id_user = user.get(SessionManager.KEY_EMAIL);
        
		if(isOnline()== true){
	        if(session.isLoggedIn()){
	        	Toast.makeText(getApplicationContext(), "Welcome to Couple Care "+id_user, Toast.LENGTH_LONG).show();
	        	 
	        	Resources res = getResources();
	            
	            TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
	            tabs.setup();
	            
	            TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
	            spec.setContent(R.id.tab1);
	            spec.setIndicator("HOME", 
	            		res.getDrawable(android.R.drawable.ic_btn_speak_now));
	            tabs.addTab(spec);
	            
	            
	            spec=tabs.newTabSpec("mitab2");
	            spec.setContent(R.id.tab2);
	            spec.setIndicator("CALENDAR", 
	            		res.getDrawable(android.R.drawable.ic_dialog_map));
	            tabs.addTab(spec);
	            
	            spec=tabs.newTabSpec("mitab3");
	            spec.setContent(R.id.tab3);
	            spec.setIndicator("SHARE", 
	            		res.getDrawable(android.R.drawable.ic_dialog_map));
	            tabs.addTab(spec);
	            
	            tabs.setCurrentTab(0);
	            
	            tabs.setOnTabChangedListener(new OnTabChangeListener() {
	    			public void onTabChanged(String tabId) {
	    				Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
	    			}
	    		});
	        }
	        else{
	        	Intent i = new Intent(this, GuideBegin.class);
	    	    startActivity(i);
	    	    finish();
	        }
	        
	        /**
	         * Call this function whenever you want to check user login
	         * This will redirect user to LoginActivity is he is not
	         * logged in
	         * */
        }
        else{
        	//En caso de que la conexion sea falsa Mandar una alerta pidiendo al usuario si desea intentarlo de nuevo
        	Intent i = new Intent(this, NoConnection.class);
    	    startActivity(i);
    	    finish();
        }
		
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
		
		switch(item.getItemId())
		{
			case R.id.action_settings:
				Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();;
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
    
    @Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		com.facebook.Settings.publishInstallAsync(getApplicationContext(),
				"163407503868589");
	}
}