package com.zoipuus.balanceball.view;

import com.zoipuus.balanceball.BallActivity;
import com.zoipuus.balanceball.ConstantUtil;
import com.zoipuus.balanceball.R;
import com.zoipuus.balanceball.util.Map;
import com.zoipuus.balanceball.util.NumericalCalculation;
import com.zoipuus.balanceball.util.RectC;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class BitmapManager {
	// public Bitmap bg_pv;
	public Bitmap bg_wv;
	public Bitmap bg_wv_text;
	public Bitmap bg_gv;
	public Bitmap btn_exit;
	public Bitmap btn_exit_down;
	public Bitmap btn_wv;
	public Bitmap btn_wv_down;
	public Bitmap ic_ball;
	public Bitmap ic_hole;
	public Bitmap ic_finish;
	public Bitmap ic_triangle_up;
	public Bitmap ic_triangle_right;
	public Bitmap ic_triangle_down;
	public Bitmap ic_triangle_left;
	public Bitmap ic_stone;
	public Bitmap ic_stone_bent1;
	public Bitmap ic_stone_bent4;
	public Bitmap ic_stone_bent2;
	public Bitmap ic_stone_bent3;
	public Bitmap ic_stone_horizontal;
	public Bitmap ic_stone_vertical;
	public Bitmap ic_stone_unkown;
	public Bitmap ic_detonate_horizontal;
	public Bitmap ic_detonate_vertical;
	public Bitmap ic_unkown;
	public Bitmap ic_rotate;
	public Bitmap ic_baffle_h;
	public Bitmap ic_baffle_v;
	public Bitmap ic_baffle_long_h;
	public Bitmap ic_baffle_long_v;
	public Bitmap ic_key;
	public Bitmap ic_lock;
	public Bitmap ic_gold;
	public Bitmap ic_stone_3bent_down;
	public Bitmap ic_play;
	public Bitmap ic_down;
	public Bitmap ic_pause;
	public Bitmap ic_back;
	public Bitmap ic_back_down;

	public BitmapManager(BallActivity ballActivity) {
		// TODO Auto-generated constructor stub
		// this.bg_pv = readBitmap(ballActivity, R.drawable.bg_pv);
		this.bg_wv = readBitmap(ballActivity, R.drawable.bg_wv);
		this.bg_wv_text = readBitmap(ballActivity, R.drawable.bg_wv_text);
		this.bg_gv = readBitmap(ballActivity, R.drawable.bg_gv);
		this.btn_exit = readBitmap(ballActivity, R.drawable.btn_exit);
		this.btn_exit_down = readBitmap(ballActivity, R.drawable.btn_exit_down);
		this.btn_wv = readBitmap(ballActivity, R.drawable.btn_wv);
		this.btn_wv_down = readBitmap(ballActivity, R.drawable.btn_wv_down);
		this.ic_ball = readBitmap(ballActivity, R.drawable.ic_ball);
		this.ic_hole = readBitmap(ballActivity, R.drawable.ic_hole);
		this.ic_finish = readBitmap(ballActivity, R.drawable.ic_finish);
		this.ic_triangle_up = readBitmap(ballActivity,
				R.drawable.ic_triangle_up);
		this.ic_triangle_right = readBitmap(ballActivity,
				R.drawable.ic_triangle_right);
		this.ic_triangle_down = readBitmap(ballActivity,
				R.drawable.ic_triangle_down);
		this.ic_triangle_left = readBitmap(ballActivity,
				R.drawable.ic_triangle_left);
		this.ic_stone = readBitmap(ballActivity, R.drawable.ic_stone);
		this.ic_stone_bent1 = readBitmap(ballActivity,
				R.drawable.ic_stone_bent1);
		this.ic_stone_bent4 = readBitmap(ballActivity,
				R.drawable.ic_stone_bent4);
		this.ic_stone_bent2 = readBitmap(ballActivity,
				R.drawable.ic_stone_bent2);
		this.ic_stone_bent3 = readBitmap(ballActivity,
				R.drawable.ic_stone_bent3);
		this.ic_stone_horizontal = readBitmap(ballActivity,
				R.drawable.ic_stone_horizontal);
		this.ic_stone_vertical = readBitmap(ballActivity,
				R.drawable.ic_stone_vertical);
		this.ic_stone_unkown = readBitmap(ballActivity,
				R.drawable.ic_stone_unkown);
		this.ic_detonate_horizontal = readBitmap(ballActivity,
				R.drawable.ic_detonate_horizontal);
		this.ic_detonate_vertical = readBitmap(ballActivity,
				R.drawable.ic_detonate_vertical);
		this.ic_unkown = readBitmap(ballActivity, R.drawable.ic_unkown);
		this.ic_rotate = readBitmap(ballActivity, R.drawable.ic_rotate);
		this.ic_baffle_h = readBitmap(ballActivity, R.drawable.ic_baffle_h);
		this.ic_baffle_v = readBitmap(ballActivity, R.drawable.ic_baffle_v);
		this.ic_baffle_long_h = readBitmap(ballActivity,
				R.drawable.ic_baffle_long_h);
		this.ic_baffle_long_v = readBitmap(ballActivity,
				R.drawable.ic_baffle_long_v);
		this.ic_key = readBitmap(ballActivity, R.drawable.ic_key);
		this.ic_lock = readBitmap(ballActivity, R.drawable.ic_lock);
		this.ic_gold = readBitmap(ballActivity, R.drawable.ic_gold_coin);
		this.ic_stone_3bent_down = readBitmap(ballActivity,
				R.drawable.ic_stone_3bent_down);
		this.ic_play = readBitmap(ballActivity, R.drawable.ic_play);
		this.ic_down = readBitmap(ballActivity, R.drawable.ic_down);
		this.ic_pause = readBitmap(ballActivity, R.drawable.ic_pause);
		this.ic_back = readBitmap(ballActivity, R.drawable.ic_back);
		this.ic_back_down = readBitmap(ballActivity, R.drawable.ic_back_down);

		DisplayMetrics metrics = new DisplayMetrics();
		ballActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		ConstantUtil.Window_Width = Math.max(metrics.widthPixels,
				metrics.heightPixels);
		ConstantUtil.Window_Height = Math.min(metrics.widthPixels,
				metrics.heightPixels);
		ConstantUtil.limit_right = ConstantUtil.Window_Width
				/ ConstantUtil.RATE + 1;
		ConstantUtil.limit_bottom = ConstantUtil.Window_Height
				/ ConstantUtil.RATE + 1;
		float w = 1.0F * ConstantUtil.Window_Width / 800;
		float h = 1.0F * ConstantUtil.Window_Height / 480;
		if ((w != 1.0F) && (h != 1.0F)) {
			Matrix localMatrix = new Matrix();
			localMatrix.setScale(w, h);
			ConstantUtil.textSize = (int) (24 * h);
			// 重新设置其他控件
			Bitmap localBitmap = Bitmap.createBitmap(this.btn_exit, 0, 0,
					this.btn_exit.getWidth(), this.btn_exit.getHeight(),
					localMatrix, true);
			this.btn_exit.recycle();
			this.btn_exit = localBitmap;
			localBitmap = Bitmap.createBitmap(this.btn_exit_down, 0, 0,
					this.btn_exit_down.getWidth(),
					this.btn_exit_down.getHeight(), localMatrix, true);
			this.btn_exit_down.recycle();
			this.btn_exit_down = localBitmap;
			localBitmap = Bitmap.createBitmap(this.btn_wv, 0, 0,
					this.btn_wv.getWidth(), this.btn_wv.getHeight(),
					localMatrix, true);
			this.btn_wv.recycle();
			this.btn_wv = localBitmap;
			localBitmap = Bitmap.createBitmap(this.btn_wv_down, 0, 0,
					this.btn_wv_down.getWidth(), this.btn_wv_down.getHeight(),
					localMatrix, true);
			this.btn_wv_down.recycle();
			this.btn_wv_down = localBitmap;
		}
		BitmapFactory.Options localOptions = readSizeBitmap(ballActivity,
				R.drawable.bg_wv);
		float f1 = 1.0F * ConstantUtil.Window_Width / localOptions.outWidth;
		float f2 = 1.0F * ConstantUtil.Window_Height / localOptions.outHeight;
		// 判断背景图是否适合屏幕大小
		if ((f1 != 1.0F) && (f2 != 1.0F)) {
			Matrix localMatrix = new Matrix();
			float f3 = Math.min(f2, f1);
			localMatrix.setScale(f3, f3);
			ConstantUtil.pw = (int) (3 * f1);
			ConstantUtil.ph = (int) (3 * f2);
			// 圆形
			Bitmap localBitmap = Bitmap.createBitmap(this.ic_ball, 0, 0,
					this.ic_ball.getWidth(), this.ic_ball.getHeight(),
					localMatrix, true);
			this.ic_ball.recycle();
			this.ic_ball = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_hole, 0, 0,
					this.ic_hole.getWidth(), this.ic_hole.getHeight(),
					localMatrix, true);
			this.ic_hole.recycle();
			this.ic_hole = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_finish, 0, 0,
					this.ic_finish.getWidth(), this.ic_finish.getHeight(),
					localMatrix, true);
			this.ic_finish.recycle();
			this.ic_finish = localBitmap;
			// 暂停按钮
			localBitmap = Bitmap.createBitmap(this.ic_play, 0, 0,
					this.ic_play.getWidth(), this.ic_play.getHeight(),
					localMatrix, true);
			this.ic_play.recycle();
			this.ic_play = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_down, 0, 0,
					this.ic_down.getWidth(), this.ic_down.getHeight(),
					localMatrix, true);
			this.ic_down.recycle();
			this.ic_down = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_pause, 0, 0,
					this.ic_pause.getWidth(), this.ic_pause.getHeight(),
					localMatrix, true);
			this.ic_pause.recycle();
			this.ic_pause = localBitmap;
			// 旋转关节
			localBitmap = Bitmap.createBitmap(this.ic_rotate, 0, 0,
					this.ic_rotate.getWidth(), this.ic_rotate.getHeight(),
					localMatrix, true);
			this.ic_rotate.recycle();
			this.ic_rotate = localBitmap;
			// 金币
			localBitmap = Bitmap.createBitmap(this.ic_gold, 0, 0,
					this.ic_gold.getWidth(), this.ic_gold.getHeight(),
					localMatrix, true);
			this.ic_gold.recycle();
			this.ic_gold = localBitmap;

			// 重新设置背景
			localMatrix.setScale(f1, f2);
			localBitmap = Bitmap.createBitmap(this.bg_wv, 0, 0,
					this.bg_wv.getWidth(), this.bg_wv.getHeight(), localMatrix,
					true);
			this.bg_wv.recycle();
			this.bg_wv = localBitmap;
			localBitmap = Bitmap.createBitmap(this.bg_wv_text, 0, 0,
					this.bg_wv_text.getWidth(),
					this.bg_wv_text.getHeight(), localMatrix, true);
			this.bg_wv_text.recycle();
			this.bg_wv_text = localBitmap;
			localBitmap = Bitmap.createBitmap(this.bg_gv, 0, 0,
					this.bg_gv.getWidth(), this.bg_gv.getHeight(), localMatrix,
					true);
			this.bg_gv.recycle();
			this.bg_gv = localBitmap;
			// 三角形
			localBitmap = Bitmap.createBitmap(this.ic_triangle_up, 0, 0,
					this.ic_triangle_up.getWidth(),
					this.ic_triangle_up.getHeight(), localMatrix, true);
			this.ic_triangle_up.recycle();
			this.ic_triangle_up = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_triangle_right, 0, 0,
					this.ic_triangle_right.getWidth(),
					this.ic_triangle_right.getHeight(), localMatrix, true);
			this.ic_triangle_right.recycle();
			this.ic_triangle_right = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_triangle_down, 0, 0,
					this.ic_triangle_down.getWidth(),
					this.ic_triangle_down.getHeight(), localMatrix, true);
			this.ic_triangle_down.recycle();
			this.ic_triangle_down = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_triangle_left, 0, 0,
					this.ic_triangle_left.getWidth(),
					this.ic_triangle_left.getHeight(), localMatrix, true);
			this.ic_triangle_left.recycle();
			this.ic_triangle_left = localBitmap;
			// 普通石头
			localBitmap = Bitmap.createBitmap(this.ic_stone, 0, 0,
					this.ic_stone.getWidth(), this.ic_stone.getHeight(),
					localMatrix, true);
			this.ic_stone.recycle();
			this.ic_stone = localBitmap;
			// 带有引线的石头
			localBitmap = Bitmap.createBitmap(this.ic_stone_3bent_down, 0, 0,
					this.ic_stone_3bent_down.getWidth(),
					this.ic_stone_3bent_down.getHeight(), localMatrix, true);
			this.ic_stone_3bent_down.recycle();
			this.ic_stone_3bent_down = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_stone_bent1, 0, 0,
					this.ic_stone_bent1.getWidth(),
					this.ic_stone_bent1.getHeight(), localMatrix, true);
			this.ic_stone_bent1.recycle();
			this.ic_stone_bent1 = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_stone_bent4, 0, 0,
					this.ic_stone_bent4.getWidth(),
					this.ic_stone_bent4.getHeight(), localMatrix, true);
			this.ic_stone_bent4.recycle();
			this.ic_stone_bent4 = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_stone_bent2, 0, 0,
					this.ic_stone_bent2.getWidth(),
					this.ic_stone_bent2.getHeight(), localMatrix, true);
			this.ic_stone_bent2.recycle();
			this.ic_stone_bent2 = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_stone_bent3, 0, 0,
					this.ic_stone_bent3.getWidth(),
					this.ic_stone_bent3.getHeight(), localMatrix, true);
			this.ic_stone_bent3.recycle();
			this.ic_stone_bent3 = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_stone_horizontal, 0, 0,
					this.ic_stone_horizontal.getWidth(),
					this.ic_stone_horizontal.getHeight(), localMatrix, true);
			this.ic_stone_horizontal.recycle();
			this.ic_stone_horizontal = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_stone_vertical, 0, 0,
					this.ic_stone_vertical.getWidth(),
					this.ic_stone_vertical.getHeight(), localMatrix, true);
			this.ic_stone_vertical.recycle();
			this.ic_stone_vertical = localBitmap;
			// 未知的石头
			localBitmap = Bitmap.createBitmap(this.ic_stone_unkown, 0, 0,
					this.ic_stone_unkown.getWidth(),
					this.ic_stone_unkown.getHeight(), localMatrix, true);
			this.ic_stone_unkown.recycle();
			this.ic_stone_unkown = localBitmap;
			// 引爆器
			localBitmap = Bitmap.createBitmap(this.ic_detonate_horizontal, 0,
					0, this.ic_detonate_horizontal.getWidth(),
					this.ic_detonate_horizontal.getHeight(), localMatrix, true);
			this.ic_detonate_horizontal.recycle();
			this.ic_detonate_horizontal = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_detonate_vertical, 0, 0,
					this.ic_detonate_vertical.getWidth(),
					this.ic_detonate_vertical.getHeight(), localMatrix, true);
			this.ic_detonate_vertical.recycle();
			this.ic_detonate_vertical = localBitmap;
			// 未知物体
			localBitmap = Bitmap.createBitmap(this.ic_unkown, 0, 0,
					this.ic_unkown.getWidth(), this.ic_unkown.getHeight(),
					localMatrix, true);
			this.ic_unkown.recycle();
			this.ic_unkown = localBitmap;
			// 钥匙
			localBitmap = Bitmap.createBitmap(this.ic_key, 0, 0,
					this.ic_key.getWidth(), this.ic_key.getHeight(),
					localMatrix, true);
			this.ic_key.recycle();
			this.ic_key = localBitmap;
			// 锁
			localBitmap = Bitmap.createBitmap(this.ic_lock, 0, 0,
					this.ic_lock.getWidth(), this.ic_lock.getHeight(),
					localMatrix, true);
			this.ic_lock.recycle();
			this.ic_lock = localBitmap;
			// 挡板
			localBitmap = Bitmap.createBitmap(this.ic_baffle_h, 0, 0,
					this.ic_baffle_h.getWidth(), this.ic_baffle_h.getHeight(),
					localMatrix, true);
			this.ic_baffle_h.recycle();
			this.ic_baffle_h = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_baffle_v, 0, 0,
					this.ic_baffle_v.getWidth(), this.ic_baffle_v.getHeight(),
					localMatrix, true);
			this.ic_baffle_v.recycle();
			this.ic_baffle_v = localBitmap;
			// 返回按钮
			localBitmap = Bitmap.createBitmap(this.ic_back, 0, 0,
					this.ic_back.getWidth(), this.ic_back.getHeight(),
					localMatrix, true);
			this.ic_back.recycle();
			this.ic_back = localBitmap;
			localBitmap = Bitmap.createBitmap(this.ic_back_down, 0, 0,
					this.ic_back_down.getWidth(),
					this.ic_back_down.getHeight(), localMatrix, true);
			this.ic_back_down.recycle();
			this.ic_back_down = localBitmap;
			if (f1 >= f2) {
				localBitmap = Bitmap.createBitmap(this.ic_baffle_long_v, 0, 0,
						this.ic_baffle_long_v.getWidth(),
						this.ic_baffle_long_v.getHeight(), localMatrix, true);
				this.ic_baffle_long_v.recycle();
				this.ic_baffle_long_v = localBitmap;
				localMatrix.setScale(f2, f1);
				localBitmap = Bitmap.createBitmap(this.ic_baffle_long_h, 0, 0,
						this.ic_baffle_long_h.getWidth(),
						this.ic_baffle_long_h.getHeight(), localMatrix, true);
				this.ic_baffle_long_h.recycle();
				this.ic_baffle_long_h = localBitmap;
			} else {
				localBitmap = Bitmap.createBitmap(this.ic_baffle_long_h, 0, 0,
						this.ic_baffle_long_h.getWidth(),
						this.ic_baffle_long_h.getHeight(), localMatrix, true);
				this.ic_baffle_long_h.recycle();
				this.ic_baffle_long_h = localBitmap;
				localMatrix.setScale(f2, f1);
				localBitmap = Bitmap.createBitmap(this.ic_baffle_long_v, 0, 0,
						this.ic_baffle_long_v.getWidth(),
						this.ic_baffle_long_v.getHeight(), localMatrix, true);
				this.ic_baffle_long_v.recycle();
				this.ic_baffle_long_v = localBitmap;

			}
		}
		// 三角形
		Matrix localMatrix = new Matrix();
		localMatrix.setScale(0.8f, 0.8f);
		Bitmap localBitmap = Bitmap.createBitmap(this.ic_triangle_up, 0, 0,
				this.ic_triangle_up.getWidth(),
				this.ic_triangle_up.getHeight(), localMatrix, true);
		this.ic_triangle_up.recycle();
		this.ic_triangle_up = localBitmap;
		localBitmap = Bitmap.createBitmap(this.ic_triangle_right, 0, 0,
				this.ic_triangle_right.getWidth(),
				this.ic_triangle_right.getHeight(), localMatrix, true);
		this.ic_triangle_right.recycle();
		this.ic_triangle_right = localBitmap;
		localBitmap = Bitmap.createBitmap(this.ic_triangle_down, 0, 0,
				this.ic_triangle_down.getWidth(),
				this.ic_triangle_down.getHeight(), localMatrix, true);
		this.ic_triangle_down.recycle();
		this.ic_triangle_down = localBitmap;
		localBitmap = Bitmap.createBitmap(this.ic_triangle_left, 0, 0,
				this.ic_triangle_left.getWidth(),
				this.ic_triangle_left.getHeight(), localMatrix, true);
		this.ic_triangle_left.recycle();
		this.ic_triangle_left = localBitmap;
		Map.a = this.ic_stone.getWidth();
		Map.b = this.ic_stone.getHeight();
	}

	public void initBitmapData(NumericalCalculation numericalCalculation) {
		int width = this.btn_wv.getWidth();
		/**
		 * 间隔
		 */
		int interval = this.btn_wv.getHeight() + 5;
		numericalCalculation.bg_Rect = new Rect(0, 0, this.bg_wv.getWidth(),
				this.bg_wv.getHeight());
		numericalCalculation.btnExitRectC = new RectC(
				numericalCalculation.bg_Rect.right - this.btn_exit.getWidth(),
				numericalCalculation.bg_Rect.top, this.btn_exit.getWidth(),
				this.btn_exit.getWidth());
		//挑战时间的按钮放在中间
		numericalCalculation.btnTimerRectC = new RectC(
				(numericalCalculation.bg_Rect.right - width) / 2,
				(int) (numericalCalculation.bg_Rect.bottom / 2 - interval*1.5), width,
				interval);
		numericalCalculation.btnSelectRectC = new RectC(
				numericalCalculation.btnTimerRectC.left,
				numericalCalculation.btnTimerRectC.top - interval, width,
				interval);
		numericalCalculation.btnAccessRectC = new RectC(
				numericalCalculation.btnTimerRectC.left,
				numericalCalculation.btnTimerRectC.top + interval, width,
				interval);
		numericalCalculation.btnShareRectC = new RectC(
				numericalCalculation.btnTimerRectC.left,
				numericalCalculation.btnAccessRectC.top + interval, width,
				interval);
		numericalCalculation.btnHelpRectC = new RectC(
				numericalCalculation.btnTimerRectC.left,
				numericalCalculation.btnShareRectC.top + interval, width,
				interval);
		numericalCalculation.btnSettingRectC = new RectC(
				numericalCalculation.btnTimerRectC.left,
				numericalCalculation.btnHelpRectC.top + interval, width,
				interval);
		// 插入数据按钮
		numericalCalculation.btnInsertRectC = new RectC(0, width, width,
				interval);
		// 按钮文字位置设置
		numericalCalculation.tvTimerRect = new Rect(
				(numericalCalculation.bg_Rect.right - ConstantUtil.textSize * 4) / 2,
				numericalCalculation.btnTimerRectC.top
						+ (numericalCalculation.btnTimerRectC.height + ConstantUtil.textSize)
						/ 2 - 3,
				(numericalCalculation.bg_Rect.right + ConstantUtil.textSize * 4) / 2,
				numericalCalculation.btnTimerRectC.top
						+ (numericalCalculation.btnTimerRectC.height + ConstantUtil.textSize)
						/ 2 + ConstantUtil.textSize - 3);
		numericalCalculation.tvSelectRect = new Rect(
				numericalCalculation.tvTimerRect.left,
				numericalCalculation.tvTimerRect.top - interval,
				numericalCalculation.tvTimerRect.right,
				numericalCalculation.tvTimerRect.top - interval
						+ ConstantUtil.textSize);
		numericalCalculation.tvAccessRect = new Rect(
				numericalCalculation.tvTimerRect.left,
				numericalCalculation.tvTimerRect.top + interval,
				numericalCalculation.tvTimerRect.right,
				numericalCalculation.tvTimerRect.top + interval
						+ ConstantUtil.textSize);
		numericalCalculation.tvShareRect = new Rect(
				numericalCalculation.tvTimerRect.left,
				numericalCalculation.tvAccessRect.top + interval,
				numericalCalculation.tvTimerRect.right,
				numericalCalculation.tvAccessRect.top + interval
						+ ConstantUtil.textSize);
		numericalCalculation.tvHelpRect = new Rect(
				numericalCalculation.tvTimerRect.left,
				numericalCalculation.tvShareRect.top + interval,
				numericalCalculation.tvTimerRect.right,
				numericalCalculation.tvShareRect.top + interval
						+ ConstantUtil.textSize);
		numericalCalculation.tvSettingRect = new Rect(
				numericalCalculation.tvTimerRect.left,
				numericalCalculation.tvHelpRect.top + interval,
				numericalCalculation.tvTimerRect.right,
				numericalCalculation.tvHelpRect.top + interval
						+ ConstantUtil.textSize);
		// 积分显示位置
		numericalCalculation.tvPointsBalance = new Rect(Map.a * 1,
				(Map.b - ConstantUtil.textSize) / 3, Map.a * 7,
				(Map.b + ConstantUtil.textSize) * 2 / 5);
		// 奖励显示位置
		numericalCalculation.tvPointsReward = new Rect(
				numericalCalculation.bg_Rect.right / 2 - Map.a * 4,
				(Map.b - ConstantUtil.textSize) / 3,
				numericalCalculation.bg_Rect.right / 2 + Map.a * 3,
				(Map.b + ConstantUtil.textSize) * 2 / 5);
		// 剩余钥匙显示位置
		numericalCalculation.tvKeyNumber = new Rect(
				numericalCalculation.bg_Rect.right - Map.a * 4,
				(Map.b - ConstantUtil.textSize) / 3,
				numericalCalculation.bg_Rect.right,
				(Map.b + ConstantUtil.textSize) * 2 / 5);
		// 倒计时显示位置
		numericalCalculation.tvSecond = new Rect(
				numericalCalculation.bg_Rect.right - Map.a
						- ConstantUtil.textSize * 2,
				numericalCalculation.bg_Rect.bottom
						- (Map.b + ConstantUtil.textSize) / 3,
				numericalCalculation.bg_Rect.right - Map.a
						- ConstantUtil.textSize,
				numericalCalculation.bg_Rect.bottom
						- (Map.b - ConstantUtil.textSize) / 2);
		numericalCalculation.tvMinute = new Rect(
				numericalCalculation.tvSecond.left - ConstantUtil.textSize,
				numericalCalculation.bg_Rect.bottom
						- (Map.b + ConstantUtil.textSize) / 3,
				numericalCalculation.tvSecond.left,
				numericalCalculation.bg_Rect.bottom
						- (Map.b - ConstantUtil.textSize) / 2);
		// 暂停按钮
		numericalCalculation.btnPauseRectC = new RectC(
				numericalCalculation.bg_Rect.left,
				numericalCalculation.bg_Rect.top + Map.b,
				this.ic_play.getWidth(), this.ic_play.getHeight());
		// 返回按钮
		numericalCalculation.btnBackRectC = new RectC(
				numericalCalculation.bg_Rect.left,
				numericalCalculation.bg_Rect.bottom - this.ic_back.getHeight(),
				this.ic_back.getWidth(), this.ic_back.getHeight());
	}

	public Bitmap readBitmap(Context paramContext, int paramInt) {
		android.graphics.BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		localOptions.inPurgeable = true;
		localOptions.inInputShareable = true;
		return BitmapFactory.decodeStream(paramContext.getResources()
				.openRawResource(paramInt), null, localOptions);
	}

	public android.graphics.BitmapFactory.Options readSizeBitmap(
			Context paramContext, int paramInt) {
		android.graphics.BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				paramContext.getResources().openRawResource(paramInt), null,
				localOptions);
		return localOptions;
	}
}
