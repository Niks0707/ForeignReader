package niks.foreignreader.data.model

class BookPage() {

    constructor(pageNumber: Int, content: String) : this() {

        this.pageNumber = pageNumber
        this.content = content
    }

    var pageNumber: Int? = null
    var content: String? = null
}