package com.ioshomebrew.securevault;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.*;
import android.net.*;
import android.webkit.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;

public class FileBrowser extends AppCompatActivity {
    private TableView tableView;
    private String password;
    private File currentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        // get password
        password = getIntent().getStringExtra("pass");

        // get path for /Files
        String path = getApplicationContext().getFilesDir().toString()+"/Files";
        Log.d("FileBrowser.java", "Path is: " + path);

        // create files directory is doesn't exist already
        File directory = new File(path);
        if(!directory.exists())
        {
            directory.mkdir();
        }

        // first get directories
        List<Files> data = new ArrayList<>();
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        Log.d("FileBrowser.java", "Num of dirs: " + files.length);
        for (int i = 0; i < files.length; i++)
        {
            Files temp = new Files(true, files[i].getName(), files[i].getPath());
            data.add(temp);
        }

        // next get files
        files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isDirectory();
            }
        });
        Log.d("FileBrowser.java", "Num of files: " + files.length);
        for (int i = 0; i < files.length; i++)
        {
            Files temp = new Files(false, files[i].getName(), files[i].getPath());
            data.add(temp);
        }

        // create table view
        tableView = (TableView) findViewById(R.id.tableView);
        final FileBrowserDataAdapter dataAdaptor = new FileBrowserDataAdapter(this, data, tableView);
        tableView.setDataAdapter(dataAdaptor);
        tableView.addDataClickListener(new FilesClickListener());
        tableView.setSwipeToRefreshEnabled(true);
        tableView.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                String path = getApplicationContext().getFilesDir().toString()+"/Files";
                File directory = new File(path);

                // first get directories
                File[] files = directory.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                                return file.isDirectory();
                    }
                });
                for (int i = 0; i < files.length; i++)
                {
                    Files temp = new Files(true, files[i].getName(), files[i].getPath());
                    dataAdaptor.add(temp);
                }

                // next get files
                files = directory.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                                return !file.isDirectory();
                    }
                });
                for (int i = 0; i < files.length; i++)
                {
                    Files temp = new Files(false, files[i].getName(), files[i].getPath());
                    dataAdaptor.add(temp);
                }
                refreshIndicator.hide();
            }
        });
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

    public void newFilePressed(View view)
    {
        // create test file
        PrintWriter writer=null;
        File directory = new File(getApplicationContext().getFilesDir().toString()+"/Files/test4.txt");

        try {
            FileOutputStream os = new FileOutputStream(directory);
            writer=new PrintWriter(os);
        } catch (Exception e) {
            // log exception
        }
        writer.print("test");
        writer.flush();
        writer.close();

        // encrypt file
        try {
            Encryption.encryptData(password, directory);
        } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e)
        {
            displayAlert(e.getMessage());
        }
    }

    public String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }

    public void open_file(File file) {
        // Get URI and MIME type of file
        Uri uri = Uri.fromFile(file).normalizeScheme();
        String mime = get_mime_type(uri.toString());
        currentFile = file;
        Log.d("FileBrowser.java", uri.getPath());

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setType(mime);
        startActivity(Intent.createChooser(intent, "Open file with"));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("FileBrowser.java", "resumed");
        if(currentFile != null)
        {
            try {
                Encryption.encryptData(password, currentFile);
            } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e)
            {
            }
            currentFile = null;
        }
    }

    // listener for clicked data
    private class FilesClickListener implements TableDataClickListener<Files> {
        @Override
        public void onDataClicked(final int rowIndex, final Files clickedData) {
            // decrypt data
            File file = new File(clickedData.getPath());
            try {
                Encryption.decryptData(password, file);
            } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e)
            {
                try {
                    Encryption.encryptData(password, file);
                } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e1)
                {
                    displayAlert("Internal Encryption error");
                }
            }

            open_file(file);
        }
    }
}
