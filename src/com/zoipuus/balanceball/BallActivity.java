package com.zoipuus.balanceball;

import net.youmi.android.AdManager;
import net.youmi.android.offers.EarnPointsOrderInfo;
import net.youmi.android.offers.EarnPointsOrderList;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsEarnNotify;
import net.youmi.android.offers.PointsManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import org.jbox2d.common.Vec2;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.zoipuus.balanceball.database.DatabaseHelper;
import com.zoipuus.balanceball.rigidbody.MyBody;
import com.zoipuus.balanceball.rigidbody.MyLockBody;
import com.zoipuus.balanceball.rigidbody.MyRotateBody;
import com.zoipuus.balanceball.rigidbody.MyUnkownStoneBody;
import com.zoipuus.balanceball.util.NumericalCalculation;
import com.zoipuus.balanceball.util.SoundManager;
import com.zoipuus.balanceball.view.BitmapManager;
import com.zoipuus.balanceball.view.GameView;
import com.zoipuus.balanceball.view.WelcomeView;
import com.zoipuus.balanceball.weiboapi.AccessTokenKeeper;
import com.zoipuus.balanceball.weiboapi.WBShareActivity;

public class BallActivity extends Activity implements PointsChangeNotify,
		PointsEarnNotify {

	static class myHandler extends Handler {

	}

	private boolean isExit = false;
	private int activityStatus;
	private BitmapManager bitmapManager;
	private NumericalCalculation numericalCalculation;
	private Dialog progressDialog;
	private int GameMode = 1;
	private int GameLevel = 1;
	private SensorManager mSensorMgr;
	private Sensor mSensor;
	private GameView gv;
	private Vibrator vibrator;// 震动
	public boolean pause = false;
	private AuthInfo mAuthInfo;
	private Oauth2AccessToken mAccessToken;
	private SsoHandler mSsoHandler;

	private SensorEventListener mySensorListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];

			// X轴的速度
			// float speadX = (x - bx) / (System.currentTimeMillis() - btime);
			// y轴的速度
			// float speadY = (y - by) / (System.currentTimeMillis() - btime);
			// 重新赋值
			ConstantUtil.GRAVITYTEMP = new Vec2(y * ConstantUtil.lingMinDu, x
					* ConstantUtil.lingMinDu);

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	private final myHandler mHandler = new myHandler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			default:
				break;
			case ConstantUtil.btn_Pause:
				btn_Pause();
				break;
			// 震动
			case ConstantUtil.Vibrate:
				vibrator.vibrate(ConstantUtil.COLLISION_SOUND_PATTERN, -1);
				break;
			// 插入数据
			case ConstantUtil.InsertData:
				InsertData();
				break;
			// 添加广告条
			case ConstantUtil.AdvertisingView:
				if (ConstantUtil.isOpenAds) {
					AdvertisingView();
				}
				break;
			// 播放碰撞音效
			case ConstantUtil.SOUND_Collision:
				mSoundPool.play(ConstantUtil.SOUND_Collision);
				break;
			// 增加钥匙
			case ConstantUtil.getKey:
				getKey();
				break;
			// 减少钥匙
			case ConstantUtil.lostKey:
				ConstantUtil.KeyNumber--;
				upDataKeyNumber();
				break;
			// 消耗积分
			case ConstantUtil.openLock:
				pause = true;
				if (pointsBalance >= 20) {
					int data = msg.arg1;
					DecideDialog("提示", "消耗20积分可以开启，是否继续？",
							ConstantUtil.openLock, data);
				} else {
					DecideDialog("提示", "你目前积分不足够开启，点击确定可以免费获取积分。",
							ConstantUtil.showAccessIntegralDialog, -1);
				}
				break;
			// 获得奖励
			case ConstantUtil.getGold:
				AccessIntegral();
				break;
			// 显示加载界面
			case ConstantUtil.dismissProcessDialog:
				if (progressDialog != null && progressDialog.isShowing()) {
					dismissprogressDialog();
				}
				break;
			// 显示欢迎界面
			case ConstantUtil.showWelcomeView:
				initWelcomeView();
				break;
			// 弹出失败对话框
			case ConstantUtil.showFailView:
				pause = true;
				createDialog(ConstantUtil.showFailView);
				break;
			// 弹出胜利对话框
			case ConstantUtil.showWinView:
				pause = true;
				createDialog(ConstantUtil.showWinView);
				break;
			// 弹出退出对话框
			case ConstantUtil.showExitGameDialog:
				DecideDialog("离开游戏", "是否离开游戏？",
						ConstantUtil.showExitGameDialog, -1);
				break;
			// 弹出正常关卡对话框
			case ConstantUtil.showSelectLevelDialog:
				GameMode = ConstantUtil.GameMode_Normal;
				showSelectLevelDialog(nCursor);
				break;
			// 弹出挑战关卡对话框
			case ConstantUtil.showChallengeTimeDialog:
				GameMode = ConstantUtil.GameMode_Challenge;
				showSelectLevelDialog(cCursor);
				break;
			// 显示获取积分界面
			case ConstantUtil.showAccessIntegralDialog:
				accessIntegralView();
				break;
			case ConstantUtil.showShareGameDialog:
				ShareGameView();
				break;
			case ConstantUtil.showHelpGameDialog:
				GameHelpView();
				break;
			// 显示设置界面
			case ConstantUtil.showGameSettingDialog:
				GameSettingView();
				break;
			case ConstantUtil.GameMode_Normal:
			case ConstantUtil.GameMode_Challenge:
				startGameView();
				break;
			}
		}

		private void dismissprogressDialog() {
			// TODO Auto-generated method stub
			progressDialog.cancel();
		}

	};
	private PowerManager powerManager;
	private WakeLock wakeLock;
	private SoundManager mSoundPool;
	private String win_msg;

	protected void DecideDialog(String title, String msg, final int type,
			final int data) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(BallActivity.this,
				R.style.CustomDialog);
		dialog.setContentView(R.layout.dialog_exit);
		TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
		TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
		tv_title.setText(title);
		tv_msg.setText(msg);
		tv_title.setTextSize(15);
		tv_msg.setTextSize(17);
		final Button btn_sure = (Button) dialog.findViewById(R.id.btn_sure);
		final Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btn_sure.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn_sure.setBackgroundResource(R.drawable.bg_dialog_btn_down);
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					btn_sure.setBackgroundResource(R.drawable.bg_dialog_btn);
				}
				return false;
			}
		});
		btn_cancel.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn_cancel
							.setBackgroundResource(R.drawable.bg_dialog_btn_down);
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					btn_cancel.setBackgroundResource(R.drawable.bg_dialog_btn);
				}
				return false;
			}
		});
		btn_sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				pause = false;
				switch (type) {
				case ConstantUtil.lostKey:
				case ConstantUtil.spendPoints:
					// 生成ContentValue对象
					ContentValues values = new ContentValues();
					// 向该对象插入键值对，其中键为列名，值为希望插入的值
					values.put("isLock", 0);
					if (type == ConstantUtil.lostKey) {
						ConstantUtil.KeyNumber--;
						upDataKeyNumber();
					} else {
						PointsManager.getInstance(BallActivity.this)
								.spendPoints(20);
					}
					if (GameMode == ConstantUtil.GameMode_Normal) {
						// 更新下一关
						mDatabase.update("normal_level", values, "level=?",
								new String[] { Integer.toString(GameLevel) });
						mHandler.sendEmptyMessage(ConstantUtil.showSelectLevelDialog);
					} else {
						// 更新下一关
						mDatabase.update("challenge_limit", values, "level=?",
								new String[] { Integer.toString(GameLevel) });
						mHandler.sendEmptyMessage(ConstantUtil.showChallengeTimeDialog);
					}
					upDataDB();
					break;
				case ConstantUtil.openLock:
					openLock(data);
					break;
				case ConstantUtil.playerRevive:
					playerRevive();
					break;
				case ConstantUtil.showExitGameDialog:
					BallActivity.this.finish();
					break;
				case ConstantUtil.showAccessIntegralDialog:
					accessIntegralView();
					break;
				}
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				pause = false;
			}
		});
		dialog.show();
	}

	protected void btn_Pause() {
		// TODO Auto-generated method stub
		gv.myPlayer.body.setLinearVelocity(new Vec2(0, 0));
		this.pause = true;
		if (ConstantUtil.isOpenAds) {
			// 展示插播广告，可以不调用loadSpot独立使用
			SpotManager.getInstance(BallActivity.this).showSpotAds(
					BallActivity.this, new SpotDialogListener() {
						@Override
						public void onShowSuccess() {
						}

						@Override
						public void onShowFailed() {
						}

						@Override
						public void onSpotClosed() {
						}

					}); // //
		}
	}

	protected void getKey() {
		// TODO Auto-generated method stub
		mSoundPool.play(ConstantUtil.SOUND_AccessIntegral);
		keyReward++;
		gv.reward = "闯关成功后可获得" + Integer.toString(pointsReward) + "积分,"
				+ Integer.toString(keyReward) + "把钥匙";
	}

	protected void upDataDB() {
		// TODO Auto-generated method stub
		mDatabase.close();
		nCursor.close();
		cCursor.close();
		// 数据库操作
		DatabaseHelper dbHelper = new DatabaseHelper(BallActivity.this,
				"Game_Database");
		mDatabase = dbHelper.getWritableDatabase();
		nCursor = mDatabase.query("normal_level", new String[] { "level",
				"isLock", "isWin" }, null, null, null, null, null);
		cCursor = mDatabase.query("challenge_limit", new String[] { "level",
				"isLock", "isWin", "minute", "second" }, null, null, null,
				null, null);
	}

	protected void openLock(int data) {
		// TODO Auto-generated method stub
		PointsManager.getInstance(this).spendPoints(20);
		MyLockBody myLock;
		for (MyBody mb : this.gv.bl) {
			if (mb instanceof MyLockBody) {
				myLock = (MyLockBody) mb;
				if (myLock.detonate_num == data) {
					myLock.isDraw = false;
					break;
				}
			}
		}
		for (MyUnkownStoneBody musb : this.gv.musbList) {
			if (musb instanceof MyUnkownStoneBody) {
				if (musb.stone_unkown_num == data) {
					musb.function(null);
				}
			} else if (musb instanceof MyRotateBody) {
				MyRotateBody mrb = (MyRotateBody) musb;
				if (mrb.stone_unkown_num == data) {
					mrb.function(null);
				}
			}
		}
	}

	protected void InsertData() {
		// TODO Auto-generated method stub
		// 生成ContentValue对象
		ContentValues normal_values = new ContentValues();
		// 向该对象插入键值对，其中键为列名，值为希望插入的值
		normal_values.put("level", 1);
		normal_values.put("isLock", 0);
		normal_values.put("isWin", 0);
		// 调用insert()方法，向数据库插入数据
		mDatabase.insert("normal_level", null, normal_values);
		for (int i = 2; i < 11; i++) {
			normal_values.put("level", i);
			normal_values.put("isLock", 1);
			normal_values.put("isWin", 0);
			// 调用insert()方法，向数据库插入数据
			mDatabase.insert("normal_level", null, normal_values);
		}
		// 生成ContentValue对象
		ContentValues challenge_values = new ContentValues();
		// 向该对象插入键值对，其中键为列名，值为希望插入的值
		challenge_values.put("level", 1);
		challenge_values.put("isLock", 0);
		challenge_values.put("isWin", 0);
		challenge_values.put("minute", 0);
		challenge_values.put("second", 0);
		// 调用insert()方法，向数据库插入数据
		mDatabase.insert("challenge_limit", null, challenge_values);
		for (int i = 2; i < 11; i++) {
			challenge_values.put("level", i);
			challenge_values.put("isLock", 1);
			challenge_values.put("isWin", 0);
			challenge_values.put("minute", 0);
			challenge_values.put("second", 0);
			// 调用insert()方法，向数据库插入数据
			mDatabase.insert("challenge_limit", null, challenge_values);
		}
	}

	protected void AdvertisingView() {
		// TODO Auto-generated method stub// 广告条接口调用（适用于应用）
		// 将广告条adView添加到需要展示的layout控件中
		// LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// adLayout.addView(adView);
		// 广告条接口调用（适用于游戏）

		// 实例化LayoutParams(重要)
		// FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
		// FrameLayout.LayoutParams.WRAP_CONTENT,
		// FrameLayout.LayoutParams.WRAP_CONTENT);
		// // 设置广告条的悬浮位置
		// layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		// // 实例化广告条
		// AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// // 调用Activity的addContentView函数
		// this.addContentView(adView, layoutParams);
		//
		// // 监听广告条接口
		// adView.setAdListener(new AdViewListener() {
		//
		// @Override
		// public void onSwitchedAd(AdView arg0) {
		// }
		//
		// @Override
		// public void onReceivedAd(AdView arg0) {
		//
		// }
		//
		// @Override
		// public void onFailedToReceivedAd(AdView arg0) {
		// }
		// });
	}

	protected void ShareGameView() {
		mAuthInfo = new AuthInfo(this, ConstantUtil.APP_KEY,
				ConstantUtil.REDIRECT_URL, ConstantUtil.SCOPE);
		mSsoHandler = new SsoHandler(BallActivity.this, mAuthInfo);
		//授权操作
		mSsoHandler.authorize(new AuthListener());
	}

	private int[] imgHelpId = { R.drawable.ic_detonate_horizontal,
			R.drawable.ic_hole, R.drawable.ic_triangle_up,
			R.drawable.ic_unkown, R.drawable.ic_lock };
	private int[] textHelpId = { R.string.tv_detonate, R.string.tv_hole,
			R.string.tv_triangle, R.string.tv_unkown, R.string.tv_lock };

	protected void GameHelpView() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(BallActivity.this,
				R.style.CustomDialog);
		dialog.setContentView(R.layout.dialog_help);
		final Button btn_back = (Button) dialog
				.findViewById(R.id.btn_back_help);
		btn_back.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn_back.setBackgroundResource(R.drawable.bg_dialog_btn_down);
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					btn_back.setBackgroundResource(R.drawable.bg_dialog_btn);
				}
				return false;
			}
		});
		ListView mListView = (ListView) dialog.findViewById(R.id.listViewHelp);
		BaseAdapter mBaseAdapter = new BaseAdapter() {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				LinearLayout mLinearLayout = new LinearLayout(BallActivity.this);
				mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);// 设置朝向
				mLinearLayout.setPadding(5, 5, 5, 5);// 四周空白
				ImageView mImageView = new ImageView(BallActivity.this);
				mImageView.setImageDrawable(getResources().getDrawable(
						imgHelpId[arg0]));
				mLinearLayout.addView(mImageView);
				TextView msg = new TextView(BallActivity.this);
				msg.setPadding(5, 5, 5, 5);
				msg.setTextSize(24);
				msg.setText(getResources().getString(textHelpId[arg0]));
				msg.setGravity(Gravity.LEFT);
				mLinearLayout.addView(msg);

				return mLinearLayout;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return imgHelpId.length;
			}
		};

		mListView.setAdapter(mBaseAdapter);

		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}

		});
		dialog.show();
	}

	protected void GameSettingView() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClassName(BallActivity.this,
				"com.zoipuus.balanceball.Setting");
		startActivityForResult(intent, ConstantUtil.showGameSettingDialog);
	}

	protected void accessIntegralView() {
		// TODO Auto-generated method stub
		// 调用方式一：直接打开全屏积分墙
		OffersManager.getInstance(BallActivity.this).showOffersWall();

		// 调用方式二：直接打开全屏积分墙，并且监听积分墙退出的事件onDestory
		// OffersManager.getInstance(MenuActivity.this).showOffersWall(
		// new Interface_ActivityListener() {
		//
		// /**
		// * 但积分墙销毁的时候，即积分墙的Activity调用了onDestory的时候回调
		// */
		// @Override
		// public void onActivityDestroy(Context context) {
		// Toast.makeText(context, "全屏积分墙退出了",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
	}

	protected void AccessIntegral() {
		// TODO Auto-generated method stub
		mSoundPool.play(ConstantUtil.SOUND_AccessIntegral);
		pointsReward += 10;
		gv.reward = "闯关成功后可获得" + Integer.toString(pointsReward) + "积分,"
				+ Integer.toString(keyReward) + "把钥匙";
	}

	protected void showSelectLevelDialog(final Cursor localCursor) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(BallActivity.this,
				R.style.CustomDialog);
		dialog.setContentView(R.layout.select_level);
		final Button btn_back = (Button) dialog.findViewById(R.id.btn_back);
		btn_back.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn_back.setBackgroundResource(R.drawable.bg_dialog_btn_down);
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					btn_back.setBackgroundResource(R.drawable.bg_dialog_btn);
				}
				return false;
			}
		});
		ListView mListView = (ListView) dialog.findViewById(R.id.listView1);
		BaseAdapter mBaseAdapter = new BaseAdapter() {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				localCursor.moveToPosition(arg0);
				int isWin = localCursor.getInt(localCursor
						.getColumnIndex("isWin"));
				String isWinmsg = null;
				if (isWin == 1) {
					if (GameMode == ConstantUtil.GameMode_Challenge) {
						int minute = localCursor.getInt(localCursor
								.getColumnIndex("minute"));
						int second = localCursor.getInt(localCursor
								.getColumnIndex("second"));
						isWinmsg = "最好成绩：" + Integer.toString(minute) + "分"
								+ Integer.toString(second) + "秒";
					} else {
						isWinmsg = "已通关";
					}
				} else {
					isWinmsg = "未通关";
				}
				LinearLayout mLinearLayout = new LinearLayout(BallActivity.this);
				mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);// 设置朝向
				mLinearLayout.setPadding(5, 5, 5, 5);// 四周空白
				ImageView mImageView = new ImageView(BallActivity.this);
				mImageView.setImageDrawable(getResources().getDrawable(
						imgId[localCursor.getInt(localCursor
								.getColumnIndex("isLock"))]));
				mImageView.setScaleType(ScaleType.FIT_XY);
				mImageView.setLayoutParams(new Gallery.LayoutParams(50, 50));
				mLinearLayout.addView(mImageView);
				TextView msg = new TextView(BallActivity.this);
				msg.setPadding(5, 5, 5, 5);
				msg.setTextSize(24);
				msg.setText("关卡："
						+ Integer.toString(localCursor.getInt(localCursor
								.getColumnIndex("level"))) + "\n" + isWinmsg);
				msg.setGravity(Gravity.LEFT);
				mLinearLayout.addView(msg);

				return mLinearLayout;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return localCursor.getCount();
			}
		};

		mListView.setAdapter(mBaseAdapter);

		// 为mListView对象设置监听器
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				localCursor.moveToPosition(arg2);
				GameLevel = arg2 + 1;
				dialog.cancel();
				if (localCursor.getInt(localCursor.getColumnIndex("isLock")) == 0) {
					mHandler.sendEmptyMessage(GameMode);
				} else {
					if (ConstantUtil.KeyNumber > 0) {
						DecideDialog("提示", "开启此关卡将用掉一条钥匙，是否继续？",
								ConstantUtil.lostKey, GameLevel);
					} else {
						if (pointsBalance >= 20) {
							DecideDialog("提示", "消耗20积分可以开启，是否继续？",
									ConstantUtil.spendPoints, GameLevel);
						} else {
							DecideDialog("提示", "你目前积分不足够开启，点击确定可以免费获取积分。",
									ConstantUtil.showAccessIntegralDialog, -1);
						}
					}
				}
			}

		});
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}

		});
		dialog.show();
	}

	protected void startGameView() {
		// TODO Auto-generated method stub
		gv = null;
		if (GameLevel > 4) {
			pointsReward = 20;
		} else {
			pointsReward = 0;
		}
		gv = new GameView(this, this.mHandler, GameLevel, GameMode);
		gv.pointsBalance = "积分余额：" + Integer.toString(pointsBalance);
		gv.reward = "闯关成功后可获得" + Integer.toString(pointsReward) + "积分,"
				+ Integer.toString(keyReward) + "把钥匙";
		gv.keyNumber = "剩余钥匙：" + Integer.toString(ConstantUtil.KeyNumber);
		gv.initBitmapData(bitmapManager, numericalCalculation);
		// 屏幕常亮
		this.wakeLock.acquire();
		setContentView(gv);
		// 注册listener，第三个参数是检测的精确度

		// SENSOR_DELAY_FASTEST 最灵敏 因为太快了没必要使用

		// SENSOR_DELAY_GAME 游戏开发中使用

		// SENSOR_DELAY_NORMAL 正常速度

		// SENSOR_DELAY_UI 最慢的速度

		mSensorMgr.registerListener(mySensorListener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);
		activityStatus = ConstantUtil.showGameView;
	}

	boolean win = false;

	protected void createDialog(int id) {
		// TODO Auto-generated method stub
		win = false;
		switch (id) {
		default:
			break;
		case ConstantUtil.showWinView:
			win_msg = "恭喜你闯关成功！";
			win = true;
			String rewardMsg = null;
			if (pointsReward != 0 && keyReward == 0) {
				rewardMsg = "恭喜你获得了" + Integer.toString(pointsReward) + "积分";
			} else if (pointsReward == 0 && keyReward != 0) {
				rewardMsg = "恭喜你获得了" + Integer.toString(keyReward) + "把钥匙";
			} else if (pointsReward != 0 && keyReward != 0) {
				rewardMsg = "恭喜你获得了" + Integer.toString(pointsReward) + "积分和"
						+ Integer.toString(keyReward) + "把钥匙";
			}
			if (rewardMsg != null) {
				Toast.makeText(this, rewardMsg, Toast.LENGTH_SHORT).show();
			}
			PointsManager.getInstance(this).awardPoints(pointsReward);
			ConstantUtil.KeyNumber += keyReward;
			upDataKeyNumber();
			keyReward = 0;
			pointsReward = 0;
			gv.reward = "闯关成功后可获得" + Integer.toString(pointsReward) + "积分,"
					+ Integer.toString(keyReward) + "把钥匙";
			cCursor.moveToPosition(GameLevel - 1);
			int minute = cCursor.getInt(cCursor.getColumnIndex("minute"));
			int second = cCursor.getInt(cCursor.getColumnIndex("second"));
			int isWin = cCursor.getInt(cCursor.getColumnIndex("isWin"));
			// 生成ContentValue对象
			ContentValues values = new ContentValues();
			// 向该对象插入键值对，其中键为列名，值为希望插入的值
			values.put("isLock", 0);
			values.put("isWin", 0);
			GameLevel++;
			if (GameMode == ConstantUtil.GameMode_Normal) {
				if (GameLevel <= 10) {
					// 更新下一关
					mDatabase.update("normal_level", values, "level=?",
							new String[] { Integer.toString(GameLevel) });
				}
				// 更新刚通过的关卡
				values.put("isWin", 1);
				mDatabase.update("normal_level", values, "level=?",
						new String[] { Integer.toString(GameLevel - 1) });
			} else {
				if (GameLevel <= 10) {
					// 更新下一关
					mDatabase.update("challenge_limit", values, "level=?",
							new String[] { Integer.toString(GameLevel) });
				}
				// 更新刚通过的关卡
				values.put("isWin", 1);
				int min = this.gv.usedTime / 60;
				int sec = this.gv.usedTime % 60;
				if ((isWin != 1) || (minute * 60 + second > this.gv.usedTime)) {
					// 刷新了记录
					values.put("minute", min);
					values.put("second", sec);
					win_msg = "恭喜你刷新了记录！" + "\n" + "用时" + Integer.toString(min)
							+ "分" + Integer.toString(sec) + "秒";
				}
				// if (isWin == 1) {
				// // 这一关之前已通关
				// if (minute * 60 + second > this.gv.usedTime) {
				// // 刷新了记录
				// values.put("minute", min);
				// values.put("second", sec);
				// win_msg = "恭喜你刷新了记录！" + "\n" + "用时"
				// + Integer.toString(min) + "分"
				// + Integer.toString(sec) + "秒";
				// }
				// } else {
				// values.put("minute", min);
				// values.put("second", sec);
				// }
				mDatabase.update("challenge_limit", values, "level=?",
						new String[] { Integer.toString(GameLevel - 1) });
			}
			GameLevel--;
			upDataDB();
		case ConstantUtil.showFailView:
			final Dialog dialog = new Dialog(BallActivity.this,
					R.style.CustomDialog);
			dialog.setContentView(R.layout.dialog_finish);
			TextView finish_msg = (TextView) dialog
					.findViewById(R.id.tv_finish_dialog_message);
			finish_msg.setTextSize(20);
			final Button btn_next = (Button) dialog
					.findViewById(R.id.btn_finish_dialog_next);
			if (win) {
				if (GameLevel < 10) {
					btn_next.setText("下一关");
				} else {
					btn_next.setVisibility(View.GONE);
				}
				finish_msg.setText(win_msg);
			} else {
				btn_next.setText("复活");
				finish_msg.setText("很遗憾你闯关失败！");
			}
			final Button btn_retry = (Button) dialog
					.findViewById(R.id.btn_finish_dialog_retry);
			final Button btn_home = (Button) dialog
					.findViewById(R.id.btn_finish_dialog_home);
			btn_next.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						btn_next.setBackgroundResource(R.drawable.ic_next_down);
					} else if (event.getAction() == MotionEvent.ACTION_UP
							|| event.getAction() == MotionEvent.ACTION_CANCEL) {
						btn_next.setBackgroundResource(R.drawable.ic_next);
					}
					return false;
				}
			});
			btn_retry.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						btn_retry
								.setBackgroundResource(R.drawable.ic_retry_down);
					} else if (event.getAction() == MotionEvent.ACTION_UP
							|| event.getAction() == MotionEvent.ACTION_CANCEL) {
						btn_retry.setBackgroundResource(R.drawable.ic_retry);
					}
					return false;
				}
			});
			btn_home.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						btn_home.setBackgroundResource(R.drawable.ic_home_down);
					} else if (event.getAction() == MotionEvent.ACTION_UP
							|| event.getAction() == MotionEvent.ACTION_CANCEL) {
						btn_home.setBackgroundResource(R.drawable.ic_home);
					}
					return false;
				}
			});
			btn_next.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.cancel();
					if (win) {
						GameLevel++;
						startGameView();
					} else {
						if (pointsBalance >= 20) {
							DecideDialog("提示", "消耗20积分可以复活，是否继续？",
									ConstantUtil.playerRevive, -1);
							dialog.cancel();
						} else {
							DecideDialog("提示", "你目前积分不足够复活，点击确定可以免费获取积分。",
									ConstantUtil.showAccessIntegralDialog, -1);
						}
					}
				}
			});
			btn_retry.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.cancel();
					keyReward = 0;
					pointsReward = 0;
					startGameView();
				}
			});
			btn_home.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.cancel();
					mHandler.sendEmptyMessage(ConstantUtil.showWelcomeView);
				}
			});
			dialog.show();
			break;

		case ConstantUtil.showProcessDialog:
			LayoutInflater inflater = LayoutInflater.from(BallActivity.this);
			View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
			LinearLayout layout = (LinearLayout) v
					.findViewById(R.id.dialog_view);// 加载布局
			// dialog_loading.xml中的ImageView
			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
			TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
					BallActivity.this, R.anim.loading_animation);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
			tipTextView.setText(getString(R.string.tv_dialog_progress));// 设置加载信息

			Dialog loadingDialog = new Dialog(BallActivity.this,
					R.style.loading_dialog);// 创建自定义样式dialog

			loadingDialog.setCancelable(false);// 不可以用“返回键”取消
			loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
			progressDialog = loadingDialog;
			progressDialog.show();
			break;
		}
	}

	protected void playerRevive() {
		// TODO Auto-generated method stub
		PointsManager.getInstance(this).spendPoints(20);
		this.gv.playerRevive();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 读取默认设置
		preferences = this.getSharedPreferences("settingPreferences",
				Context.MODE_PRIVATE);
		ConstantUtil.isVibrate = preferences.getBoolean("isVibrate", true);
		ConstantUtil.isPlayMusic = preferences.getBoolean("isPlayMusic", true);
		ConstantUtil.isOpenAds = preferences.getBoolean("isSpotAds", true);
		ConstantUtil.lingMinDu = preferences.getInt("volume_value", 10);

		mSoundPool = new SoundManager(this);

		initYouMi();
		bitmapManager = new BitmapManager(BallActivity.this);
		numericalCalculation = new NumericalCalculation();
		bitmapManager.initBitmapData(numericalCalculation);
		/** 得到SensorManager对象 **/
		mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (mSensorMgr == null) {
			Toast.makeText(BallActivity.this, "没有SensorManager对象",
					Toast.LENGTH_SHORT).show();
		} else {
			mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			if (mSensor == null) {
				Toast.makeText(BallActivity.this, "没有mSensor对象",
						Toast.LENGTH_SHORT).show();
			}
		}
		/** 得到震动器对象 **/
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		this.powerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		this.wakeLock = this.powerManager.newWakeLock(
				PowerManager.FULL_WAKE_LOCK, "MyLock");

		// 数据库操作
		DatabaseHelper dbHelper = new DatabaseHelper(BallActivity.this,
				"Game_Database");
		mDatabase = dbHelper.getWritableDatabase();
		nCursor = mDatabase.query("normal_level", new String[] { "level",
				"isLock", "isWin" }, null, null, null, null, null);
		cCursor = mDatabase.query("challenge_limit", new String[] { "level",
				"isLock", "isWin", "minute", "second" }, null, null, null,
				null, null);
		if (gv != null) {
			setContentView(gv);
		} else {
			initWelcomeView();
		}
	}

	private void initYouMi() {
		// TODO Auto-generated method stub
		// 有米android 积分墙sdk 5.0.0之后支持定制浏览器顶部标题栏的部分UI
		// setOfferBrowserConfig();

		// (可选)关闭有米log输出，建议开发者在嵌入有米过程中不要关闭，以方便随时捕捉输出信息，出问题时可以快速定位问题
		// AdManager.getInstance(Context context).setEnableDebugLog(false);

		// 设置为true时，可以获取测试广告，正式发布请设置此参数为false
		// 初始化接口，应用启动的时候调用，参数：appId, appSecret
		AdManager.getInstance(this).init("18bc37fda5164c79",
				"80bf58202118a208", false);

		// (可选)开启用户数据统计服务,(sdk v4.08之后新增功能)默认不开启，传入false值也不开启，只有传入true才会调用
		AdManager.getInstance(this).setUserDataCollect(true);

		// 如果使用积分广告，请务必调用积分广告的初始化接口:
		OffersManager.getInstance(this).onAppLaunch();

		// （可选）注册积分监听-随时随地获得积分的变动情况
		PointsManager.getInstance(this).registerNotify(this);

		// （可选）注册积分订单赚取监听（sdk v4.10版本新增功能）
		PointsManager.getInstance(this).registerPointsEarnNotify(this);

		// (可选)设置是否在通知栏显示下载相关提示。默认为true，标识开启；设置为false则关闭。（sdk v4.10版本新增功能）
		// AdManager.setIsDownloadTipsDisplayOnNotification(false);

		// (可选)设置安装完成后是否在通知栏显示已安装成功的通知。默认为true，标识开启；设置为false则关闭。（sdk
		// v4.10版本新增功能）
		// AdManager.setIsInstallationSuccessTipsDisplayOnNotification(false);

		// (可选)设置是否在通知栏显示积分赚取提示。默认为true，标识开启；设置为false则关闭。
		// PointsManager.setEnableEarnPointsNotification(true);

		// (可选)设置是否开启积分赚取的Toast提示。默认为true，标识开启；设置为false这关闭。
		// PointsManager.setEnableEarnPointsToastTips(false);

		// 查询积分余额
		pointsBalance = PointsManager.getInstance(this).queryPoints();
		if (ConstantUtil.isOpenAds) {
			// 加载插播资源
			SpotManager.getInstance(this).loadSpotAds();
			// 插屏出现动画效果，0:ANIM_NONE为无动画，1:ANIM_SIMPLE为简单动画效果，2:ANIM_ADVANCE为高级动画效果
			SpotManager.getInstance(this).setAnimationType(
					SpotManager.ANIM_ADVANCE);
			// 设置插屏动画的横竖屏展示方式，如果设置了横屏，则在有广告资源的情况下会是优先使用横屏图。
			SpotManager.getInstance(this).setSpotOrientation(
					SpotManager.ORIENTATION_PORTRAIT);
		}

	}

	/**
	 * 获得的奖励
	 */
	private int pointsReward = 0;
	private int keyReward = 0;
	/**
	 * 剩余积分
	 */
	private int pointsBalance;
	private SQLiteDatabase mDatabase;
	private Cursor nCursor;
	private Cursor cCursor;
	/**
	 * 下标为0表示解锁
	 */
	private int[] imgId = { R.drawable.ic_launcher, R.drawable.ic_lock };
	private SharedPreferences preferences;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (gv != null && gv.myPlayer != null) {
			gv.myPlayer.body.setLinearVelocity(new Vec2(0, 0));
		}
		pause = true;
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		SpotManager.getInstance(this).onStop();
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pause = false;
		ConstantUtil.haveInsertData = preferences.getBoolean("haveInsertData",
				false);
		ConstantUtil.KeyNumber = preferences.getInt("KeyNumber", 1);
		// 第一次运行此程序
		if (!ConstantUtil.haveInsertData) {
			PointsManager.getInstance(this).awardPoints(50);
			mHandler.sendEmptyMessage(ConstantUtil.InsertData);
			Editor editor = preferences.edit();// 得到编辑器对象
			editor.putBoolean("haveInsertData", true);
			editor.commit();// 将数据写入偏好设置文件
		}
	}

	private void upDataKeyNumber() {
		SharedPreferences preferences = this.getSharedPreferences(
				"settingPreferences", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();// 得到编辑器对象
		editor.putInt("KeyNumber", ConstantUtil.KeyNumber);
		editor.commit();// 将数据写入偏好设置文件
		if (this.gv != null) {
			this.gv.keyNumber = "剩余钥匙："
					+ Integer.toString(ConstantUtil.KeyNumber);
		}
	}

	private void initWelcomeView() {
		// TODO Auto-generated method stub
		upDataDB();
		WelcomeView wv = new WelcomeView(this, mHandler);
		wv.initBitmapData(bitmapManager, numericalCalculation);
		setContentView(wv);
		// 取消全屏常亮
		if (this.wakeLock.isHeld()) {
			this.wakeLock.release();
		}
		this.keyReward = 0;
		this.pointsReward = 0;
		mSensorMgr.unregisterListener(mySensorListener);
		activityStatus = ConstantUtil.showWelcomeView;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// 如果有需要，可以点击后退关闭插播广告。
		if (!SpotManager.getInstance(this).disMiss()) {
			if (activityStatus == ConstantUtil.showWelcomeView) {
				if (isExit) {
					isExit = false;
					finish();
				} else {
					isExit = true;
					new Thread() {
						public void run() {
							try {
								sleep(2500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								isExit = false;
							}
						}
					}.start();
					Toast.makeText(BallActivity.this, "再按一次退出游戏",
							Toast.LENGTH_SHORT).show();
				}
			} else if (activityStatus == ConstantUtil.showGameView) {
				createDialog(ConstantUtil.showProcessDialog);
				this.mHandler.sendEmptyMessage(ConstantUtil.showWelcomeView);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.ball, menu);
		return true;
	}
	
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 浠� Bundle 涓В鏋� Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {

				// 淇濆瓨 Token 鍒� SharedPreferences
				AccessTokenKeeper.writeAccessToken(BallActivity.this,
						mAccessToken);
				Toast.makeText(BallActivity.this,
						R.string.weibosdk_demo_toast_auth_success,
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(BallActivity.this,
						com.zoipuus.balanceball.weiboapi.WBShareActivity.class);
				intent.putExtra(WBShareActivity.KEY_SHARE_TYPE,
						WBShareActivity.SHARE_ALL_IN_ONE);
				startActivity(intent);
			} else {
				// 浠ヤ笅鍑犵鎯呭喌锛屾偍浼氭敹鍒� Code锛�
				// 1. 褰撴偍鏈湪骞冲彴涓婃敞鍐岀殑搴旂敤绋嬪簭鐨勫寘鍚嶄笌绛惧悕鏃讹紱
				// 2. 褰撴偍娉ㄥ唽鐨勫簲鐢ㄧ▼搴忓寘鍚嶄笌绛惧悕涓嶆纭椂锛�
				// 3. 褰撴偍鍦ㄥ钩鍙颁笂娉ㄥ唽鐨勫寘鍚嶅拰绛惧悕涓庢偍褰撳墠娴嬭瘯鐨勫簲鐢ㄧ殑鍖呭悕鍜岀鍚嶄笉鍖归厤鏃躲��
				String code = values.getString("code");
				String message = getString(R.string.weibosdk_demo_toast_auth_failed);
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(BallActivity.this, message, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(BallActivity.this,
					R.string.weibosdk_demo_toast_auth_canceled,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(BallActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * requestCode是判断哪个Activity返回的，resultCode是由子Activity通过其setResult()方法返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO Auto-generated method stub
		if (requestCode == ConstantUtil.showGameSettingDialog) {
			ConstantUtil.isVibrate = preferences.getBoolean("isVibrate", true);
			ConstantUtil.isPlayMusic = preferences.getBoolean("isPlayMusic",
					true);
			ConstantUtil.isOpenAds = preferences.getBoolean("isSpotAds", true);
			ConstantUtil.lingMinDu = preferences.getInt("volume_value", 10);
			initWelcomeView();
		}
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 积分余额发生变动时，就会回调本方法（本回调方法执行在UI线程中）
	 */
	@Override
	public void onPointBalanceChange(int pointsBalance) {
		this.pointsBalance = pointsBalance;
		if (gv != null) {
			this.gv.pointsBalance = "积分余额：" + Integer.toString(pointsBalance);
		}
	}

	/**
	 * 积分订单赚取时会回调本方法（本回调方法执行在UI线程中）
	 */
	@Override
	public void onPointEarn(Context arg0, EarnPointsOrderList list) {
		// 遍历订单
		for (int i = 0; i < list.size(); ++i) {
			EarnPointsOrderInfo info = list.get(i);
			/**
			 * 获取积分订单的状态： 1. 表示开发者获得了收入并且用户获得了积分。 2.
			 * 表示开发者没有获得收入但用户获得了积分（未通过审核以及测试模式下结算无效等情况）。
			 */
			// public int getStatus()
			/**
			 * 本次获得的积分
			 */
			// public int getPoints();
			if (info.getStatus() == 1) {
				PointsManager.getInstance(this).awardPoints(info.getPoints());
				Toast.makeText(this, info.getMessage(), Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	/**
	 * 退出时回收资源
	 */
	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).onDestroy();
		super.onDestroy();

		// （可选）注销积分监听
		// 如果在onCreate调用了PointsManager.getInstance(this).registerNotify(this)进行积分余额监听器注册，那这里必须得注销
		PointsManager.getInstance(this).unRegisterNotify(this);

		// （可选）注销积分订单赚取监听
		// 如果在onCreate调用了PointsManager.getInstance(this).registerPointsEarnNotify(this)进行积分订单赚取监听器注册，那这里必须得注销
		PointsManager.getInstance(this).unRegisterPointsEarnNotify(this);

		// 回收积分广告占用的资源
		OffersManager.getInstance(this).onAppExit();
		mDatabase.close();
		nCursor.close();
		cCursor.close();
		preferences = this.getSharedPreferences("settingPreferences",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();// 得到编辑器对象
		editor.putInt("KeyNumber", ConstantUtil.KeyNumber);
		editor.commit();// 将数据写入偏好设置文件
	}
}
