package activities.CCM;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.http.protocol.HTTP;

import listcalendar.ListEntrance;
import listcalendar.List_Adapter;

import calculatedays.ColorEnum;
import calculatedays.Day;
import calculatedays.SimpleDate;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import AlertDialogManager.AlertDialogManager;
import Gson.Men;
import Gson.Period;
import Gson.Women;
import SessionManager.SessionManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DrawerHomeCal extends ActionBarActivity {

	TextView txtFecha, txtMsg, txtNext, txtNextLabel;
	String fecha2, msg, msgNext;

	String url = "";

	AlertDialogManager alert = new AlertDialogManager();

	Women jsonWomen;
	Men jsonMen;

	int PIN = 0;

	AsyncHttpClient client = new AsyncHttpClient();
	Gson g = new Gson();
	Gson gwomen = new Gson();
	Gson gmen = new Gson();
	Gson gStatusW = new Gson();
	Gson gStatus = new Gson();
	
	String idmen="";
	String status = "";
	
	ShareActionProvider myShareActionProvider;

	SessionManager session;

	ListView list;
	int DurationCycle;
	int PeriodDays;
	Calendar cal2;

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

	String idwomen = "";

	boolean sdDisponible = false;
	boolean sdAccesoEscritura = false;

	SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy/MM/dd");
	Date CPeriodStart = null;
	Date CPeriodStart1 = null;
	Date CPeriodEnd = null;
	Date CFertileStart1 = null;
	Date CFertileEnd1 = null;
	Date CMostFertile = null;
	Date CFertileStart2 = null;
	Date CFertileEnd2 = null;
	Date CLessFertileStart = null;
	Date CLessFertileEnd = null;

	Date fechasumar = null;

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

			// Se asigna la URL del JSON alojado en Internet
			client.get("http://couplecare.us/backendcouple/JSON/men/" + id
					+ ".json", new AsyncHttpResponseHandler() {
				// En caso de que haya una respuesta
				@Override
				public void onSuccess(String response) {
					// Se usa el GSON pasandole los paremetros recibido a la
					// clase Women en donde tendremos nuestras
					// variables en donde los valores serán pasados desde el
					// JSON.

					jsonMen = gmen.fromJson(response, Men.class);

					CouplePin = jsonMen.CouplePin.toString();
					UserName = jsonMen.Name.toString();

					if (CouplePin.equals("")) {
						// Starting MainActivity
						Intent i = new Intent(getApplicationContext(),
								CouplePin.class);
						// Delete backStack
						startActivity(i);
						finish();
					} else {
						PIN = 1;
					}
				}
			});

			// Se asigna la URL del JSON alojado en Internet
			client.get("http://couplecare.us/backendcouple/JSON/men/" + id
					+ ".json", new AsyncHttpResponseHandler() {
				// En caso de que haya una respuesta
				@Override
				public void onSuccess(String response) {
					// Se usa el GSON pasandole los paremetros recibido a la
					// clase Women en donde tendremos nuestras
					// variables en donde los valores serán pasados desde el
					// JSON.
					jsonMen = gmen.fromJson(response, Men.class);
					CouplePin = jsonMen.CouplePin.toString();
					UserName = jsonMen.Name.toString();
				}
			});
		} else {
		}

		// session.checkLogin();

		if (!session.isLoggedIn()) {
			Intent i = new Intent(getApplicationContext(), GuideBegin.class);
			// Closing all the Activities

			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			startActivity(i);
			finish();
		}

		// ViewPager
		vp = (ViewPager) findViewById(R.id.viewpager);

		myAdapter = new vpAdapter();
		vp.setAdapter(myAdapter);

		opcionesMenu = new String[] { "Home", "Settings", "Status","Log out" };
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
					Intent home = new Intent(getApplicationContext(),
							DrawerHomeCal.class);

					// Closing all the Activities
					home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					// Add new Flag to start new Activity
					home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					startActivity(home);

					finish();
					break;
				case 1:
					Intent toDrawerSettings = new Intent(
							getApplicationContext(), DrawerSettings.class);

					// Closing all the Activities
					toDrawerSettings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					// Add new Flag to start new Activity
					toDrawerSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					startActivity(toDrawerSettings);

					finish();
					break;
					
				case 2:
					showDialogStatus(R.layout.promptsync);
					
					break;
				case 3:
					session.logoutUser();

					// Closing all the Activities
					Intent login = new Intent(getApplicationContext(),
							GuideBegin.class);

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
				ActivityCompat.invalidateOptionsMenu(DrawerHomeCal.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(UserName);
				ActivityCompat.invalidateOptionsMenu(DrawerHomeCal.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem item = menu.findItem(R.id.action_share);
		
		myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
				//
				//AnalyticsBoton("Share_App");
				//---
    	    	Intent i = new Intent(Intent.ACTION_SEND);
    			i.setType("text/plain");
    			i.putExtra(Intent.EXTRA_TEXT, "Hey there, I'm using CoupleCare it's great, download it at http://www.couplecare.us/index.php/download and give it a try :D.");
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
			//Home and logout are changed.
		case R.id.action_home:
			Intent home = new Intent(getApplicationContext(),DrawerHomeCal.class); 
			 
			// Closing all the Activities
			home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
			// Add new Flag to start new Activity
			home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			startActivity(home);
				
			finish();
			
			
			break;
		case R.id.action_logout:
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
		case R.id.action_status:
			showDialogStatus(R.layout.promptsync);
			
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

		public void fechaactual() {
			// Obtenemos la fecha actual
			Calendar ahoraCal = Calendar.getInstance();
			int dia = ahoraCal.get(Calendar.DAY_OF_MONTH);
			int mes = ahoraCal.get(Calendar.MONTH);
			mes = mes + 1; // Se le suma uno al mes para obtener bien el numero
							// del
							// mes
			int anio = ahoraCal.get(Calendar.YEAR);
			fecha2 = anio + "/" + mes + "/" + dia;
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
				v = inflater.inflate(R.layout.activity_home, null);

				// Textview de la fecha actual
				txtFecha = (TextView) v.findViewById(R.id.fecha);
				txtMsg = (TextView) v.findViewById(R.id.msg);

				fechaactual();

				id = id.replace(" ", "");
				if (id.equals("0") || id.equals("")) {
					session.logoutUser();
				}

				else {
					// Se asigna la URL del JSON alojado en Internet
					url = "http://couplecare.us/backendcouple/swpin.php?pin=";
					client.get(url, new AsyncHttpResponseHandler() {
						// En caso de que haya una respuesta
						@Override
						public void onSuccess(String response) {

							url = "http://couplecare.us/backendcouple/searchwomenbypin.php?pin="
									+ CouplePin;

							client.get(url, new AsyncHttpResponseHandler() {
								// En caso de que haya una respuesta
								@Override
								public void onSuccess(String response) {
									url = "http://couplecare.us/backendcouple/swpin.php?pin="
											+ CouplePin;
									// Se usa el GSON pasandole los paremetros
									// recibido a la clase Women en donde
									// tendremos nuestras
									response = response.replace(" ", "");

									idwomen = response;

									// Se asigna la URL del JSON alojado en
									// Internet
									client.get(
											"http://couplecare.us/backendcouple/JSON/periods/"
													+ idwomen + ".json",
											new AsyncHttpResponseHandler() {
												// En caso de que haya una
												// respuesta
												@Override
												public void onSuccess(
														String response) {
													// Se usa el GSON pasandole
													// los paremetros recibido a
													// la clase numero en donde
													// tendremos nuestras
													// variables en donde los
													// valores serán pasados
													// desde el JSON.
													Period json = g.fromJson(
															response,
															Period.class); // del
																			// jason
																			// que
																			// tenemos
																			// conviertelo
																			// en
																			// un
																			// arreglo
													fechaactual();

													msgNext = json.NPeriodStart
															.toString();
													txtFecha.setText(""
															+ fecha2);

													Date fechasumar = null;

													try {
														String SDuration = json.CDurationCycle
																.toString();
														if (!SDuration
																.equals("")) {
															int DurationCycle = Integer
																	.parseInt(json.CDurationCycle
																			.toString());
														}

														fechasumar = formatoDelTexto
																.parse(fecha2);
														CPeriodStart = formatoDelTexto
																.parse(json.CPeriodStart
																		.toString());
														CPeriodStart1 = formatoDelTexto
																.parse(json.CPeriodStart
																		.toString());
														CPeriodEnd = formatoDelTexto
																.parse(json.CPeriodEnd
																		.toString());
														CFertileStart1 = formatoDelTexto
																.parse(json.CFertileStart1
																		.toString());
														CFertileEnd1 = formatoDelTexto
																.parse(json.CFertileEnd1
																		.toString());
														CMostFertile = formatoDelTexto
																.parse(json.CMostFertile
																		.toString());
														CFertileStart2 = formatoDelTexto
																.parse(json.CFertileStart2
																		.toString());
														CFertileEnd2 = formatoDelTexto
																.parse(json.CFertileEnd2
																		.toString());
														CLessFertileStart = formatoDelTexto
																.parse(json.CLessFertileStart
																		.toString());
														CLessFertileEnd = formatoDelTexto
																.parse(json.CLessFertileEnd
																		.toString());
														String newdate = "";

														Calendar cal = Calendar
																.getInstance();
														cal.setTime(CPeriodStart);
														cal.add(Calendar.DAY_OF_MONTH,
																1);
														newdate = formatoDelTexto.format(cal
																.getTime());

														int i = 0;
														do {

															CPeriodStart = formatoDelTexto
																	.parse(newdate);
															Calendar cal2 = Calendar
																	.getInstance();
															cal2.setTime(CPeriodStart);
															cal2.add(
																	Calendar.DAY_OF_MONTH,
																	1);
															newdate = formatoDelTexto
																	.format(cal2
																			.getTime());

															if ((fechasumar
																	.after(CPeriodStart1) && fechasumar
																	.before(CPeriodEnd))
																	|| fechasumar
																			.equals(CPeriodStart1)
																	|| fechasumar
																			.equals(CPeriodEnd)) {
																txtMsg.setText("Period Day");
															} else if (fechasumar
																	.after(CFertileStart1)
																	&& fechasumar
																			.before(CFertileEnd1)
																	|| fechasumar
																			.equals(CFertileStart1)
																	|| fechasumar
																			.equals(CFertileEnd1)) {
																txtMsg.setText("Fertile Day");
															} else if (fechasumar
																	.equals(CMostFertile)) {
																txtMsg.setText("Most Fertile Day");
															} else if (fechasumar
																	.after(CFertileStart2)
																	&& fechasumar
																			.before(CFertileEnd2)
																	|| fechasumar
																			.equals(CFertileStart2)
																	|| fechasumar
																			.equals(CFertileEnd2)) {
																txtMsg.setText("Fertile Day");
															} else if (fechasumar
																	.after(CLessFertileStart)
																	&& fechasumar
																			.before(CLessFertileEnd)
																	|| fechasumar
																			.equals(CLessFertileStart)
																	|| fechasumar
																			.equals(CLessFertileEnd)) {
																txtMsg.setText("Less Fertile Day");
															}

															i++;
														} while (i < DurationCycle);

													} catch (ParseException ex) {

														ex.printStackTrace();

													}

												}
											});
								}
							});
						}
					});

				}
				break;

			case 1:
				v = inflater.inflate(R.layout.activity_listcalendar, null);

				id = id.replace(" ", "");
				if (id.equals("0") || id.equals("")) {
					session.logoutUser();
				} else {
					// Se asigna la URL del JSON alojado en Internet
					url = "http://couplecare.us/backendcouple/swpin.php?pin=";
					client.get(url, new AsyncHttpResponseHandler() {
						// En caso de que haya una respuesta
						@Override
						public void onSuccess(String response) {

							url = "http://couplecare.us/backendcouple/searchwomenbypin.php?pin="
									+ CouplePin;

							client.get(url, new AsyncHttpResponseHandler() {
								// En caso de que haya una respuesta
								@Override
								public void onSuccess(String response) {
									url = "http://couplecare.us/backendcouple/swpin.php?pin="
											+ CouplePin;
									// Se usa el GSON pasandole los paremetros
									// recibido a la clase Women en donde
									// tendremos nuestras
									response = response.replace(" ", "");

									idwomen = response;

									// Se asigna la URL del JSON alojado en
									// Internet
									client.get(
											"http://couplecare.us/backendcouple/JSON/periods/"
													+ idwomen + ".json",
											new AsyncHttpResponseHandler() {
												// En caso de que haya una
												// respuesta
												@Override
												public void onSuccess(
														String response) {
													// Se usa el GSON pasandole
													// los paremetros recibido a
													// la clase numero en donde
													// tendremos nuestras
													// variables en donde los
													// valores serán pasados
													// desde el JSON.

													// Se usa el GSON pasandole
													// los paremetros recibido a
													// la clase numero en donde
													// tendremos nuestras
													// variables en donde los
													// valores serán pasados
													// desde el JSON.
													Period json = g.fromJson(
															response,
															Period.class); // del
																			// jason
																			// que
																			// tenemos
																			// conviertelo
																			// en
																			// un
																			// arreglo
													fechaactual();

													Date fechasumar = null;

													try {
														int DurationCycle = 0;
														String SDuration = json.CDurationCycle
																.toString();

														if (ImprimirPin(SDuration) == true) {
															DurationCycle = Integer
																	.parseInt(json.CDurationCycle
																			.toString());
														}

														PeriodDays = Integer
																.parseInt(json.PeriodDays
																		.toString());
														fechasumar = formatoDelTexto
																.parse(fecha2);
														CPeriodStart = formatoDelTexto
																.parse(json.CPeriodStart
																		.toString());
														CPeriodEnd = formatoDelTexto
																.parse(json.CPeriodEnd
																		.toString());
														CFertileStart1 = formatoDelTexto
																.parse(json.CFertileStart1
																		.toString());
														CFertileEnd1 = formatoDelTexto
																.parse(json.CFertileEnd1
																		.toString());
														CMostFertile = formatoDelTexto
																.parse(json.CMostFertile
																		.toString());
														CFertileStart2 = formatoDelTexto
																.parse(json.CFertileStart2
																		.toString());
														CFertileEnd2 = formatoDelTexto
																.parse(json.CFertileEnd2
																		.toString());
														CLessFertileStart = formatoDelTexto
																.parse(json.CLessFertileStart
																		.toString());
														CLessFertileEnd = formatoDelTexto
																.parse(json.CLessFertileEnd
																		.toString());

														String newdate = "";

														Calendar cal = Calendar
																.getInstance();
														cal.setTime(CPeriodStart);

														newdate = formatoDelTexto.format(cal
																.getTime());

														int i = 0;

														CPeriodStart = formatoDelTexto
																.parse(newdate);
														Calendar cal2 = Calendar
																.getInstance();
														cal2.setTime(CPeriodStart);
														cal2.add(
																Calendar.DAY_OF_MONTH,
																1);
														newdate = formatoDelTexto.format(cal2
																.getTime());

														cal.setTime(CPeriodStart);

														ArrayList<ListEntrance> datos = new ArrayList<ListEntrance>();
														Day dia = new Day(cal,
																true, "hola",
																ColorEnum.BLUE);
														dia.setCyclePeriod(DurationCycle);
														dia.setPeriod(PeriodDays);
														dia.calculateFertilesDays();

														for (SimpleDate fecha : dia
																.getSimpleDatesList()) {

															datos.add(new ListEntrance(
																	R.drawable.caudritotrans,
																	fecha.toString(),
																	fecha.getColor()
																			.toString()));

														}

														list = (ListView) findViewById(R.id.ListView_listado); // Aqui
																												// le
																												// hablo
																												// al
																												// listview
																												// de
																												// la
																												// vista
																												// que
																												// te
																												// mostres
														list.setAdapter(new List_Adapter(
																getApplicationContext(),
																R.layout.activity_listlayout,
																datos) { // Aqui
																			// utilizo
																			// el
																			// adapter
																			// que
																			// se
																			// crea
																			// para
																			// manejar
																			// visual

															public void onEntrada(
																	Object entrada,
																	View view) {

																if (entrada != null) {

																	TextView texto_superior_entrada = (TextView) view
																			.findViewById(R.id.textView_superior);
																	if (texto_superior_entrada != null) {
																		texto_superior_entrada
																				.setText(((ListEntrance) entrada)
																						.get_textoEncima());
																	}

																	TextView texto_inferior_entrada = (TextView) view
																			.findViewById(R.id.textView_inferior);
																	if (texto_inferior_entrada != null) {

																		texto_inferior_entrada
																				.setText(((ListEntrance) entrada)
																						.get_textoDebajo());

																		view.setBackgroundColor(Color
																				.parseColor(((ListEntrance) entrada)
																						.get_textoDebajo()));// Cambio
																												// el
																												// color
																												// para
																												// que
																												// se
																												// note?
																												// si,
																												// pero
																												// esperate,
																												// deja
																												// cambio
																												// algo
																												// más
																		texto_inferior_entrada
																				.setVisibility(View.INVISIBLE);
																		String colorstring = ((ListEntrance) entrada)
																				.get_textoDebajo();
																		if (colorstring
																				.equals("YELLOW")) {
																			view.setBackgroundColor(Color
																					.rgb(255,
																							206,
																							25)); // 250,250,77
																		} else if (colorstring
																				.equals("GREEN")) {
																			view.setBackgroundColor(Color
																					.rgb(20,
																							204,
																							176));
																		} else if (colorstring
																				.equals("CYAN")) {
																			view.setBackgroundColor(Color
																					.rgb(51,
																							190,
																							242));
																		} else if (colorstring
																				.equals("RED")) {
																			view.setBackgroundColor(Color
																					.rgb(236,
																							0,
																							140));
																		}

																		// view.setBackgroundColor(Color.rgb(new
																		// Color(0,0,0)));
																		// //Amarillo
																		// 224,208,53,
																		// 71,
																		// 171,
																		// 49
																	}

																	ImageView imagen_entrada = (ImageView) view
																			.findViewById(R.id.imageView_imagen);
																	if (imagen_entrada != null)
																		imagen_entrada
																				.setImageResource(((ListEntrance) entrada)
																						.get_idImagen());
																}
															}
														});

														list.setOnItemClickListener(new OnItemClickListener() {
															@Override
															public void onItemClick(
																	AdapterView<?> pariente,
																	View view,
																	int posicion,
																	long id) {
																ListEntrance elegido = (ListEntrance) pariente
																		.getItemAtPosition(posicion);
															}
														});

														i++;

													} catch (ParseException ex) {

														ex.printStackTrace();

													}

												}
											});
								}
							});
						}
					});
				}
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
	
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		com.facebook.Settings.publishInstallAsync(getApplicationContext(),
				"163407503868589");
	}

	public boolean ImprimirPin(String pin) {
		if (pin.equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isSynchronized(String id){
		 if(id.equals("0")){
			 status = "Not synchronized";
			 return false;
		 }
		 return true;
	 }
	
	public void showDialogStatus(int la) {
		LayoutInflater li = LayoutInflater.from(DrawerHomeCal.this);
		final View promptsView = li.inflate(la, null);

		final TextView txtpin = (TextView)promptsView.findViewById(R.id.txtcpin);
		
		HashMap<String, String> user = session.getUserDetails();
        
        // name
        id = user.get(SessionManager.KEY_PASS);
        
        client.get("http://couplecare.us/backendcouple/JSON/men/"+id+".json", new AsyncHttpResponseHandler() {
    	    //En caso de que haya una respuesta
			@Override
    	    public void onSuccess(String response) {
				//Se usa el GSON pasandole los paremetros recibido a la clase Women en donde tendremos nuestras 
				//variables en donde los valores serán pasados desde el JSON.
    	        Women jsonStatus = gStatus.fromJson(response, Women.class); 
    	        CouplePin = jsonStatus.CouplePin.toString();
    	        
    	        client.get("http://couplecare.us/backendcouple/searchwomenbypin.php?pin="+CouplePin, new AsyncHttpResponseHandler() {
    	    	    //En caso de que haya una respuesta
    				@Override
    	    	    public void onSuccess(String response) {
    					
    					response = response.replace(" ", "");
    					if(isSynchronized(idmen) == false){
    						txtpin.setText("Not Synchronized");
    					}
    					else{
    						client.get("http://couplecare.us/backendcouple/JSON/women/"+response+".json", new AsyncHttpResponseHandler() {
    				    	    //En caso de que haya una respuesta
    							@Override
    				    	    public void onSuccess(String response) {
    								
    								Men jsonWomenM = gStatusW.fromJson(response, Men.class); 
    								txtpin.setText(Html.fromHtml("Synchronized with <br><b>"+jsonWomenM.Name.toString()+"</b>"));
    								
    							}
    						}); //End Second Cliente HTTP
    					}
    					//End Else
    				}
    			});//End First Client HTTP
			}
		});

		AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(
				DrawerHomeCal.this);
		
		
		alerDialogBuilder.setView(promptsView);

		alerDialogBuilder
				.setCancelable(false)
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