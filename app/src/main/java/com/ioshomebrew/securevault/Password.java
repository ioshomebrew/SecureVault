package com.ioshomebrew.securevault;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.util.Log;
import java.io.*;
import org.jasypt.util.text.BasicTextEncryptor;

public class Password extends AppCompatActivity {

    private TextView passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        passwordField = (TextView) findViewById(R.id.passwordField);
    }

    public void displayAlert(String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void goPressed(View v)
    {
        if(passwordField.getText().length() > 0)
        {
            // check if password is correct
            File file = new File(getApplicationContext().getFilesDir(), "vaultPass");
            FileInputStream fis = null;
            Log.d("Password.java", "Path is: " + file.toString());

            try {
                FileReader fr = new FileReader(file);
                BufferedReader in = new BufferedReader(fr);

                // decrypt password
                String data = in.readLine();
                in.close();
                BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
                textEncryptor.setPassword("123124235439582309534984uifsdhisahbfisaufbib");
                String myDecryptedText = textEncryptor.decrypt(data.toString());
                if(myDecryptedText.equals(passwordField.getText().toString()))
                {
                    // go to file browser
                    Intent intent = new Intent(getApplicationContext(), FileBrowser.class);
                    intent.putExtra("pass", myDecryptedText);
                    startActivity(intent);
                }
                else
                {
                    // display error saying incorrect pass entered
                    displayAlert("Incorrect Password");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null)
                        fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}
