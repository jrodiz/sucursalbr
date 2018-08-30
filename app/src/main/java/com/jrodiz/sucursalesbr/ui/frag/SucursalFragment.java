package com.jrodiz.sucursalesbr.ui.frag;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jrodiz.sucursalesbr.R;
import com.jrodiz.sucursalesbr.base.AppConstants;
import com.jrodiz.sucursalesbr.base.AppUtils;
import com.jrodiz.sucursalesbr.obj.Sucursal;
import com.jrodiz.sucursalesbr.ui.OnFragmentInteractionListener;
import com.jrodiz.sucursalesbr.utils.ContextUtils;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SucursalFragment extends Fragment implements ISucursalDrawer {

    private OnFragmentInteractionListener mListener;
    private Sucursal mSucursal;

    private TextView mTvName;
    private ImageView mImgSucursal;
    private ImageView mIcon;
    private TextView mTvDir;
    private TextView mTvPhone;

    public static SucursalFragment newInstance(@NonNull final Sucursal current) {
        SucursalFragment fragment = new SucursalFragment();
        Bundle args = new Bundle();
        args.putParcelable(AppConstants.KEY_SUCURSAL, current);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSucursal = (Sucursal) ContextUtils.retrieveParceable(savedInstanceState, getArguments(), AppConstants.KEY_SUCURSAL);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(AppConstants.KEY_SUCURSAL, mSucursal);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sucursal, container, false);

        mTvName = v.findViewById(R.id.name);
        mImgSucursal = v.findViewById(R.id.image);
        mIcon = v.findViewById(R.id.icon);

        ViewCompat.setTransitionName(mIcon, AppConstants.SUCURSAL);
        ViewCompat.setTransitionName(mIcon, AppConstants.CAJERO);

        mTvDir = v.findViewById(R.id.dir);
        mTvPhone = v.findViewById(R.id.tel_portal);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateSucursal(mSucursal);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateSucursal(Sucursal data) {
        mSucursal = data;
        mIcon.setImageResource(mSucursal.isSucursal() ? R.drawable.pin1b : R.drawable.pin2b);
        mTvName.setText(mSucursal.getNombre());

        Glide.with(getContext())
                .load(mSucursal.getUrlFoto())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
                                getResources().getDisplayMetrics()),
                        20)))
                .into(mImgSucursal);

        mTvDir.setText(mSucursal.getDescription());
        mTvPhone.setText(!TextUtils.isEmpty(mSucursal.getTelefonoPortal()) ?
                mSucursal.getTelefonoPortal() : mSucursal.getTelefonoApp());
        mTvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
