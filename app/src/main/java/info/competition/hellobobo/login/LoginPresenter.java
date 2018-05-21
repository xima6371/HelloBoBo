package info.competition.hellobobo.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;
import info.competition.hellobobo.utils.ActivityUtils;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private BmobQuery<UserBean> mBmobQuery;
    private Context mContext;

    public LoginPresenter(@NonNull LoginContract.View view) {
        mView = view;
        mContext = (Context) view;
        mBmobQuery = new BmobQuery<>();
    }

    @Override
    public void login(final String userName, final String password) {
        if (ActivityUtils.checkNull(userName) || ActivityUtils.checkNull(password)) {
            Toasty.error(mContext, "请填写账号密码").show();
            return;
        }
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(userName);
        bmobUser.setPassword(password);
        bmobUser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    hideInput();
                    mView.showTip("欢迎" + userName + "使用Hello BoBo", true);
                    mView.showMap(userName);
                } else {
                    Toasty.error(mContext, "账号密码错误或账号未注册").show();
                }
            }
        });
    }

    public void hideInput() {
        InputMethodManager manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(((LoginActivity) mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void registered(final String userName, final String password) {
        if (ActivityUtils.checkNull(userName) || ActivityUtils.checkNull(password)) {
            Toasty.error(mContext, "请填写账号密码").show();
            return;
        }
        BmobUser user = new BmobUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null)
                    login(userName, password);
                else
                    mView.showTip("该用户名已经注册", false);
            }
        });
    }


}
