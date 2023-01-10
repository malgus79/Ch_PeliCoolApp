package com.moviemain

import com.moviemain.model.local.MovieDaoTest
import com.moviemain.model.local.MovieDatabaseTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ExampleInstrumentedTest::class,
    MovieDatabaseTest::class,
    MovieDaoTest::class
)
class MovieTestSuit