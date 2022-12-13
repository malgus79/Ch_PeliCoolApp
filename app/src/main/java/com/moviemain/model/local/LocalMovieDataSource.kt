package com.moviemain.model.local

class LocalMovieDataSource(private val movieDao: MovieDao) {

//    suspend fun getPopularMovies(): MovieList {
//        return movieDao.getAllMovies().filter { it.movie_type == "popular" }.toMovieList()
//    }
//
//    suspend fun getTopRatedMovies(): MovieList {
//        return movieDao.getAllMovies().filter { it.movie_type == "top_rated" }.toMovieList()
//    }
//
//    suspend fun getUpcomingMovies(): MovieList {
//        return movieDao.getAllMovies().filter { it.movie_type == "now_playing" }.toMovieList()
//    }
//
//    suspend fun getNowPlayingMovies(): MovieList {
//        return movieDao.getAllMovies().filter { it.movie_type == "upcoming" }.toMovieList()
//    }

    // Save a MovieEntity
    suspend fun saveMovie(movie: MovieEntity) {
        movieDao.saveMovie(movie)
    }
}