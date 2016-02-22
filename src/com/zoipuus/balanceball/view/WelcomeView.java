package com.zoipuus.balanceball.view;

import com.zoipuus.balanceball.ConstantUtil;
import com.zoipuus.balanceball.R;
import com.zoipuus.balanceball.util.NumericalCalculation;
import com.zoipuus.balanceball.util.RectC;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WelcomeView extends SurfaceView implements
		android.view.SurfaceHolder.Callback {

	public WelcomeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private BitmapManager bitmapManager;
	private NumericalCalculation numericalCalculation;
	private Bitmap btn_exit;
	private Bitmap btn_select;
	private Bitmap btn_timer;
	private Bitmap btn_access;
	private Bitmap btn_share;
	private Bitmap btn_help;
	private Bitmap btn_setting;
	private RectC rectC;
	private Handler mHandler;
	private Paint paint;

	public WelcomeView(Context context, Handler mHandler) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mHandler = mHandler;
		getHolder().addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setTextSize(ConstantUtil.textSize);
	}

	public void initBitmapData(BitmapManager bitmapManager,
			NumericalCalculation numericalCalculation) {
		// TODO Auto-generated method stub
		this.bitmapManager = bitmapManager;
		this.numericalCalculation = numericalCalculation;
		this.btn_exit = this.bitmapManager.btn_exit;
		this.btn_select = this.bitmapManager.btn_wv;
		this.btn_timer = this.btn_select;
		this.btn_access = this.btn_select;
		this.btn_share = this.btn_select;
		this.btn_help = this.btn_select;
		this.btn_setting = this.btn_select;
	}

	private void doDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (canvas == null) {
			return;
		}
		canvas.save();
		canvas.drawBitmap(this.bitmapManager.bg_wv,
				this.numericalCalculation.bg_Rect.left,
				this.numericalCalculation.bg_Rect.top, null);
		float text_left = 0;
		float text_top = 0;
		// if (ConstantUtil.isOpenAds) {
		// text_top = this.numericalCalculation.bg_Rect.bottom
		// - this.bitmapManager.bg_wv_text.getHeight();
		// } else {
		text_left = (this.numericalCalculation.bg_Rect.right - this.bitmapManager.bg_wv_text
				.getWidth()) / 2;
		// }
		canvas.drawBitmap(this.bitmapManager.bg_wv_text, text_left, text_top,
				null);
		canvas.drawBitmap(this.btn_exit,
				this.numericalCalculation.btnExitRectC.left,
				this.numericalCalculation.btnExitRectC.top, null);
		canvas.drawBitmap(this.btn_select,
				this.numericalCalculation.btnSelectRectC.left,
				this.numericalCalculation.btnSelectRectC.top, null);
		canvas.drawBitmap(this.btn_timer,
				this.numericalCalculation.btnTimerRectC.left,
				this.numericalCalculation.btnTimerRectC.top, null);
		canvas.drawBitmap(this.btn_access,
				this.numericalCalculation.btnAccessRectC.left,
				this.numericalCalculation.btnAccessRectC.top, null);
		canvas.drawBitmap(this.btn_share,
				this.numericalCalculation.btnShareRectC.left,
				this.numericalCalculation.btnShareRectC.top, null);
		canvas.drawBitmap(this.btn_help,
				this.numericalCalculation.btnHelpRectC.left,
				this.numericalCalculation.btnHelpRectC.top, null);
		canvas.drawBitmap(this.btn_setting,
				this.numericalCalculation.btnSettingRectC.left,
				this.numericalCalculation.btnSettingRectC.top, null);
		canvas.drawText(getResources().getString(R.string.tv_select_level),
				this.numericalCalculation.tvSelectRect.left,
				this.numericalCalculation.tvSelectRect.top, paint);
		canvas.drawText(getResources().getString(R.string.tv_challenge_time),
				this.numericalCalculation.tvTimerRect.left,
				this.numericalCalculation.tvTimerRect.top, paint);
		canvas.drawText(getResources().getString(R.string.tv_access_integral),
				this.numericalCalculation.tvAccessRect.left,
				this.numericalCalculation.tvAccessRect.top, paint);
		canvas.drawText(getResources().getString(R.string.tv_share_game),
				this.numericalCalculation.tvShareRect.left,
				this.numericalCalculation.tvShareRect.top, paint);
		canvas.drawText(getResources().getString(R.string.tv_game_help),
				this.numericalCalculation.tvHelpRect.left,
				this.numericalCalculation.tvHelpRect.top, paint);
		canvas.drawText(getResources().getString(R.string.tv_game_setting),
				this.numericalCalculation.tvSettingRect.left,
				this.numericalCalculation.tvSettingRect.top, paint);
		canvas.restore();
	}

	private void rePaint() {
		// TODO Auto-generated method stub
		SurfaceHolder holder = this.getHolder();
		Canvas canvas = holder.lockCanvas();
		try {
			synchronized (holder) {
				doDraw(canvas);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float f1 = event.getX();
		float f2 = event.getY();
		switch (event.getAction()) {
		default:
			break;
		case MotionEvent.ACTION_DOWN:
			if (numericalCalculation.btnExitRectC.isInside(f1, f2)) {
				rectC = numericalCalculation.btnExitRectC;
				this.btn_exit = this.bitmapManager.btn_exit_down;
			} else if (numericalCalculation.btnSelectRectC.isInside(f1, f2)) {
				rectC = numericalCalculation.btnSelectRectC;
				this.btn_select = this.bitmapManager.btn_wv_down;
			} else if (numericalCalculation.btnTimerRectC.isInside(f1, f2)) {
				rectC = numericalCalculation.btnTimerRectC;
				this.btn_timer = this.bitmapManager.btn_wv_down;
			} else if (numericalCalculation.btnAccessRectC.isInside(f1, f2)) {
				rectC = numericalCalculation.btnAccessRectC;
				this.btn_access = this.bitmapManager.btn_wv_down;
			} else if (numericalCalculation.btnShareRectC.isInside(f1, f2)) {
				rectC = numericalCalculation.btnShareRectC;
				this.btn_share = this.bitmapManager.btn_wv_down;
			} else if (numericalCalculation.btnHelpRectC.isInside(f1, f2)) {
				rectC = numericalCalculation.btnHelpRectC;
				this.btn_help = this.bitmapManager.btn_wv_down;
			} else if (numericalCalculation.btnSettingRectC.isInside(f1, f2)) {
				rectC = numericalCalculation.btnSettingRectC;
				this.btn_setting = this.bitmapManager.btn_wv_down;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if ((rectC != null) && (!rectC.isInside(f1, f2))) {
				if (rectC == numericalCalculation.btnExitRectC) {
					this.btn_exit = this.bitmapManager.btn_exit;
				} else {
					this.btn_select = this.bitmapManager.btn_wv;
					this.btn_timer = this.btn_select;
					this.btn_access = this.btn_select;
					this.btn_share = this.btn_select;
					this.btn_help = this.btn_select;
					this.btn_setting = this.btn_select;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if ((rectC != null) && (rectC.isInside(f1, f2))) {
				if (rectC == numericalCalculation.btnExitRectC) {
					this.btn_exit = this.bitmapManager.btn_exit;
					this.mHandler
							.sendEmptyMessage(ConstantUtil.showExitGameDialog);
				} else if (rectC == numericalCalculation.btnSelectRectC) {
					this.btn_select = this.bitmapManager.btn_wv;
					this.mHandler
							.sendEmptyMessage(ConstantUtil.showSelectLevelDialog);
				} else if (rectC == numericalCalculation.btnTimerRectC) {
					this.btn_timer = this.bitmapManager.btn_wv;
					this.mHandler
							.sendEmptyMessage(ConstantUtil.showChallengeTimeDialog);
				} else if (rectC == numericalCalculation.btnAccessRectC) {
					this.btn_access = this.bitmapManager.btn_wv;
					this.mHandler
							.sendEmptyMessage(ConstantUtil.showAccessIntegralDialog);
				} else if (rectC == numericalCalculation.btnShareRectC) {
					this.btn_share = this.bitmapManager.btn_wv;
					this.mHandler
							.sendEmptyMessage(ConstantUtil.showShareGameDialog);
				} else if (rectC == numericalCalculation.btnHelpRectC) {
					this.btn_help = this.bitmapManager.btn_wv;
					this.mHandler
							.sendEmptyMessage(ConstantUtil.showHelpGameDialog);
				} else {
					this.btn_setting = this.bitmapManager.btn_wv;
					this.mHandler
							.sendEmptyMessage(ConstantUtil.showGameSettingDialog);
				}
			}
			break;
		}
		rePaint();
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		this.mHandler.sendEmptyMessage(ConstantUtil.AdvertisingView);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		rePaint();
		this.mHandler.sendEmptyMessage(ConstantUtil.dismissProcessDialog);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}

}
