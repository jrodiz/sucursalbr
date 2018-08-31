package com.jrodiz.sucursalesbr.ui;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.jrodiz.business.ctrl.SucursalCtrl;
import com.jrodiz.business.model.Sucursal;
import com.jrodiz.sucursalesbr.AppConstants;
import com.jrodiz.sucursalesbr.R;
import com.jrodiz.sucursalesbr.ui.adapter.RvSearchAdapter;
import com.jrodiz.sucursalesbr.utils.ContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity
        implements SucursalCtrl.IRptSucursal,
        TextWatcher {

    private Location mUserLocation = null;
    private final SucursalCtrl mController = new SucursalCtrl(this);
    private final List<Sucursal> mSucursals = new ArrayList<>();
    private EditText mName;
    private RecyclerView mRecyclerView;
    private RvSearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mUserLocation = (Location) ContextUtils.retrieveParceable(savedInstanceState,
                getIntent() != null ? getIntent().getExtras() : null, AppConstants.KEY_LOCATION);

        mRecyclerView = findViewById(R.id.recycler);
        mName = findViewById(R.id.search);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mController.requestDbSucursales();
        mAdapter = new RvSearchAdapter(this, mSucursals);
        mRecyclerView.setAdapter(mAdapter);
        mName.addTextChangedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(AppConstants.KEY_LOCATION, mUserLocation);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public Location getUserLocation() {
        return mUserLocation;
    }

    @Override
    public void onSucursalesSuccess(List<Sucursal> data) {
        mSucursals.clear();
        mSucursals.addAll(data);
        mAdapter.notifyDataSetChanged();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s != null) {
            mController.filterOn(mUserLocation, s.toString());
        }
    }
}
