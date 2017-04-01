package niks.foreignreader.YandexTranslate;

import android.os.AsyncTask;

/**
 * Created by Niks on 01.04.2017.
 */

public abstract class YandexTranslateAsyncQuery extends AsyncTask<String, String, String> {

    private String mResponseString;
    private String mTranslatedText;
    private String mFromLanguage;
    private String mToLanguage;

    public YandexTranslateAsyncQuery(String responseString, String fromLanguage, String toLanguage) {
        super();
        mResponseString = responseString;
        mFromLanguage = fromLanguage;
        mToLanguage = toLanguage;
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

    protected abstract void onPostExecute(String arg);

}
