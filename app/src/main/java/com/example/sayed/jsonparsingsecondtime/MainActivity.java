
package com.example.sayed.jsonparsingsecondtime;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
    private final String FLOWER_URL = "http://services.hanselandpetal.com/feeds/flowers.json";
    private TextView flowerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowerTV = findViewById(R.id.flower_name);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info.isAvailable() && info.isConnected()){
            new MyJsonDownloadTask().execute(FLOWER_URL);
        }else {
            Toast.makeText(this, "Connect To your Network Please", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyJsonDownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String ...params) {
            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }

                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray jsonArray = new JSONArray(s);
                flowerTV.setText("");

                for (int i = 0; i< jsonArray.length(); i++){
                    JSONObject flowerObj = jsonArray.getJSONObject(i);
                    String flowerName = flowerObj.getString("name");

                    flowerTV.setText(flowerTV.getText()+"\n"+flowerName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
