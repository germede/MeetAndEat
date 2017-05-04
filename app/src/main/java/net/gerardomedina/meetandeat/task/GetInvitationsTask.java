package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Invitation;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetInvitationsTask extends BaseTask {

    public GetInvitationsTask(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.getting_invitations);
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId()+"");

        response = requester.httpRequest("GetInvitations.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2:
                        ((MainActivity)activity).setBadgeCount(activity,response.getJSONArray("results").length()+"");
                        List<Invitation> invitations = new ArrayList<>();
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            invitations.add(new Invitation(results.getJSONObject(i).getInt("id"),
                                    results.getJSONObject(i).getString("title")));
                        }
                        appCommon.setInvitations(invitations);
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}