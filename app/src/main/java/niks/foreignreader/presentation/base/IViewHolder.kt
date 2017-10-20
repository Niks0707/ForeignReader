package niks.foreignreader.presentation.base

interface IViewHolder<T> {
    fun onBind(item: T)
}