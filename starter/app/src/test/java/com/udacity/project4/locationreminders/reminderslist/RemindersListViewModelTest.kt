package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.R])
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
        val value: List<ReminderDataItem>? = remindersListViewModel.remindersList.getOrAwaitValue()

        assertThat(value.orEmpty(), empty())
    }

    @After
    fun tearDown() {
        stopKoin()
    }

}