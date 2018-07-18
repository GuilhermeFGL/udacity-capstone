package com.guilhermefgl.rolling.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    private static final String PROVIDER_PASSWORD = "password";
    private static final String DATABASE_TRIPS = "trips/";
    private static final String DATABASE_USERS = "trip_has_user/";
    private static final String DATABASE_CURRENT = "current/";
    private static final String STORAGE_AVATAR = "avatar/";
    private static final String STORAGE_BANNER = "banner/";

    private FirebaseHelper() { }

    public static FirebaseAuth getAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference getTripDatabaseInstance() {
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = databaseInstance.getReference(DATABASE_TRIPS);
        databaseReference.keepSynced(true);
        return databaseReference;
    }

    public static DatabaseReference getUserDatabaseInstance() {
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = databaseInstance.getReference(DATABASE_USERS);
        databaseReference.keepSynced(true);
        return databaseReference;
    }

    public static DatabaseReference getCurrentDatabaseInstance() {
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = databaseInstance.getReference(DATABASE_CURRENT);
        databaseReference.keepSynced(true);
        return databaseReference;
    }

    public static StorageReference getAvatarStorageInstance() {
        return getStorageReferenceInstance().child(STORAGE_AVATAR);
    }

    public static StorageReference getBannerStorageInstance() {
        return getStorageReferenceInstance().child(STORAGE_BANNER);
    }

    public static String createUniqueId() {
        FirebaseUser user = getAuthInstance().getCurrentUser();
        long time = System.currentTimeMillis();
        if (user != null) {
            return String.format("%s-%s", user.getUid(), time);
        } else {
            return String.valueOf(time);
        }
    }

    public static boolean isUserPasswordProvider() {
        FirebaseUser user = getAuthInstance().getCurrentUser();
        if (user != null && user.getProviders() != null) {
            for (String provider : user.getProviders()) {
                if(provider.equals(PROVIDER_PASSWORD)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static StorageReference getStorageReferenceInstance() {
        return FirebaseStorage.getInstance().getReference();
    }
}
