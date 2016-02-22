package com.zoipuus.balanceball;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {
	private Context context;

	public PreferencesService(Context context) {
		this.context = context;
	}

	/**
	 * 保存数据
	 * 
	 * @param isPlayMusic
	 * @param isSpotAds 
	 * @param isChinese
	 * @param volume_value
	 */
	public void savePreferences(Boolean isVibrate, Boolean isPlayMusic,
			Boolean isSpotAds, int volume_value) {
		// TODO Auto-generated method stub
		/**
		 * 指定settingPreferences.xml文件存放设置好的数据，不用指定后缀名，因为这个方法内部自动添加
		 */
		SharedPreferences preferences = context.getSharedPreferences(
				"settingPreferences", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();// 得到编辑器对象
		editor.putBoolean("isVibrate", isVibrate);
		editor.putBoolean("isPlayMusic", isPlayMusic);
		editor.putBoolean("isSpotAds", isSpotAds);
		editor.putInt("volume_value", volume_value);
		editor.commit();// 将数据写入偏好设置文件
	}

	public Map<String, Boolean> getBooleanPreferences() {
		Map<String, Boolean> params = new HashMap<String, Boolean>();
		SharedPreferences preferences = context.getSharedPreferences(
				"settingPreferences", Context.MODE_PRIVATE);
		params.put("isPlayMusic", preferences.getBoolean("isPlayMusic", true));// true为默认值
		params.put("isVibrate", preferences.getBoolean("isVibrate", true));
		params.put("isSpotAds", preferences.getBoolean("isSpotAds", true));
		return params;
	}

	public Map<String, Integer> getIntegerPreferences() {
		Map<String, Integer> params = new HashMap<String, Integer>();
		SharedPreferences preferences = context.getSharedPreferences(
				"settingPreferences", Context.MODE_PRIVATE);
		params.put("volume_value", preferences.getInt("volume_value", 10));
		return params;
	}
}
