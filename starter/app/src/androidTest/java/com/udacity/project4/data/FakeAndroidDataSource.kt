package com.udacity.project4.data

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeAndroidDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

//    TODO: DONE Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders?.let {
            return Result.Success(ArrayList(it))
        }
        return Result.Error(
            "Reminders Not Found"
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders?.let {
            for (reminder in it) {
                if (id == reminder.id) {
                    return Result.Success(reminder)
                }
            }
        }
        return Result.Error("Reminder Not Found")
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}