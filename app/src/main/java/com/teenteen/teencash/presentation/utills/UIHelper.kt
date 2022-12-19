package com.teenteen.teencash.presentation.utills

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teenteen.teencash.R
import com.teenteen.teencash.presentation.extensions.showToast

fun vibrate(context: Context?) {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500 , VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }
}
//TODO: КАКБУДТА МОЖНО НАПИСАТЬ ЛУЧШЕ
fun checkField(dialogFragment: BottomSheetDialogFragment , context: Context , editText1: EditText ,
               editText2: EditText? = null, action: () -> Unit) {
    if (editText1.text.isNotBlank() && editText2 == null) {
        action()
        dialogFragment.dismiss()
    } else if (editText1.text.isNotBlank() && editText2 != null && editText2.text.isNotBlank()) {
        action()
        dialogFragment.dismiss()
    }
    else context.showToast(context.getString(R.string.check_all_data))
}
