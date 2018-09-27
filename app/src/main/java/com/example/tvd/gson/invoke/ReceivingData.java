package com.example.tvd.gson.invoke;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.tvd.gson.values.LT_Online_Values;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.tvd.gson.values.ConstantValues.LT_ONLINE_SEARCH_FAILURE;
import static com.example.tvd.gson.values.ConstantValues.LT_ONLINE_SEARCH_SUCCESS;

public class ReceivingData {
    private String parseServerXML(String result) {
        String value = "";
        XmlPullParserFactory pullParserFactory;
        InputStream res;
        try {
            res = new ByteArrayInputStream(result.getBytes());
            pullParserFactory = XmlPullParserFactory.newInstance();
            pullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(res, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        switch (name) {
                            case "string":
                                value = parser.nextText();
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    public void get_Lt_Search(String result, Handler handler, ArrayList<LT_Online_Values> arrayList) {
        Log.d("debug", "LT Search: " + result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            LT_Online_Values lt_online_values = new Gson().fromJson(jsonObject.toString(), LT_Online_Values.class);
            arrayList.add(lt_online_values);
            if (StringUtils.equalsIgnoreCase(arrayList.get(0).getMessage(),null)){
                handler.sendEmptyMessage(LT_ONLINE_SEARCH_SUCCESS);
            }else handler.sendEmptyMessage(LT_ONLINE_SEARCH_FAILURE);
            Log.d("debug","CONSNO"+lt_online_values.getCONSID());
            Log.d("debug","MTR_SLNO"+lt_online_values.getMTRSLNO());
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(LT_ONLINE_SEARCH_FAILURE);
        }
    }
}

