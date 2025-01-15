package com.sobolevkir.aipostcard

import android.app.Application
import com.sobolevkir.aipostcard.data.storage.FileStorage
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var fileStorage: FileStorage

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            fileStorage.cleanCache()
        }
    }

}