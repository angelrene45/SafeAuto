package com.example.safeauto;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessagingService extends FirebaseMessagingService {

    private static final String ALERT = "alert";
    private static final String TAG = "TOKEN";

    public FcmMessagingService() {
    }

    //este metodo recibe la notificacion cuando la app esta en primer plano
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size()>0 && remoteMessage.getData() != null){
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        //gracias a esta variable sabremos el nivel de alerta que tiene
        float alert = Float.valueOf(remoteMessage.getData().get(ALERT));

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(ALERT,alert);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //se crea una nueva instancia en lugar de la actual  si estuviera corriendo

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,
                PendingIntent.FLAG_ONE_SHOT); //flag one shot indica que solo es usara una vez

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //sonido de la notificacion
        Uri uriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //personalizacion de la notificacion
        Notification.Builder notiBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_splash)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true) //se borra la notificacion cuando el usuario toca la notificacion
                .setSound(uriSound)
                .setContentIntent(pendingIntent);

        //dependiendo del nivel de la alerta cambiara el color
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O){
            //hacemos uso de un operador ternario para asignar el color primario o secundario comparando
            //el nivel de notificacion
            notiBuilder.setColor(alert > .4?
                    ContextCompat.getColor(getApplicationContext(),R.color.red): //mayor a 40 porciento rojo
                    ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));//menor azul
        }

        //solo se podra ejecutar si se tiene una version menor a android 8 en este ejemplo
        //En android 8 se necesita un channel id para categorizar las notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Build.VERSION_CODES.O significa version android 8
            String channelId = getString(R.string.normal_channel_id);
            String channelName = getString(R.string.normal_channel_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId,channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100,200,200,50}); //secuencia de miliosegundos que el celular va a vibrar
            if(notificationManager!= null){
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notiBuilder.setChannelId(channelId);
        }

        if(notificationManager != null ){
            notificationManager.notify("",0,notiBuilder.build());
        }
    }

    //entra aqui cuando se genere el token por primera vez, cuando se borre, se desistale, o se borre la instancia de la app
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        sendRegistrationToServer(token);

    }

    private void sendRegistrationToServer(String refreshToken) {
        Log.d(TAG, "new Token: " + refreshToken);
    }
}
