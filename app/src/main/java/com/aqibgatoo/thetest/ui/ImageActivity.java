package com.aqibgatoo.thetest.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.aqibgatoo.thetest.R;
import com.aqibgatoo.thetest.controller.TestApplication;
import com.aqibgatoo.thetest.model.EntitySource;
import com.aqibgatoo.thetest.model.ImageEntity;
import com.kinvey.android.Client;
import com.kinvey.java.LinkedResources.LinkedFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ImageActivity extends ActionBarActivity {
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 11;
    private static final int MEDiA_TYPE_IMAGE = 1;
    private static final String TAG = ImageActivity.class.getSimpleName();
    private Client mKinveyClient;
    private EntitySource mEntitySource;
    private Uri mFileUri;
    private ListView mListView;
    private ArrayList<ImageEntity> mImageEntities;
    private ArrayAdapter<ImageEntity> mArrayAdapter;
    private ImageView mImageView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mEntitySource = EntitySource.getEntitySource(this);
        mKinveyClient = TestApplication.getInstance();
        mListView = (ListView) findViewById(R.id.id_listview);
        mImageView = (ImageView) findViewById(R.id.image);
        if (!mKinveyClient.user().isUserLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        mArrayAdapter = new ArrayAdapter<ImageEntity>(this, android.R.layout.simple_list_item_1, new ArrayList<ImageEntity>());

        mListView.setAdapter(mArrayAdapter);
    }

    private void getEntities() {

        mEntitySource.loadImageEntities(mArrayAdapter);

    }

    private void addEntity() {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setName("first");
        mEntitySource.saveEntity(imageEntity);
    }


    private Uri getOutputMediaFileUri(int imageType) {
        if (isExternalStorageAvailable()) {
            File mediaExternalDir = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
            if (!mediaExternalDir.exists()) {
                if (!mediaExternalDir.mkdirs()) {
                    Log.d(TAG, "failed to create dir");
                    return null;
                }
            }
            File mediaFile = null;
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String path = mediaExternalDir.getPath() + File.separator;
            if (imageType == MEDiA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            }
            Log.d(TAG, "FILE :" + Uri.fromFile(mediaFile));

            return Uri.fromFile(mediaFile);
        }

        return null;

    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);

    }

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
            getEntities();
        }

        if (id == R.id.action_logout) {

            mKinveyClient.user().logout().execute();
            Intent intent = new Intent(this, LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            return true;
        }

        if (id == R.id.aciton_capture) {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mFileUri = getOutputMediaFileUri(MEDiA_TYPE_IMAGE);
            if (mFileUri == null) {
                Toast.makeText(this, "Unable to access external storage", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, mFileUri.getPath());
            }
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
            if (resolveActivity(captureIntent)) {
                startActivityForResult(captureIntent, CAPTURE_IMAGE_REQUEST_CODE);

            } else {
                Toast.makeText(this, "Sorry no camera app", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean resolveActivity(Intent intent) {
        PackageManager pm = getPackageManager();
        ComponentName cN = intent.resolveActivity(pm);

        return cN != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = BitmapFactory.decodeFile(mFileUri.getPath());
                saveEntityInBackground(bitmap);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture not taken", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void saveEntityInBackground(Bitmap bitmap) {

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setName(mKinveyClient.user().getUsername());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] bytes = stream.toByteArray();

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageEntity.putFile("image", new LinkedFile(getFileName(mFileUri)));
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
//    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
//
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            Bitmap bitmap = null;
//
//            try {
//
//                bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            mImageView.setImageBitmap(bitmap);
//
//        }
//    }
}
