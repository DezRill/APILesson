package com.dezrill.apilesson;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText userField;
    private TextView resultInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userField=findViewById(R.id.userField);
        Button mainBtn = findViewById(R.id.mainBtn);
        resultInfo=findViewById(R.id.resultInfo);

        mainBtn.setOnClickListener(v -> {
            if (userField.getText().toString().trim().equals("")) Toast.makeText(this, "Введите текст", Toast.LENGTH_SHORT).show();
            else {
                String city=userField.getText().toString();
                String key="94326013cdc5eb092dba295762273385";
                String url="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key+"&units=metric&lang=ru";

                new GetUrlData().execute(url);
            }
        });
    }

    private class GetUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute () {
            super.onPreExecute();
            resultInfo.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection=null;
            BufferedReader reader=null;

            try {
                URL url=new URL(strings[0]);
                connection=(HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream=connection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));

                StringBuffer stringBuffer=new StringBuffer();
                String line="";

                while ((line=reader.readLine())!=null)
                    stringBuffer.append(line).append("\n");
                return stringBuffer.toString();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (connection!=null) connection.disconnect();
                try {
                    if (reader!=null)
                        reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject obj=new JSONObject(result);
                resultInfo.setText("Температура: " + obj.getJSONObject("main").getDouble("temp"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}