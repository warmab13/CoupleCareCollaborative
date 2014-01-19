package activities.couple;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class NoConnection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_connection);
	}

	//Funcion para recargar la actividad y volver a revisar el estado de conexion del dispositivo.
    public void retryConnection(View view) {
        Intent i = new Intent(this, DrawerHomeCal.class);
        startActivity(i);
        finish();
    }
    
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.no_connection, menu);
		return true;
	}
	
	//SDK de Facebook 
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		com.facebook.Settings.publishInstallAsync(getApplicationContext(),
				"230341937134870");
	}
	
	//Google Analytics
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