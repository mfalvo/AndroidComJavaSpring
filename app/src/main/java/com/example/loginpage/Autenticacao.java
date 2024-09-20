package com.example.loginpage;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Autenticacao {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Agora o método recebe uma callback para lidar com o resultado assíncrono
    public void autenticarUsuario(final String login, final String senha, final AutenticacaoCallback callback) {
        executorService.execute(() -> {
            try {
                String urlCompleta = "http://10.0.2.2:8080/usuarios/login" +
                        "?login=" + login +
                        "&senha=" + senha;

                Log.d("Autenticacao", "URL gerada: " + urlCompleta);

                URL url = new URL(urlCompleta);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                Log.d("Autenticacao", "Código de resposta: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBody = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBody.append(line);
                    }
                    reader.close();

                    String response = responseBody.toString();
                    Log.d("Autenticacao", "Corpo da resposta: " + response);

                    // Chama o callback com o resultado
                    callback.onAutenticacaoResult(response);
                } else {
                    Log.d("Autenticacao", "Falha na autenticação, código: " + responseCode +
                            ", mensagem: " + connection.getResponseMessage());
                    callback.onAutenticacaoResult("false");
                }
            } catch (Exception e) {
                Log.e("Autenticacao", "Erro ao fazer a requisição: " + e.getMessage(), e);
                callback.onAutenticacaoResult("false");
            }
        });
    }

    // Interface para tratar o resultado de forma assíncrona
    public interface AutenticacaoCallback {
        void onAutenticacaoResult(String result);
    }
}
