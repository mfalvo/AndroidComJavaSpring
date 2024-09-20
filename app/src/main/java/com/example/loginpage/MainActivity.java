package com.example.loginpage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText login_input, senha_input;
    Button logar;

    Autenticacao autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_input = findViewById(R.id.edt_login);
        senha_input = findViewById(R.id.edt_senha);
        logar = findViewById(R.id.btn_login);

        autenticacao = new Autenticacao();
    }

    public void login(View v) {
        String login_user = login_input.getText().toString();
        String senha_user = senha_input.getText().toString();

        autenticacao.autenticarUsuario(login_user, senha_user, result -> {
            // Aqui estamos de volta na thread principal usando runOnUiThread
            runOnUiThread(() -> {
                exibirAlerta("Resultado: " + result);

                // Aqui você pode decidir se o login foi bem-sucedido ou não
                if ("true".equals(result)) {
                    Intent abrir_tela = new Intent(MainActivity.this, menu.class);
                    startActivity(abrir_tela);
                    finish();
                } else {
                    exibirAlerta("Falha no login");
                }
            });
        });
    }

    private void exibirAlerta(String mensagem) {
        AlertDialog.Builder cxMsg = new AlertDialog.Builder(this);
        cxMsg.setMessage(mensagem);
        cxMsg.setNeutralButton("OK", null);
        cxMsg.show();
    }
}
