package sharedpreferences;

import java.util.HashMap;

import activities.CCM.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ManageSharedPreferences {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared preferences mode
	int PRIVATE_MODE = 0;

	// Sharedpreferences file name
	private static final String PREF_NAME = "DataWoman";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";

	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	// Password
	public static final String KEY_PASS = "pass";

	// Days of cycle
	public static final String KEY_CYCLE = "cycle";

	// Date of start period
	public static final String KEY_DSTART = "datestart";

	// Date of finish the period
	public static final String KEY_DEND = "dateend";

	// Constructor
	public ManageSharedPreferences(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createDataWoman(String name, String email, String pass) {
		// Storing name in pref
		editor.putString(KEY_NAME, name);

		// Storing email in pref
		editor.putString(KEY_EMAIL, email);

		// Storing pass in pref
		editor.putString(KEY_PASS, pass);

		// commit changes
		editor.commit();
	}

	public void createCycleShared(int cycle) {
		
		editor.putInt(KEY_CYCLE, cycle);
		
		editor.commit();

	}

	public void createDataCycle(String datestart, String dateend) {

		// Storing email in pref
		editor.putString(KEY_DSTART, datestart);

		// Storing pass in pref
		editor.putString(KEY_DEND, dateend);

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();

		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		// user password
		user.put(KEY_PASS, pref.getString(KEY_PASS, null));

		// return user
		return user;
	}

	/**
	 * Clear data of sharedpreferences
	 * */
	public void clearUserData() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
	}

	/**
	 * QuickCheck for data on SharedPreferences
	 * **/
	// Get Login State
	public String getData() {
		String name = pref.getString(KEY_NAME, "");
		String email = pref.getString(KEY_EMAIL, "");
		String pass = pref.getString(KEY_PASS, "");

		String data = name + "/" + email + "/" + pass;

		return data;
	}

	public String getEmail() {
		String email = pref.getString(KEY_EMAIL, "");
		return email;
	}

	public String getpass() {
		String pass = pref.getString(KEY_PASS, "");
		return pass;
	}

	public int getCycle() {
		int cycle = pref.getInt(KEY_CYCLE, 0);
		return cycle;
	}
	
	public String getDStart() {
		String dstart = pref.getString(KEY_DSTART, "");
		return dstart;
	}
	
	public String getDEnd() {
		String dend = pref.getString(KEY_DEND, "");
		return dend;
	}
	
}