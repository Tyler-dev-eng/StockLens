package ty.bre.stocklens.util

/**
 * A sealed class representing the state of a resource, used for managing
 * data fetching operations and handling their success, error, and loading states.
 *
 * @param T The type of data associated with the resource.
 * @property data The optional data being passed with the resource.
 * @property message An optional message, typically used for error descriptions.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true): Resource<T>(null)
}