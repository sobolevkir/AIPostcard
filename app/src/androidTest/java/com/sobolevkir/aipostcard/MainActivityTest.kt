package com.sobolevkir.aipostcard

import androidx.test.core.app.ActivityScenario
import com.sobolevkir.aipostcard.data.repository.FBImageGenerationRepositoryImpl
import com.sobolevkir.aipostcard.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Inject
    lateinit var repository: FBImageGenerationRepositoryImpl

    @Test
    fun testApp() {
        ActivityScenario.launch(MainActivity::class.java)
        Assert.assertEquals("1", "1")
    }
}