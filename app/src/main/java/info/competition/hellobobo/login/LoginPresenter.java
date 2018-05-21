package info.competition.hellobobo.login;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private BmobQuery<UserBean> mBmobQuery;

    public LoginPresenter(@NonNull LoginContract.View view) {
        mView = view;
        mBmobQuery = new BmobQuery<>();
    }

    @Override
    public void login(final String userName, final String password) {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(userName);
        bmobUser.setPassword(password);
        bmobUser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    mView.showTip("欢迎" + userName + "使用Hello BoBo", true);
                    mView.showMap(userName);
                } else {
                    registered(userName, password);
                }
            }
        });
    }

    @Override
    public void registered(final String userName, final String password) {
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
