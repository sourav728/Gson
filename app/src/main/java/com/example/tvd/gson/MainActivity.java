package com.example.tvd.gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.gson.invoke.SendingData;
import com.example.tvd.gson.values.FunctionCall;
import com.example.tvd.gson.values.LT_Online_Values;

import java.util.ArrayList;

import static com.example.tvd.gson.values.ConstantValues.LT_ONLINE_SEARCH_FAILURE;
import static com.example.tvd.gson.values.ConstantValues.LT_ONLINE_SEARCH_SUCCESS;

public class MainActivity extends Activity {
    SendingData sendingData;
    EditText acc_id, subdiv_code;
    LT_Online_Values getsetvalues;
    Button search;
    ArrayList<LT_Online_Values> arrayList;
    ProgressDialog progressDialog;
    FunctionCall functionCall;
    private static Handler handler = null;

    {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {

                    case LT_ONLINE_SEARCH_SUCCESS:
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Search Success..", Toast.LENGTH_SHORT).show();
                        Log.d("debug", "In Main Activity " + arrayList.get(0).getMTRSLNO());
                        break;
                    case LT_ONLINE_SEARCH_FAILURE:
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Search Failure!!", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendingData = new SendingData();
        arrayList = new ArrayList<LT_Online_Values>();
        functionCall = new FunctionCall();
        //progressDialog = new ProgressDialog(MainActivity.this);
        getsetvalues = new LT_Online_Values();
        acc_id = findViewById(R.id.et_account_id);
        subdiv_code = findViewById(R.id.et_online_bill_subdiv);
        search = findViewById(R.id.online_bt_search);
        subdiv_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    if (functionCall.isInternetOn(MainActivity.this))
                        search_details();
                    else Toast.makeText(MainActivity.this, "Please Turn on Internet!!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_details();
            }
        });

    }

    private void search_details() {
        if (functionCall.isInternetOn(MainActivity.this)) {
            arrayList.clear();
            progressDialog = new ProgressDialog(MainActivity.this, R.style.MyProgressDialogstyle);
            progressDialog.setTitle("Connecting To Server");
            progressDialog.setMessage("Please Wait..");
            progressDialog.show();
            SendingData.LT_bill_Online_search lt_bill_online_search = sendingData.new LT_bill_Online_search(handler, arrayList);
            lt_bill_online_search.execute(acc_id.getText().toString(), subdiv_code.getText().toString());
        } else Toast.makeText(this, "Please Turn on Internet!! ", Toast.LENGTH_SHORT).show();

    }
}
