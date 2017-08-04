package com.ioshomebrew.securevault;

import de.codecrafters.tableview.*;
import de.codecrafters.tableview.toolkit.*;

import android.content.Context;
import java.util.List;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ioshomebrew on 5/15/17.
 */

public class FileBrowserDataAdapter extends LongPressAwareTableDataAdapter<Files> {
    public FileBrowserDataAdapter(Context context, List<Files> data, final TableView<Files> tableView)
    {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Files file = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderIsFoulder(file);
                break;
            case 1:
                renderedView = renderName(file);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        return null;
    }

    private View renderIsFoulder(Files file)
    {
        if(file.isFoulder())
        {
            return renderFoulderImage();
        }
        else
        {
            //return renderString("Not Foulder");
            return null;
        }
    }

    private View renderName(Files file)
    {
        return renderString(file.getName());
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(20);
        return textView;
    }

    private View renderFoulderImage()
    {
        final ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.folder);
        imageView.setPadding(20, 10, 20, 10);
        return imageView;
    }
}
