package niks.foreignreader;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Niks on 12.12.2016.
 */
public abstract class LongClickableSpan extends ClickableSpan {

    abstract public void onLongClick(View view);
}
