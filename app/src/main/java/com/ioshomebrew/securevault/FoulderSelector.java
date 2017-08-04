package com.ioshomebrew.securevault;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FoulderSelector extends AppCompatActivity {
    private ArrayList<String> files;
    private TableView tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foulder_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get path for sd card
        String path = getExternalFilesDir(null).getPath().toString();
        Log.d("FoulderSelector.java", "Path is: " + path);

        // create files directory is doesn't exist already
        File directory = new File(path);
        if(directory == null)
        {
            Log.d("FoulderSelector.java", "directory doesn't exist");
        }

        // first get directories
        List<Files> data = new ArrayList<>();
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        if(files == null)
        {
            Log.d("FoulderSelector.java", "files is null");
        }
        Log.d("FoulderSelector.java", "Num of dirs: " + files.length);
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
        tableView.addDataClickListener(new FoulderSelector.FilesClickListener());
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


    // listener for clicked data
    private class FilesClickListener implements TableDataClickListener<Files> {
        @Override
        public void onDataClicked(final int rowIndex, final Files clickedData) {
            // go to director
        }
    }
}
