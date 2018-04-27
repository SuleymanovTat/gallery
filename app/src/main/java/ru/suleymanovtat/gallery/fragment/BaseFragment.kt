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
            var messageError = "Возможно нет подключения к Интернету"
            when (code) {
                400 -> messageError = "Неверный запрос, возможно введены неправильные данные или Вам нужно обновить приложение"
                401 -> messageError = "Для этого действия требуется авторизация, если Вы уже авторизованы, пожалуйста, переустановите приложение и войдите снова"
                403 -> messageError = "Вы пытаетесь подключить аккаунт социальной сети, который уже подключен другим пользователем сервиса"
                404 -> messageError = "Объект не найден"
                408 -> messageError = "Таймаут запроса, возможно выполняются технические работы"
                500 -> messageError = "Возможно выполняются технические работы на сервере"
                501 -> messageError = "Эта функция возможно еще не реализована"
                502 -> messageError = "Неверный шлюз, возможно выполняются технические работы"
                503 -> messageError = "Сервис временно недоступен"
                504 -> messageError = "Таймаут шлюза, по техническим причинам сервер не отвечает"
            }
            return messageError
        }
        if (t is SocketTimeoutException) {
            if (!isNetworkAvailable() || t is UnknownHostException || t is ConnectException) {
                return "Сеть недоступна"
            }
        }
        if (t is NullPointerException) {
            return "Произошла ошибка"
        }
        if (t is JsonSyntaxException) {
            return "Произошла ошибка при получении данных с сервера"
        }
        if (t is SSLHandshakeException || t is MalformedJsonException || t is IOException || t is SocketException) {
            return "Проверьте подключение к сети"
        }
        return "Ошибка"
    }

    fun isNetworkAvailable(): Boolean {
        val connectivity = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        info?.indices?.filter { info[it].state == NetworkInfo.State.CONNECTED }?.forEach { return true }
        return false
    }
}