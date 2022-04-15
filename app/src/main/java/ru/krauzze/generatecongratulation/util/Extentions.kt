package ru.krauzze.generatecongratulation.util

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.krauzze.generatecongratulation.GenerateCongratulationApplication.Companion.APP_SCOPE
import ru.krauzze.generatecongratulation.di.ViewModelFactory
import ru.krauzze.generatecongratulation.util.DarkenColorLabel.getDarkenColor
import toothpick.Toothpick
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private inline fun <T> SharedPreferences.delegate(
    defaultValue: T,
    key: String? = null,
    crossinline getter: SharedPreferences.(String, T) -> T,
    crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, defaultValue)!!

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
            edit().setter(key ?: property.name, value).apply()
    }

fun SharedPreferences.int(def: Int = 0, key: String? = null): ReadWriteProperty<Any, Int> =
    delegate(def, key, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

fun SharedPreferences.long(def: Long = 0, key: String? = null): ReadWriteProperty<Any, Long> =
    delegate(def, key, SharedPreferences::getLong, SharedPreferences.Editor::putLong)

fun SharedPreferences.float(
    def: Float = 0f,
    key: String? = null
): ReadWriteProperty<Any, Float> =
    delegate(def, key, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)

fun SharedPreferences.boolean(
    def: Boolean = false,
    key: String? = null
): ReadWriteProperty<Any, Boolean> =
    delegate(def, key, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)

fun SharedPreferences.string(
    def: String = "",
    key: String? = null
): ReadWriteProperty<Any, String?> =
    delegate(def, key, SharedPreferences::getString, SharedPreferences.Editor::putString)

fun SharedPreferences.stringSet(
    def: MutableSet<String> = mutableSetOf(),
    key: String? = null
): ReadWriteProperty<Any, MutableSet<String>?> =
    delegate(def, key, SharedPreferences::getStringSet, SharedPreferences.Editor::putStringSet)

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun <T : ViewModel> ComponentActivity.obtainViewModel(viewModelClass: Class<T>): T {
    println("======================== ${this}")
    return ViewModelProvider(
        this,
        Toothpick.openScope(APP_SCOPE).getInstance(ViewModelFactory::class.java)
    )[viewModelClass]
}

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is Boolean -> putBoolean(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Short -> putShort(key, value)
        is Long -> putLong(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun <T : Any> argument(): ReadWriteProperty<Fragment, T> = FragmentArgumentDelegate()

fun <T : Any> argumentNullable(): ReadWriteProperty<Fragment, T?> =
    FragmentNullableArgumentDelegate()

fun Activity.showKeyboard(view: View?) {
    view?.let {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

//fun Context.showAlert(button: Int, title: Int, message: Int, action: (() -> Unit)? = null) {
//    AlertDialog.Builder(this, R.style.AlertDialogTheme)
//        .setTitle(this.getString(title))
//        .setMessage(this.getString(message))
//        .setPositiveButton(this.getString(button)) { dialog, _ ->
//            action?.invoke()
//            dialog.dismiss()
//        }
//        .show()
//}
//
//
//fun Context.showQuestionAlert(
//    buttonNegative: Int,
//    buttonPositive: Int,
//    message: Int,
//    action: (() -> Unit)? = null
//) {
//    AlertDialog.Builder(this, R.style.AlertDialogTheme)
//        .setMessage(this.getString(message))
//        .setPositiveButton(this.getString(buttonPositive)) { dialog, _ ->
//            action?.invoke()
//            dialog.dismiss()
//        }
//        .setNegativeButton(this.getString(buttonNegative)) { dialog, _ ->
//            dialog.dismiss()
//        }
//        .show()
//}
//
//fun Context.showAlertNoTitle(button: Int, message: Int, action: (() -> Unit)? = null) {
//    AlertDialog.Builder(this, R.style.AlertDialogTheme)
//        .setMessage(this.getString(message))
//        .setPositiveButton(this.getString(button)) { dialog, _ ->
//            action?.invoke()
//            dialog.dismiss()
//        }
//        .show()
//}

fun postDelayed(action: () -> Unit, time: Long) {
    Handler(Looper.myLooper() ?: throw Exception("Looper must not be null")).postDelayed(
        action,
        time
    )
}

fun <A, B> Map<A, B>.getIfExist(key: A, defaultValue: B? = null): B? {
    return if (this.containsKey(key)) {
        this[key] ?: defaultValue
    } else {
        defaultValue
    }
}

/**
 * Get darken color label for light theme
 */
object DarkenColorLabel {
    private var value: Color? = null
    fun getDarkenColor(color: Color) = value ?: run { value = color.darkenColor(0.73f); value!! }
}

/**
 * Custom colors for material 3 theme for material 2
 */
@Composable
fun MaterialTheme.textFieldColors(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
): TextFieldColors {
    val labelColor = if (useDarkTheme) colors.primaryVariant else getDarkenColor(colors.primary)
    return TextFieldDefaults.textFieldColors(
        backgroundColor = colors.surface,
        textColor = colors.onSurface,
        cursorColor = colors.onSurface,
        focusedLabelColor = labelColor,
        unfocusedLabelColor = labelColor,
        focusedIndicatorColor = labelColor,
    )
}

/**
 * Darken the color
 */
fun Color.darkenColor(value: Float = 0.3f): Color {
    return Color(
        android.graphics.Color.HSVToColor(
            FloatArray(3).apply {
                android.graphics.Color.colorToHSV(this@darkenColor.toArgb(), this)
                this[2] *= value
            }
        )
    )
}

fun String.replaceSupportCase(oldValue: String, newVal: String): String {
    run {
        var newValue = newVal
        var occurrenceIndex: Int = indexOf(oldValue, 0)
        // FAST PATH: no match
        if (occurrenceIndex < 0) {
            occurrenceIndex = indexOf(oldValue.replaceFirstChar { it.uppercase() }, 0)
            newValue = newValue.replaceFirstChar { it.uppercase() }
        }
        if (occurrenceIndex < 0) return this

        val oldValueLength = oldValue.length
        val searchStep = oldValueLength.coerceAtLeast(1)
        val newLengthHint = length - oldValueLength + newValue.length
        if (newLengthHint < 0) throw OutOfMemoryError()
        val stringBuilder = StringBuilder(newLengthHint)

        var i = 0
        do {
            stringBuilder.append(this, i, occurrenceIndex).append(newValue)
            i = occurrenceIndex + oldValueLength
            if (occurrenceIndex >= length) break
            occurrenceIndex = indexOf(oldValue, occurrenceIndex + searchStep)
        } while (occurrenceIndex > 0)
        return stringBuilder.append(this, i, length).toString()
    }
}

