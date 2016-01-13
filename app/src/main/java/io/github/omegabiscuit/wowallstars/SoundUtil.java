package io.github.omegabiscuit.wowallstars;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for file functions
 * Created by Jawnnypoo on 11/4/2015.
 */
public class SoundUtil {

    /**
     * Takes a sound resource id and stores it to the users Notifications folder
     * @param context context
     * @param soundResId the resource id of the sound ie. R.raw.leeroy
     * @return the full path to the stored notification tone
     */
    public static String saveNotificationTone(Context context, int soundResId) {

        //Gets the path to the folder called "Notifications"
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).getPath();
        if (TextUtils.isEmpty(folderPath)) {
            folderPath = Environment.getExternalStorageDirectory().getPath();
        }
        byte[] buffer;
        InputStream fIn = context.getResources().openRawResource(soundResId);
        int size;

        //reads the sound resource into a buffer byte array
        try {
            size = fIn.available();
            buffer = new byte[size];
            fIn.read(buffer);
            fIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String filename = context.getResources().getResourceEntryName(soundResId) + ".mp3";
        String fullPath = folderPath + "/" + filename;
        //stores the buffer byte array to a file
        FileOutputStream save;
        try {
            save = new FileOutputStream(fullPath);
            save.write(buffer);
            save.flush();
            save.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //Make the file show up to the system
        MediaScannerConnection.scanFile(context, new String[]{fullPath}, null, null);
        return fullPath;
    }

    public static boolean setTone(Context context, int soundResId) {
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).getPath();
        if (TextUtils.isEmpty(folderPath)) {
            folderPath = Environment.getExternalStorageDirectory().getPath();
        }
        String fileName = context.getResources().getResourceEntryName(soundResId);
        File ring = new File(folderPath, fileName + ".mp3");
        if (!ring.exists()) {
            //Tone is not already saved. So save it.
            String pathToFile = saveNotificationTone(context, soundResId);
            if (TextUtils.isEmpty(pathToFile)) {
                return false;
            }
        }

        //We now create a new content values object to store all the information
        //about the ringtone.
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, ring.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        values.put(MediaStore.MediaColumns.SIZE, ring.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, context.getString(R.string.app_name));
        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        //Work with the content resolver now
        //First get the file we may have added previously and delete it,
        //otherwise we will fill up the ringtone manager with a bunch of copies over time.
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ring.getAbsolutePath());
        context.getContentResolver()
                .delete(uri, MediaStore.MediaColumns.DATA + "=\"" + ring.getAbsolutePath() + "\"", null);

        //Ok now insert it
        Uri newUri = context.getContentResolver().insert(uri, values);

        //Ok now set the ringtone from the content manager's uri, NOT the file's uri
        RingtoneManager.setActualDefaultRingtoneUri(
                context,
                RingtoneManager.TYPE_NOTIFICATION,
                newUri
        );
        return true;
    }
}
