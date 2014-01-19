package activities.couple;

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
        Intent i = new Intent(this, MainActivity.class);
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
}