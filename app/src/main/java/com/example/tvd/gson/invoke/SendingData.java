package com.example.tvd.gson.invoke;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.tvd.gson.values.LT_Online_Values;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.example.tvd.gson.values.ConstantValues.API;

public class SendingData {
    private ReceivingData receivingData = new ReceivingData();
    private String UrlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        String response;
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(30000);
        conn.setConnectTimeout(30000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(datamap));
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
            response = responseBuilder.toString();
        } else response = "";
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    private String UrlGetConnection(String Get_Url) throws IOException {
        String response;
        URL url = new URL(Get_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(30000);
        conn.setConnectTimeout(30000);
        int responseCode=conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line=br.readLine()) != null) {
                responseBuilder.append(line);
            }
            response = responseBuilder.toString();
        }
        else response="";
        return response;
    }

    //LT_bill_Online_search
    @SuppressLint("StaticFieldLeak")
    public class LT_bill_Online_search extends AsyncTask<String, String, String> {
        String response="";
        Handler handler;
        ArrayList<LT_Online_Values> arrayList;
        public LT_bill_Online_search(Handler handler, ArrayList<LT_Online_Values> arrayList) {
            this.handler = handler;
            this.arrayList = arrayList;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("Account_ID", strings[0]);
            datamap.put("SubDivCode", strings[1]);
            try {
                response = UrlPostConnection(API,datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.get_Lt_Search(result,handler,arrayList);
        }
    }
}
