package com.zoipuus.balanceball.util;

import java.io.InputStream;  

import android.content.ContentResolver;  
import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.BitmapFactory.Options;  
import android.net.Uri;  
  
/** 
 * @author frankiewei. 
 * ������. 
 */  
public class ImageCacheUtil {  
  
    /** 
     * ��ȡ���ʵ�Bitmapƽʱ��ȡBitmap�������������. 
     * @param path ·��. 
     * @param data byte[]����. 
     * @param context ������ 
     * @param uri uri 
     * @param target ģ�����߸ߵĴ�С. 
     * @param width �Ƿ��ǿ�� 
     * @return 
     */  
    public static Bitmap getResizedBitmap(String path, byte[] data,  
            Context context,Uri uri, int target, boolean width) {  
        Options options = null;  
  
        if (target > 0) {  
  
            Options info = new Options();  
            //��������true��ʱ��decodeʱ��Bitmap���ص�Ϊ�գ�  
            //��ͼƬ��߶�ȡ����Options��.  
            info.inJustDecodeBounds = false;  
              
            decode(path, data, context,uri, info);  
              
            int dim = info.outWidth;  
            if (!width)  
                dim = Math.max(dim, info.outHeight);  
            int ssize = sampleSize(dim, target);  
  
            options = new Options();  
            options.inSampleSize = ssize;  
  
        }  
  
        Bitmap bm = null;  
        try {  
            bm = decode(path, data, context,uri, options);  
        } catch(Exception e){  
            e.printStackTrace();  
        }  
        return bm;  
  
    }  
      
    /** 
     * ����Bitmap�Ĺ��÷���. 
     * @param path 
     * @param data 
     * @param context 
     * @param uri 
     * @param options 
     * @return 
     */  
    public static Bitmap decode(String path, byte[] data, Context context,  
            Uri uri, BitmapFactory.Options options) {  
  
        Bitmap result = null;  
  
        if (path != null) {  
  
            result = BitmapFactory.decodeFile(path, options);  
  
        } else if (data != null) {  
  
            result = BitmapFactory.decodeByteArray(data, 0, data.length,  
                    options);  
  
        } else if (uri != null) {  
            //uri��Ϊ�յ�ʱ��contextҲ��ҪΪ��.  
            ContentResolver cr = context.getContentResolver();  
            InputStream inputStream = null;  
  
            try {  
                inputStream = cr.openInputStream(uri);  
                result = BitmapFactory.decodeStream(inputStream, null, options);  
                inputStream.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
  
        }  
  
        return result;  
    }  
      
      
    /** 
     * ��ȡ���ʵ�sampleSize. 
     * ����ͼ�ʵ�ֶ���2�ı�����. 
     * @param width 
     * @param target 
     * @return 
     */  
    private static int sampleSize(int width, int target){             
            int result = 1;           
            for(int i = 0; i < 10; i++){               
                if(width < target * 2){  
                    break;  
                }                 
                width = width / 2;  
                result = result * 2;                  
            }             
            return result;  
        }  
}  
