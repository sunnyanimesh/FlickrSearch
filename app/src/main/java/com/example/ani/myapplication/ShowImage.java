package com.example.ani.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowImage extends AsyncTask<String,Void,Bitmap> {
    private WeakReference<ImageView> imageview;
    public ShowImage(ImageView imv){
        imageview=new WeakReference<ImageView>(imv);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {

        return getBitMapFromUrl(urls[0]);

    }
    @Override
    protected void onPostExecute(Bitmap result) {
        if((imageview!=null)&&(result!=null)){
            ImageView imgview=imageview.get();


            if(imgview!=null){

                imgview.setImageBitmap(result);
            }
        }
    }

    private Bitmap getBitMapFromUrl( String imageuri){
        HttpURLConnection connection=null;

        try {
            URL url=new URL(imageuri);
            //  Log.d("bucky","bitmap" + imageuri);
            connection= (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is=connection.getInputStream();
            Bitmap mybitmap= BitmapFactory.decodeStream(is);

            return mybitmap;


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(connection!=null) {
                connection.disconnect();
            }
        }
    }

}