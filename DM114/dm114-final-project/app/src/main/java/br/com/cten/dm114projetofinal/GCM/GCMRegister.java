package br.com.cten.dm114projetofinal.GCM;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GCMRegister {

    private Context context;
    private GoogleCloudMessaging gcm;
    private String regid;

    public GCMRegister(Context context) {

        this.context = context;
        registerBackground();
    }


    @SuppressLint("StaticFieldLeak")
    private void registerBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register("SENDER_ID");
                    //setRegistrationId(regid);
                    msg = regid;

                    updateRegId();

                } catch (IOException ex) {
                    msg = null;
                }
                return msg;
            }
            @Override
            protected void onPostExecute(String registrationID) {

            }
        }.execute(null, null, null);
    }

    private void updateRegId() {
        // update reg ID on server
        RequestsManager.getInstance(context).updateUserRegGCM(regid, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // Toast.makeText(context, "Sucesso ao realizar o registro no GCM", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Falha ao realizar o registro no GCM", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
