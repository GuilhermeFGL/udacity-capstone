package com.guilhermefgl.rolling.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    private static final String FIREBASE_AVATAR_DIR = "avatar/";
    private static final String FIREBASE_IMAGE_EXT = ".jpg";

    public static FirebaseAuth getAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public static StorageReference getAvatarStorageInstance() {
        return getStorageReferenceInstance().child(FIREBASE_AVATAR_DIR);
    }

    private static StorageReference getStorageReferenceInstance() {
        return FirebaseStorage.getInstance().getReference();
    }

    public static String buildImageName(String fileName) {
        return fileName.concat(FIREBASE_IMAGE_EXT);
    }

}
