package com.stocard.coolchat.ui.profile

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.graphics.Color
import com.stocard.coolchat.ServiceLocator
import kotlin.math.absoluteValue

class ProfileViewModel(context: Application) : AndroidViewModel(context) {

    private val chatRepository = ServiceLocator.get(context).chatRepository
    val name: MutableLiveData<String> = MutableLiveData()
    val color: LiveData<Int> = Transformations.map(name) { name ->
        val nameHash = name?.hashCode()?.absoluteValue ?: 0
        when (nameHash % 5) {
            0 -> Color.GREEN
            1 -> Color.BLUE
            2 -> Color.MAGENTA
            3 -> Color.CYAN
            4 -> Color.RED
            else -> Color.BLACK
        }
    }

    init {
        name.postValue(chatRepository.nameState().value)
    }

    fun save() {
        name.value?.let { chatRepository.setName(it) }
    }
}