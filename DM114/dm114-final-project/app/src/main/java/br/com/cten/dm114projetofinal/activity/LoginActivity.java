package br.com.cten.dm114projetofinal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.Token;
import br.com.cten.dm114projetofinal.model.UserCredentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText txtLogin;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        Button btnEnter = findViewById(R.id.btnEnter);

        btnEnter.setOnClickListener(v -> {

            if(txtLogin == null || txtPassword == null
                    || txtLogin.getText() == null || txtLogin.getText().toString().isEmpty()
                    || txtPassword.getText() == null || txtPassword.getText().toString().isEmpty()) {

                Toast.makeText(this, "Dados inválidos.", Toast.LENGTH_SHORT).show();
            } else {

                // Validar dados
                final String login = txtLogin.getText().toString().trim();
                final String password = txtPassword.getText().toString().trim();

                storeUserCredentials(login, password);
                getSalesAccessToken();
            }
        });

        checkUserLogin();
    }

    private void checkUserLogin() {
        UserCredentials user = getUserCredentialsStored();
        if(user != null) {

            txtLogin.setText(user.getLogin());
            txtPassword.setText(user.getPassword());

            setProgressDialog();

            getSalesAccessToken();
        }
    }

    private void setProgressDialog() {
        ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "",
                "Realizando login. Por favor aguarde", true);
        progressDialog.setCancelable(false);
    }

    private void getSalesAccessToken() {
        RequestsManager.getInstance(this).getSalesAccessToken(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                if(response != null && response.body() != null) {
                    storeSalesAccessToken(response.body());
                    getMessageAccessToken();
                } else {
                    clearPreferences();
                    Toast.makeText(LoginActivity.this, "Falha ao requisitar token de acesso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                clearPreferences();
                Toast.makeText(LoginActivity.this, "Falha ao requisitar token de acesso", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMessageAccessToken() {
        RequestsManager.getInstance(this).getMessageAccessToken(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                if(response != null && response.body() != null) {

                    storeMessageAccessToken(response.body());
                    goToMainScreen();

                } else {
                    clearPreferences();
                    Toast.makeText(LoginActivity.this, "Falha ao requisitar token de acesso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                clearPreferences();
                Toast.makeText(LoginActivity.this, "Falha ao requisitar token de acesso", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void storeUserCredentials(String login, String password) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE).edit();
        editor.putString("login", login);
        editor.putString("password", password);
        editor.apply();
    }

    private void storeSalesAccessToken(Token token) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE).edit();

        long expireTime = Calendar.getInstance().getTime().getTime() + token.getExpiresIn() * 1000;

        editor.putString("salesAccessToken", token.getAccessToken());
        editor.putLong("salesTokenExpiresIn", expireTime);
        editor.apply();
    }

    private void storeMessageAccessToken(Token token) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE).edit();

        long expireTime = Calendar.getInstance().getTime().getTime() + token.getExpiresIn() * 1000;

        editor.putString("messageAccessToken", token.getAccessToken());
        editor.putLong("messageTokenExpiresIn", expireTime);
        editor.apply();
    }

    private void clearPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    private UserCredentials getUserCredentialsStored() {
        SharedPreferences preferences = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE);
        String login = preferences.getString("login", null);
        String password = preferences.getString("password", null);

        UserCredentials user = null;
        if(login != null && password != null) {
            user = new UserCredentials();
            user.setLogin(login);
            user.setPassword(password);
        }

        return user;
    }

    private boolean isSalesAccessTokenValid() {

        SharedPreferences preferences = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE);
        String salesAccessToken = preferences.getString("salesAccessToken", null);
        long salesTokenExpiresIn = preferences.getLong("salesTokenExpiresIn", 0L);

        return salesAccessToken != null
                && Calendar.getInstance().getTime().getTime() < salesTokenExpiresIn;
    }

    private boolean isMessageAccessTokenValid() {

        SharedPreferences preferences = getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE);
        String messageAccessToken = preferences.getString("messageAccessToken", null);
        long messageTokenExpiresIn = preferences.getLong("messageTokenExpiresIn", 0L);

        return messageAccessToken != null
                && Calendar.getInstance().getTime().getTime() < messageTokenExpiresIn;
    }
}
