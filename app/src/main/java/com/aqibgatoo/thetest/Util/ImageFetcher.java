package com.aqibgatoo.thetest.Util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.aqibgatoo.thetest.R;
import com.aqibgatoo.thetest.controller.TestApplication;
import com.aqibgatoo.thetest.model.ImageEntity;
import com.kinvey.android.Client;
import com.kinvey.java.core.DownloaderProgressListener;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpDownloader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aqib on 12/22/2014.
 */
public class ImageFetcher {
    private static final String TAG = ImageFetcher.class.getSimpleName();
    private ImageView mImageView;
    private Context mContext;
    private Client mKinveyClient;

    public ImageFetcher(Context context) {
        mContext = context;
        mKinveyClient = TestApplication.getClientInstance();
    }


    public void loadImage(ImageView imageView, String id) {
        mImageView = imageView;
        mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
        mKinveyClient.linkedData("events", ImageEntity.class).getEntity(id,
                new KinveyClientCallback<ImageEntity>() {
                    @Override
                    public void onSuccess(ImageEntity entity) {
                        Log.d(TAG, entity.getName() + entity.getId());
                        FileOutputStream fStream = null;
                        try {
                            fStream = mContext.openFileOutput("" + entity.getId(), Context.MODE_PRIVATE);
                            ByteArrayOutputStream bos = entity.getFile("image").getOutput();
                            bos.writeTo(fStream);
                            bos.flush();
                            fStream.flush();
                            bos.close();
                            fStream.close();
                        } catch (Exception ex) {
                        }

                        try {
                            InputStream inputStream = mContext.openFileInput(entity.getId());
                            mImageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(TAG, throwable.getMessage());
                    }
                }, new DownloaderProgressListener() {
                    @Override
                    public void progressChanged(MediaHttpDownloader mediaHttpDownloader) throws IOException {
                        Log.d(TAG, mediaHttpDownloader.getDownloadState() + "");

                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "image downloaded");

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });

    }


}

