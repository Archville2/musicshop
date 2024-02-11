package by.kurlovich.musicshop.util;

import by.kurlovich.musicshop.entity.User;

public class UserUtil {
    public static String getId(User currentUser) {
        return currentUser != null ? currentUser.getId() : "0";
    }
}
