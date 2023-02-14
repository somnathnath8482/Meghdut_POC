package com.meghdutpoc.android.interfaces;

import java.util.List;

public interface OnClick {
    void isClicked(boolean is);
    void Clicked(String item);
    void OnSuccess(int code, String message);
    void OnSuccess(List<String> list);
}
