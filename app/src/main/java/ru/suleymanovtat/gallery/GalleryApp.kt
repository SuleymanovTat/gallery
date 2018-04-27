package ru.suleymanovtat.gallery

import android.app.Application

class GalleryApp : Application() {

    companion object {
        lateinit var instance: GalleryApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}