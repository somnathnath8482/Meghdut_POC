package com.meghdutpoc.android;

import static com.meghdutpoc.android.Networking.API_CONSTANT.ASK_PRINT;
import static com.meghdutpoc.android.Networking.API_CONSTANT.LOAD_PRINT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.meghdutpoc.android.Helper.Methodclass;
import com.meghdutpoc.android.databinding.ActivityMainBinding;
import com.meghdutpoc.android.interfaces.AllInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements PrintingCallback {
Printing printing;
ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
Init();
        HashMap<String,String>map = new HashMap<>();
        map.put("retail","4");
        map.put("shop","1");
        Methodclass.callPostForm(map,ASK_PRINT,new AllInterface(){
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
               Success(message);
            }
        },this);
    }

    private void Init() {
        if (printing!=null){
            printing.setPrintingCallback(this);
        }

        binding.connect.setOnClickListener(view -> {
            if (Printooth.INSTANCE.hasPairedPrinter()) {
                Printooth.INSTANCE.removeCurrentPrinter();
            }else{
                startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
                changePairAndUnpair();
            }
        });

        binding.print.setOnClickListener(view -> {
            if (Printooth.INSTANCE.hasPairedPrinter()) {
              PrintText();
            }else{
                startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
            }
        });

        changePairAndUnpair();
    }

    private void PrintText() {

        ArrayList<Printable> printables = new ArrayList<>();
        printables.add(new RawPrintable.Builder(new byte[]{27,100,4}).build());
        printables.add(new TextPrintable.Builder()
                .setText("Message By Somn")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());

        printables.add(new TextPrintable.Builder()
                .setText("Message From SOMNA").setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setEmphasizedMode( DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
                .setNewLinesAfter(1)
                .build());

        printing.print(printables);

    }

    private void changePairAndUnpair() {
        if (Printooth.INSTANCE.hasPairedPrinter()) {
            binding.connect.setText(new StringBuilder("Unpair: ").append(Printooth.INSTANCE.getPairedPrinter().getName()));
        }else{
            binding.connect.setText("Pair with new printer");
        }
    }

    private void Success(String message) {

        String[] res =  message.split("\\|");
        if (res!=null && res.length>0){
            if (res[0].equalsIgnoreCase("0")){
                Methodclass.hasError(MainActivity.this, res[1]);
            }else{
                HashMap<String,String>map = new HashMap<>();
                map.put("retail",res[0]);
                map.put("shop",res[1]);
                map.put("year",res[2]);
                map.put("sale",res[3]);
                callprint(map);
            }
        }

    }


    private void callprint(HashMap<String, String> map){

        Methodclass.callPostForm(map, LOAD_PRINT, new AllInterface(){
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
                Log.e("TAG", "OnSuccess: "+message );
                String[] res =  message.split("\\|");
                if (res!=null && res.length>0){
                    if (res[0].equalsIgnoreCase("0")){
                        Methodclass.hasError(MainActivity.this, res[1]);

                    }else{

                    }
                }

            }
        }, this);

    }


    @Override
    public void connectingWithPrinter() {

        Toast.makeText(MainActivity.this, "Connected with printer", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionFailed(@NonNull String s) {
        Toast.makeText(MainActivity.this, "Connection_failed : "+s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void disconnected() {
        Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(@NonNull String s) {
        Toast.makeText(MainActivity.this, "Error : "+s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMessage(@NonNull String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void printingOrderSentSuccessfully() {
        Toast.makeText(MainActivity.this, "Printing Order Sent Successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK){
            initPrinting();
            changePairAndUnpair();
        }
    }

    private void initPrinting() {
        if (!Printooth.INSTANCE.hasPairedPrinter()){
            printing = Printooth.INSTANCE.printer();

        }
        if (printing!=null){
            printing.setPrintingCallback(this);
        }
    }
}