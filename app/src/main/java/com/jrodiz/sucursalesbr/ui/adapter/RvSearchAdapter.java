package com.jrodiz.sucursalesbr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jrodiz.business.model.Sucursal;
import com.jrodiz.sucursalesbr.AppConstants;
import com.jrodiz.sucursalesbr.R;
import com.jrodiz.sucursalesbr.ui.SucursalDetailActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class RvSearchAdapter extends RecyclerView.Adapter<RvSearchAdapter.SucursalVh> {

    private final List<Sucursal> mSucursals;
    private final LayoutInflater mInflater;
    private final WeakReference<Context> mContext;

    public RvSearchAdapter(Context context, List<Sucursal> sucursals) {
        mInflater = LayoutInflater.from(context);
        mSucursals = sucursals;
        mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public SucursalVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = mInflater.inflate(R.layout.row_search, viewGroup, false);
        return new SucursalVh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SucursalVh vh, int i) {
        Sucursal current = mSucursals.get(i);
        vh.itemView.setOnClickListener(v -> {
            Intent detail = new Intent(mContext.get(), SucursalDetailActivity.class);
            Bundle p = new Bundle();
            p.putParcelable(AppConstants.KEY_SUCURSAL, current);
            detail.putExtras(p);
            mContext.get().startActivity(detail);

        });
        vh.icon.setImageResource(current.isSucursal() ? R.drawable.pin1 : R.drawable.pin2);
        vh.title.setText(current.getNombre());
        vh.subTitle.setText(current.getDomicilio());
    }

    @Override
    public int getItemCount() {
        return mSucursals.size();
    }

    public static class SucursalVh extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView subTitle;

        public SucursalVh(View v) {
            super(v);
            icon = v.findViewById(R.id.img_sucursal);
            title = v.findViewById(R.id.title);
            subTitle = v.findViewById(R.id.subtitle);
        }
    }
}
