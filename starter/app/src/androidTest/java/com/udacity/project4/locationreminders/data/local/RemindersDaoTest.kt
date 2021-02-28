package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    //    TODO: Add testing implementation to the RemindersDao.kt
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb()=database.close()


    @Test
    fun saveReminder_getReminderById()= runBlockingTest{

        //Given - Save a Reminder
        val reminderDTO=ReminderDTO("Title","Description",
        "New Delhi",12.0,13.0)
        database.reminderDao().saveReminder(reminderDTO)


        //When - get reminder by id
        val loaded=database.reminderDao().getReminderById(reminderDTO.id)

        //Then - Check for expected data

        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.title,`is`("Title"))
        assertThat(loaded.description,`is`("Description"))
        assertThat(loaded.location,`is`("New Delhi"))
        assertThat(loaded.latitude,`is`(12.0))
        assertThat(loaded.longitude,`is`(13.0))
    }



}