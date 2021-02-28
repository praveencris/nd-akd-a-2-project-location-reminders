package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SaveReminderViewModelTest() {
    //TODO: DONE provide testing to the SaveReminderView and its live data objects

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private val appContext: Application = ApplicationProvider.getApplicationContext()

    @Before
    fun setupViewModel() {
        //Given a fresh viewModel
        val reminderDataSource = FakeDataSource()
        appContext
        saveReminderViewModel = SaveReminderViewModel(
            appContext,
            reminderDataSource
        )

    }

    @Test
    fun testOnClear() {

        // When onClear called
        saveReminderViewModel.onClear()

        //Then values of all LiveData's associated with viewModel is set to null
        val title: String? = saveReminderViewModel.reminderTitle.getOrAwaitValue()
        MatcherAssert.assertThat(title, Matchers.nullValue())

        val description: String? = saveReminderViewModel.reminderDescription.getOrAwaitValue()
        MatcherAssert.assertThat(description, Matchers.nullValue())

        val selectedLocString: String? =
            saveReminderViewModel.reminderSelectedLocationStr.getOrAwaitValue()
        MatcherAssert.assertThat(selectedLocString, Matchers.nullValue())

        val selectedPOI: PointOfInterest? = saveReminderViewModel.selectedPOI.getOrAwaitValue()
        MatcherAssert.assertThat(selectedPOI, Matchers.nullValue())

        val latitude: Double? = saveReminderViewModel.latitude.getOrAwaitValue()
        MatcherAssert.assertThat(latitude, Matchers.nullValue())

        val longitude: Double? = saveReminderViewModel.longitude.getOrAwaitValue()
        MatcherAssert.assertThat(longitude, Matchers.nullValue())
    }

    @Test
    fun testValidateEnteredData_emptyTitle_error() {

        val reminderDataItem = ReminderDataItem("", "", "", 0.0, 0.0)

        // When validateEnteredData called
        saveReminderViewModel.validateEnteredData(reminderDataItem)

        //Then values of all LiveData's associated with viewModel is changed
        val value: Int? = saveReminderViewModel.showSnackBarInt.value
        MatcherAssert.assertThat(value, Matchers.`is`(R.string.err_enter_title))
    }

    @Test
    fun testSaveReminder_correctData_Saved() {

        val reminderDataItem = ReminderDataItem("Title", "Description", "Location", 0.0, 0.0)

        //When saveReminder called with correct data
        saveReminderViewModel.saveReminder(reminderDataItem)

        //Then values of  LiveData's associated with viewModel is changed

        val value: String? = saveReminderViewModel.showToast.value
        MatcherAssert.assertThat(
            value,
            Matchers.`is`(appContext.resources.getString(R.string.reminder_saved))
        )
    }


    @After
    fun tearDown(){
        stopKoin()
    }



}