package niks.foreignreader.presentation.reader

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idea.engineering.ssska.data_layer.data_source.DataSourceListener
import niks.foreignreader.R
import niks.foreignreader.data.datasource.BookDataSource
import niks.foreignreader.data.model.BookPage
import java.util.*

class ReaderPagerAdapter(private val listener: ReaderListener,
                         private val bookDataSource: BookDataSource,
                         private val textSize: Float) :
        PagerAdapter(), DataSourceListener<BookPage> {

    private val holders = ArrayList<ReaderPageViewHolder>()

    init {
        bookDataSource.addListener(this)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (holders.size > position) {
            val holder = holders[position]
            container.addView(holder.itemView)
            return holder.itemView
        }

        val inflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_reader_page, container, false)

        val holder = ReaderPageViewHolder(view)

        holder.setListener(listener)
        holder.setTextSize(textSize)
        holders.add(holder)

        bookDataSource.getPage(position)?.let {
            holder.onBind(it)
        }

        container.addView(holder.itemView)
        return holder.itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getItemPosition(obj: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun isViewFromObject(view: View?, obj: Any?): Boolean {
        return view === obj
    }

    override fun getCount(): Int {
        return bookDataSource.getCountOfLoadedPages()
    }

    override fun notifyWillLoadItems() {

    }

    override fun notifyDidLoadItemsWithError(error: String) {
        notifyDataSetChanged()
    }

    override fun notifyDidLoadItems(data: BookPage?) {
        if (data?.pageNumber != null && data.pageNumber!! <= holders.size - 1) {
            val holder = holders[data.pageNumber!!]
            holder.itemView.post {
                holder.onBind(data)
                this@ReaderPagerAdapter.notifyDataSetChanged()
            }
        }
    }
}
