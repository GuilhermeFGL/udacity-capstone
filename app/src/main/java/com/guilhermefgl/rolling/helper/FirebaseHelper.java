package com.guilhermefgl.rolling.helper;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseHelper {

    public static FirebaseAuth getAuthInstance() {
        return FirebaseAuth.getInstance();
    }

}
