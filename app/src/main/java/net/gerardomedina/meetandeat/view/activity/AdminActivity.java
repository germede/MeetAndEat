package net.gerardomedina.meetandeat.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.model.Option;
import net.gerardomedina.meetandeat.task.AdminTask;
import net.gerardomedina.meetandeat.view.adapter.OptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends BaseActivity {

    private Meeting meeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        meeting = appCommon.getSelectedMeeting();
        setupActionBar();
        setupOptionsList();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupOptionsList() {
        ListView optionsListView = (ListView) findViewById(R.id.options);
        List<Option> options = new ArrayList<>();
        options.add(new Option(getString(R.string.change_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getBaseActivity());
                input.setText(meeting.getTitle());
                final AlertDialog alertDialog = new AlertDialog.Builder(getBaseActivity()).create();
                alertDialog.setMessage(getString(R.string.request_email));
                alertDialog.setView(input);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().length() > 20) showToast(R.string.error_field_too_long);
                        else new AdminTask(getBaseActivity(),0,input.getText().toString());
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }));
        options.add(new Option(getString(R.string.change_location), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        options.add(new Option(getString(R.string.change_date_and_time), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        options.add(new Option(getString(R.string.change_color), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        options.add(new Option(getString(R.string.delete_participant), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        options.add(new Option(getString(R.string.delete_meeting), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(getBaseActivity(), R.style.MyAlertDialogStyle)
                        .setMessage(R.string.are_you_sure)
                        .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {new AdminTask(getBaseActivity(),5,"").execute();}
                        })
                        .create()
                        .show();
            }
        }));
        optionsListView.setAdapter(new OptionAdapter(this,options));


    }

}
