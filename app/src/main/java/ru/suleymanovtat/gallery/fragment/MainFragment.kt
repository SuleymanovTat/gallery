package ru.suleymanovtat.gallery.fragment

import ApiCore
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import ru.suleymanovtat.gallery.BuildConfig
import ru.suleymanovtat.gallery.R
import ru.suleymanovtat.gallery.adapter.GalleryAdapter
import ru.suleymanovtat.gallery.model.Items

class MainFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_main

    private val mCompositeDisposable = CompositeDisposable()
    private var listImage: ArrayList<Items> = arrayListOf()
    private lateinit var adaper: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //adapter
        adaper = GalleryAdapter()
        recyclerViewGallery.adapter = adaper
        //click item
        adaper.clickEvent.subscribe({
            val currentFragment = fragmentManager!!.findFragmentByTag(BuildConfig.TAG_MAIN_FRAGMENT) as MainFragment
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.hide(currentFragment).add(R.id.container, FullScreenImageFragment.newInstance(it)).addToBackStack(null).commit()
        })
        //layoutManager
        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
        recyclerViewGallery.layoutManager = GridLayoutManager(this.activity!!, spanCount)
        //get data
        if (savedInstanceState != null) {
            val result = savedInstanceState.getParcelableArrayList<Items>(BuildConfig.LIST_IMAGE)
            adaper.updateData(result)
            if (result == null || result.isEmpty()) tvErrorMessageList.visibility = View.VISIBLE
        } else {
            //query
            getListImage()
        }
        tvErrorMessageList.setOnClickListener { getListImage() }
    }

    private fun getListImage() {
        tvErrorMessageList.visibility = View.GONE
        mCompositeDisposable.add(ApiCore.service.getListImage()
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .observeOn(AndroidSchedulers.mainThread()) // "listen" on UIThread
                .subscribe({ resources ->
                    listImage = resources.items
                    adaper.updateData(resources.items)
                }, { t: Throwable? ->
                    tvErrorMessageList!!.visibility = View.VISIBLE
                    Snackbar.make(recyclerViewGallery!!, checkError(activity!!, t!!), Snackbar.LENGTH_SHORT).show()
                }))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(BuildConfig.LIST_IMAGE, listImage)
    }

    override fun onPause() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.clear()
        }
        super.onPause()
    }
}
