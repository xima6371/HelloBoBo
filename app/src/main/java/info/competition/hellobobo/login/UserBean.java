package info.competition.hellobobo.login;

import cn.bmob.v3.BmobObject;

public class UserBean extends BmobObject {

    public static final String KEY_USERNAME = "key_username";
    public static final int REQUESTCODE_LOGIN = 1000;
    private String mUserName;
    private String mPassword;
    private String mImgUrl;//用户头像

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }
}
