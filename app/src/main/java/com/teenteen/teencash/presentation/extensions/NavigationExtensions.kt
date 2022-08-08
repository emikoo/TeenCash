package com.example.singleactivity

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.teenteen.teencash.R

fun Fragment.activityNavController() = requireActivity().findNavController(R.id.nav_host_fragment)

fun NavController.navigateSafely(@IdRes actionId: Int) {
    currentDestination?.getAction(actionId)?.let { navigate(actionId) }
}

fun NavController.navigateSafely(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
}

fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP , this.toFloat() , context.resources.displayMetrics
).toInt()

fun EditText.shakeError() {
    val shake = TranslateAnimation(0f , 10f , 0f , 0f)
    shake.duration = 500
    shake.interpolator = CycleInterpolator(7f)
    this.startAnimation(shake)
}