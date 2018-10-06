package br.com.cten.dm114projetofinal.GCM;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import br.com.cten.dm114projetofinal.activity.MainActivity;
import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.model.OrderInfo;
import br.com.cten.dm114projetofinal.model.Product;

public class GcmBroadcastReceiver extends BroadcastReceiver {

    private Context context;
    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);

        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            Bundle extras = intent.getExtras();

            Gson gson = new Gson();
            if (extras.containsKey("orderInfo")) {
                String strOrderInfo = extras.getString("orderInfo");
                if (strOrderInfo != null) {

                    OrderInfo orderInfo = gson.fromJson(strOrderInfo, OrderInfo.class);
                    sendNotification(orderInfo);
                }
            } else if (extras.containsKey("productOfInterest")) {

                String strProductOfInterest = extras.getString("productOfInterest");
                if (strProductOfInterest != null) {

                    Product product = gson.fromJson(strProductOfInterest, Product.class);
                    sendNotification(product);
                }
            }
        }

        setResultCode(Activity.RESULT_OK);
    }

    private void sendNotification(OrderInfo orderInfo) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("orderInfo", orderInfo);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(
                context)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setAutoCancel(true)
                .setContentTitle("Vendas")
                .setStyle(new Notification.BigTextStyle().bigText("Pedido:"
                        + orderInfo.getId() + " - " + orderInfo.getStatus()))
                .setContentText("Pedido:" + orderInfo.getId() + " - " + orderInfo.getStatus());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId("1");
        }

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendNotification(Product product) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("productOfInterest", product);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(
                context)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setAutoCancel(true)
                .setContentTitle("Seu produto de interesse abaixou o preço")
                .setStyle(new Notification.BigTextStyle().bigText("O produto "
                        + product.getCode() + " agora está por " + product.getPrice()))
                .setContentText("O produto "
                        + product.getCode() + " agora está por " + product.getPrice());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId("1");
        }

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
