package net.gerardomedina.meeteat.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.persistence.local.ContactValues;
import net.gerardomedina.meeteat.persistence.local.DBHelper;
import net.gerardomedina.meeteat.task.NewMeeetingTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewMeetingActivity extends BaseActivity {

    private TextView titleInput;
    private TextView locationInput;
    private TextView datetimeInput;
    private TextView colorInput;
    private TextView participantsInput;
    private String selectedDatetime;
    private String selectedParticipants;
    public static final int PLACE_PICKER_REQUEST = 1;
    private DBHelper dbHelper;

    Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newmeeting, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_new_meeting) attemptNewMeeting();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmeeting);
        setToolbar();


        titleInput = (TextView) findViewById(R.id.newMeetingTitleInput);
        dbHelper = new DBHelper(getActivity());

        setLocationPicker();
        setDateAndTimePicker();
        setColorPicker();
        setParticipantsPicker();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setLocationPicker() {
        locationInput = (TextView) findViewById(R.id.newMeetingLocationInput);
        locationInput.setKeyListener(null);
        locationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e("Google Play", e.getMessage());
                }
            }
        });
    }

    private void setDateAndTimePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                final SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                final SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                new TimePickerDialog(getBaseActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0);
                        selectedDatetime = dateFormatter1.format(newDate.getTime());
                        datetimeInput.setText(dateFormatter2.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true).show();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(newCalendar.getTime().getTime());
        datetimeInput = (TextView) findViewById(R.id.newMeetingDatetimeInput);
        datetimeInput.setKeyListener(null);
        datetimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

    }

    private void setColorPicker() {
        colorInput = (TextView) findViewById(R.id.newMeetingColorInput);
        colorInput.setKeyListener(null);
        colorInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder.with(getActivity()).setTitle(getString(R.string.choose_color))
                        .noSliders().wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(7)
                        .setPositiveButton(android.R.string.ok, new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                colorInput.setText("#" + Integer.toHexString(selectedColor));
                                colorInput.setTextColor(selectedColor);
                            }
                        }).build().show();
            }
        });
    }

    private void setParticipantsPicker() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ContactValues.COLUMN_NAME_USERNAME + " from " +
                ContactValues.TABLE_NAME + " order by "
                + ContactValues.COLUMN_NAME_USERNAME + " ASC;", null);
        participantsInput = (TextView) findViewById(R.id.newMeetingContactsInput);
        participantsInput.setKeyListener(null);
        if (cursor.getCount() > 0) {
            final String[] contacts = new String[cursor.getCount()];
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                contacts[i] = cursor.getString(cursor.getColumnIndexOrThrow(ContactValues.COLUMN_NAME_USERNAME));
                i++;
            }
            final boolean[] isChecked = new boolean[contacts.length];
            final List<String> selectedContacts = new ArrayList<>();
            participantsInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getBaseActivity())
                            .setTitle(getString(R.string.select_from_contacts))
                            .setMultiChoiceItems(contacts, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if (isChecked) selectedContacts.add(contacts[which]);
                                    else selectedContacts.remove(contacts[which]);
                                }
                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String result = "";
                                    for (String selectedContact : selectedContacts) result = result + selectedContact +",";
                                    if (result.charAt(result.length()-1) == ',') result = result.substring(0,result.length()-1);
                                    participantsInput.setText(result);
                                }
                            })
                            .create().show();
                }
            });
        } else {
            getBaseActivity().showSimpleDialog(R.string.new_meeting_no_contact);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                locationInput.setText(place.getLatLng().latitude + "," + place.getLatLng().longitude);
            }
        }
    }


    private void attemptNewMeeting() {
        List<TextView> inputs = new ArrayList<>();
        inputs.add(titleInput);
        inputs.add(locationInput);
        inputs.add(datetimeInput);
        inputs.add(colorInput);

        for (TextView input : inputs) input.setError(null);

        boolean cancel = false;
        View focusView = null;
        for (TextView input : inputs) {
            if (TextUtils.isEmpty(input.getText())) {
                int ecolor = getResources().getColor(R.color.white);
                String estring = getString(R.string.error_field_required);
                ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                input.setError(ssbuilder);
                focusView = input;
                cancel = true;
            }
        }
        if (titleInput.getText().length() > 20) {
            int ecolor = getResources().getColor(R.color.white);
            String estring = getString(R.string.error_field_too_long);
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
            ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
            titleInput.setError(ssbuilder);
        }
        if (cancel) focusView.requestFocus();
        else new NewMeeetingTask(this,
                titleInput.getText().toString(),
                locationInput.getText().toString(),
                selectedDatetime,
                colorInput.getText().toString(),
                participantsInput.getText().toString()).execute();

    }
}