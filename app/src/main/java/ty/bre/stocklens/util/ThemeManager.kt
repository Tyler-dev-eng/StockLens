package ty.bre.stocklens.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.crypto.AEADBadTagException
import javax.inject.Inject


class ThemeManager
@Inject
constructor(
    @ApplicationContext context: Context,
) {
    companion object {
        private const val PREFERENCES_FILE = "secure_prefs"
        private const val THEME_KEY = "theme"
    }

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = createEncryptedSharedPreferences(context)
    }

    private fun createEncryptedSharedPreferences(context: Context): SharedPreferences =
        try {
            val masterKey =
                MasterKey
                    .Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

            EncryptedSharedPreferences.create(
                context,
                PREFERENCES_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        } catch (e: AEADBadTagException) {
            // Handle corrupted data case by clearing preferences
            context
                .getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            // Retry creating EncryptedSharedPreferences
            val masterKey =
                MasterKey
                    .Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

            EncryptedSharedPreferences.create(
                context,
                PREFERENCES_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        }

    fun saveTheme(theme: Int) {
        sharedPreferences.edit().putInt(THEME_KEY, theme).apply()
    }

    fun getTheme(): Int = sharedPreferences.getInt(THEME_KEY, 0)
}