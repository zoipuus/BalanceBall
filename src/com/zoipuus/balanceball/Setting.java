package com.zoipuus.balanceball;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Setting extends Activity {

	private CheckBox isPlayMusicCB;
	private SeekBar volume_seekBar;
	private PreferencesService mPreferencesService = null;
	private CheckBox isVibrate;
	private CheckBox isSpotAds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		sharedPreferences();
		mPreferencesService = new PreferencesService(this);
		readPreferences();
		TextView btn_SettingBack = (TextView) findViewById(R.id.btn_SettingBack);
		btn_SettingBack.setClickable(true);
		btn_SettingBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private void sharedPreferences() {
		// TODO Auto-generated method stub
		isVibrate = (CheckBox) findViewById(R.id.isVibrate);
		isPlayMusicCB = (CheckBox) findViewById(R.id.isplayMusicCB);
		isSpotAds = (CheckBox) findViewById(R.id.isSpotAds);
		final TextView sensitivity = (TextView) findViewById(R.id.textView2);
		sensitivity.setTextSize(20);
		volume_seekBar = (SeekBar) findViewById(R.id.seekBar1);
		volume_seekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						sensitivity.setText("¡È√Ù∂»£∫" + Integer.toString(progress));
					}
				});
	}

	private void readPreferences() {
		// TODO Auto-generated method stub
		Map<String, Boolean> paramsBoolean = mPreferencesService
				.getBooleanPreferences();
		isVibrate.setChecked(paramsBoolean.get("isVibrate"));
		isPlayMusicCB.setChecked(paramsBoolean.get("isPlayMusic"));
		isSpotAds.setChecked(paramsBoolean.get("isSpotAds"));
		Map<String, Integer> paramsInteger = mPreferencesService
				.getIntegerPreferences();
		volume_seekBar.setProgress(paramsInteger.get("volume_value"));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Boolean Vibrate = isVibrate.isChecked();
		Boolean isPlayMusic = isPlayMusicCB.isChecked();
		Boolean isSpotAdv = isSpotAds.isChecked();
		int volume_value = volume_seekBar.getProgress();
		mPreferencesService.savePreferences(Vibrate, isPlayMusic, isSpotAdv,
				volume_value);
	}
}
