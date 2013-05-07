package com.versidyne.vexis;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class ItemDetailActivity extends FragmentActivity implements LocationListener {
    
	private TextView accuracyField;
	private TextView altitudeField;
	private TextView bearingField;
	private TextView latitudeField;
	private TextView longitudeField;
	private TextView speedField;
	private LocationManager locationManager;
	private Bundle arguments;
	private String provider;
	private String ItemID;
	private String MEID;
	private String SIM;
	private String session;
	//private boolean mTwoPane;
	
	private float acc;
	private double alt;
	private float ber;
	private double lat;
	private double lng;
	private float spe;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        // clear bundle data
        super.onCreate(savedInstanceState);
        StrictMode.enableDefaults();
        // gather arguments
    	arguments = getIntent().getExtras();
        ItemID = (String) arguments.get(ItemDetailFragment.ARG_ITEM_ID);
        session = (String) arguments.get("session");
        //mTwoPane = Boolean.valueOf((String)arguments.get("mTwoPane"));
        // gather IDs
        final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    	MEID = mTelephony.getDeviceId();
    	SIM = mTelephony.getSimSerialNumber();
    	String Subscriber = mTelephony.getSubscriberId();
        
        // begin handling data
    	
    	/*if (ItemID.contentEquals("1")) {
    	
	    	setContentView(R.layout.account);
	    	
	    }
		
		else*/ /*if (ItemID.contentEquals("2")) {
        	
        	setContentView(R.layout.associates);
        	
        }
		
    	else*/ if (ItemID.contentEquals("3")) {
        	
    		setContentView(R.layout.device);
        	
        	TextView MEIDField = (TextView) findViewById(R.id.TextView02);
        	TextView SIMField = (TextView) findViewById(R.id.TextView04);
        	TextView SubscriberField = (TextView) findViewById(R.id.TextView06);
        	
        	MEIDField.setText(MEID);
	    	SIMField.setText(SIM);
	    	SubscriberField.setText(Subscriber);
        	
        }
    	
    	/*else if (ItemID.contentEquals("4")) {
    		
	    	setContentView(R.layout.email);
	    	
	    }*/
        
        else if (ItemID.contentEquals("5")) {
        	
        	setContentView(R.layout.location);
        	
        	accuracyField = (TextView) findViewById(R.id.TextView02);
        	altitudeField = (TextView) findViewById(R.id.TextView04);
        	bearingField = (TextView) findViewById(R.id.TextView06);
        	latitudeField = (TextView) findViewById(R.id.TextView08);
    	    longitudeField = (TextView) findViewById(R.id.TextView10);
    	    speedField = (TextView) findViewById(R.id.TextView12);
    	    
    	    // Get the location manager
    	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	    // Define the criteria how to select the location provider -> use default
    	    Criteria criteria = new Criteria();
    	    criteria.setAccuracy(1);
    	    criteria.setAltitudeRequired(true);
    	    criteria.setBearingRequired(true);
    	    criteria.setSpeedRequired(true);
    	    
    	    provider = locationManager.getBestProvider(criteria, true);
    	    Location location = locationManager.getLastKnownLocation(provider);
    	    
    	    // Initialize the location fields
    	    if (location != null) {
    	    	System.out.println("Provider " + provider + " has been selected.");
    	    	onLocationChanged(location);
    	    	
    	    } else {
    	    	accuracyField.setText("Not available");
    	    	altitudeField.setText("Not available");
    	    	bearingField.setText("Not available");
    	    	latitudeField.setText("Not available");
    	    	longitudeField.setText("Not available");
    	    	speedField.setText("Not available");
    	    }
    	    
        }
    	
        /*else if (ItemID.contentEquals("6")) {
        	
        	setContentView(R.layout.messaging);
        	
        }
    	
        else if (ItemID.contentEquals("7")) {
        	
        	setContentView(R.layout.notifications);
        	
        }
        
        else if (ItemID.contentEquals("8")) {
        	
        	setContentView(R.layout.profile);
        	
        }*/
        
        else {
        	
        	setContentView(R.layout.activity_item_detail);
        	
        	if (savedInstanceState == null) {
                Bundle arguments = new Bundle();
                arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                        getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            }
        	
        }
        
        //NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
        
        // create action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
    }
    
    /*private void alert (String title, String message) {
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle(title);
    	alertDialog.setMessage(message);
    	//alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    	   //public void onClick(DialogInterface dialog, int which) {
    	   //}
    	//});
    	//alertDialog.setIcon(R.drawable.icon);
    	alertDialog.show();
    }*/
    
    // menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if (ItemID.contentEquals("3")) {
    		menu.add("Register");
    	}
    	else if (ItemID.contentEquals("5")) {
    		menu.add("Save");
    	}
    	else {
    		//menu.add("Save");
    	}
    	return true;
    }
    
    // menu events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
        if (item.getItemId() == android.R.id.home) {
        	Intent detailIntent = new Intent(this, ItemListActivity.class);
        	detailIntent.putExtra("session", session);
            NavUtils.navigateUpTo(this, detailIntent);
            return true;
        }
        else {
        	if (ItemID.contentEquals("3")) {
        		if (item.getItemId() == 0) {
                	if (register()) {
                		Toast.makeText(this, "Registration successful.", Toast.LENGTH_LONG).show();
                	}
                	else {
                		Toast.makeText(this, "Registration unsuccessful.", Toast.LENGTH_LONG).show();
                	}
                	return true;
                }
        	}
        	else if (ItemID.contentEquals("5")) {
        		if (item.getItemId() == 0) {
                	if (save_location()) {
                		Toast.makeText(this, "Location saved successfully.", Toast.LENGTH_LONG).show();
                	}
                	else {
                		Toast.makeText(this, "Failed to save location.", Toast.LENGTH_LONG).show();
                	}
                	return true;
                }
        	}
        }
        Toast.makeText(this, "This feature is currently under construction.", Toast.LENGTH_LONG).show();
    	return super.onOptionsItemSelected(item);
    }
    
    // Request updates at startup
	@Override
	protected void onResume() {
		super.onResume();
		if (ItemID.contentEquals("5")) {
			locationManager.requestLocationUpdates(provider, 1, 1, this);
		}
	}
	
	// Remove the location listener updates when Activity is paused
	@Override
	protected void onPause() {
		super.onPause();
		if (ItemID.contentEquals("5")) {
			locationManager.removeUpdates(this);
		}
	}

    // default location manager functions
	@Override
	public void onLocationChanged(Location location) {
		if (ItemID.contentEquals("5")) {
			
			// gather data
		    acc = location.getAccuracy();
		    alt = location.getAltitude();
		    ber = location.getBearing();
		    lat = location.getLatitude();
		    lng = location.getLongitude();
		    spe = location.getSpeed();
		    
		    // units
		    String accunits = " meters";
		    String altunits = " meters";
		    String speunits = " kph";
		    
		    // imperial conversion
		    alt = 3.28083989501312 * alt;
		    altunits = " feet";
		    acc = (float) (3.28083989501312 * acc);
		    accunits = " feet";
		    spe = (float) (2.2369362920544 * spe);
		    speunits = " mph";
		    
		    // display data
		    accuracyField.setText(String.valueOf((int)acc) + accunits);
		    altitudeField.setText(String.valueOf((int)alt) + altunits);
		    bearingField.setText(String.valueOf((int)ber) + "°");
		    latitudeField.setText(String.valueOf(lat));
		    longitudeField.setText(String.valueOf(lng));
		    speedField.setText(String.valueOf((int)spe) + speunits);
		    
		}
	    
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		if (ItemID.contentEquals("5")) {
			Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		if (ItemID.contentEquals("5")) {
			Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	
	private DefaultHttpClient getClient() {
		DefaultHttpClient ret = null;
		//SETS UP PARAMETERS
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);
		//REGISTERS SCHEMES FOR BOTH HTTP AND HTTPS
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
		sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));
		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
		ret = new DefaultHttpClient(manager, params);
		return ret;
	}
	
	private boolean register() {
    	try {
	    	HttpClient httpclient = getClient();
	    	String getURL = "http://api.getvexis.com/?session=" + session + "&meid=" + MEID + "&sim=" + SIM;
	    	HttpGet httpget = new HttpGet(getURL);
	    	HttpResponse response = httpclient.execute(httpget);
	    	HttpEntity entity = response.getEntity();
	    	InputStream is = entity.getContent();
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	    	StringBuilder sb = new StringBuilder();
	    	String line;
			while ((line = reader.readLine()) != null) {
			    sb.append(line + "\n");
			}
			is.close();
			String output = sb.toString().trim();
			if (output.contentEquals("0")) {
	    		return false;
	    	} else if (output.contentEquals("2")) {
	    		Toast.makeText(this, "This device is already registered with your account.", Toast.LENGTH_LONG).show();
	    		return false;
	    	} else {
	    		return true;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
    
	private boolean save_location() {
    	try {
	    	HttpClient httpclient = getClient();
	    	String getURL = "http://api.getvexis.com/?session=" + session + "&accuracy=" + acc + "&altitude=" + alt + "&bearing=" + ber + "&latitude=" + lat + "&longitude=" + lng + "&speed=" + spe;
	    	HttpGet httpget = new HttpGet(getURL);
	    	HttpResponse response = httpclient.execute(httpget);
	    	HttpEntity entity = response.getEntity();
	    	InputStream is = entity.getContent();
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	    	StringBuilder sb = new StringBuilder();
	    	String line;
			while ((line = reader.readLine()) != null) {
			    sb.append(line + "\n");
			}
			is.close();
			String output = sb.toString().trim();
			if (output.contentEquals("0")) {
	    		return false;
	    	} else {
	    		return true;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
	
}
