package com.hobeez.sourcerise.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hobeez.sourcerise.domain.service.CatService
import com.hobeez.sourcerise.ui.util.BaseViewModel
import com.hobeez.sourcerise.util.extensions.applySchedulers
import com.hobeez.sourcerise.util.network.Loading
import com.hobeez.sourcerise.util.network.Resource
import com.hobeez.sourcerise.util.network.Success
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

const val TEMP_LIMIT_KITTENS = 1
class ExampleViewModel(
    private val context: Context,
    val catService: CatService
) : BaseViewModel() {

    private var _kittenList = MutableLiveData<Resource<List<String>>>()
    val kittenList: LiveData<Resource<List<String>>> = _kittenList

    fun loadKittens() {
        catService.getCats(TEMP_LIMIT_KITTENS)
            .applySchedulers()
            .doOnSubscribe { _kittenList.value = Loading() }
            .subscribe({
                _kittenList.postValue(Success(it))
            }, {
                Timber.e(it)
            }
            ).addTo(disposables)
    }
}
