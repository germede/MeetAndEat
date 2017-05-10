package net.gerardomedina.meeteat.task;

import android.util.Log;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.view.activity.BaseActivity;
import net.gerardomedina.meeteat.view.activity.MainActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MeetingOptionsTask extends BaseTask {

    private final String parameter;
    private final int type;

    public MeetingOptionsTask(BaseActivity activity, int type, String parameter) {
        this.activity = activity;
        this.type = type;
        this.parameter = parameter;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("user_id",appCommon.getUser().getId()+"");
        parameters.put("meeting_id",appCommon.getSelectedMeeting().getId()+"");
        parameters.put("type", type+"");
        parameters.put("parameter", parameter);

        response = requester.httpRequest("MeetingOptions.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: activity.showSimpleDialog(R.string.error_retrieving_data);
                            break;
                    case 2: activity.showToast(R.string.done);
                            activity.changeToActivityNoBackStack(MainActivity.class);
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}