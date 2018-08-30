package com.jrodiz.sucursalesbr.ws.manager;

import com.jrodiz.sucursalesbr.base.AppConstants;
import com.jrodiz.sucursalesbr.obj.Sucursal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BrInterface {

    @GET(AppConstants.Ws.GET_SUCURSALES)
    Call<List<Sucursal>> getSucursales();
}
