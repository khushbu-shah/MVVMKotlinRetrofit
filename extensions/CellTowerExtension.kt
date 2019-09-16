package com.cell_tower.extensions

import android.arch.lifecycle.*
import android.support.v4.app.FragmentActivity

/*
inline fun < reified T : ViewModel> FragmentActivity.getViewModel():T
{
    return ViewModelProviders.of(this)[T::class.java]
}
*/

inline fun <reified T : ViewModel> FragmentActivity.getObserver(body : T.() -> Unit) : T
{
    val viewModel = ViewModelProviders.of(this)[T::class.java]

//    val viewModel = getViewModel<T>()

    viewModel.body()
    return viewModel
}

fun <ld : LiveData<T>, T : Any> LifecycleOwner.getResultFromObserver(liveData: ld,data: (T?) -> Unit)
{
liveData.observe(this,Observer(data))
}