package niks.foreignreader.view;

import android.os.AsyncTask;
import android.text.TextPaint;

import niks.foreignreader.activities.ReaderActivity;

/**
 * Created by Niks on 08.04.2017.
 */

public class PagerTask extends AsyncTask<ReaderActivity.ViewAndPaint, ReaderActivity.ProgressTracker, Void> {
    private ReaderActivity mActivity;

    public PagerTask(ReaderActivity activity){
        this.mActivity = activity;
    }

    protected Void doInBackground(ReaderActivity.ViewAndPaint... vps) {

        ReaderActivity.ViewAndPaint vp = vps[0];
        ReaderActivity.ProgressTracker progress = new ReaderActivity.ProgressTracker();
        TextPaint paint = vp.paint;
        int numChars = 0;
        int lineCount = 0;
        int maxLineCount = vp.maxLineCount;
        int totalCharactersProcessedSoFar = 0;

        // contentString is the whole string of the book
        int totalPages = 0;
        while (vp.contentString != null && vp.contentString.length() != 0 )
        {
            while ((lineCount < maxLineCount) && (numChars < vp.contentString.length())) {
                numChars = numChars + paint.breakText(vp.contentString.substring(numChars), true, vp.screenWidth, null);
                lineCount ++;
            }

            // Retrieve the String to be displayed in the current textview
            String stringToBeDisplayed = vp.contentString.substring(0, numChars);
            int nextIndex = numChars;
            char nextChar = nextIndex < vp.contentString.length() ? vp.contentString.charAt(nextIndex) : ' ';
            if (!Character.isWhitespace(nextChar)) {
                stringToBeDisplayed = stringToBeDisplayed.substring(0, stringToBeDisplayed.lastIndexOf(" "));
            }
            numChars = stringToBeDisplayed.length();
            vp.contentString = vp.contentString.substring(numChars);

            // publish progress
            progress.totalPages = totalPages;
            progress.addPage(totalPages, totalCharactersProcessedSoFar, totalCharactersProcessedSoFar + numChars);
            publishProgress(progress);

            totalCharactersProcessedSoFar += numChars;

            // reset per page items
            numChars = 0;
            lineCount = 0;

            // increment  page counter
            totalPages ++;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(ReaderActivity.ProgressTracker... values) {
        mActivity.onPageProcessedUpdate(values[0]);
    }

}
