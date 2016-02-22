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
	 * ��������
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
		 * ָ��settingPreferences.xml�ļ�������úõ����ݣ�����ָ����׺������Ϊ��������ڲ��Զ����
		 */
		SharedPreferences preferences = context.getSharedPreferences(
				"settingPreferences", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();// �õ��༭������
		editor.putBoolean("isVibrate", isVibrate);
		editor.putBoolean("isPlayMusic", isPlayMusic);
		editor.putBoolean("isSpotAds", isSpotAds);
		editor.putInt("volume_value", volume_value);
		editor.commit();// ������д��ƫ�������ļ�
	}

	public Map<String, Boolean> getBooleanPreferences() {
		Map<String, Boolean> params = new HashMap<String, Boolean>();
		SharedPreferences preferences = context.getSharedPreferences(
				"settingPreferences", Context.MODE_PRIVATE);
		params.put("isPlayMusic", preferences.getBoolean("isPlayMusic", true));// trueΪĬ��ֵ
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
