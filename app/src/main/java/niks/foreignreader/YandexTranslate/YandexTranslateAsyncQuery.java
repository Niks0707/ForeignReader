package niks.foreignreader.YandexTranslate;

import android.os.AsyncTask;

/**
 * Created by Niks on 01.04.2017.
 */

public abstract class YandexTranslateAsyncQuery extends AsyncTask<String, String, String> {

    private String mResponseString;
    private String mTranslatedText;

    public YandexTranslateAsyncQuery(String responseString) {
        super();
        mResponseString = responseString;
    }

    public String getTranslatedText() {
        return mTranslatedText;
    }



    @SuppressWarnings("deprecation")
    protected String doInBackground(String... args) {

        try {
            Translate.setKey(ApiKeys.YANDEX_API_KEY);
            mTranslatedText = Translate.execute(mResponseString, "en", "ru");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected abstract void onPostExecute(String arg);

}
