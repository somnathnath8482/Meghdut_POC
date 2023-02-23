package com.meghdutpoc.android;

import static com.meghdutpoc.android.Networking.API_CONSTANT.ASK_PRINT;
import static com.meghdutpoc.android.Networking.API_CONSTANT.LOAD_PRINT;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.meghdutpoc.android.Helper.Methodclass;
import com.meghdutpoc.android.databinding.ActivityMainBinding;
import com.meghdutpoc.android.interfaces.AllInterface;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HashMap<String, String> map = new HashMap<>();
        map.put("retail", "4");
        map.put("shop", "1");
        Methodclass.callPostForm(map, ASK_PRINT, new AllInterface() {
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
                Success(message);
            }
        }, this);
    }


    private void Success(String message) {

        String[] res = message.split("\\|");
        if (res != null && res.length > 0) {
            if (res[0].equalsIgnoreCase("0")) {
                Methodclass.hasError(MainActivity.this, res[1]);
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put("retail", res[0]);
                map.put("shop", res[1]);
                map.put("year", res[2]);
                map.put("sale", res[3]);
                callprint(map);
            }
        }

    }


    private void callprint(HashMap<String, String> map) {

        Methodclass.callPostForm(map, LOAD_PRINT, new AllInterface() {
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
                Log.e("TAG", "OnSuccess: " + message);
                String[] res = message.split("\\|");
                if (res != null && res.length > 0) {
                    if (res[0].equalsIgnoreCase("0")) {
                        Methodclass.hasError(MainActivity.this, res[1]);

                    } else {

                    }
                }

            }
        }, this);

    }


}