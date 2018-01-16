package tomaszgorecki.simpleweather.inject

import android.app.Activity
import android.content.Context
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(var activity: Activity) {

    @Provides
    @ActivityContext fun activityContext(): Context {
        return activity
    }

    @Provides
    @PerActivity
    fun rxPersmissions(): RxPermissions {
        return RxPermissions(activity)
    }
}