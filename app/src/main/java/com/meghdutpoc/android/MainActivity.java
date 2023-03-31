package com.meghdutpoc.android;

import static com.meghdutpoc.android.Networking.API_CONSTANT.ASK_PRINT;
import static com.meghdutpoc.android.Networking.API_CONSTANT.LOAD_BILL;
import static com.meghdutpoc.android.Networking.API_CONSTANT.LOAD_KOT;
import static com.meghdutpoc.android.Networking.API_CONSTANT.LOAD_PRINT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.meghdutpoc.android.Helper.Mdel.BillItem;
import com.meghdutpoc.android.Helper.Mdel.KotItem;
import com.meghdutpoc.android.Helper.Methodclass;
import com.meghdutpoc.android.async.AsyncBluetoothEscPosPrint;
import com.meghdutpoc.android.async.AsyncEscPosPrint;
import com.meghdutpoc.android.async.AsyncEscPosPrinter;
import com.meghdutpoc.android.databinding.ActivityMainBinding;
import com.meghdutpoc.android.interfaces.AllInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;
    private BluetoothConnection selectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        browseBluetoothDevice();
        HashMap<String, String> map = new HashMap<>();
        map.put("retail", "4");
        map.put("shop", "1");
        /*Methodclass.callPostForm(map, ASK_PRINT, new AllInterface() {
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
                SuccessINV(message);
            }
        }, this);*/
        /*Methodclass.callPostForm(map, ASK_KOT, new AllInterface() {
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
                SuccessKOT(message);
            }
        }, this);*/
    /*    Methodclass.callPostForm(map, ASk_BILL, new AllInterface() {
            @Override
            public void OnSuccess(int code, String message) {
                 super.OnSuccess(code, message);
                SuccessBill(message);
            }
        }, this);
*/
        binding.BILL.setOnClickListener(view -> {
            String message = "1[SEP]MHUB|204 LAKETOWN||ADMIN|9874842193||15-03-2023|08:37:55 pm|270|0|27|27|243|ABCDEFGH1234|9748421938@kotak.jpg[SEP]|1~Noodles Chilli Garlic ~10010070002~1~Plate~80~0~80|2~Burger Veggie Cheese ~10010110002~1~Pc~90~0~90|3~Crispy Wrap Chiken~10020010002~1~Pc~100~0~100[SEP]1[SEP]1";
            String[] res = message.split("\\[SEP]");
            String[] header = res[1].split("\\|");
            String[] rec_str = res[2].split("\\|");
            String invCnt = res[3];
            String chalanCnt = res[4];

            List<BillItem> list = new ArrayList<>();
            for (int i = 0; i < rec_str.length; i++) {
                String[] item = rec_str[i].split("~");
                try {
                    list.add(new BillItem(item[0],
                            item[1],
                            item[2],
                            item[3]+" "+item[4],
                            item[5],
                            item[7]
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            AsyncEscPosPrinter aa = formate_BILL(res, header, list, invCnt, chalanCnt);
           printBluetooth(aa);
        });

        binding.kot.setOnClickListener(view -> {
            String message  ="1[SEP]MHUB|204 LAKETOWN||ADMIN|9874842193||16-03-2023|08:06 pm|T-1[SEP][SEP]|1~Noodles Chilli Garlic ~1~Plate|2~Burger Veggie Cheese ~1~Pc|3~Crispy Wrap Chiken~1~Pc[SEP]1[SEP]1";
            String[] res = message.split("\\[SEP]");


            if (res != null && res.length > 0) {
                if (res[0].equalsIgnoreCase("0")) {
                    // Methodclass.hasError(MainActivity.this, res[1]);

                } else {
                    String[] header = res[1].split("\\|");
                    String[] rec_str_old = res[2].split("\\|");
                    String[] rec_str_new = res[3].split("\\|");
                    String invCnt = res[4];
                    String chalanCnt = res[5];
                    List<KotItem> list = new ArrayList<>();
                    for (int i = 0; i < rec_str_new.length; i++) {
                        String[] item = rec_str_new[i].split("~");
                        try {
                            list.add(new KotItem(item[0],item[1],item[2]+" "+item[3]));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    AsyncEscPosPrinter aa = formate_KOT(res, header, list, invCnt, chalanCnt);
                    printBluetooth(aa);
                }
            }

        });





        binding.INVOICE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message  = "1[SEP]MHUB|204 LAKETOWN||ADMIN|NI||MHUB-2023-321|10-03-2023|12:49:44 pm|40|0|0||0|0|0|40|0|40|0|0|0|CASH|ABCDEFGH1234|9748421938@kotak.jpg[SEP]|1~S~   Lemon Tea full~20010010004~4~Cup~10~0~40~0~40~0~0~0~0~0~0~0~0[SEP]1[SEP]1";
                String[] res = message.split("\\|");


                if (res != null && res.length > 0) {
                    if (res[0].equalsIgnoreCase("0")) {
                        // Methodclass.hasError(MainActivity.this, res[1]);

                    } else {
                        res = message.split("\\[SEP]");
                        String[] header = res[1].split("\\|");
                        String[] rec_str = res[2].split("\\|");
                        String invCnt = res[3];
                        String chalanCnt = res[4];

                        List<BillItem> list = new ArrayList<>();
                        for (int i = 0; i < rec_str.length; i++) {
                            String[] item = rec_str[i].split("~");
                            try {
                                list.add(new BillItem(item[0],
                                        item[1],
                                        item[2],
                                        item[3]+" "+item[4],
                                        item[5],
                                        item[7]
                                ));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        AsyncEscPosPrinter aa = formate_Invoice(res, header, rec_str, invCnt, chalanCnt);
                        printBluetooth(aa);

                    }
                }
            }
        });

    }

    /*success of request 200*/
    private void SuccessBill(String message) {

        String[] res = message.split("\\|");
        if (res != null && res.length > 0) {
            if (res[0].equalsIgnoreCase("0")) {
                //Methodclass.hasError(MainActivity.this, res[1]);
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put("retail", res[0]);
                map.put("shop", res[1]);
                map.put("year", res[2]);
                map.put("bill", res[3]);
                callprintBill(map);
            }
        }



    }
private void SuccessKOT(String message) {

        String[] res = message.split("\\|");
        if (res != null && res.length > 0) {
            if (res[0].equalsIgnoreCase("0")) {
                //Methodclass.hasError(MainActivity.this, res[1]);
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put("retail", res[0]);
                map.put("shop", res[1]);
                map.put("year", res[2]);
                map.put("order", res[3]);
                callprintKOT(map);
            }
        }



    }
    private void SuccessINV(String message) {

        String[] res = message.split("\\|");
        if (res != null && res.length > 0) {
            if (res[0].equalsIgnoreCase("0")) {
                //Methodclass.hasError(MainActivity.this, res[1]);
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put("retail", res[0]);
                map.put("shop", res[1]);
                map.put("year", res[2]);
                map.put("sale", res[3]);
                callprintINV(map);
            }
        }

    }

    /*Init The Printer*/
    private void callprintBill(HashMap<String, String> map) {
        Methodclass.callPostForm(map, LOAD_BILL, new AllInterface() {
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
                Log.e("TAG", "OnSuccess: " + message);
                String[] res = message.split("\\|");


                if (res != null && res.length > 0) {
                    if (res[0].equalsIgnoreCase("0")) {
                       // Methodclass.hasError(MainActivity.this, res[1]);

                    } else {
                       res = message.split("\\[SEP]");
                        String[] header = res[1].split("\\|");
                        String[] rec_str = res[2].split("\\|");
                        String invCnt = res[3];
                        String chalanCnt = res[4];

                        List<BillItem> list = new ArrayList<>();
                        for (int i = 0; i < rec_str.length; i++) {
                            String[] item = rec_str[i].split("~");
                            list.add(new BillItem(item[0],
                                    item[1],
                                    item[2],
                                    item[3]+" "+item[4],
                                    item[5],
                                    item[7]
                                    ));

                        }
                     AsyncEscPosPrinter aa = formate_BILL(res, header, list, invCnt, chalanCnt);
                      //printBluetooth(aa);

                    }
                }

            }
        }, this);


    }

    private void callprintKOT(HashMap<String, String> map) {
        Methodclass.callPostForm(map, LOAD_KOT, new AllInterface() {
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
                Log.e("TAG", "OnSuccess: " + message);
                String[] res = message.split("\\[SEP]");


                if (res != null && res.length > 0) {
                    if (res[0].equalsIgnoreCase("0")) {
                       // Methodclass.hasError(MainActivity.this, res[1]);

                    } else {
                        String[] header = res[1].split("\\|");
                        String[] rec_str_old = res[2].split("\\|");
                        String[] rec_str_new = res[3].split("\\|");
                        String invCnt = res[4];
                        String chalanCnt = res[5];
                        List<KotItem> list = new ArrayList<>();
                        for (int i = 0; i < rec_str_new.length; i++) {
                            String[] item = rec_str_new[i].split("~");
                            list.add(new KotItem(item[0],item[1],item[2]+" "+item[3]));

                        }
                       AsyncEscPosPrinter aa = formate_KOT(res, header, list, invCnt, chalanCnt);
                      //printBluetooth(aa);
                    }
                }

            }
        }, this);


    }


    private void callprintINV(HashMap<String, String> map) {

        Methodclass.callPostForm(map, LOAD_PRINT, new AllInterface() {
            @Override
            public void OnSuccess(int code, String message) {
                super.OnSuccess(code, message);
                Log.e("TAG", "OnSuccess: " + message);
                String[] res = message.split("\\|");

                if (res != null && res.length > 0) {
                    if (res[0].equalsIgnoreCase("0")) {
                       // Methodclass.hasError(MainActivity.this, res[1]);

                    } else {
                        res = message.split("\\[SEP]");
                        String[] header = res[1].split("\\|");
                        String[] rec_str = res[2].split("\\|");
                        String invCnt = res[3];
                        String chalanCnt = res[4];

                        List<BillItem> list = new ArrayList<>();
                        for (int i = 0; i < rec_str.length; i++) {
                            String[] item = rec_str[i].split("~");
                            list.add(new BillItem(item[0],
                                    item[1],
                                    item[2],
                                    item[3]+" "+item[4],
                                    item[5],
                                    item[7]
                            ));

                        }
                        AsyncEscPosPrinter aa = formate_Invoice(res, header, rec_str, invCnt, chalanCnt);
                        //printBluetooth(aa);

                    }
                }

            }
        }, this);

    }

    private AsyncEscPosPrinter formate_Invoice(String[] res, String[] header, String[] rec_str, String invCnt, String chalanCnt) {

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(selectedDevice, 203, 48f, 32);
        return printer.addTextToPrint(
                "[L]<u><font size='normal'>" + header[0] + "</font></u>\n" +
                        "[L]" + "[L]<b>" + header[1] + "</b>\n" +
                        "[L]DATE " + header[7] +"\n"+
                        "[L]TIME " + header[8] + "\n" +
                        "[L]Inv_No " + header[6] + "\n" +
                        "--------------------------------\n" +
                        "[L]INVOICE\n" +
                        "--------------------------------\n" +
                        "[L]ITEM"+"[R]QTY"+"[R]PRICE"+"[R]VALUE"+"\n"+
                        "--------------------------------\n" +
                        "[L] " + header[2] + "\n" +
                        "[L]<b>" + header[3] + "</b>\n" +
                        "[L]" + header[4] +
                        "[L]" + header[5] +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[L]inv_no: " + header[6] + "\n" +
                        "[L]inv_date:" + header[7] +
                        "[L]time: " + header[8] + "\n" +
                        "[L]g_amt: " + header[9] + "\n" +
                        "[L]promo_disc: " + header[10] + "\n" +
                        "[L]inv_disc: " + header[11] + "\n" +
                        "[L]coupon: " + header[12] + "\n" +
                        "[L]coupon_disc: " + header[13] + "\n" +
                        "[L]counter_disc: " + header[14] + "\n" +
                        "[L]total_disc: " + header[15] + "\n" +
                        "[L]n_amt: " + header[16] + "\n" +
                        "[L]b_amt: " + header[17] + "\n" +
                        "[L]burn_point: " + header[18] + "\n" +
                        "[L]band_point: " + header[19] + "\n" +
                        "[L]earn_point: " + header[20] + "\n" +
                        "[L]bill_mop: " + header[21] + "\n" +
                        "[L]gst_no: " + header[22] + "\n" +
                        "[C]<qrcode size='20'>" + header[23] + "</qrcode>\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n"
                        /*"[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
                        "[L]Raymond DUPONT\n" +
                        "[L]5 rue des girafes\n" +
                        "[L]31547 PERPETES\n" +
                        "[L]Tel : +33801201456\n" +
                        "\n" +
                        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                        "[L]\n" +
                        "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>\n"
        */);


    }
 private AsyncEscPosPrinter formate_KOT(String[] res, String[] header, List<KotItem> items, String invCnt, String chalanCnt) {

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(selectedDevice, 203, 48f, 32);

        String itemss = "";
     for (int i = 0; i < items.size(); i++) {
         KotItem ii= items.get(i);
         itemss+= "[L]" + ii.getId()+"[L]" +ii.getName() +"[R]" +ii.getQuantity()+ "\n" ;

     }

        return printer.addTextToPrint(
                "[C]<u><font size='normal'>" + header[0] + "</font></u>\n"+
                        "[C]" + header[1] +"\n"+
                        "[L]Table No " + header[8] + "[R]Date " + header[6] + "\n"+
                        "[R]Time " + header[7] + "\n" +
                        "--------------------------------\n" +
                        "[L]SL[L]ITEM[R]QUAN\n" +
                        "--------------------------------\n" +
                        itemss+
                        "--------------------------------\n");


    }
private AsyncEscPosPrinter formate_BILL(String[] res, String[] header, List<BillItem> items, String invCnt, String chalanCnt) {
/*MHUB|204 LAKETOWN||ADMIN|9874842193||15-03-2023|08:37:55 pm|270|0|27|27|243|ABCDEFGH1234|9748421938@kotak.jpg*/
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(selectedDevice, 203, 48f, 32);

    String itemss = "";
    int qty =0;
    int pri =0;
    for (int i = 0; i < items.size(); i++) {
        BillItem ii= items.get(i);
        pri+=Integer.parseInt(ii.getPrice());
        //qty+=Integer.parseInt(ii.getQty());
        itemss+= "[L]"+ ii.getName()+"\n" +
                "[L]" +ii.getCode() + "[R]" +ii.getQty()+"[R]"+ii.getPrice()+"[R]"+ii.getValue()+ "\n" ;

    }





    return printer.addTextToPrint(
                "[L]<font size='normal'>" + header[0] + "</font>\n" +
                        "[L]" + header[1] + "\n" +
                        "[L]DATE " + header[6] +"\n"+
                        "[L]TIME " + header[7] + "\n" +
                        "[L]BILL NO " + "not found" + "\n" +
                        "--------------------------------\n" +
                        "[C]CASH BILL\n" +
                        "--------------------------------\n" +
                        "[L]ITEM"+"[R]QTY"+"[R]PRICE"+"[R]VALUE"+"\n"+
                        "--------------------------------\n" +
                        itemss +
                        "[L]<b> TOTAL ITEM= " + items.size()+ "</b>" + "[L] QTY " + qty+"[R]"+pri+"\n"+
                        "[C]--------------------------------\n" +
                        "[L]NET AMOUNT:" + "[R]"+header[8] + "\n" +
                        "[L]TOTAL DISCOUNT:" + "[R]"+ header[11] + "\n" +
                        "[L]LOYALITY REDEMPTION:" + "[R]"+"0.00" + "\n" +
                        "[C]--------------------------------\n" +
                        "[L]TENDER AMOUNT:" + "[R]"+ header[12] + "\n" +
                        "[C]--------------------------------\n" +
                        "[L]MODE OF PAYMENT: " + "NOF"+ "\n" +
                        "[C]--------------------------------\n" +
                        "[L]AMOUNT IS INCLUSIVE OF TAXES \n" +
                        "OUR GST NO" +header[13] +"\n"+
                        "[C]GST BILL IS AVAILABLE ON  \n" +
                        "[C]LOYALTY DASHBOARD\n"+

                          "[C]POINTS EARNED FROM THIS PURCHASE\n"+
                        "[C]THANKS FOR YOUR VISIT \n" +
                        "[C]LOOKING FOROWARD FOR YOUR NEXT VISIT\n"+
                        "[C]<qrcode size='20'>" + header[14] + "</qrcode>\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n"
                       );


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case MainActivity.PERMISSION_BLUETOOTH:
                case MainActivity.PERMISSION_BLUETOOTH_ADMIN:
                case MainActivity.PERMISSION_BLUETOOTH_CONNECT:
                case MainActivity.PERMISSION_BLUETOOTH_SCAN:
                    //this.printBluetooth();
                    break;
            }
        }
    }


    public void browseBluetoothDevice() {
        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

        if (bluetoothDevicesList != null) {
            selectedDevice = bluetoothDevicesList[0];
           /* final String[] items = new String[bluetoothDevicesList.length + 1];
            items[0] = "Default printer";
            int i = 0;
            for (BluetoothConnection device : bluetoothDevicesList) {
                items[++i] = device.getDevice().getName();
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Bluetooth printer selection");
            alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int index = i - 1;
                    if (index == -1) {
                        selectedDevice = null;
                    } else {
                        selectedDevice = bluetoothDevicesList[index];
                    }
                    Button button = (Button) findViewById(R.id.button_bluetooth_browse);
                    button.setText(items[i]);
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
*/
        }
    }

    public void printBluetooth(AsyncEscPosPrinter AsyncEscPosPrinter) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, MainActivity.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, MainActivity.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, MainActivity.PERMISSION_BLUETOOTH_SCAN);
        } else {
            new AsyncBluetoothEscPosPrint(
                    this,
                    new AsyncEscPosPrint.OnPrintFinished() {
                        @Override
                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                        }

                        @Override
                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                        }
                    }
            ).execute(AsyncEscPosPrinter)/*execute(this.getAsyncEscPosPrinter(selectedDevice))*/;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.meghdut, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        "[L]\n" +
                        "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                        "[L]\n" +
                        "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                        "[C]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99€\n" +
                        "[L]  + Size : S\n" +
                        "[L]\n" +
                        "[L]<b>AWESOME HAT</b>[R]24.99€\n" +
                        "[L]  + Size : 57/58\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[R]TOTAL PRICE :[R]34.98€\n" +
                        "[R]TAX :[R]4.23€\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
                        "[L]Raymond DUPONT\n" +
                        "[L]5 rue des girafes\n" +
                        "[L]31547 PERPETES\n" +
                        "[L]Tel : +33801201456\n" +
                        "\n" +
                        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                        "[L]\n" +
                        "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>\n"
        );
    }

}