/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zoipuus.balanceball.weiboapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.zoipuus.balanceball.ConstantUtil;
import com.zoipuus.balanceball.R;
import com.zoipuus.balanceball.util.ImageCacheUtil;

/**
 * 璇ョ被婕旂ず浜嗙涓夋柟搴旂敤濡備綍閫氳繃寰崥瀹㈡埛绔垎浜枃瀛椼?佸浘鐗囥?佽棰戙?侀煶涔愮瓑銆? 鎵ц娴佺▼锛?
 * 浠庢湰搴旂敤->寰崥->鏈簲鐢?
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class WBShareActivity extends Activity implements IWeiboHandler.Response {
	protected final class shareGame implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mTextCheckbox.isChecked() || mImageCheckbox.isChecked()) {
				sendMessage(mTextCheckbox.isChecked(),
						mImageCheckbox.isChecked());
			} else {
				Toast.makeText(WBShareActivity.this,
						R.string.weibosdk_share_chose_one, Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	@SuppressWarnings("unused")
	private static final String TAG = "WBShareActivity";

	public static final String KEY_SHARE_TYPE = "key_share_type";
	public static final int SHARE_CLIENT = 1;
	public static final int SHARE_ALL_IN_ONE = 2;
	/**
	 * 打开本地相册的requestcode.
	 */
	public static final int OPEN_PHOTO_REQUESTCODE = 0x1;

	/**
	 * 图片的target大小.
	 */
	private static final int target = 200;

	private TextView mTitleView;

	private ImageView mImageView;

	private CheckBox mTextCheckbox;

	private CheckBox mImageCheckbox;

	private Button mSharedBtn;

	/** 寰崥寰崥鍒嗕韩鎺ュ彛瀹炰緥 */
	private IWeiboShareAPI mWeiboShareAPI = null;

	private int mShareType = SHARE_CLIENT;

	/**
	 * @see {@link Activity#onCreate}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		initViews();

		mShareType = getIntent().getIntExtra(KEY_SHARE_TYPE, SHARE_CLIENT);

		// 鍒涘缓寰崥鍒嗕韩鎺ュ彛瀹炰緥
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this,
				ConstantUtil.APP_KEY);

		// 娉ㄥ唽绗笁鏂瑰簲鐢ㄥ埌寰崥瀹㈡埛绔腑锛屾敞鍐屾垚鍔熷悗璇ュ簲鐢ㄥ皢鏄剧ず鍦ㄥ井鍗氱殑搴旂敤鍒楄〃涓??
		// 浣嗚闄勪欢鏍忛泦鎴愬垎浜潈闄愰渶瑕佸悎浣滅敵璇凤紝璇︽儏璇锋煡鐪? Demo 鎻愮ず
		// NOTE锛氳鍔″繀鎻愬墠娉ㄥ唽锛屽嵆鐣岄潰鍒濆鍖栫殑鏃跺?欐垨鏄簲鐢ㄧ▼搴忓垵濮嬪寲鏃讹紝杩涜娉ㄥ唽
		mWeiboShareAPI.registerApp();

		// 褰? Activity 琚噸鏂板垵濮嬪寲鏃讹紙璇? Activity 澶勪簬鍚庡彴鏃讹紝鍙兘浼氱敱浜庡唴瀛樹笉瓒宠鏉?鎺変簡锛夛紝
		// 闇?瑕佽皟鐢? {@link IWeiboShareAPI#handleWeiboResponse}
		// 鏉ユ帴鏀跺井鍗氬鎴风杩斿洖鐨勬暟鎹??
		// 鎵ц鎴愬姛锛岃繑鍥? true锛屽苟璋冪敤 {@link IWeiboHandler.Response#onResponse}锛?
		// 澶辫触杩斿洖 false锛屼笉璋冪敤涓婅堪鍥炶皟
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
	}

	private void initViews() {
		mTitleView = (TextView) findViewById(R.id.share_title);
		mTitleView.setText(R.string.weibosdk_demo_share_to_weibo_title);
		mImageView = (ImageView) findViewById(R.id.share_imageview);
		mTextCheckbox = (CheckBox) findViewById(R.id.share_text_checkbox);
		mImageCheckbox = (CheckBox) findViewById(R.id.shared_image_checkbox);
		mImageCheckbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							openPhotos();
						} else {
							mImageView.setImageDrawable(getResources()
									.getDrawable(R.drawable.ic_launcher));
						}
					}
				});

		mSharedBtn = (Button) findViewById(R.id.share_to_btn);
		mSharedBtn.setOnClickListener(new shareGame());
	}

	/**
	 * 打开本地相册.
	 */
	private void openPhotos() {

		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");

		startActivityForResult(intent, OPEN_PHOTO_REQUESTCODE);

	}

	/**
	 * @see {@link Activity#onNewIntent}
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// 浠庡綋鍓嶅簲鐢ㄥ敜璧峰井鍗氬苟杩涜鍒嗕韩鍚庯紝杩斿洖鍒板綋鍓嶅簲鐢ㄦ椂锛岄渶瑕佸湪姝ゅ璋冪敤璇ュ嚱鏁?
		// 鏉ユ帴鏀跺井鍗氬鎴风杩斿洖鐨勬暟鎹紱鎵ц鎴愬姛锛岃繑鍥? true锛屽苟璋冪敤
		// {@link IWeiboHandler.Response#onResponse}锛涘け璐ヨ繑鍥? false锛屼笉璋冪敤涓婅堪鍥炶皟
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

	/**
	 * 鎺ユ敹寰鎴风鍗氳姹傜殑鏁版嵁銆? 褰撳井鍗氬鎴风鍞よ捣褰撳墠搴旂敤骞惰繘琛屽垎浜椂锛岃鏂规硶琚皟鐢ㄣ??
	 * 
	 * @param baseRequest
	 *            寰崥璇锋眰鏁版嵁瀵硅薄
	 * @see {@link IWeiboShareAPI#handleWeiboRequest}
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, R.string.weibosdk_demo_toast_share_success,
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled,
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(
					this,
					getString(R.string.weibosdk_demo_toast_share_failed)
							+ "Error Message: " + baseResp.errMsg,
					Toast.LENGTH_LONG).show();
			break;
		}
	}

	private void sendMessage(boolean hasText, boolean hasImage) {

		if (mShareType == SHARE_CLIENT) {
			if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
				int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
				if (supportApi >= 10351 /* ApiUtils.BUILD_INT_VER_2_2 */) {
					sendMultiMessage(hasText, hasImage);
				} else {
					sendSingleMessage(hasText, hasImage);
				}
			} else {
				Toast.makeText(this,
						R.string.weibosdk_demo_not_support_api_hint,
						Toast.LENGTH_SHORT).show();
			}
		} else if (mShareType == SHARE_ALL_IN_ONE) {
			sendMultiMessage(hasText, hasImage);
		}
	}

	/**
	 * 绗笁鏂瑰簲鐢ㄥ彂閫佽姹傛秷鎭埌寰崥锛屽敜璧峰井鍗氬垎浜晫闈€�� 褰搟@link
	 * IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 鏃讹紝鍙敮鎸佸垎浜崟鏉℃秷鎭紝鍗�
	 * 鏂囨湰銆佸浘鐗囥�佺綉椤点�侀煶涔愩�佽棰戜腑鐨勪竴绉嶏紝涓嶆敮鎸乂oice娑堟伅銆�
	 * 
	 * @param hasText
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁鏂囨湰
	 * @param hasImage
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁鍥剧墖
	 * @param hasWebpage
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁缃戦〉
	 * @param hasMusic
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁闊充箰
	 * @param hasVideo
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁瑙嗛
	 */
	private void sendSingleMessage(boolean hasText, boolean hasImage) {

		// 1. 鍒濆鍖栧井鍗氱殑鍒嗕韩娑堟伅
		// 鐢ㄦ埛鍙互鍒嗕韩鏂囨湰銆佸浘鐗囥�佺綉椤点�侀煶涔愩�佽棰戜腑鐨勪竴绉�
		WeiboMessage weiboMessage = new WeiboMessage();
		if (hasText) {
			weiboMessage.mediaObject = getTextObj();
		}
		if (hasImage) {
			weiboMessage.mediaObject = getImageObj();
		}
		/*
		 * if (hasVoice) { weiboMessage.mediaObject = getVoiceObj(); }
		 */

		// 2. 鍒濆鍖栦粠绗笁鏂瑰埌寰崥鐨勬秷鎭姹�
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		// 鐢╰ransaction鍞竴鏍囪瘑涓�涓姹�
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;

		// 3. 鍙戦�佽姹傛秷鎭埌寰崥锛屽敜璧峰井鍗氬垎浜晫闈�
		mWeiboShareAPI.sendRequest(WBShareActivity.this, request);
	}

	/**
	 * 绗笁鏂瑰簲鐢ㄥ彂閫佽姹傛秷鎭埌寰崥锛屽敜璧峰井鍗氬垎浜晫闈€?? 娉ㄦ剰锛氬綋
	 * {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351
	 * 鏃讹紝鏀寔鍚屾椂鍒嗕韩澶氭潯娑堟伅锛?
	 * 鍚屾椂鍙互鍒嗕韩鏂囨湰銆佸浘鐗囦互鍙婂叾瀹冨獟浣撹祫婧愶紙缃戦〉銆侀煶涔愩?佽棰戙?佸０闊充腑鐨勪竴绉嶏級銆?
	 * 
	 * @param hasText
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁鏂囨湰
	 * @param hasImage
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁鍥剧墖
	 * @param hasWebpage
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁缃戦〉
	 * @param hasMusic
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁闊充箰
	 * @param hasVideo
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁瑙嗛
	 * @param hasVoice
	 *            鍒嗕韩鐨勫唴瀹规槸鍚︽湁澹伴煶
	 */
	private void sendMultiMessage(boolean hasText, boolean hasImage) {

		// 1. 鍒濆鍖栧井鍗氱殑鍒嗕韩娑堟伅
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		if (hasText) {
			weiboMessage.textObject = getTextObj();
		} else {
			weiboMessage.textObject = getTextObj(R.string.weibosdk_app_url);
		}

		if (hasImage) {
			weiboMessage.imageObject = getImageObj();
		}

		// 2. 鍒濆鍖栦粠绗笁鏂瑰埌寰崥鐨勬秷鎭姹?
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 鐢╰ransaction鍞竴鏍囪瘑涓?涓姹?
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		// 3. 鍙戦?佽姹傛秷鎭埌寰崥锛屽敜璧峰井鍗氬垎浜晫闈?
		if (mShareType == SHARE_CLIENT) {
			mWeiboShareAPI.sendRequest(WBShareActivity.this, request);
		} else if (mShareType == SHARE_ALL_IN_ONE) {
			AuthInfo authInfo = new AuthInfo(this, ConstantUtil.APP_KEY,
					ConstantUtil.REDIRECT_URL, ConstantUtil.SCOPE);
			Oauth2AccessToken accessToken = AccessTokenKeeper
					.readAccessToken(getApplicationContext());
			String token = "";
			if (accessToken != null) {
				token = accessToken.getToken();
			}
			mWeiboShareAPI.sendRequest(this, request, authInfo, token,
					new WeiboAuthListener() {

						@Override
						public void onWeiboException(WeiboException arg0) {
						}

						@Override
						public void onComplete(Bundle bundle) {
							// TODO Auto-generated method stub
							Oauth2AccessToken newToken = Oauth2AccessToken
									.parseAccessToken(bundle);
							AccessTokenKeeper.writeAccessToken(
									getApplicationContext(), newToken);
							Toast.makeText(
									getApplicationContext(),
									"onAuthorizeComplete token = "
											+ newToken.getToken(),
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onCancel() {
						}
					});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case OPEN_PHOTO_REQUESTCODE:
			if (resultCode == RESULT_OK) {
				// 如果用这个方法，Options为null时候，就是默认decode会出现oom哦.
				// Bitmap bm = ImageCacheUtil.decode(null, null,
				// ImageCacheDemoActivity.this, data.getData(), null);

				// 这里调用这个方法就不会oom.屌丝们就用这个方法吧.
				Bitmap bm = ImageCacheUtil.getResizedBitmap(null, null,
						WBShareActivity.this, data.getData(), target, false);
				mImageView.setImageBitmap(bm);
			}

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 鑾峰彇鍒嗕韩鐨勬枃鏈ā鏉裤??
	 * 
	 * @return 鍒嗕韩鐨勬枃鏈ā鏉?
	 */
	private String getSharedText() {
		String text = getString(R.string.weibosdk_demo_share_text_template);

		return text;
	}

	/**
	 * 鍒涘缓鏂囨湰娑堟伅瀵硅薄銆?
	 * 
	 * @return 鏂囨湰娑堟伅瀵硅薄銆?
	 */
	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = getSharedText()
				+ getString(R.string.weibosdk_app_url);
		return textObject;
	}

	private TextObject getTextObj(int id) {
		TextObject textObject = new TextObject();
		textObject.text = getString(R.string.weibosdk_app_url);
		return textObject;
	}

	/**
	 * 鍒涘缓鍥剧墖娑堟伅瀵硅薄銆?
	 * 
	 * @return 鍥剧墖娑堟伅瀵硅薄銆?
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView
				.getDrawable();
		imageObject.setImageObject(bitmapDrawable.getBitmap());
		return imageObject;
	}
}
