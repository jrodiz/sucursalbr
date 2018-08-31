package com.jrodiz.sucursalesbr.ui.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jrodiz.sucursalesbr.R;


public class IconFragment extends Fragment {
    private ImageView mIconSucursal;
    private ImageView mIconCajero;

    public static IconFragment newInstance() {
        IconFragment fragment = new IconFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_icon, container, false);
        mIconSucursal = v.findViewById(R.id.img_sucursal);
        mIconCajero = v.findViewById(R.id.img_cajero);

        ViewCompat.setTransitionName(mIconSucursal, "S");
        ViewCompat.setTransitionName(mIconCajero, "C");
        return v;
    }

    public ImageView getIcon(boolean isSucursal) {
        return isSucursal ? mIconSucursal : mIconCajero;
    }
}
