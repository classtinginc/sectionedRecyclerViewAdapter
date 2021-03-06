package com.classtinginc.sectionedrecyclerviewadapter.basic_list

import android.os.Handler
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by BN on 2015. 12. 8..
 */
class BasicRecyclerViewPresenter {
    private val ITEM_COUNT = 10
    private val DELAY_TIME = 2000L

    private val subscription by lazy { CompositeSubscription() }
    private val items by lazy { mutableListOf<String>() }
    var view: BasicRecyclerViewView? = null

    fun onDetach() {
        subscription.unsubscribe()
    }

    fun getData(loadMore: Boolean = false) {
        view?.showLoadingFooter()

        Handler().postDelayed({
            getSampleData(loadMore)
        }, DELAY_TIME)
    }

    private fun getSampleData(loadMore: Boolean) {
        subscription.add(Observable.create<MutableList<String>> { subscriber ->
            val data = mutableListOf<String>()
            val start = items.size
            val end = start + ITEM_COUNT - 1
            (start..end).forEachIndexed { index, _ ->
                data.add("item ${index + start}")
            }
            items.addAll(data)
            subscriber.onNext(data)
        }.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.newThread())
        .subscribe({ data ->
            view?.showEmptyFooter()
            if (loadMore) {
                view?.notifyItemRangeInserted(data)
            } else {
                view?.notifyDataSetChanged(data)
            }
        }, { e ->
            e.printStackTrace()
        }))
    }
}