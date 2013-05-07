package com.versidyne.vexis;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class MessageService extends Service {
    
	private final IBinder mBinder = new MyBinder();
	private ArrayList<String> list = new ArrayList<String>();
	
	private String session;
	
    @Override
	public void onCreate() {
    	
        // clear bundle data
    	super.onCreate();
        StrictMode.enableDefaults();
        
    }
    
    @Override
	public void onDestroy() {
		super.onDestroy();
	}
    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
    	// gather arguments
    	session = intent.getExtras().getString("session");
    	// run the updater
	    //updater();
	    
	    /*Random random = new Random();
	    if (random.nextBoolean()) {
	      list.add("Linux");
	    }
	    if (random.nextBoolean()) {
	      list.add("Android");
	    }
	    if (random.nextBoolean()) {
	      list.add("iPhone");
	    }
	    if (random.nextBoolean()) {
	      list.add("Windows7");
	    }
	    if (list.size() >= 20) {
	      list.remove(0);
	    }*/
	    
    	// keep service running until it is explicitly stopped
        return START_STICKY;
        // or not
        //return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	public class MyBinder extends Binder {
		MessageService getService() {
			return MessageService.this;
		}
	}
	
	public List<String> getUserList() {
		return list;
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
    
	private boolean sendinfo() {
    	try {
	    	HttpClient httpclient = getClient();
	    	//String getURL = "http://api.getvexis.com/?session=" + session + "&accuracy=" + acc + "&altitude=" + alt + "&bearing=" + ber + "&latitude=" + lat + "&longitude=" + lng + "&speed=" + spe;
	    	String getURL = "http://api.getvexis.com/?session=" + session + "&a=";
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
	
	private void updater () {
		new Thread() {
            public void run() {
            	Looper.prepare();
                try{
                    sendinfo();
                    sleep(5000);
                } catch (Exception e) {
                	e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
	}
	
}
