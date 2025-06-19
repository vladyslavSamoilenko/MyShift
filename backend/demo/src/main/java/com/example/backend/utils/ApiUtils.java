package com.example.backend.utils;

import com.example.backend.model.constants.ApiConstants;

public class ApiUtils {
    public static String getMethodName(){
        try{
            return Thread.currentThread().getStackTrace()[1].getMethodName();
        }catch (Exception cause){
            return ApiConstants.UNDEFINED;
        }
    }
}
