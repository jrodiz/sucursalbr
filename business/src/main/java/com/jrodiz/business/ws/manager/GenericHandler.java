package com.jrodiz.business.ws.manager;

import com.jrodiz.business.utils.AppUtils;
import com.jrodiz.common.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericHandler<T> implements Callback<T> {

    private final IRptCallHandler<T> mDataListener;

    public interface IRptCallHandler<T> {
        void onDataSuccess(T data);

        void onDataError(int errCode, Throwable t);
    }

    public GenericHandler(IRptCallHandler<T> dataListener) {
        mDataListener = dataListener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            mDataListener.onDataSuccess(response.body());
        } else {
            int statusCode = response.code();
            ResponseBody errorBody = response.errorBody();

            Throwable failException = new RetrofitManager.RequestException(errorBody != null ? errorBody.toString() : null);
            AppUtils.handleThrowable(failException);
            mDataListener.onDataError(statusCode, failException);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        AppUtils.handleThrowable(t);
        mDataListener.onDataError(Constants.Errors.DEFAULT_ERR_CODE, t);
    }
}
