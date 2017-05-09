package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class RememberPasswordTask extends BaseTask {

    private final String username;

    public RememberPasswordTask(BaseActivity activity, String username) {
        this.activity = activity;
        this.username = username;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);

        response = requester.httpRequest("RememberPassword.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2:
                        activity.showSimpleDialog(R.string.remember_password_confirmation);
                        break;
                    default:
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}