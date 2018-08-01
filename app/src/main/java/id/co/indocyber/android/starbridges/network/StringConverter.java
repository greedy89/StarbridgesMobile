package id.co.indocyber.android.starbridges.network;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 5/8/2018.
 */

public class StringConverter implements JsonSerializer<String>, JsonDeserializer<String> {
    public JsonElement serialize(String src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        if ( src == null ) {
            return new JsonPrimitive("");
        } else {
            return new JsonPrimitive(src.toString());
        }
    }
    public String deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context)
            throws JsonParseException {
        return json.getAsJsonPrimitive().getAsString();
    }

    public String numberFormat(String number)
    {
        String result="";
        try{
            NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
            result = format.format(new BigInteger(number));
        }
        catch (Exception e)
        {

        }

        return result;
    }

    public String dateFormat3(String dateInput)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        DateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
        String dateResult = "";
        try{
            Date result =  df.parse(dateInput);
            dateResult=sdf.format(result);
        }catch (Exception e)
        {

        }

        return dateResult;
    }

    public String dateFormatDDMMYYYY(String dateInput)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateResult = "";
        try{
            Date result =  df.parse(dateInput);
            dateResult=sdf.format(result);
        }catch (Exception e)
        {

        }

        return dateResult;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public String scalingImage(Bitmap selectedImage)
    {
        int newWidth=0;
        int newHeight=0;
        int maxPixel=1000;
        if(selectedImage.getWidth()>selectedImage.getHeight())
        {
            newHeight=maxPixel;
            newWidth=maxPixel*selectedImage.getWidth()/selectedImage.getHeight();
        }
        else
        {
            newWidth=maxPixel;
            newHeight=maxPixel*selectedImage.getHeight()/selectedImage.getWidth();

        }
//                    selectedImage2.createScaledBitmap(selectedImage, newWidth, newHeight, false);
        Bitmap selectedImage2 = Bitmap.createScaledBitmap(selectedImage, newWidth, newHeight, false);
        return encodeImage(selectedImage2);
    }

    public String dateFormatInput2dMMMMYYYY(String dateInput)
    {
        DateFormat df = new SimpleDateFormat("d MMMM yyyy");
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateResult = "";
        try{
            Date result =  sdf.parse(dateInput);
            dateResult=df.format(result);
        }catch (Exception e)
        {

        }

        return dateResult;
    }
}
