package com.facebook.android;

import com.facebook.android.framework.DialogError;
import com.facebook.android.framework.FacebookError;
import com.facebook.android.framework.Facebook.DialogListener;

/**
 * Skeleton base class for RequestListeners, providing default error 
 * handling. Applications should handle these error conditions.
 *
 */
public abstract class BaseDialogListener implements DialogListener {

    public void onFacebookError(FacebookError e) {
        e.printStackTrace();
    }

    public void onError(DialogError e) {
        e.printStackTrace();        
    }

    public void onCancel() {        
    }
    
}
