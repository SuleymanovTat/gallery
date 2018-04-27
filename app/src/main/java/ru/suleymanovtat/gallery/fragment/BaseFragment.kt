package ru.suleymanovtat.gallery.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import retrofit2.HttpException
import ru.suleymanovtat.gallery.R
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


abstract class BaseFragment : Fragment() {

    abstract fun layoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(layoutId(), container, false)

    fun checkError(context: Context, t: Throwable): String {
        if (t is HttpException) {
            val code = t.code()
            var messageError = getString(R.string.no_internet_connection)
            when (code) {
                400 -> messageError = getString(R.string.invalid_request)
                401 -> messageError = getString(R.string.no_auth)
                403 -> messageError = getString(R.string.auth_error)
                404 -> messageError = getString(R.string.not_found)
                408 -> messageError = getString(R.string.timeout)
                500 -> messageError = getString(R.string.error_servire)
                501 -> messageError = getString(R.string.no_implemented)
                502 -> messageError = getString(R.string.invalid_gateway)
                503 -> messageError = getString(R.string.service_temporarily_unavailable)
                504 -> messageError = getString(R.string.gateway_timeout)
            }
            return messageError
        }
        if (t is SocketTimeoutException) {
            if (!isNetworkAvailable() || t is UnknownHostException || t is ConnectException) {
                return getString(R.string.network_unavailable)
            }
        }
        if (t is NullPointerException) {
            return getString(R.string.error_occurred)
        }
        if (t is JsonSyntaxException) {
            return getString(R.string.error_data_from_server)
        }
        if (t is SSLHandshakeException || t is MalformedJsonException || t is IOException || t is SocketException) {
            return getString(R.string.check_your_network_connection)
        }
        return getString(R.string.error)
    }

    fun isNetworkAvailable(): Boolean {
        val connectivity = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        info?.indices?.filter { info[it].state == NetworkInfo.State.CONNECTED }?.forEach { return true }
        return false
    }
}