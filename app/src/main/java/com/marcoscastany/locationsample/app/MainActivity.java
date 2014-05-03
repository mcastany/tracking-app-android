package com.marcoscastany.locationsample.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private LocationManager locationManager;
    private String provider;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView)findViewById(R.id.longitudValue)).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.latitudValue)).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.lastUpdate)).setVisibility(View.INVISIBLE);

        configureLocation();
    }

    private void configureLocation() {
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            provider = LocationManager.NETWORK_PROVIDER;

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            provider = LocationManager.GPS_PROVIDER;

        ((TextView)findViewById(R.id.locationProvider)).setText(provider);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if (currentLocation != location) {
                    Date date = new Date();

                    String longitud = String.valueOf(location.getLongitude());
                    String latitud = String.valueOf(location.getLatitude());

                    currentLocation = location;

                    ((TextView) findViewById(R.id.longitudValue)).setText(longitud);
                    ((TextView) findViewById(R.id.latitudValue)).setText(latitud);
                    ((TextView) findViewById(R.id.lastUpdate)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

                    ((TextView) findViewById(R.id.longitudValue)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.latitudValue)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.lastUpdate)).setVisibility(View.VISIBLE);

                    new Server().execute(latitud, longitud);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        this.locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
