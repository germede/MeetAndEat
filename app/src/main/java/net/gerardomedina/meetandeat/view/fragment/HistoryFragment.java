package net.gerardomedina.meetandeat.view.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.Meeting;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.persistence.local.PassedMeetingValues;
import net.gerardomedina.meetandeat.task.GetMeetingsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.NewMeetingActivity;
import net.gerardomedina.meetandeat.view.adapter.MeetingsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryFragment extends BaseFragment {
    private View view;
    private ListView meetingListView;
    private DBHelper dbHelper;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        FloatingActionButton newMeetingButton = (FloatingActionButton) view.findViewById(R.id.newMeetingButton);
        newMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).changeToActivity(NewMeetingActivity.class);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_top);
            }
        });

        meetingListView = (ListView) view.findViewById(R.id.meetings);
        dbHelper = new DBHelper(getActivity());
        if (appCommon.hasInternet(getActivity())) new GetMeetingsTask(this).execute();
        else loadMeetingListFromLocalDB();

        return view;
    }

    public void saveMeetingListToLocalDB(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(PassedMeetingValues.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        for (int i = 0; i < results.length(); i++) {
            values.put(PassedMeetingValues._ID, results.getJSONObject(i).getInt("id"));
            values.put(PassedMeetingValues.COLUMN_NAME_TITLE, results.getJSONObject(i).getString("title"));
            values.put(PassedMeetingValues.COLUMN_NAME_LOCATION, results.getJSONObject(i).getString("location"));
            values.put(PassedMeetingValues.COLUMN_NAME_DATE, results.getJSONObject(i).getString("date"));
            values.put(PassedMeetingValues.COLUMN_NAME_TIME, results.getJSONObject(i).getString("time"));
            values.put(PassedMeetingValues.COLUMN_NAME_COLOR, results.getJSONObject(i).getString("color"));
            db.insert(PassedMeetingValues.TABLE_NAME, null, values);
        }
        loadMeetingListFromLocalDB();

    }

    private void loadMeetingListFromLocalDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+PassedMeetingValues.TABLE_NAME+
                " order by " +PassedMeetingValues.COLUMN_NAME_DATE+","+PassedMeetingValues.COLUMN_NAME_TIME+ " ASC;"
                ,null);
        if (cursor.getCount()>0 && cursor.moveToFirst()) appCommon.setNextMeeting(new Meeting(1,"","",
                cursor.getString(cursor.getColumnIndexOrThrow(PassedMeetingValues.COLUMN_NAME_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(PassedMeetingValues.COLUMN_NAME_TIME)),
                ""));
        meetingListView.setAdapter(new MeetingsAdapter(getActivity(), (BaseActivity) getActivity(),cursor,true));
    }

}
