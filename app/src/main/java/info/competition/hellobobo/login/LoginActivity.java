package info.competition.hellobobo.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import es.dmoral.toasty.Toasty;
import info.competition.hellobobo.R;
import info.competition.hellobobo.utils.BmobUtils;

import static info.competition.hellobobo.login.UserBean.KEY_USERNAME;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {
    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegistered;
    private ImageView ivBack;

    private LoginPresenter mLoginPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        BmobUtils.init(this);
        initView();
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegistered = findViewById(R.id.btn_registered);
        ivBack = findViewById(R.id.iv_back);

        etPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegistered.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        mLoginPresenter = new LoginPresenter(this);
    }


    @Override
    public String getUserName() {
        Log.i("getUserName", "getUserName: " + etAccount.getText().toString());
        return etAccount.getText().toString();
    }

    @Override
    public String getPassword() {
        Log.i("getUserName", "getUserName: " + etPassword.getText().toString());
        return etPassword.getText().toString();
    }

    @Override
    public void showTip(String tip, boolean isSuccess) {
        if (isSuccess)
            Toasty.success(this, tip).show();
        else
            Toasty.warning(this, tip).show();
    }

    @Override
    public void showMap(String userName) {
        Intent intent = new Intent();
        intent.putExtra(KEY_USERNAME, userName);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showProgress() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registered:
                mLoginPresenter.registered(getUserName(), getPassword());
                break;
            case R.id.btn_login:
                mLoginPresenter.login(getUserName(), getPassword());
                break;

            case R.id.iv_back:
                mLoginPresenter.hideInput();
                finish();
                break;

        }
    }
}

