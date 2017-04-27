package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.LoginActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class NewMeeetingTask extends BaseTask {

    private final String username;
    private final String password;

    public NewMeeetingTask(BaseActivity activity, String username, String password) {
        this.activity = activity;
        this.username = username;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.login_dialog);
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);

        response = requester.httpRequest("LoginUser.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        progressDialog.cancel();
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0:
                        activity.showSimpleDialog(activity.getString(R.string.error_creating_meeting));
                        break;
                    case 2:
                        activity.showSimpleDialog("BIEEN");
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}