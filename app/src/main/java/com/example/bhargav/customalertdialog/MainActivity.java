package com.example.bhargav.customalertdialog;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    PasswordCriteria passwordCriteria;
    TextView resultText;
    TextView passwordRules;
    ImageButton helpButton, shareButton;

    String message = "";

    // Shared preferences
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String rules =
            "1. Create a password rule.\n"+
            "2. Generate passwords as many times as you wish with that rule.\n"+
            "3. Copy to clipboard or share it as you wish.\n"+
            "4. Comeback to see the last password rule still working.\n"+
            "5. Happy generating passwords!";

    private ClipboardManager cbmClipboardManager;
    private ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hiding the Action Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        Button mShowDialog = (Button)  findViewById(R.id.showDialog);

        resultText = (TextView) findViewById(R.id.resultView);

        passwordRules = (TextView) findViewById(R.id.passwordRules);

        shareButton = (ImageButton) findViewById(R.id.shareButton);

        helpButton = (ImageButton) findViewById(R.id.helpButton);

        passwordRules.setText(rules);

        cbmClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        sharedPreferences = getSharedPreferences("passwordGenerator", Context.MODE_PRIVATE);
        editor =  sharedPreferences.edit();

        if(passwordCriteria == null) {
            if(sharedPreferences.getInt("length", 0) > 0) {
                passwordCriteria = new PasswordCriteria(sharedPreferences.getBoolean("lower", false), sharedPreferences.getBoolean("upper", false), sharedPreferences.getBoolean("number", false), sharedPreferences.getBoolean("special", false), sharedPreferences.getInt("length", 1), sharedPreferences.getString("specialChars", ""));
            }
        }

        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);

                View mView  = getLayoutInflater().inflate(R.layout.dialog_selector, null);

                final CheckBox lower = (CheckBox) mView.findViewById(R.id.lowerCheck);
                final TextView lowerLabel = (TextView) mView.findViewById(R.id.lowerLabel);
                final CheckBox upper = (CheckBox) mView.findViewById(R.id.upperCheck);
                final TextView upperLabel = (TextView) mView.findViewById(R.id.upperLabel);
                final CheckBox number = (CheckBox) mView.findViewById(R.id.numberCheck);
                final TextView numberLabel = (TextView) mView.findViewById(R.id.numberLabel);
                final CheckBox special = (CheckBox) mView.findViewById(R.id.specialCheck);
                final TextView specialLabel = (TextView) mView.findViewById(R.id.specialLabel);
                final TextView textField = (TextView) mView.findViewById(R.id.specialText);
                final TextView passwordLength = (TextView) mView.findViewById(R.id.passwordText);
                Button submit = (Button) mView.findViewById(R.id.submitButton);
                Button clearAll = (Button) mView.findViewById(R.id.clearAll);

                if(passwordCriteria != null) {
                    lower.setChecked(passwordCriteria.getLower());
                    upper.setChecked(passwordCriteria.getUpper());
                    number.setChecked(passwordCriteria.getnumber());
                    special.setChecked(passwordCriteria.getSpecial());
                    textField.setText(passwordCriteria.getSpecialCharacters());
                    passwordLength.setText(passwordCriteria.getLength()+"");
                }

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                clearAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lower.setChecked(false);
                        upper.setChecked(false);
                        number.setChecked(false);
                        special.setChecked(false);
                        textField.setText("");
                        passwordLength.setText("");

                        editor.putBoolean("lower",false);
                        editor.putBoolean("upper",false);
                        editor.putBoolean("number",false);
                        editor.putBoolean("special",false);
                        editor.putInt("length",0);
                        editor.putString("specialChars","");

                        editor.apply();

                        passwordCriteria = null;

                        resultText.setText("");

                        message = "Cleared All!";
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int l = lower.isChecked()?1:0;
                        int u = upper.isChecked()?1:0;
                        int n = number.isChecked()?1:0;
                        int s = special.isChecked()?1:0;
                        if(!lower.isChecked() && !upper.isChecked() && !number.isChecked() && !special.isChecked()) {
                            message = "Check at least Lowercase!";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        } else if((passwordLength.getText() == null || passwordLength.getText().toString().isEmpty()) || Integer.parseInt(passwordLength.getText().toString()) <= 0) {
                            message = "Password length must be a valid number greater than 0!";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        } else if(Integer.parseInt(passwordLength.getText().toString()) < (l+u+n+s)) {
                            message = "Password length must be minimum " + (l+u+n+s) + " characters in length!";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        } else if(Integer.parseInt(passwordLength.getText().toString()) > 300) {
                            message = "Password length cannot exceed 300 characters!";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        } else {
                            passwordCriteria = new PasswordCriteria(lower.isChecked(), upper.isChecked(), number.isChecked(), special.isChecked(), Integer.parseInt(passwordLength.getText().toString()), textField.getText().toString());
                            //generatePassword();

                            // Shared Preferences
                            editor.putBoolean("lower",lower.isChecked());
                            editor.putBoolean("upper",upper.isChecked());
                            editor.putBoolean("number",number.isChecked());
                            editor.putBoolean("special",special.isChecked());
                            editor.putInt("length",Integer.parseInt(passwordLength.getText().toString()));
                            editor.putString("specialChars",textField.getText().toString());

                            editor.apply();

                            String password = new utility().generatePassword(passwordCriteria.getLower(), passwordCriteria.getUpper(), passwordCriteria.getnumber(), passwordCriteria.getSpecial(), passwordCriteria.getLength(), passwordCriteria.getSpecialCharacters());
                            // Toast.makeText(MainActivity.this, "Sai Baba: "+password, Toast.LENGTH_SHORT).show();

                            resultText.setText(password);
                            resultText.setTextColor(Color.parseColor(new utility().passwordStrenght(password)));

                            message = "Your password rule is saved!";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                            dialog.dismiss();

                        }
                    }
                });

                lowerLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lower.setChecked(!lower.isChecked());
                    }
                });

                upperLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upper.setChecked(!upper.isChecked());
                    }
                });

                numberLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        number.setChecked(!number.isChecked());
                    }
                });

                specialLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        special.setChecked(!special.isChecked());
                    }
                });
            }
        });

    }

    public void generatePassword(View view) {
        if(passwordCriteria != null) {
            String password = new utility().generatePassword(passwordCriteria.getLower(), passwordCriteria.getUpper(), passwordCriteria.getnumber(), passwordCriteria.getSpecial(), passwordCriteria.getLength(), passwordCriteria.getSpecialCharacters());

            resultText.setText(password);
            resultText.setTextColor(Color.parseColor(new utility().passwordStrenght(password)));

        } else {
            String message = "Set password rule first!";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void copyToClipboard(View view) {
        String message;
        String result = resultText.getText().toString();
        if(result != null && !result.isEmpty()) {
            clipData = ClipData.newPlainText("text", result);
            cbmClipboardManager.setPrimaryClip(clipData);

            message = "Password copied to Clipboard!";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        } else {
            message = "First generate password!";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

}
