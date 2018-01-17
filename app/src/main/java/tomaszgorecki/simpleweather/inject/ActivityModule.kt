package tomaszgorecki.simpleweather.inject

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private var activity: AppCompatActivity) {

    @Provides
    @ActivityContext fun activityContext(): Context {
        return activity
    }

    @Provides
    fun activity(): Activity {
        return activity
    }

    @Provides
    fun appCompatActivity(): AppCompatActivity {
        return activity
    }


    @Provides
    fun supportFragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    @PerActivity
    fun rxPermissions(): RxPermissions {
        return RxPermissions(activity)
    }
}