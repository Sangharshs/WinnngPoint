package com.scratchandwin.sratchwin;

import android.content.Context;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class Application extends android.app.Application {
    public static Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext=this;

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler
    {
        @Override
        public void notificationOpened(OSNotificationOpenResult result)
        {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;



        }
    }
}
