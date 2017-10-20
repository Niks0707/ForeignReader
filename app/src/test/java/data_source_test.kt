import com.idea.engineering.ssska.data_layer.data_source.DataSourceListener
import niks.foreignreader.data.datasource.BookDataSource
import niks.foreignreader.data.model.BookPage
import org.junit.Test

class data_source_test {

    val bookDataSource = BookDataSource("/home/niks/text.txt", 100)
    var index: Int = 0

    private val listener = object : DataSourceListener<BookPage> {
        override fun notifyWillLoadItems() {

        }

        override fun notifyDidLoadItems(data: BookPage?) {
//            assertNotNull(data)
            print(data?.content?.length)
            print("\n")
            if (index < 5) {
                bookDataSource.getPage(index++)
            }
        }

        override fun notifyDidLoadItemsWithError(error: String) {
            print(error)
        }
    }

    @Test
    fun test() {
        bookDataSource.addListener(listener)
        bookDataSource.startLoad()
        print(bookDataSource.getPage(index++)?.content?.length)
    }
}