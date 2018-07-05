package com.guilhermefgl.rolling.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    private static final String FIREBASE_AVATAR_DIR = "avatar/";

    public static FirebaseAuth getAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public static StorageReference getAvatarStorageInstance() {
        return getStorageReferenceInstance().child(FIREBASE_AVATAR_DIR);
    }

    public static String createUniqueFileName() {
        FirebaseUser user = getAuthInstance().getCurrentUser();
        long time = System.currentTimeMillis();
        if (user != null) {
            return String.format("%s-%s", user.getUid(), time);
        } else {
            return String.valueOf(time);
        }
    }

    private static StorageReference getStorageReferenceInstance() {
        return FirebaseStorage.getInstance().getReference();
    }
}
