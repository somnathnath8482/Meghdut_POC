package com.meghdutpoc.android.Helper.Mdel;

/**
 * Created by Somnath nath on 16,March,2023
 * Artix Development,
 * India.
 */
public class KotItem {
    String id,name,quantity;

    public KotItem(String id, String name, String quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }
}
