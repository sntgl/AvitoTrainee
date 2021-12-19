package ru.tagilov.avitotrainee.util

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions

fun NavController.navigateWithParcelable(
    route: String,
    key: String,
    parcelable: Parcelable?,
    builder: NavOptionsBuilder.() -> Unit,
) {
    navigate(route, navOptions(builder))
    currentBackStackEntry?.arguments?.putParcelable(key, parcelable)
}