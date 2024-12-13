package com.sobolevkir.aipostcard

import com.sobolevkir.aipostcard.data.repository.FusionBrainRepository
import com.sobolevkir.aipostcard.util.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class FusionBrainRepositoryIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Inject
    lateinit var repository: FusionBrainRepository

    @Test
    fun testImageGenerationWithRealApi(): Unit = runBlocking {
        val prompt = "A cat wearing a hat."
        val negativePrompt = "blurry, low quality"
        val style = "DEFAULT"
        val modelId = "4"
        val result = repository.sendImageGenerationRequest(prompt, negativePrompt, style, modelId)

        when (result) {
            is Resource.Success -> {
                assertNotNull(result.data)
                println(result.data.toString())

            }

            is Resource.Error -> {
                fail("API request failed: ${result.message}")
            }
        }
    }

    @Test
    fun testLatestModelIdFromRealApi() = runBlocking {

        when (val result = repository.getLatestGenerationModelId()) {
            is Resource.Success -> {
                assertNotNull(result.data)
                println("Model id: " + result.data.toString())
            }

            is Resource.Error -> {
                fail("API request failed: ${result.message}")
                println("Error message: " + result.message)
            }
        }
    }
}
