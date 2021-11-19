package com.cname.nada.functions;

public class CurrentFriendID {
    private static String friendId;

    public static String getFriendId() {
        return friendId;
    }

    public static void setFriendId(String friendId) {
        CurrentFriendID.friendId = friendId;
    }
}
