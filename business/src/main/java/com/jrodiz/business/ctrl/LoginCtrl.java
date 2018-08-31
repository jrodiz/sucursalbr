package com.jrodiz.business.ctrl;

import android.app.Activity;

import com.jrodiz.business.IRptContext;
import com.jrodiz.business.model.User;
import com.jrodiz.business.ws.manager.GenericHandler;
import com.jrodiz.business.ws.manager.RetrofitManager;
import com.jrodiz.common.Constants;

public class LoginCtrl extends BaseController {

    private final IRptLogin mListener;

    public LoginCtrl(IRptLogin listener) {
        mListener = listener;
    }

    public interface IRptLogin extends IRptContext<Activity> {
        void onLoginSuccess();

        void onLoginFail(int errorCode, Throwable e);
    }

    public void requestLogin(User user) {
        if (!isNetworkConnected(mListener.getViewContext())) {
            mListener.onLoginFail(Constants.Errors.NO_NETWORK, new RetrofitManager.RequestException("No network"));
            return;
        }
        RetrofitManager.get().requestLogin(new GenericHandler<>(
                new GenericHandler.IRptCallHandler<User>() {
                    @Override
                    public void onDataSuccess(User data) {
                        if (!mListener.isAlive()) return;

                        if (user.getUser().equals(data.getUser())
                                && user.getPassword().equals(data.getPassword())) {
                            mListener.onLoginSuccess();
                        } else {
                            mListener.onLoginFail(Constants.Errors.WRONG_CREDENTIALS, null);
                        }
                    }

                    @Override
                    public void onDataError(int errCode, Throwable t) {
                        if (mListener.isAlive()) {
                            mListener.onLoginFail(errCode, t);
                        }
                    }
                })
        );
    }
}
