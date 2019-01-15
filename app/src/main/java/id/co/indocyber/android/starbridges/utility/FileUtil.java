package id.co.indocyber.android.starbridges.utility;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtil {
    public static String getRealPathFromURI(Context context, Uri uri) {
        String result;
        try {
            String[] proj = {MediaStore.Files.FileColumns.DATA};
            CursorLoader loader = new CursorLoader(context, uri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        } catch (Exception ex) {
            result = uri.getPath();
        }
        return result;
    }
}
