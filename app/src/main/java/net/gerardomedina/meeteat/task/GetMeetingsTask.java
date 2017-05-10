package net.gerardomedina.meeteat.task;

import android.util.Log;

import net.gerardomedina.meeteat.view.fragment.BaseFragment;
import net.gerardomedina.meeteat.view.fragment.DashboardFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetMeetingsTask extends BaseTask {

    public GetMeetingsTask(BaseFragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getBaseActivity();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId() + "");

        response = requester.httpRequest("GetMeetings.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2:
                        ((DashboardFragment) fragment).saveMeetingListToLocalDB(response);
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}