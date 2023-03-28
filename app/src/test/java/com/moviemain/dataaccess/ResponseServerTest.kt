package com.moviemain.dataaccess

import com.google.gson.Gson
import com.moviemain.model.data.MovieList
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection


@RunWith(MockitoJUnitRunner::class)
class ResponseServerTest {
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `read json file success`() {
        val reader = JSONFileLoader().loadJSONString("movie_response_success")
        assertThat(reader, `is`(notNullValue()))
        assertThat(reader, containsString("Avatar: El sentido del agua"))
    }

    @Test
    fun `get movies and check title exist`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("movie_response_success")
                    ?: "{errorCode:34}"
            )
        mockWebServer.enqueue(response)

        //validar que tenga la propiedad "timezone"
        assertThat(response.getBody()?.readUtf8(), containsString("\"title\""))
    }

    @Test
    fun `get movies and check fail response`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("movie_response_fail")
                    ?: "{errorCode:34}"
            )
        mockWebServer.enqueue(response)

        assertThat(
            response.getBody()?.readUtf8(),
            containsString("{\"status_code\":7,\"status_message\":\"" +
                    "Invalid API key: You must be granted a valid key.\",\"success\":false}")
        )
    }

    @Test
    fun `get movies and check contains result list no empty`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("movie_response_success")
                    ?: "{errorCode:34}"
            )
        mockWebServer.enqueue(response)

        assertThat(response.getBody()?.readUtf8(), containsString("results"))

        val json = Gson().fromJson(response.getBody()?.readUtf8() ?: "", MovieList::class.java)
        assertThat(json.results.isEmpty(), `is`(false))
    }
}