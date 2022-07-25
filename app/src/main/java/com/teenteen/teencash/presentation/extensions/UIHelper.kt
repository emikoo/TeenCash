package com.teenteen.teencash.presentation.extensions

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import android.view.View
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText

fun View.isGone() {
    this.visibility = View.GONE
}

fun View.isInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible() {
    this.visibility = View.VISIBLE
}

fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun EditText.shakeError() {
    val shake = TranslateAnimation(0f, 10f, 0f, 0f)
    shake.duration = 500
    shake.interpolator = CycleInterpolator(7f)
    this.startAnimation(shake)
}

fun vibrate(context: Context?) {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }
}