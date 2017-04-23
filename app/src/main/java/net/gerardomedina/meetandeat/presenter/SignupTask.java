package net.gerardomedina.meetandeat.presenter;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.BaseActivity;
import net.gerardomedina.meetandeat.view.DashboardActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class SignupTask extends BaseTask {

    private final String username;
    private final String password;
    private final String email;

    public SignupTask(BaseActivity activity, String username, String password, String email) {
        this.activity = activity;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.signup_dialog);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("username",username);
        parameters.put("password",password);
        parameters.put("email",email);

        response = requester.httpRequest("SignupUser.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        progressDialog.cancel();
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: activity.showSimpleDialog(activity.getString(R.string.error_taken_email));
                            break;
                    case 2: activity.changeToActivity(DashboardActivity.class);
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}