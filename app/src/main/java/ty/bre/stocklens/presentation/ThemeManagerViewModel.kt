package ty.bre.stocklens.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ty.bre.stocklens.util.ThemeManager
import javax.inject.Inject

@HiltViewModel
class ThemeManagerViewModel
@Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    fun saveTheme(theme: Int) {
        themeManager.saveTheme(theme)
    }

    fun getTheme(): Int = themeManager.getTheme()

}