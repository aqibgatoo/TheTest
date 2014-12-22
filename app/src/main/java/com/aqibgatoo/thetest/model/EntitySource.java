package com.aqibgatoo.thetest.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.aqibgatoo.thetest.controller.TestApplication;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;

import java.io.IOException;

/**
 * Created by Aqib on 12/20/2014.
 */
public class EntitySource {

    private static final String TAG = EntitySource.class.getSimpleName();
    private static EntitySource mEntitySource = null;
    private Client mKinveyClient;
    private Context mContext;
    private ImageEntity mImageEntity;
    private Bitmap bitmap;

    private EntitySource(Context context) {
        mContext = context;
        mKinveyClient = TestApplication.getClientInstance();

    }

    public static EntitySource getEntitySource(Context context) {
        if (mEntitySource == null) {
            mEntitySource = new EntitySource(context);
        }
        return mEntitySource;
    }

    public void saveEntity(ImageEntity entity) {
        AsyncAppData<ImageEntity> events = mKinveyClient.appData("events", ImageEntity.class);
        events.save(entity, new KinveyClientCallback<ImageEntity>() {
            @Override
            public void onSuccess(ImageEntity entity) {
                Log.d(TAG, "Entity Id" + entity.getId());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
            }
        });

    }

    public ImageEntity getImageEntity(String id) {

        AsyncAppData<ImageEntity> events = mKinveyClient.appData("events", ImageEntity.class);
        events.getEntity(id, new KinveyClientCallback<ImageEntity>() {
            @Override
            public void onSuccess(ImageEntity entity) {
                mImageEntity = entity;
                Log.d(TAG, "Entity retrieved");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
            }
        });


        return mImageEntity;

    }


    public void saveLinkedEntity(ImageEntity entity) {

        mKinveyClient.linkedData("events", ImageEntity.class).save(entity, new KinveyClientCallback<ImageEntity>() {
            @Override
            public void onSuccess(ImageEntity entity) {
                Log.d(TAG, "Uploaded entity id" + entity.getId());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "Failed to save entity");

            }
        }, new UploaderProgressListener() {
            @Override
            public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {

            }

            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Image uploaded successfully");

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "Image upload unsuccessful");

            }
        });
    }


}
