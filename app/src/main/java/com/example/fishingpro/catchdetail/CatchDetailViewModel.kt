package com.example.fishingpro.catchdetail

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.domain.LocalDailyCatch
import javax.inject.Inject

class CatchDetailViewModel @ViewModelInject constructor(
    @Assisted val catchData: SavedStateHandle
) : ViewModel(){

    companion object {
        val TAG = CatchDetailViewModel::class.java.simpleName
    }

    private val _catchInfo = MutableLiveData<LocalDailyCatch>()
    val catchInfo: LiveData<LocalDailyCatch>
        get() = _catchInfo

    init {
        Log.d(TAG, catchData.get<LocalDailyCatch>("catchData").toString())
        _catchInfo.value = catchData.get<LocalDailyCatch>("catchData")
    }
}