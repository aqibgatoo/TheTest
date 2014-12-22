package com.aqibgatoo.thetest.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.aqibgatoo.thetest.R;
import com.aqibgatoo.thetest.adapters.ImageAdapter;
import com.aqibgatoo.thetest.controller.TestApplication;
import com.aqibgatoo.thetest.model.EntitySource;
import com.aqibgatoo.thetest.model.ImageEntity;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.LinkedResources.LinkedFile;
import com.kinvey.java.core.DownloaderProgressListener;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpDownloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class ImageActivity extends ActionBarActivity {
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 11;
    private static final int MEDiA_TYPE_IMAGE = 1;
    private static final int PICK_IMAGE = 2;
    private static final String TAG = ImageActivity.class.getSimpleName();

    private Client mKinveyClient;
    private EntitySource mEntitySource;
    private Uri mFileUri;
    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private ArrayList<ImageEntity> mImageEntities;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mEntitySource = EntitySource.getEntitySource(this);
        mKinveyClient = TestApplication.getClientInstance();
        mGridView = (GridView) findViewById(R.id.gridview);
        mImageEntities = new ArrayList<>();
        if (!mKinveyClient.user().isUserLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        mImageAdapter = new ImageAdapter(this, mImageEntities);
        mGridView.setAdapter(mImageAdapter);
    }

    private void updateData() {

        AsyncAppData<ImageEntity> events = mKinveyClient.appData("events", ImageEntity.class);
//        Query query = new Query();
//        query.regEx("name", "^aqibgatoo");
        events.get(new KinveyListCallback<ImageEntity>() {
            @Override
            public void onSuccess(ImageEntity[] imageEntities) {

                mImageEntities.addAll(Arrays.asList(imageEntities));
                for (ImageEntity imageEntity : imageEntities) {
                    System.out.println(imageEntity.getId());
                }
                mImageAdapter.notifyDataSetChanged();
//                loadLinkedData(mImageEntities.get(0));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "Fail " + throwable.getMessage());
            }
        });


    }


    /* private void downloadImage() {

         String path = Environment.getExternalStoragePublicDirectory
                 (Environment.DIRECTORY_PICTURES) + File.separator + getString(R.string.app_name);
         final File file = new File(path + File.separator + "newfile.jpg");
         Query query = new Query();
         query.regEx("_id", "^IMG");
         try {
             FileOutputStream fos = new FileOutputStream(file);
             mKinveyClient.file().download(query, fos, new DownloaderProgressListener() {
                 @Override
                 public void progressChanged(MediaHttpDownloader mediaHttpDownloader) throws IOException {

                     switch (mediaHttpDownloader.getDownloadState()) {
                         case DOWNLOAD_IN_PROGRESS:
                             System.out.println("Download in progress");
                             System.out.println("Download percentage: " + mediaHttpDownloader.getProgress());
                             break;
                         case DOWNLOAD_COMPLETE:
                             System.out.println("Download Completed!");
                             break;
                     }
                 }

                 @Override
                 public void onSuccess(Void aVoid) {
                     Log.d(TAG, "entered on sucess");

                     try {
                         mImageView.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(file)));
                     } catch (FileNotFoundException e) {
                         e.printStackTrace();
                     }

                 }

                 @Override
                 public void onFailure(Throwable throwable) {
                     Log.d(TAG, throwable.getMessage());
                 }
             });

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
     }
 */
//    private void addEntity() {
//        ImageEntity imageEntity = new ImageEntity();
//        imageEntity.setName("first");
//        mEntitySource.saveEntity(imageEntity);
//    }
//
//
//    private Uri getOutputMediaFileUri(int imageType) {
//        if (isExternalStorageAvailable()) {
//            File mediaExternalDir = new File(Environment.getExternalStoragePublicDirectory
//                    (Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
//            if (!mediaExternalDir.exists()) {
//                if (!mediaExternalDir.mkdirs()) {
//                    Log.d(TAG, "failed to create dir");
//                    return null;
//                }
//            }
//            File mediaFile = null;
//            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            String path = mediaExternalDir.getPath() + File.separator;
//            if (imageType == MEDiA_TYPE_IMAGE) {
//                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
//            }
//            Log.d(TAG, "FILE :" + Uri.fromFile(mediaFile));
//
//            return Uri.fromFile(mediaFile);
//        }
//
//        return null;
//
//    }
//
//    private boolean isExternalStorageAvailable() {
//        String state = Environment.getExternalStorageState();
//        return state.equals(Environment.MEDIA_MOUNTED);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_getitems) {
            updateData();
            return true;
        }

        if (id == R.id.action_logout) {

            mKinveyClient.user().logout().execute();
            Intent intent = new Intent(this, LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            return true;
        }

        if (id == R.id.aciton_capture) {
//            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            mFileUri = getOutputMediaFileUri(MEDiA_TYPE_IMAGE);
//            if (mFileUri == null) {
//                Toast.makeText(this, "Unable to access external storage", Toast.LENGTH_LONG).show();
//            } else {
//                Log.d(TAG, mFileUri.getPath());
//            }
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
//            if (resolveActivity(captureIntent)) {
//                startActivityForResult(captureIntent, CAPTURE_IMAGE_REQUEST_CODE);
//
//            } else {
//                Toast.makeText(this, "Sorry no camera app", Toast.LENGTH_LONG).show();
//            }
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public boolean resolveActivity(Intent intent) {
//        PackageManager pm = getPackageManager();
//        ComponentName cN = intent.resolveActivity(pm);
//        return cN != null;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = BitmapFactory.decodeFile(mFileUri.getPath());
                saveEntityInBackground(bitmap, mFileUri);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture not taken", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, uri.getPath());
                saveEntityInBackground(bitmap, uri);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "failed to get result", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void saveEntityInBackground(Bitmap bitmap, Uri uri) {

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setName(mKinveyClient.user().getUsername());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] bytes = stream.toByteArray();

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageEntity.putFile("image", new LinkedFile(getFileName(uri)));
        imageEntity.getFile("image").setInput(new ByteArrayInputStream(bytes));
        mEntitySource.saveLinkedEntity(imageEntity);


    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void loadImageEntities() {


    }

    public void loadLinkedData(ImageEntity entity) {

        String id = entity.getId();

        mKinveyClient.linkedData("events", ImageEntity.class).getEntity(id, new KinveyClientCallback<ImageEntity>() {
            @Override
            public void onSuccess(ImageEntity entity) {
                Log.d(TAG, entity.getName() + entity.getId());
                FileOutputStream fStream = null;
                try {
                    fStream = getApplicationContext().openFileOutput("" + entity.getId(), Context.MODE_PRIVATE);
                    ByteArrayOutputStream bos = entity.getFile("image").getOutput();
                    bos.writeTo(fStream);
                    bos.flush();
                    fStream.flush();
                    bos.close();
                    fStream.close();
                } catch (Exception ex) {
                }

                try {
                    InputStream inputStream = getApplicationContext().openFileInput(entity.getId());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    mImageView.setImageBitmap(bitmap);
//                    entity.setBitmap(bitmap);
//                    mImageAdapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        }, new DownloaderProgressListener() {
            @Override
            public void progressChanged(MediaHttpDownloader mediaHttpDownloader) throws IOException {

            }

            @Override
            public void onSuccess(Void aVoid) {
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}