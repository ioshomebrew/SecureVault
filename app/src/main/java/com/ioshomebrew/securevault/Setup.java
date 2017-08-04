package com.ioshomebrew.securevault;

import android.os.Bundle;
import android.support.v7.app.*;
import android.util.Log;
import android.view.View;
import android.content.DialogInterface;
import android.widget.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import android.content.Intent;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

public class Setup extends AppCompatActivity {
    private Button finish;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        passwordField = (EditText) findViewById(R.id.passwordField);
        finish = (Button) findViewById(R.id.finish);

        // check if vault already exists
        File file = new File(getApplicationContext().getFilesDir(), "vaultPass");
        if(file.exists())
        {
            Intent intent = new Intent(getApplicationContext(), Password.class);
            startActivity(intent);
        }
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

    public void finishPressed(View view)
    {
        // verify if password is entered
        if(passwordField.getText().length() > 0)
        {
            // create vault

            // encrypt vault pass
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword("123124235439582309534984uifsdhisahbfisaufbib");
            String myEncryptedText = textEncryptor.encrypt(passwordField.getText().toString());
            Log.d("Setup.java", myEncryptedText);

            // write vault pass
            PrintWriter writer=null;

            try {
                FileOutputStream os = openFileOutput("vaultPass", getApplicationContext().MODE_PRIVATE);
                writer=new PrintWriter(os);
            } catch (Exception e) {
                // log exception
            }
            writer.print(myEncryptedText);
            writer.flush();
            writer.close();

            // go to intent
            Intent intent = new Intent(getApplicationContext(), Password.class);
            startActivity(intent);
        }
        else
        {
            displayAlert("Please input a password");
        }
    }
}
