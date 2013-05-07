package com.versidyne.vexis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity implements ItemListFragment.Callbacks {
	
    private boolean mTwoPane;
    private Boolean LoggedIn = false;
	private String MEID;
	private String SIM;
	private Bundle arguments;
	private String session;
	private Intent serviceIntent;
	private LocationService s;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // disable strict mode for http
        StrictMode.enableDefaults();
        // gather arguments
    	arguments = getIntent().getExtras();
    	if (arguments == null) { session = "0"; }
    	else { session = (String) arguments.get("session"); }
        // gather device information
        final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    	MEID = mTelephony.getDeviceId();
    	SIM = mTelephony.getSimSerialNumber();
    	// authenticate
    	if (session == null || session == "0") {
    		if (authenticate()) { authenticated(); }
    		else { login(); }
    	} else {
    		authenticated();
    	}
    	
    	//wordList = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, wordList);
        //setListAdapter(adapter);
    	//doBindService();
    	
    	// TODO: If exposing deep links into your app, handle intents here.
    	
    }
    
    // service
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
          s = ((LocationService.MyBinder) binder).getService();
          Toast.makeText(ItemListActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        }
        public void onServiceDisconnected(ComponentName className) {
          s = null;
        }
    };
    private ArrayAdapter<String> adapter;
    private List<String> UserList;
    
    // menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("Exit");
    	return true;
    }
    
    // menu events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == 0) {
    		int pid = android.os.Process.myPid(); 
    		android.os.Process.killProcess(pid); 
    		return true;
    	}
    	Toast.makeText(this, "This feature is currently under construction.", Toast.LENGTH_LONG).show();
    	return super.onOptionsItemSelected(item);
    }
    
    /**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
        	// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
        	// In single-pane mode, simply start the detail activity
			// for the selected item ID.
        	Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra("session", session);
            detailIntent.putExtra("mTwoPane", mTwoPane);
            startActivity(detailIntent);
        }
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
    
    private boolean login() {
    	if (LoggedIn == false) {
	    	setContentView(R.layout.login);
			Button launch = (Button)findViewById(R.id.login_button);
			launch.setOnClickListener( new OnClickListener() {
				public void onClick(View viewParam) {
					// hide keyboard
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					// gather text
					EditText emailEditText = (EditText) findViewById(R.id.txt_email);
					EditText passwordEditText = (EditText) findViewById(R.id.txt_password);
					if(emailEditText == null || passwordEditText == null){
						Toast.makeText(ItemListActivity.this, "Please enter an email address and password to continue.", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(ItemListActivity.this, "Authenticating...", Toast.LENGTH_SHORT).show();
						String email = emailEditText.getText().toString();
						String password = passwordEditText.getText().toString();
						LoggedIn = authenticate(email, password);
						if (LoggedIn) { authenticated(); }
						else { Toast.makeText(ItemListActivity.this, "Login unsuccessful.", Toast.LENGTH_LONG).show(); }
					}
				}
			});
    	}
    	else {
    		authenticated();
    	}
    	return LoggedIn;
    }
    
    private boolean authenticate(String email, String password) {
    	try {
	    	HttpClient httpclient = getClient();
	    	String getURL = "http://api.getvexis.com/?login=" + email + "&pass=" + password;
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
			session = sb.toString().trim();
			if (session.contentEquals("0")) {
	    		return false;
	    	} else {
	    		return true;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    private boolean authenticate() {
    	try {
	    	HttpClient httpclient = getClient();
	    	String getURL = "http://api.getvexis.com/?meid=" + MEID + "&sim=" + SIM;
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
			session = sb.toString().trim();
			if (session.contentEquals("0")) {
	    		return false;
	    	} else {
	    		return true;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    // begin service
    private void doBindService() {
    	serviceIntent = new Intent(this, MessageService.class);
    	serviceIntent.putExtra("session", session);
        //startService(serviceIntent);
    	bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }
    
    // return service data
    public void showServiceData(View view) {
        if (s != null) {
        	Toast.makeText(this, "Number of elements" + s.getUserList().size(), Toast.LENGTH_SHORT).show();
        	UserList.clear();
        	UserList.addAll(s.getUserList());
        	adapter.notifyDataSetChanged();
        }
    }
    
    // only after authenticated
    private void authenticated () {
    	
    	//doBindService();
    	
    	setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
        	// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
            mTwoPane = true;
            
            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }
    }
}
