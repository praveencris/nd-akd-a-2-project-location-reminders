package com.udacity.project4.locationreminders.data.local

import androidx.lifecycle.MutableLiveData
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.util.LinkedHashMap

class FakeRemindersLocalRepository:RemindersRepositoryInterface {

    var reminderServiceData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if(shouldReturnError){
           return Result.Error("Error Occurred")
        }
        return Result.Success(reminderServiceData.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderServiceData[reminder.id]=reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if(shouldReturnError){
          return  Result.Error("Error Occurred")
        }
         reminderServiceData[id]?.let {
           return Result.Success<ReminderDTO>(it)
        }
        return Result.Error("Reminder not found!")
    }

    override suspend fun deleteAllReminders() {
        reminderServiceData.clear()
    }
}