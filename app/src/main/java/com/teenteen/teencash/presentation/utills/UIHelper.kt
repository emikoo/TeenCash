package com.teenteen.teencash.presentation.utills

import android.content.Context
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teenteen.teencash.R
import com.teenteen.teencash.presentation.extensions.showToast

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
