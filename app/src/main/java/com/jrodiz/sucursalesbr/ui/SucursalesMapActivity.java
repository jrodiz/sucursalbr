package com.jrodiz.sucursalesbr.ui;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jrodiz.business.ctrl.LocationAwareCtrl;
import com.jrodiz.business.ctrl.SucursalCtrl;
import com.jrodiz.business.model.Sucursal;
import com.jrodiz.common.Constants;
import com.jrodiz.sucursalesbr.R;
import com.jrodiz.sucursalesbr.AppConstants;
import com.jrodiz.sucursalesbr.ui.custom.DetailsTransition;
import com.jrodiz.sucursalesbr.ui.frag.IconFragment;
import com.jrodiz.sucursalesbr.ui.frag.SucursalFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SucursalesMapActivity extends FragmentActivity
        implements OnMapReadyCallback,
        SucursalCtrl.IRptSucursal,
        GoogleMap.OnMarkerClickListener,
        OnFragmentInteractionListener,
        SlidingUpPanelLayout.PanelSlideListener,
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    private static final String TAG = SucursalesMapActivity.class.getSimpleName();
    private static final int ICON_FRAGMENT = 1;

    private final SucursalCtrl mController = new SucursalCtrl(this);
    private final LocationAwareCtrl mLocationAwareCtrl = new LocationAwareCtrl(this);
    private LiveData<Location> mLiveLocation = mLocationAwareCtrl.getLiveLocation();
    private static BitmapDescriptor mSucursalPin = null;
    private static BitmapDescriptor mCajeroPin = null;


    private GoogleMap mMap;
    private final List<Sucursal> mSucursales = new ArrayList<>();
    private SlidingUpPanelLayout mPanelView;
    private Sucursal mCurrrentSucursal;
    private FloatingActionsMenu mFab;
    private Location mUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mPanelView = findViewById(R.id.sliding_layout);
        mPanelView.addPanelSlideListener(this);
        resetPanelView();

        mLiveLocation.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                mUserLocation = location;
            }
        });
        mController.requestSucursales();
        startMonitingLocation();

        mFab = findViewById(R.id.fab);
        mFab.setOnFloatingActionsMenuUpdateListener(this);
    }

    private void resetPanelView() {
        mCurrrentSucursal = null;
        setSlideFragment(IconFragment.newInstance());
        mPanelView.setTouchEnabled(false);
        if (mPanelView.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mPanelView.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    private void startMonitingLocation() {

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    AppConstants.RQST_FINE_PERMISSION);
            return;
        }
        mLocationAwareCtrl.startMonitingLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationAwareCtrl.stopMonitoringLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.RQST_FINE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMonitingLocation();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationAwareCtrl.NUEVO_LEON, 12));
        updateSucursalesUI();
    }

    private void updateSucursalesUI() {
        if (mMap == null) return;
        mMap.clear();
        initLazyIcons();

        for (Sucursal s : mSucursales) {
            MarkerOptions markerOpts = new MarkerOptions()
                    .position(s.getLatLng())
                    .snippet(s.getTelefonoApp())
                    .icon(s.isSucursal() ? mSucursalPin : mCajeroPin)
                    .title(s.getNombre());
            Marker marker = mMap.addMarker(markerOpts);
            marker.setTag(s);
        }
    }

    private void initLazyIcons() {
        if (mSucursalPin == null || mCajeroPin == null) {
            Resources r = getResources();
            int pxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r.getInteger(R.integer.pin_size_width), r.getDisplayMetrics());
            int pxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r.getInteger(R.integer.pin_size_height), r.getDisplayMetrics());

            BitmapDrawable sDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.pin1b);
            BitmapDrawable cDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.pin2b);
            Bitmap bs = sDrawable.getBitmap();
            Bitmap bc = cDrawable.getBitmap();
            mSucursalPin = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bs, pxWidth, pxHeight, false));

            mCajeroPin = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bc, pxWidth, pxHeight, false));
        }
    }

    @Nullable
    @Override
    public Location getUserLocation() {
        return mUserLocation;
    }

    @Override
    public void onSucursalesSuccess(List<Sucursal> data) {
        mSucursales.addAll(data);
        updateSucursalesUI();
    }

    @Override
    public void onSucursalesFail(int errCode, Throwable t) {

    }

    @Override
    public boolean isAlive() {
        return !isFinishing();
    }

    @Override
    public Activity getViewContext() {
        return this;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mCurrrentSucursal = (Sucursal) marker.getTag();

        setSlideFragment(SucursalFragment.newInstance(mCurrrentSucursal));
        mPanelView.setTouchEnabled(true);
        mPanelView.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        return true;
    }

    private void setSlideFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }

        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mCurrrentSucursal != null) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Fade());

            for (Fragment f : getSupportFragmentManager().getFragments()) {
                if (f instanceof IconFragment) {
                    IconFragment iconFrag = (IconFragment) f;
                    ImageView sucursalIcon = iconFrag.getIcon(true);
                    ImageView cajeroIcon = iconFrag.getIcon(false);
                    fragment.setSharedElementReturnTransition(new DetailsTransition());

                    transition.addSharedElement(sucursalIcon, Constants.SUCURSAL);
                    transition.addSharedElement(cajeroIcon, Constants.CAJERO);
                    getWindow().setSharedElementEnterTransition(new ChangeBounds().setDuration(50000));
                    break;
                }
            }
        } else {
            transition.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        }

        transition
                .replace(R.id.panel_up, fragment, TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mPanelView.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            resetPanelView();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if (previousState == SlidingUpPanelLayout.PanelState.DRAGGING
                && newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            resetPanelView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AppConstants.RQST_CODE_SEARCH && mFab != null) {
            mFab.collapse();
        }
    }

    @Override
    public void onMenuExpanded() {
        Intent searchAct = new Intent(this, SearchActivity.class);
        Bundle p = new Bundle();
        p.putParcelable(AppConstants.KEY_LOCATION, mUserLocation);
        searchAct.putExtras(p);
        startActivityForResult(searchAct, AppConstants.RQST_CODE_SEARCH);
    }

    @Override
    public void onMenuCollapsed() {

    }
}
