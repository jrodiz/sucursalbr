package com.jrodiz.sucursalesbr.ctrl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.jrodiz.sucursalesbr.base.AppConstants;
import com.jrodiz.sucursalesbr.base.AppUtils;
import com.jrodiz.sucursalesbr.base.IRptContext;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationAwareCtrl {
    public static final LatLng NUEVO_LEON = new LatLng(25.6708689,-100.3603441);

    private final WeakReference<Activity> mActivity;

    public LocationAwareCtrl(Activity context) {
        this.mActivity = new WeakReference<>(context);
    }

    private FusedLocationProviderClient mFusedLocationClient;

    private MutableLiveData<Location> mLocationLiveData = new MutableLiveData<>();

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            if (locationResult.getLocations().size() > 0) {
                setUserLocation(locationResult.getLocations().get(0));
            }
            stopMonitoringLocation();
        }
    };

    public void stopMonitoringLocation() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public void startMonitingLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity.get());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(mActivity.get(), location -> {
                    if (location != null) {
                        setUserLocation(location);
                    }
                });


        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5_000);
        mLocationRequest.setFastestInterval(2_500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(mActivity.get());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(mActivity.get(), new OnSuccessListener<LocationSettingsResponse>() {

            private AtomicBoolean registered = new AtomicBoolean(false);

            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (!registered.get()) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback,
                            null /* Looper */);
                    registered.set(true);
                }
            }
        });

        task.addOnFailureListener(mActivity.get(), AppUtils::handleThrowable);
    }

    public void setUserLocation(Location userLocation) {
        mLocationLiveData.setValue(userLocation);
    }

    public LiveData<Location> getLiveLocation() {
        return mLocationLiveData;
    }
}
