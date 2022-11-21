package com.moviemain.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moviemain.MainDispatcherRule
import com.moviemain.model.data.*
import com.moviemain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class MovieListViewModelTest() {
    @get:Rule
    var testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when all sections retrieves data correctly`() = runTest {

        //Arrange
        val popular = Popular(false,
            "/nmGWzTLMXy9x7mKd8NKPLmHtWGa.jpg",
            438148,
            "en",
            "Minions: The Rise of Gru",
            "A fanboy of a supervillain supergroup known as the Vicious 6, Gru hatches a plan to become evil enough to join them, with the backup of his followers, the Minions.",
            11087.461,
            "/wKiOkZTN9lUUUNZLmtnwubZYONg.jpg",
            "2022-06-29",
            "Minions: The Rise of Gru",
            false,
            7.6,
            333)
        val popularList = PopularList(listOf(popular))

        val topRated = TopRated(false,
            "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
            278,
            "en",
            "The Shawshank Redemption",
            "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.",
            78.428,
            "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            "1994-09-23",
            "The Shawshank Redemption",
            false,
            8.7,
            21785)
        val topRatedList = TopRatedList(listOf(topRated))

        val nowPlaying = NowPlaying(false,
            "/fQq1FWp1rC89xDrRMuyFJdFUdMd.jpg",
            761053,
            "en",
            "Gabriel's Inferno: Part III",
            "The final part of the film adaption of the erotic romance novel Gabriel's Inferno written by an anonymous Canadian author under the pen name Sylvain Reynard.",
            49.252,
            "/uqmTxOP3gNl5MWRt1wlrUnzTphM.jpg",
            "2020-11-19",
            "Gabriel's Inferno: Part III",
            false,
            8.5,
            974)
        val nowPlayingList = NowPlayingList(listOf(nowPlaying))

        val upcoming = Upcoming(false,
            "/tsC3DRGAQvkoA88lQQfbQ40byFS.jpg",
            756999,
            "en",
            "The Black Phone",
            "Finney Shaw, a shy but clever 13-year-old boy, is abducted by a sadistic killer and trapped in a soundproof basement where screaming is of little use. When a disconnected phone on the wall begins to ring, Finney discovers that he can hear the voices of the killer’s previous victims. And they are dead set on making sure that what happened to them doesn’t happen to Finney.",
            2131.933,
            "/wd6WxLLR2w8aAXmLPDW5CN0iSB3.jpg",
            "2022-06-22",
            "The Black Phone",
            false,
            7.6,
            443)
        val upcomingList = UpcomingList(listOf(upcoming))

        val repository = mockk<HomeRepository>()

        coEvery { repository.getPopularMovies() } returns popularList
        coEvery { repository.getTopRatedMovies() } returns topRatedList
        coEvery { repository.getNowPlayingMovies() } returns nowPlayingList
        coEvery { repository.getUpcomingMovies() } returns upcomingList

        val movieListViewModel = MovieListViewModel(repository)

        //Act
        movieListViewModel.getPopularMovies()
        movieListViewModel.getTopRatedMovies()
        movieListViewModel.getNowPlayingMovies()
        movieListViewModel.getUpcomingMovies()

        //Assert
        assert(movieListViewModel.popularList.value?.equals(null) == popularList.data.isNullOrEmpty())
        assert(movieListViewModel.topRatedList.value?.equals(null) == topRatedList.data.isNullOrEmpty())
        assert(movieListViewModel.nowPlayingList.value?.equals(null) == nowPlayingList.data.isNullOrEmpty())
        assert(movieListViewModel.upcomingList.value?.equals(null) == upcomingList.data.isNullOrEmpty())

    }

    @Test
    fun `when popular section retrieves data correctly`() = runTest {

        //Arrange
        val popular = Popular(false,
            "/nmGWzTLMXy9x7mKd8NKPLmHtWGa.jpg",
            438148,
            "en",
            "Minions: The Rise of Gru",
            "A fanboy of a supervillain supergroup known as the Vicious 6, Gru hatches a plan to become evil enough to join them, with the backup of his followers, the Minions.",
            11087.461,
            "/wKiOkZTN9lUUUNZLmtnwubZYONg.jpg",
            "2022-06-29",
            "Minions: The Rise of Gru",
            false,
            7.6,
            333)
        val popularList = PopularList(listOf(popular))

        val repository = mockk<HomeRepository>()

        coEvery { repository.getPopularMovies() } returns popularList

        val popularViewModel = MovieListViewModel(repository)

        //Act
        popularViewModel.getPopularMovies()

        //Assert
        assert(popularViewModel.popularList.value!!.equals(popular) == popularList.data?.isNullOrEmpty())

    }

    @Test
    fun `when top_rated section retrieves data correctly`() = runTest {

        //Arrange
        val topRated = TopRated(false,
            "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
            278,
            "en",
            "The Shawshank Redemption",
            "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.",
            78.428,
            "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            "1994-09-23",
            "The Shawshank Redemption",
            false,
            8.7,
            21785)
        val topRatedList = TopRatedList(listOf(topRated))

        val repository = mockk<HomeRepository>()

        coEvery { repository.getTopRatedMovies() } returns topRatedList

        val topRatedViewModel = MovieListViewModel(repository)

        //Act
        topRatedViewModel.getTopRatedMovies()

        //Assert
        assert(topRatedViewModel.topRatedList.value!!.equals(topRated) == topRatedList.data?.isNullOrEmpty())

    }

    @Test
    fun `when now_playing section retrieves data correctly`() = runTest {

        //Arrange
        val nowPlaying = NowPlaying(false,
            "/fQq1FWp1rC89xDrRMuyFJdFUdMd.jpg",
            761053,
            "en",
            "Gabriel's Inferno: Part III",
            "The final part of the film adaption of the erotic romance novel Gabriel's Inferno written by an anonymous Canadian author under the pen name Sylvain Reynard.",
            49.252,
            "/uqmTxOP3gNl5MWRt1wlrUnzTphM.jpg",
            "2020-11-19",
            "Gabriel's Inferno: Part III",
            false,
            8.5,
            974)
        val nowPlayingList = NowPlayingList(listOf(nowPlaying))

        val repository = mockk<HomeRepository>()

        coEvery { repository.getNowPlayingMovies() } returns nowPlayingList

        val nowPlayingViewModel = MovieListViewModel(repository)

        //Act
        nowPlayingViewModel.getNowPlayingMovies()

        //Assert
        assert(nowPlayingViewModel.nowPlayingList.value!!.equals(nowPlaying) == nowPlayingList.data?.isNullOrEmpty())

    }

    @Test
    fun `when upcoming section retrieves data correctly`() = runTest {

        //Arrange
        val upcoming = Upcoming(false,
            "/tsC3DRGAQvkoA88lQQfbQ40byFS.jpg",
            756999,
            "en",
            "The Black Phone",
            "Finney Shaw, a shy but clever 13-year-old boy, is abducted by a sadistic killer and trapped in a soundproof basement where screaming is of little use. When a disconnected phone on the wall begins to ring, Finney discovers that he can hear the voices of the killer’s previous victims. And they are dead set on making sure that what happened to them doesn’t happen to Finney.",
            2131.933,
            "/wd6WxLLR2w8aAXmLPDW5CN0iSB3.jpg",
            "2022-06-22",
            "The Black Phone",
            false,
            7.6,
            443)
        val upcomingList = UpcomingList(listOf(upcoming))

        val repository = mockk<HomeRepository>()

        coEvery { repository.getUpcomingMovies() } returns upcomingList

        val upcomingViewModel = MovieListViewModel(repository)

        //Act
        upcomingViewModel.getUpcomingMovies()

        //Assert
        assert(upcomingViewModel.upcomingList.value!!.equals(upcoming) == upcomingList.data?.isNullOrEmpty())

    }


    @Test
    fun `when all sections retrieves data empty or null`() = runTest {

        //Arrange
        val popular = Popular(false,
            "",
            -1,
            "",
            "",
            "",
            -1.0,
            "",
            "",
            "",
            false,
            -1.0,
            -1)
        val popularList = PopularList(listOf(popular))

        val topRated = TopRated(false,
            "",
            -1,
            "",
            "",
            "",
            -1.0,
            "",
            "",
            "",
            false,
            -1.0,
            -1)
        val topRatedList = TopRatedList(listOf(topRated))

        val nowPlaying = NowPlaying(false,
            "",
            -1,
            "",
            "",
            "",
            -1.0,
            "",
            "",
            "",
            false,
            -1.0,
            -1)
        val nowPlayingList = NowPlayingList(listOf(nowPlaying))

        val upcoming = Upcoming(false,
            "",
            -1,
            "",
            "",
            "",
            -1.0,
            "",
            "",
            "",
            false,
            -1.0,
            -1)
        val upcomingList = UpcomingList(listOf(upcoming))

        val repository = mockk<HomeRepository>()

        coEvery { repository.getPopularMovies() } returns popularList
        coEvery { repository.getTopRatedMovies() } returns topRatedList
        coEvery { repository.getNowPlayingMovies() } returns nowPlayingList
        coEvery { repository.getUpcomingMovies() } returns upcomingList

        val movieListViewModel = MovieListViewModel(repository)

        //Act
        movieListViewModel.getPopularMovies()
        movieListViewModel.getTopRatedMovies()
        movieListViewModel.getNowPlayingMovies()
        movieListViewModel.getUpcomingMovies()

        //Assert
        assert(movieListViewModel.popularList.value?.equals(null) == popularList.data.isEmpty())
        assert(movieListViewModel.topRatedList.value?.equals(null) == topRatedList.data.isEmpty())
        assert(movieListViewModel.nowPlayingList.value?.equals(null) == nowPlayingList.data.isEmpty())
        assert(movieListViewModel.upcomingList.value?.equals(null) == upcomingList.data.isEmpty())

    }

    @Test
    fun `when all sections fail`() = runTest {

        //Arrange
        val popularList = PopularList(listOf())
        val topRatedList = TopRatedList(listOf())
        val nowPlayingList = NowPlayingList(listOf())
        val upcomingList = UpcomingList(listOf())

        val repository = mockk<HomeRepository>()

        coEvery { repository.getPopularMovies() } returns popularList
        coEvery { repository.getTopRatedMovies() } returns topRatedList
        coEvery { repository.getNowPlayingMovies() } returns nowPlayingList
        coEvery { repository.getUpcomingMovies() } returns upcomingList

        val movieListViewModel = MovieListViewModel(repository)

        //Act
        movieListViewModel.getPopularMovies()
        movieListViewModel.getTopRatedMovies()
        movieListViewModel.getNowPlayingMovies()
        movieListViewModel.getUpcomingMovies()

        //Assert
        assert(movieListViewModel.popularList.value?.equals(null) == false)
        assert(movieListViewModel.topRatedList.value?.equals(null) == false)
        assert(movieListViewModel.nowPlayingList.value?.equals(null) == false)
        assert(movieListViewModel.upcomingList.value?.equals(null) == false)

    }

    @Test
    fun `when only popular sections retrieves data correctly`() = runTest {

        //Arrange
        val popular = Popular(false,
            "/nmGWzTLMXy9x7mKd8NKPLmHtWGa.jpg",
            438148,
            "en",
            "Minions: The Rise of Gru",
            "A fanboy of a supervillain supergroup known as the Vicious 6, Gru hatches a plan to become evil enough to join them, with the backup of his followers, the Minions.",
            11087.461,
            "/wKiOkZTN9lUUUNZLmtnwubZYONg.jpg",
            "2022-06-29",
            "Minions: The Rise of Gru",
            false,
            7.6,
            333)
        val popularList = PopularList(listOf(popular))

        val topRated = TopRated(false,
            "",
            -1,
            "",
            "",
            "",
            -1.0,
            "",
            "",
            "",
            false,
            -1.0,
            -1)
        val topRatedList = TopRatedList(listOf(topRated))

        val nowPlaying = NowPlaying(false,
            "",
            -1,
            "",
            "",
            "",
            -1.0,
            "",
            "",
            "",
            false,
            -1.0,
            -1)
        val nowPlayingList = NowPlayingList(listOf(nowPlaying))

        val upcoming = Upcoming(false,
            "",
            -1,
            "",
            "",
            "",
            -1.0,
            "",
            "",
            "",
            false,
            -1.0,
            -1)
        val upcomingList = UpcomingList(listOf(upcoming))

        val repository = mockk<HomeRepository>()

        coEvery { repository.getPopularMovies() } returns popularList
        coEvery { repository.getTopRatedMovies() } returns topRatedList
        coEvery { repository.getNowPlayingMovies() } returns nowPlayingList
        coEvery { repository.getUpcomingMovies() } returns upcomingList

        val movieListViewModel = MovieListViewModel(repository)

        //Act
        movieListViewModel.getPopularMovies()
        movieListViewModel.getTopRatedMovies()
        movieListViewModel.getNowPlayingMovies()
        movieListViewModel.getUpcomingMovies()

        //Assert
        assert(movieListViewModel.popularList.value?.equals(null) == popularList.data.isNullOrEmpty())
        assert(movieListViewModel.topRatedList.value?.equals(null) == false)
        assert(movieListViewModel.nowPlayingList.value?.equals(null) == false)
        assert(movieListViewModel.upcomingList.value?.equals(null) == false)

    }

    @Test
    fun `when only popular sections fails`() = runTest {

        //Arrange
        val popular = Popular(false,
            "",
            -1,
            "",
            "",
            "",
            -1.0,
            "",
            "",
            "",
            false,
            -1.0,
            -1)
        val popularList = PopularList(listOf(popular))

        val topRated = TopRated(false,
            "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
            278,
            "en",
            "The Shawshank Redemption",
            "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.",
            78.428,
            "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            "1994-09-23",
            "The Shawshank Redemption",
            false,
            8.7,
            21785)
        val topRatedList = TopRatedList(listOf(topRated))

        val nowPlaying = NowPlaying(false,
            "/fQq1FWp1rC89xDrRMuyFJdFUdMd.jpg",
            761053,
            "en",
            "Gabriel's Inferno: Part III",
            "The final part of the film adaption of the erotic romance novel Gabriel's Inferno written by an anonymous Canadian author under the pen name Sylvain Reynard.",
            49.252,
            "/uqmTxOP3gNl5MWRt1wlrUnzTphM.jpg",
            "2020-11-19",
            "Gabriel's Inferno: Part III",
            false,
            8.5,
            974)
        val nowPlayingList = NowPlayingList(listOf(nowPlaying))

        val upcoming = Upcoming(false,
            "/tsC3DRGAQvkoA88lQQfbQ40byFS.jpg",
            756999,
            "en",
            "The Black Phone",
            "Finney Shaw, a shy but clever 13-year-old boy, is abducted by a sadistic killer and trapped in a soundproof basement where screaming is of little use. When a disconnected phone on the wall begins to ring, Finney discovers that he can hear the voices of the killer’s previous victims. And they are dead set on making sure that what happened to them doesn’t happen to Finney.",
            2131.933,
            "/wd6WxLLR2w8aAXmLPDW5CN0iSB3.jpg",
            "2022-06-22",
            "The Black Phone",
            false,
            7.6,
            443)
        val upcomingList = UpcomingList(listOf(upcoming))


        val repository = mockk<HomeRepository>()

        coEvery { repository.getPopularMovies() } returns popularList
        coEvery { repository.getTopRatedMovies() } returns topRatedList
        coEvery { repository.getNowPlayingMovies() } returns nowPlayingList
        coEvery { repository.getUpcomingMovies() } returns upcomingList

        val movieListViewModel = MovieListViewModel(repository)

        //Act
        movieListViewModel.getPopularMovies()
        movieListViewModel.getTopRatedMovies()
        movieListViewModel.getNowPlayingMovies()
        movieListViewModel.getUpcomingMovies()

        //Assert
        assert(movieListViewModel.popularList.value?.equals(null) == false)
        assert(movieListViewModel.topRatedList.value?.equals(null) == topRatedList.data.isNullOrEmpty())
        assert(movieListViewModel.nowPlayingList.value?.equals(null) == nowPlayingList.data.isNullOrEmpty())
        assert(movieListViewModel.upcomingList.value?.equals(null) == upcomingList.data.isNullOrEmpty())

    }

}