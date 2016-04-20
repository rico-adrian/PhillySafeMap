package com.example.ryan.phillysafemap;

/**
 * Created by hna63 on 3/12/2016.
 */
public class LastAccess {
    private static boolean isRecent = true;

    public static void setIsRecent(boolean result){
        isRecent = result;
    }

    public static boolean getIsRecent(){
        return isRecent;
    }
}
