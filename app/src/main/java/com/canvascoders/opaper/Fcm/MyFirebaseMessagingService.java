/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canvascoders.opaper.Fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.activity.DashboardActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
//    NotificationBean notificationBean;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
//            Map<String, String> gotData = remoteMessage.getData();
//            Log.e(TAG, "Firebase Message Rec. " + gotData.toString());
//            JSONObject job = new JSONObject(remoteMessage.getData());
//            try {
//                notificationBean = new NotificationBean(job);
//                if (DashboardActivity.dashboardInstance != null) {
//                    DashboardActivity.dashboardInstance.getMessageDetail();
//                    JSONObject jsonObject = new JSONObject(remoteMessage.getData());
//                    notificationBean = new NotificationBean(jsonObject);
//
//                    Log.e("noti", "flag:" + notificationBean.flag);
//                    Log.e("noti", "user_id:" + notificationBean.user_id);
//                    Log.e("noti", "desc:" + notificationBean.Description);
//
//

//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        ShortcutBadger.applyCount(this, 1);
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, DashboardActivity.class);
//        intent.putExtra(Constants.ISFROMPUSHNOTIFICATION, true);
//        intent.putExtra("notification", notificationBean);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
//        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        v.vibrate(500);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.applogo))
                .setContentTitle("OPaper")
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000,1000,1000})
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
//        ShortcutBadger.applyNotification(getApplicationContext(), notification, 1);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
///


        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
