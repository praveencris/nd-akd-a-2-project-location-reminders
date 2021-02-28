package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.MainCoroutineRule
import com.udacity.project4.data.FakeAndroidDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.*
import org.junit.runner.RunWith
import java.security.MessageDigest.isEqual


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO:  DONE Add testing implementation to the RemindersLocalRepository.kt


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule=MainCoroutineRule()

    private lateinit var reminderDao: RemindersDao
    // Class under test
    private lateinit var remindersLocalRepository: RemindersLocalRepository

    private lateinit var database: RemindersDatabase

    @Before
    fun initDbAndCreateRepository() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

         reminderDao=database.reminderDao()
        // Get a reference to the class under test
        remindersLocalRepository = RemindersLocalRepository(database.reminderDao(),Dispatchers.Main)
    }

    @After
    fun closeDb()=database.close()


    @Test
    fun saveReminder_getReminderByID()= mainCoroutineRule.runBlockingTest{
        //Given - Save a Reminder
        val reminderDTO=ReminderDTO("Title","Description",
            "New Delhi",12.0,13.0)
        remindersLocalRepository.saveReminder(reminderDTO)

        //When - get reminder by id
        val loaded=remindersLocalRepository.getReminder(reminderDTO.id) as Result.Success

        //Then - Check for expected data
        assertThat(loaded.data, equalTo(reminderDTO))
    }


}