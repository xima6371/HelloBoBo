package info.competition.hellobobo.login;

public interface LoginContract {
    interface View {

        String getUserName();

        String getPassword();

        void showTip(String tip, boolean isSuccess);

        void showMap(String userName);

        void showProgress();
    }

    interface Presenter {

        void login(String userName, String password);

        void registered(String userName, String password);
    }

}
