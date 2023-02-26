package com.attend.common.interfaces;


import com.attend.common.enums.Actions;

public interface GeneralListener<T> {
    void onClick(Actions action, T t);
}
