package ru.suleymanovtat.gallery.fragment

import ApiCore
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Callback
import kotlinx.android.synthetic.main.fragment_full_screen_image.*
import ru.suleymanovtat.gallery.BuildConfig
import ru.suleymanovtat.gallery.R
import java.lang.Exception

class FullScreenImageFragment : BaseFragment(), Callback {

    override fun layoutId(): Int = R.layout.fragment_full_screen_image

    companion object {

        private lateinit var fileLink: String

        @JvmStatic
        fun newInstance(fileLink: String): FullScreenImageFragment {
            val fullScreenImageFragment = FullScreenImageFragment()
            val args = Bundle()
            args.putString(BuildConfig.FILE_LINK, fileLink)
            fullScreenImageFragment.arguments = args
            return fullScreenImageFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileLink = arguments!!.getString(BuildConfig.FILE_LINK)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingImage()
        tvErrorMessageFullImage.setOnClickListener({
            tvErrorMessageFullImage.visibility = View.GONE
            progressBarFullImage.visibility = View.VISIBLE
            loadingImage()
        })
    }

    fun loadingImage() {
        ApiCore.picasso!!.load(fileLink).into(fullImage, this)
    }

    override fun onSuccess() {
        if (progressBarFullImage != null)
            progressBarFullImage!!.visibility = View.GONE
    }

    override fun onError(e: Exception?) {
        progressBarFullImage!!.visibility = View.GONE
        tvErrorMessageFullImage!!.visibility = View.VISIBLE
    }
}
