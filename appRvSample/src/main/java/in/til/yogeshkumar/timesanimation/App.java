package in.til.yogeshkumar.timesanimation;

import android.app.Application;

import in.til.yogeshkumar.timesanimation.Utils.ResourceUtils;

/**
 * Created by Yogesh Kumar.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceUtils.initialize(getApplicationContext());
    }
}
