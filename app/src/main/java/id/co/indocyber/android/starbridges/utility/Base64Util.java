package id.co.indocyber.android.starbridges.utility;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64Util {

    public static String getBase64FromPath(String path) {
        String base64 = "";
        try {
            File file = new File(path);
            byte[] buffer= new byte[(int) file.length() + 100];
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return base64;
    }

    public static void saveBase64ToFile(Context context, String base64, String filename) {
        try {
            byte[] fileBytes = Base64.decode(base64, Base64.DEFAULT);

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+filename;
            File file = new File(path);
            if(file.exists())
            {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file, false);
            stream.write(fileBytes);
            stream.flush();
            stream.close();
            Toast.makeText(context, "Success save file", Toast.LENGTH_SHORT).show();
            NotificationUtil.showDownloadSuccessNotification(context, "Download Success", "File saved in Download Directory", path);

        }catch (Exception ex) {
            Toast.makeText(context, "Failed save file", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }


}
