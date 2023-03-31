package com.meghdutpoc.android.Helper.Mdel;

/**
 * Created by Somnath nath on 17,March,2023
 * Artix Development,
 * India.
 * 1~Noodles Chilli Garlic ~10010070002~1~Plate~80~0~80
 */
public class BillItem {
    String id,name,code,qty,price,value;

    public BillItem(String id, String name, String code, String qty, String price, String value) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.qty = qty;
        this.price = price;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
    }

    public String getValue() {
        return value;
    }
}
