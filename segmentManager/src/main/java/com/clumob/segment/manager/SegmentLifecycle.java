package com.clumob.segment.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by prashant.rathore on 08/07/18.
 */

public interface SegmentLifecycle {

    public void onCreate(@Nullable Bundle savedInstance);
    public void onStart();
    public void onResume();
    public void onPause();
    public void onSaveInstanceState(Bundle outBundle);
    public void onStop();
    public void onDestroy();
    public boolean handleBackPressed();

}
