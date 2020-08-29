package com.shouman.apps.thirdwayv.calac.executors

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * A Singleton class for executing operation in background thread.
 * @param diskIO : A singleThreadExecutors
 */
class AppExecutors private constructor(
    val diskIO: Executor,
) {

    companion object {
        private val LOCK = Any()
        private var sInstance: AppExecutors? = null
        fun getsInstance(): AppExecutors {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = AppExecutors(
                        Executors.newSingleThreadExecutor()
                    )
                }
            }
            return sInstance!!
        }
    }
}
