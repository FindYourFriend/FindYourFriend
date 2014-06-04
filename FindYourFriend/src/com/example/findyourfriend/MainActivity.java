package com.example.findyourfriend;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {

	private LinearLayout slidingPanel;
	private boolean isExpanded;
	private DisplayMetrics metrics;
	private ListView listView;
	private RelativeLayout headerPanel;
	private RelativeLayout menuPanel;
	private int panelWidth;
	private ImageView menuViewButton;
	Button menu1;
	Button menu2;
	Button menu3;
	Button menu4;
	TextView txtpays;
	String[] listeStrings = { "Tunisie", "Libye", "Egypte", "Yemen", "Syrie" };
	String[] listeStrings2 = { "Mauritanie", "Maroc", "Algerie",
			"Arabie saoudite", "jordanie", "Pays du golf" };
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;

	ArrayList<String> phone_numbers = new ArrayList<String>();

	ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	GoogleMap mMap;
	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;
	boolean mUpdatesRequested;
	SharedPreferences mPrefs;
	Editor mEditor;
	Marker marker;
	Marker destinationMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_view);
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mLocationClient = new LocationClient(this, this, this);
		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

		// Open the shared preferences
		mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		// Get a SharedPreferences editor
		mEditor = mPrefs.edit();
		// Start with updates turned off
		mUpdatesRequested = false;

		// Initialize
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		panelWidth = (int) ((metrics.widthPixels) * 0.75);

		headerPanel = (RelativeLayout) findViewById(R.id.header);
		headerPanelParameters = (LinearLayout.LayoutParams) headerPanel
				.getLayoutParams();
		headerPanelParameters.width = metrics.widthPixels;
		headerPanel.setLayoutParams(headerPanelParameters);

		menuPanel = (RelativeLayout) findViewById(R.id.menuPanel);
		menuPanelParameters = (FrameLayout.LayoutParams) menuPanel
				.getLayoutParams();
		menuPanelParameters.width = panelWidth;
		menuPanel.setLayoutParams(menuPanelParameters);

		slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
		slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel
				.getLayoutParams();
		slidingPanelParameters.width = metrics.widthPixels;
		slidingPanel.setLayoutParams(slidingPanelParameters);

		listView = (ListView) findViewById(R.id.listView1);
		listViewParameters = (LinearLayout.LayoutParams) listView
				.getLayoutParams();
		listViewParameters.width = metrics.widthPixels;
		listView.setLayoutParams(listViewParameters);

		// Slide the Panel

		menu1 = (Button) findViewById(R.id.menu_item_1);

		menu2 = (Button) findViewById(R.id.menu_item_2);
		menu1.setOnClickListener(this);
		menu2.setOnClickListener(this);
		menu3 = (Button) findViewById(R.id.menu_item_3);
		menu3.setOnClickListener(this);
		menu4 = (Button) findViewById(R.id.menu_item_4);
		menu4.setOnClickListener(this);
		menuViewButton = (ImageView) findViewById(R.id.menuViewButton);

		menuViewButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!isExpanded) {
					isExpanded = true;

					// Expand
					new ExpandAnimation(slidingPanel, panelWidth,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);
				} else {
					isExpanded = false;

					// Collapse
					new CollapseAnimation(slidingPanel, panelWidth,
							TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
							TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f,
							0, 0.0f);

				}
			}
		});

		Inet inet = new Inet();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String userPhone = telephonyManager.getLine1Number();
		if (inet.getListOfNotices(userPhone) != null)
			Toast.makeText(this, "You have new notifications, check them", 5000)
					.show();
	}

	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();

	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		// If the client is connected
		if (mLocationClient.isConnected()) {
			/*
			 * Remove location updates for a listener. The current Activity is
			 * the listener, so the argument is "this".
			 */
			mLocationClient.removeLocationUpdates(this);
		}
		/*
		 * After disconnect() is called, the client is considered "dead".
		 */
		mLocationClient.disconnect();
		super.onStop();
	}

	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated

		mCurrentLocation = location;

		if (marker != null)
			marker.remove();
		// Bitmap bmp = BitmapFactory.decodeResource(getResources(),
		// R.drawable.ic_launcher);
		// marker = mMap.addMarker(new MarkerOptions()
		// .position(
		// new LatLng(mCurrentLocation.getLatitude(),
		// mCurrentLocation.getLongitude()))
		// .title("Julia Vovk").snippet("I'm here")
		// .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
		mMap.setMyLocationEnabled(true);
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		if (mUpdatesRequested) {
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}

		mCurrentLocation = mLocationClient.getLastLocation();
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation
						.getLongitude()), 17));
		mMap.setMyLocationEnabled(true);
		// marker = mMap.addMarker(new MarkerOptions()
		// .position(
		// new LatLng(mCurrentLocation.getLatitude(),
		// mCurrentLocation.getLongitude()))
		// .title("Julia Vovk").snippet("I'm here"));
		// mMap.addMarker(new MarkerOptions()
		// .position(
		// new LatLng(50.02,
		// 36.2)));
		mUpdatesRequested = true;

		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {

				// destinationMarker = mMap.addMarker(new MarkerOptions()
				// .position(point));

			}
		});

	}

	protected void onPause() {
		// Save the current setting for updates
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();
		super.onPause();
	}

	@Override
	protected void onResume() {
		/*
		 * Get any previous setting for location updates Gets "false" if an
		 * error occurs
		 */
		super.onResume();
		if (mPrefs.contains("KEY_UPDATES_ON")) {
			mUpdatesRequested = mPrefs.getBoolean("KEY_UPDATES_ON", false);

			// Otherwise, turn off location updates
		} else {
			mEditor.putBoolean("KEY_UPDATES_ON", false);
			mEditor.commit();
		}
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */

			showDialog(connectionResult.getErrorCode());
		}
	}

	public String makeURL(double sourcelat, double sourcelog, double destlat,
			double destlog) {
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString(sourcelog));
		urlString.append("&destination=");// to
		urlString.append(Double.toString(destlat));
		urlString.append(",");
		urlString.append(Double.toString(destlog));
		urlString.append("&sensor=false&mode=driving&alternatives=true");
		return urlString.toString();
	}

	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	private class connectAsyncTask extends AsyncTask<Void, Void, String> {
		private ProgressDialog progressDialog;
		String url;

		public connectAsyncTask(String urlPass) {
			url = urlPass;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage("Fetching route, Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			JSONParser jParser = new JSONParser();
			String json = jParser.getJSONFromUrl(url);
			return json;
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.menu_item_1:
			intent = new Intent(this, ContactsActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_item_2:
			intent = new Intent(this, FriendsActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_item_3:
			intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_item_4:
			intent = new Intent(this, NotificationsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}
}
