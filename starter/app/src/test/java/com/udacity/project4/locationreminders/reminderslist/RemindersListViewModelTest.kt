package com.udacity.project4.locationreminders.reminderslist

import android.app.usage.UsageEvents
import android.os.Build
import android.util.EventLog
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.Event
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.utils.SingleLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.apache.tools.ant.taskdefs.Tstamp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*
import com.udacity.project4.Event.*
import org.hamcrest.Matchers.empty
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.P])
class RemindersListViewModelTest {

    //TODO: DONE provide testing to the RemindersListViewModel and its live data objects

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadReminders_addRemindersToList() {
        //Given a fresh viewModel
        val reminderDataSource = FakeDataSource()
        val remindersListViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            reminderDataSource
        )

        //When loading reminders
        remindersListViewModel.loadReminders()

        //Then reminderList event triggered
        val value : List<ReminderDataItem>? = remindersListViewModel.remindersList.getOrAwaitValue()

        assertThat(value.orEmpty(), empty())
    }

}