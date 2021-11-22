package com.cname.nada.functions;

import java.util.ArrayList;

public class CurrentFriendID {
    private static String friendId;

    public static String getFriendId() {
        return friendId;
    }

    public static void setFriendId(String friendId) {
        CurrentFriendID.friendId = friendId;
    }
}

