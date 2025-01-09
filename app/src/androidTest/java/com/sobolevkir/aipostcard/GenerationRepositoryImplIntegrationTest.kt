package com.sobolevkir.aipostcard

import android.util.Log
import com.sobolevkir.aipostcard.data.repository.GenerationRepositoryImpl
import com.sobolevkir.aipostcard.util.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class GenerationRepositoryImplIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Inject
    lateinit var repository: GenerationRepositoryImpl

    @Test
    fun testImageGenerationWithRealApi(): Unit = runBlocking {
        val prompt = "A cat wearing a hat."
        val negativePrompt = "blurry, low quality"
        val styleTitle = "DEFAULT"
        val requestResult =
            repository.requestGeneration(prompt, negativePrompt, styleTitle).first()
        when (requestResult) {
            is Resource.Success -> {
                assertNotNull(requestResult.data)
                Log.d("ON_REQUEST", requestResult.data.toString())
                val uuid = requestResult.data.uuid
                when (val result = repository.getStatusOrImage(uuid)) {
                    is Resource.Success -> {
                        assertNotNull(result.data)
                        Log.d("ON_RESULT", result.data.toString())
                    }

                    is Resource.Error -> {
                        fail("API request failed: ${result.error}")
                    }

                    else -> Unit
                }
            }

            is Resource.Error -> {
                fail("API request failed: ${requestResult.error}")
            }

            else -> Unit
        }


    }


}
