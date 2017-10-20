package niks.foreignreader.data.datasource

import com.idea.engineering.ssska.data_layer.data_source.BaseDataSource
import niks.foreignreader.data.model.BookPage
import niks.foreignreader.data.model.PageSize
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PushbackReader
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class BookDataSource(fileName: String, private val pageSize: PageSize) : BaseDataSource<BookPage>() {

    private val pushBackReader: PushbackReader
    private val bufferSize: Int

    private val pages = CopyOnWriteArrayList<String>()

    private val countOfLoadedPages: AtomicInteger = AtomicInteger(-1)
    private val maxRequiredPage: AtomicInteger = AtomicInteger(0)
    private val isLoading = AtomicReference<LoadingState>(LoadingState.NOT_LOAD)

    init {
        val file = File(fileName)
        bufferSize = pageSize.charsPerLine * pageSize.lines * 2
        val bufferedReader = BufferedReader(FileReader(file), bufferSize)
        pushBackReader = PushbackReader(bufferedReader, bufferSize)
    }

    @Deprecated("", ReplaceWith("throw IllegalStateException(\"reload data is not supported\")"))
    override fun reloadData() = throw IllegalStateException("reload data is not supported")

    fun getCountOfLoadedPages(): Int {
        return countOfLoadedPages.get()
    }

    fun getPage(page: Int): BookPage? {
        return if (page >= this.countOfLoadedPages.get()) {
            loadPage(page)
            null
        } else {
            data = BookPage(page, pages[page])
            notifyListenersDidLoadItems()
            data
        }
    }

    fun startLoad() {
        Runnable {
            isLoading.set(LoadingState.LOADING)
            val endPage = maxRequiredPage.get() + PAGE_OFFSET
            while (this.countOfLoadedPages.get() < endPage) {
                if (!loadNextPage()) {
                    isLoading.set(LoadingState.DID_LOAD_ALL)
                    return@Runnable
                }
            }
            isLoading.set(LoadingState.DID_LOAD)
        }.run()
    }

    private fun loadPage(page: Int) {
        Runnable {
            if (maxRequiredPage.get() < page) {
                maxRequiredPage.set(page)
                isLoading.set(LoadingState.NOT_LOAD)
            }
            while (page >= this.countOfLoadedPages.get()) {
                when (isLoading.get() ?: LoadingState.NOT_LOAD) {
                    LoadingState.NOT_LOAD -> startLoad()
                    LoadingState.LOADING -> Thread.sleep(WAIT_LOAD_TIMEOUT)
                    LoadingState.DID_LOAD ->
                        if (page >= this.countOfLoadedPages.get()) {
                            notifyListenersDidLoadItemsWithError("Wrong page number")
                            return@Runnable
                        } else {
                            data = BookPage(page, pages[page])
                            notifyListenersDidLoadItems()
                            return@Runnable
                        }
                    LoadingState.DID_LOAD_ALL ->
                        if (page >= this.countOfLoadedPages.get()) {
                            notifyListenersDidLoadItemsWithError("Wrong page number")
                            return@Runnable
                        } else {
                            data = BookPage(page, pages[page])
                            notifyListenersDidLoadItems()
                            return@Runnable
                        }

                }
                notifyListenersDidLoadItems()
            }
        }.run()
    }

    private fun loadNextPage(): Boolean {

        var charsBuffer: CharArray? = CharArray(bufferSize)

        pushBackReader.read(charsBuffer, 0, bufferSize)
        val result: Boolean
        result = if (charsBuffer != null) {
            val buffer = String(charsBuffer)
            val (length, text) = prepareText(buffer)

            pages.add(text)
            pushBackReader.unread(buffer.substring(length).toCharArray())

            countOfLoadedPages.incrementAndGet()
            true
        } else {
            notifyListenersDidLoadItemsWithError("Error read file")
            false
        }
        return result
    }

    private fun prepareText(string: String): Pair<Int, String> {

        val buffer = string //.trim('\n').replace("\r\n\r\n", "\r\n")
        var usedLines = 0
        var text = ""
        var lastIndex = 0
        while (usedLines < pageSize.lines) {
            val newLineIndex = buffer.indexOf('\n')
            if (newLineIndex != -1) {
                val textLine = buffer.substring(lastIndex, newLineIndex)
                usedLines += Math.ceil((textLine.length / pageSize.charsPerLine).toDouble()).toInt()
                text += textLine
                lastIndex = newLineIndex
            } else {
                var textLine = buffer.substring(lastIndex)
                val freeSpaceForText = pageSize.charsPerLine * (pageSize.lines - usedLines)
                if (textLine.length > freeSpaceForText) {
                    val lastSpace = buffer.lastIndexOf(" ")
                    if (lastSpace != -1) {
                        textLine = textLine.substring(lastSpace)
                        text += textLine
                        usedLines += Math.ceil((textLine.length / pageSize.charsPerLine).toDouble()).toInt()
                        lastIndex += lastSpace
                        return Pair(lastIndex, text)
                    } else {
                        textLine = textLine.substring(0, freeSpaceForText)
                        text += textLine
                        usedLines += Math.ceil((textLine.length / pageSize.charsPerLine).toDouble()).toInt()
                        lastIndex += freeSpaceForText
                        return Pair(lastIndex, text)
                    }
                } else {
                    text += textLine
                    usedLines += Math.ceil((textLine.length / pageSize.charsPerLine).toDouble()).toInt()
                    return Pair(lastIndex, text)
                }
            }
        }
        return Pair(lastIndex, text)
    }

    enum class LoadingState(val value: Int) {
        NOT_LOAD(0),
        LOADING(1),
        DID_LOAD(2),
        DID_LOAD_ALL(3);
    }

    companion object {
        private val WAIT_LOAD_TIMEOUT: Long = 500
        private val PAGE_OFFSET: Int = 10
    }
}