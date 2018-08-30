package com.jrodiz.sucursalesbr.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jrodiz.sucursalesbr.R;
import com.jrodiz.sucursalesbr.base.AppConstants;
import com.jrodiz.sucursalesbr.obj.Sucursal;
import com.jrodiz.sucursalesbr.ui.frag.SucursalFragment;
import com.jrodiz.sucursalesbr.utils.ContextUtils;

import java.util.Objects;

public class SucursalDetailActivity extends AppCompatActivity
        implements OnFragmentInteractionListener {

    private Sucursal mSucursal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursal_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mSucursal = (Sucursal) ContextUtils.retrieveParceable(savedInstanceState,
                getIntent() != null ? getIntent().getExtras() : null, AppConstants.KEY_SUCURSAL);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SucursalFragment.newInstance(mSucursal))
                .commit();
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
        outState.putParcelable(AppConstants.KEY_SUCURSAL, mSucursal);
        super.onSaveInstanceState(outState);
    }
}
