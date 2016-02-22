package com.zoipuus.balanceball.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapOperation {
	public static Bitmap readBitmapAutoSize(String filePath, int outWidth,
			int outHeight) {
		// outWidth��outHeight��Ŀ��ͼƬ������Ⱥ͸߶ȣ���������
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath, outWidth,
					outHeight);
			return BitmapFactory.decodeStream(bs, null, options);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static BitmapFactory.Options setBitmapOption(String file, int width,
			int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// ����ֻ�ǽ���ͼƬ�ı߾࣬�˲���Ŀ���Ƕ���ͼƬ��ʵ�ʿ�Ⱥ͸߶�
		BitmapFactory.decodeFile(file, opt);

		int outWidth = opt.outWidth; // ���ͼƬ��ʵ�ʸߺͿ�
		int outHeight = opt.outHeight;
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// ���ü���ͼƬ����ɫ��Ϊ16bit��Ĭ����RGB_8888����ʾ24bit��ɫ��͸��ͨ������һ���ò���
		opt.inSampleSize = 1;
		// �������ű�,1��ʾԭ������2��ʾԭ�����ķ�֮һ....
		// �������ű�
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			opt.inSampleSize = sampleSize;
		}

		opt.inJustDecodeBounds = false;// ���ѱ�־��ԭ
		return opt;
	}
}
