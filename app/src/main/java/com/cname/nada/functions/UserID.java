package com.cname.nada.functions;

public class UserID {
    private static String userId;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        UserID.userId = userId;
    }
}
