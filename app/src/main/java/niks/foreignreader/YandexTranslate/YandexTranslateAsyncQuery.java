package niks.foreignreader.yandextranslate;

import android.os.AsyncTask;

import niks.foreignreader.activities.ReaderActivity;

/**
 * Created by Niks on 01.04.2017.
 */

public class YandexTranslateAsyncQuery extends AsyncTask<String, String, String> {

    private String mResponseString;
    private String mTranslatedText;
    private String mFromLanguage;
    private String mToLanguage;
    private ReaderActivity mActivity;

    public YandexTranslateAsyncQuery(ReaderActivity activity, String responseString, String fromLanguage, String toLanguage) {
        super();
        mResponseString = responseString;
        mFromLanguage = fromLanguage;
        mToLanguage = toLanguage;
        mActivity = activity;
    }

    public String getTranslatedText() {
        return mTranslatedText;
    }

    @Override
    protected String doInBackground(String... args) {

        try {
            Translate.setKey(ApiKeys.YANDEX_API_KEY);
            mTranslatedText = Translate.execute(mResponseString, mFromLanguage, mToLanguage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String arg) {
        mActivity.showTranslatedText(mTranslatedText);
    }

}
