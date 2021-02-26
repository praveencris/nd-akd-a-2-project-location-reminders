package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.dto.Result.Success
import java.lang.Exception

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders?.let {
            return Success(ArrayList(it))
        }
        return Result.Error(
            "Reminders Not Found"
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        //TODO("save the reminder")
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        //TODO("return the reminder with the id")
        reminders?.let {
            for(reminder in it){
               if(id == reminder.id) {
                   return Success(reminder)
               }
            }
        }
        return Result.Error("Reminder Not Found")
    }

    override suspend fun deleteAllReminders() {
        //TODO("delete all the reminders")
        reminders?.clear()
    }


}