package com.felipecsl.asymmetricgridview.app.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.felipecsl.asymmetricgridview.app.Utils;

public class AsymmetricGridView extends ListView {

    private static final int DEFAULT_COLUMN_COUNT = 2;
    private static final String TAG = "MultiColumnListView";
    private int numColumns = DEFAULT_COLUMN_COUNT;
    private final Rect padding;
    private final int defaultPadding;
    private final int requestedHorizontalSpacing;
    private final int requestedVerticalSpacing;
    private int requestedColumnWidth;

    public AsymmetricGridView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        defaultPadding = Utils.dpToPx(context, 5);
        requestedHorizontalSpacing = defaultPadding;
        requestedVerticalSpacing = defaultPadding;
        padding = new Rect(defaultPadding, defaultPadding, defaultPadding, defaultPadding);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                determineColumns();
                ((AsymmetricGridViewAdapter)getAdapter()).notifyDataSetChanged();
                return false;
            }
        });
    }

    public void setRequestedColumnWidth(final int width) {
        requestedColumnWidth = width;
    }

    public int getRequestedHorizontalSpacing() {
        return requestedHorizontalSpacing;
    }

    public int getRequestedVerticalSpacing() {
        return requestedVerticalSpacing;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        determineColumns();
    }

    public int determineColumns() {
        int numColumns;
        final int availableSpace = getAvailableSpace();

        if (requestedColumnWidth > 0) {
            numColumns = (availableSpace + requestedHorizontalSpacing) /
                    (requestedColumnWidth + requestedHorizontalSpacing);
        } else {
            // Default to 2 columns
            numColumns = DEFAULT_COLUMN_COUNT;
        }

        if (numColumns <= 0)
            numColumns = 1;

        this.numColumns = numColumns;

        return numColumns;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getColumnWidth() {
        return (getAvailableSpace() - ((numColumns - 1) * requestedHorizontalSpacing)) / numColumns;
    }

    public int getAvailableSpace() {
        return getMeasuredWidth() - padding.left - padding.right;
    }
}