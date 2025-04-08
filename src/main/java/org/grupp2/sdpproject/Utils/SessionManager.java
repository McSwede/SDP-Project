package org.grupp2.sdpproject.Utils;

import org.grupp2.sdpproject.entities.User;

public class SessionManager {
    private static String loggedInUser = null;

    public static void login(String username) {
        loggedInUser = username;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }

}



