package com.zoipuus.balanceball.util;

import java.io.IOException;
import java.util.HashMap;

import com.zoipuus.balanceball.ConstantUtil;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

public class SoundManager {

	private AssetManager assetManager;
	private HashMap<Integer, Integer> soundIds;
	private SoundPool soundPool;

	public SoundManager(Context context) {
		// TODO Auto-generated constructor stub
		soundIds = new HashMap<Integer, Integer>();
		soundPool = new SoundPool(20, 3, 0);
		assetManager = context.getAssets();
		try {
			AssetFileDescriptor assetfiledescriptor = assetManager
					.openFd("AccessIntegral.ogg");
			soundIds.put(Integer.valueOf(ConstantUtil.SOUND_AccessIntegral),
					Integer.valueOf(soundPool.load(assetfiledescriptor, 1)));
			assetfiledescriptor = assetManager.openFd("Collision.ogg");
			soundIds.put(Integer.valueOf(ConstantUtil.SOUND_Collision),
					Integer.valueOf(soundPool.load(assetfiledescriptor, 1)));
			assetfiledescriptor = assetManager.openFd("InHole.ogg");
			soundIds.put(Integer.valueOf(ConstantUtil.SOUND_InHole),
					Integer.valueOf(soundPool.load(assetfiledescriptor, 1)));
			return;
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	public void play(int paramInt) {
		// TODO Auto-generated method stub
		paramInt = ((Integer) soundIds.get(Integer.valueOf(paramInt)))
				.intValue();
		soundPool.play(paramInt, 1, 1, 0, 0, 1);
	}
}
