package com.ctacek.yandexschool.doitnow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import com.ctacek.yandexschool.doitnow.ui.fragment.login.LoginViewModel
import com.ctacek.yandexschool.doitnow.ui.fragment.main.MainViewModel
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.ManageTaskViewModel
import com.ctacek.yandexschool.doitnow.ui.fragment.settings.SettingsFragment
import com.ctacek.yandexschool.doitnow.utils.internetchecker.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val repositoryImpl: ToDoItemsRepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver,
    private val coroutineScope: CoroutineScope
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> MainViewModel(
                repositoryImpl, connectivityObserver, coroutineScope
            )

            LoginViewModel::class.java ->
                LoginViewModel(repositoryImpl, coroutineScope)

            ManageTaskViewModel::class.java ->
                ManageTaskViewModel(repositoryImpl, coroutineScope)

            SettingsFragment::class.java ->
                MainViewModel(
                    repositoryImpl, connectivityObserver, coroutineScope
                )

            else -> {
                error("Unknown view model class")
            }
        }
        return viewModel as T
    }
}
