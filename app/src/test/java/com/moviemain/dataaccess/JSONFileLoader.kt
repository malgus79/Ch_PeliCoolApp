package com.moviemain.dataaccess

import com.google.gson.Gson
import com.moviemain.data.model.MovieList
import java.io.InputStreamReader

class JSONFileLoader {
    private var jsonStr: String? = null

    fun loadJSONString(file: String): String?{
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return jsonStr
    }

    fun loadMovieList(file: String): MovieList?{
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return Gson().fromJson(jsonStr, MovieList::class.java)
    }
}