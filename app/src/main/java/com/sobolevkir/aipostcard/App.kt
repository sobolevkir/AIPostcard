package com.sobolevkir.aipostcard

import android.app.Application
import com.sobolevkir.aipostcard.domain.api.ImageFileManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var fileManager: ImageFileManager

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            fileManager.cleanCache()
        }
    }

}