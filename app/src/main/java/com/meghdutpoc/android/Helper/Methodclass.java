package com.meghdutpoc.android.Helper;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.meghdutpoc.android.Networking.ApiClient;
import com.meghdutpoc.android.Networking.ApiInterface;
import com.meghdutpoc.android.Networking.NetWorkChecker;
import com.meghdutpoc.android.interfaces.AllInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Methodclass {

    public static void callPostForm(HashMap<String,String>map, String url, AllInterface allInterface, Activity activity){

        if (!NetWorkChecker.check(activity))
            return;


        new Thread(new Runnable() {
            @Override
            public void run() {
               activity.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       CustomProgressbar.showProgressBar(activity, false);
                   }
               });

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                Call<String>call =  apiInterface.PostReqForm(url, "",map);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        CustomProgressbar.hideProgressBar();
                        if (response.isSuccessful() && response.code()==200){
                            if (allInterface!=null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        allInterface.OnSuccess(200, response.body());
                                    }
                                });

                            }
                        }else{
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    allInterface.OnSuccess(response.code(), response.message());
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CustomProgressbar.hideProgressBar();
                                Log.e("TAG", "onFailure: "+t.getMessage() );
                            }
                        });

                    }
                });

            }
        }).start();

    }

    public static boolean  hasError(final Activity activity, String error) {

        if (error!=null){

            LottieAlertDialog lottieAlertDialog = new  LottieAlertDialog.Builder(activity, DialogTypes.TYPE_ERROR)
                    .setPositiveButtonColor(Color.parseColor("#048B3A"))
                    .setNegativeButtonColor(Color.parseColor("#DA1606"))
                    .setNoneButtonColor(Color.parseColor("#038DFC"))
                    .setPositiveTextColor(Color.WHITE)
                    .setNegativeTextColor(Color.WHITE)
                    .setNoneTextColor(Color.WHITE)
                    .setTitle("Error")
                    .setDescription(error)
                    .setNoneText("OK")
                    .setNoneListener(new ClickListener() {
                        @Override
                        public void onClick(@NonNull LottieAlertDialog lottieAlertDialog) {
                            // activity.onBackPressed();
                            lottieAlertDialog.dismiss();
                        }
                    })
                    .build();

            lottieAlertDialog.setCancelable(false);
            lottieAlertDialog.setCanceledOnTouchOutside(false);
            lottieAlertDialog.show();


            return false;
        }


        return true;
    }


}
