package com.teenteen.teencash.presentation.extensions

import android.content.Context
import android.widget.Toast
import com.teenteen.teencash.R

//Зачем создавать эктеншин и забивать в него строку, это странное расширение
/*
    А что будет если нужно типовые сообщения вывести, тут тип для инета,
    а если в другом месте нужно тоже похожее вызывать, то снова экстенш создавать?
*/
fun Context.showNoConnectionToast() {
    Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
}
//TODO: Если не используете может удалить? [3]
//Зачем создавать эктеншин и забивать в него строку, это странное расширение
fun Context.showItemExistsToast() {
    Toast.makeText(this , getString(R.string.item_exists) , Toast.LENGTH_LONG).show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this , text , Toast.LENGTH_SHORT).show()
}