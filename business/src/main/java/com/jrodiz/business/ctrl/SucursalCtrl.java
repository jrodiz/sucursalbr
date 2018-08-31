package com.jrodiz.business.ctrl;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.annimon.stream.Stream;
import com.jrodiz.business.IRptContext;
import com.jrodiz.business.model.Sucursal;
import com.jrodiz.business.ws.manager.GenericHandler;
import com.jrodiz.business.ws.manager.RetrofitManager;
import com.jrodiz.common.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.annimon.stream.Collectors.toList;
import static com.jrodiz.business.ctrl.BaseController.DbOpt.GET_ALL_SUCURSAL;
import static com.jrodiz.business.ctrl.BaseController.DbOpt.INSERT_SUCURSALS;

public class SucursalCtrl extends BaseController implements Observer<List<Sucursal>> {

    private Set<Sucursal> mSucursalSet = new HashSet<>();

    private final IRptSucursal mListener;

    public SucursalCtrl(IRptSucursal listener) {
        mListener = listener;
    }

    public interface IRptSucursal extends IRptContext<Activity> {

        @Nullable
        Location getUserLocation();

        void onSucursalesSuccess(List<Sucursal> data);

        void onSucursalesFail(int errCode, Throwable t);
    }


    public void requestSucursales() {
        if (!isNetworkConnected(mListener.getViewContext())) {
            mListener.onSucursalesFail(Constants.Errors.NO_NETWORK, new RetrofitManager.RequestException("No network"));
            return;
        }
        RetrofitManager.get().requestSucursales(mHandler);
    }

    private final GenericHandler<List<Sucursal>> mHandler = new GenericHandler<>(
            new GenericHandler.IRptCallHandler<List<Sucursal>>() {
                @Override
                public void onDataSuccess(List<Sucursal> data) {
                    new ShortDbTask().execute(INSERT_SUCURSALS.setData(data));

                    if (mListener.isAlive()) {
                        mListener.onSucursalesSuccess(data);
                    }
                }

                @Override
                public void onDataError(int errCode, Throwable t) {
                    if (mListener.isAlive()) {
                        mListener.onSucursalesFail(errCode, t);
                    }
                }
            });

    public void requestDbSucursales() {
        new ShortDbTask().execute(GET_ALL_SUCURSAL);
    }


    public void filterOn(@Nullable final Location userLocation,
                         @NonNull final String filter) {
        if (TextUtils.isEmpty(filter)) {
            mListener.onSucursalesSuccess(new ArrayList<>(mSucursalSet));
            return;
        }

        List<Sucursal> list = Stream.of(mSucursalSet)
                .filter(it -> it.getNombre().toLowerCase().contains(filter.toLowerCase()) ||
                        it.getDomicilio().toLowerCase().contains(filter.toLowerCase())).collect(toList());
        orderByCloserTo(userLocation, list);
        mListener.onSucursalesSuccess(list);
    }

    protected class ShortDbTask extends AsyncTask<DbOpt, Void, DbOpt> {

        public ShortDbTask() {

        }

        @Override
        protected DbOpt doInBackground(DbOpt... opts) {
            DbOpt dbOp = opts[0];
            switch (dbOp) {
                case GET_ALL_SUCURSAL:
                    if (mListener.getViewContext() != null) {
                        dbOp.setData(getDb(mListener.getViewContext()).getAllLive());
                    }
                    break;
                case INSERT_SUCURSALS:
                    @SuppressWarnings("unchecked")
                    List<Sucursal> data = (List<Sucursal>) dbOp.getData();
                    getDb(mListener.getViewContext()).insertAll(data.toArray(new Sucursal[]{}));
                    break;
            }
            return dbOp;
        }

        @Override
        protected void onPostExecute(DbOpt dbOpt) {
            switch (dbOpt) {
                case GET_ALL_SUCURSAL:
                    @SuppressWarnings("unchecked")
                    LiveData<List<Sucursal>> liveData = (LiveData<List<Sucursal>>) dbOpt.getData();

                    if (mListener.getViewContext() instanceof LifecycleOwner) {
                        liveData.observe((LifecycleOwner) mListener.getViewContext(), SucursalCtrl.this);
                    }
                    break;
                case INSERT_SUCURSALS:
                    //Intentional fall-through
                default:
                    break;
            }
        }
    }

    private List<Sucursal> orderByCloserTo(@Nullable final Location userLocation,
                                           @NonNull final List<Sucursal> list) {
        if (userLocation == null) return list;

        Collections.sort(list, new Comparator<Sucursal>() {
            static final int EQUALS = 0;
            static final int LESS_THAN = -1;
            static final int GREATER_THAN = 1;

            @Override
            public int compare(Sucursal o1, Sucursal o2) {
                Location first = new Location(o1.toString());
                Location second = new Location(o2.toString());

                first.setLatitude(o1.getLatitudDouble());
                first.setLongitude(o1.getLongitudDouble());

                second.setLatitude(o2.getLatitudDouble());
                second.setLongitude(o2.getLongitudDouble());

                float firstDistance = userLocation.distanceTo(first);
                float secondDistance = userLocation.distanceTo(second);
                return firstDistance == secondDistance ? EQUALS :
                        firstDistance >= secondDistance ? GREATER_THAN : LESS_THAN;
            }
        });
        return list; // Already mutated
    }

    @Override
    public void onChanged(@Nullable List<Sucursal> sucursals) {
        if (sucursals != null) {
            if (mListener.isAlive()) {
                mSucursalSet.addAll(orderByCloserTo(mListener.getUserLocation(), sucursals));

                mListener.onSucursalesSuccess(sucursals);
            }
        }
    }

}
