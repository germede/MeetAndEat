package net.gerardomedina.meetandeat.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.gerardomedina.meetandeat.R;

public abstract class BaseActivity extends AppCompatActivity {

    public void changeToActivity(Class activity) {
        this.startActivity(new Intent(this, activity));
        this.overridePendingTransition(0,0);

    }

    public void showSimpleDialog(String message) {
        new android.support.v7.app.AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
