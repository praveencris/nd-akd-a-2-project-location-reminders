package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.MainCoroutineRule
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.FakeRemindersLocalRepository
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : KoinTest {// Extended Koin Test - embed autoclose @after method to close Koin after every test

//    TODO: DONE test the navigation of the fragments.
//    TODO: DONE test the displayed data on the UI.
//    TODO: DONE add testing for the error messages.

    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { FakeRemindersLocalRepository() as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
        //Get our real repository
        repository = get()

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }

    @After
    fun after() {
        stopKoin()
    }



    @Test
    fun fabAddPressed_NavigateToSaveFragment() = runBlockingTest {

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        //Use Mockito's mock function to create a mock
        val navController = mock(NavController::class.java)

        //Make your new mock the fragment's NavController
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the first FAB add Navigate to SaveFragment
        onView(withId(R.id.addReminderFAB))
            .perform(click())


        //THEN - Verify that we navigate to SaveFragment Screen
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
    }

    @Test
    fun openReminderListFragment_checkReminders() = runBlockingTest {

        //GIVEN - reminder is saved in repo
        repository.saveReminder(
            ReminderDTO("Title1", "Description1", "Location1", 0.0, 0.0)
        )
        repository.saveReminder(
            ReminderDTO("Title2", "Description2", "Location1", 0.0, 0.0)
        )

        // WHEN- UI is launched
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        // THEN - Task details are displayed on the screen
        // make sure that the title/description are both shown and correct

        // Attempt to scroll to an item that contains the special text.
        onView(ViewMatchers.withId(R.id.reminderssRecyclerView))
            .perform(
                // scrollTo will fail the test if no item matches.
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Title1"))
                )
            )

    }


    @Test
    fun saveReminder_errorOnGetReminder() = runBlockingTest {
        //GIVEN - reminder is saved in repo
        val reminder1 = ReminderDTO("Title1", "Description1", "Location1", 0.0, 0.0)
        repository.saveReminder(reminder1)
        val reminder2 = ReminderDTO("Title2", "Description2", "Location2", 0.0, 0.0)
        repository.saveReminder(reminder2)
        (repository as FakeRemindersLocalRepository).setReturnError(true)
        // WHEN- UI is launched


        val error = repository.getReminder(reminder1.id) as Result.Error

        assertThat(error.message, equalTo("Error Occurred"))
        // THEN - Task details are displayed on the screen
        // make sure that the title/description are both shown and correct

        // Attempt to scroll to an item that contains the special text.


    }


}