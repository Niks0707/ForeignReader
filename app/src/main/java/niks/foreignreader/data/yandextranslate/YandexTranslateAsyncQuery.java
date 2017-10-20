package niks.foreignreader.data.yandextranslate;

import android.os.AsyncTask;

public class YandexTranslateAsyncQuery extends AsyncTask<String, String, String> {

    private String mResponseString;
    private String mTranslatedText;
    private String mFromLanguage;
    private String mToLanguage;
    private YandexTranslateListener listener;

    public YandexTranslateAsyncQuery(YandexTranslateListener listener, String responseString, String fromLanguage, String toLanguage) {
        super();
        mResponseString = responseString;
        mFromLanguage = fromLanguage;
        mToLanguage = toLanguage;
        this.listener = listener;
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
        listener.showTranslatedText(mTranslatedText);
    }

    public interface YandexTranslateListener {
        void showTranslatedText(String translatedText);
    }
}
