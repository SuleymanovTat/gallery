package ru.suleymanovtat.gallery.core

import io.reactivex.Flowable
import retrofit2.http.GET
import ru.suleymanovtat.gallery.model.ImageData

interface RetrofitInterface {

    @GET("v1/disk/resources/public")
    fun getListImage(): Flowable<ImageData>
}