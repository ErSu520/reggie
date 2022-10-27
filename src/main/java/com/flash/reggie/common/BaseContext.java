package com.flash.reggie.common;

public class BaseContext {

    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentUserId(Long id){
        THREAD_LOCAL.set(id);
    }

    public static Long getCurrentUserId(){
        return THREAD_LOCAL.get();
    }

}
