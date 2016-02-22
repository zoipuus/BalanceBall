package com.zoipuus.balanceball.view;

import java.util.ArrayList;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;
import com.zoipuus.balanceball.BallActivity;
import com.zoipuus.balanceball.ConstantUtil;
import com.zoipuus.balanceball.rigidbody.Box2DUtil;
import com.zoipuus.balanceball.rigidbody.MyBlankHoleBody;
import com.zoipuus.balanceball.rigidbody.MyBody;
import com.zoipuus.balanceball.rigidbody.MyCommonBody;
import com.zoipuus.balanceball.rigidbody.MyDetonateBody;
import com.zoipuus.balanceball.rigidbody.MyFinishBody;
import com.zoipuus.balanceball.rigidbody.MyGoldBody;
import com.zoipuus.balanceball.rigidbody.MyKeyBody;
import com.zoipuus.balanceball.rigidbody.MyLockBody;
import com.zoipuus.balanceball.rigidbody.MyPlayerBody;
import com.zoipuus.balanceball.rigidbody.MyPolygonBody;
import com.zoipuus.balanceball.rigidbody.MyRotateBody;
import com.zoipuus.balanceball.rigidbody.MyUnkownBody;
import com.zoipuus.balanceball.rigidbody.MyUnkownStoneBody;
import com.zoipuus.balanceball.util.BodySearchUtil;
import com.zoipuus.balanceball.util.Map;
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

public class GameView extends SurfaceView implements
		android.view.SurfaceHolder.Callback {

	private BitmapManager bitmapManager;
	private NumericalCalculation numericalCalculation;
	Handler mHandler;
	private Paint paint;
	private int gameMode;
	private int[][] mapData;
	public ArrayList<MyBlankHoleBody> mbhbList = new ArrayList<MyBlankHoleBody>();
	public ArrayList<MyBody> bl = new ArrayList<MyBody>();
	// public ArrayList<MyPolygonBody> mpbList = new ArrayList<MyPolygonBody>();
	public ArrayList<MyUnkownStoneBody> musbList = new ArrayList<MyUnkownStoneBody>();
	public DrawThread dt;
	public World world;
	/**
	 * 剩余积分
	 */
	public String pointsBalance;
	/**
	 * 闯关成功的奖励
	 */
	public String reward;
	/**
	 * 剩余钥匙
	 */
	public String keyNumber;
	private MyFinishBody mfb;
	public MyPlayerBody myPlayer = null;
	public int usedTime = 0;
	public int minute = 0;
	public int second = 0;
	private int limitTime = 0;
	private int detonate_num = 0;
	private int stone_unkown_num = 0;
	private boolean isLevel10 = false;
	private Bitmap btn_pause;
	private Bitmap btn_back;
	private boolean isPause = false;
	RectC rectc = null;

	public BallActivity ballActivity;
	private boolean isTimerRunning = false;

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public GameView(BallActivity ballActivity, Handler mHandler, int level,
			int gameMode) {
		super(ballActivity);
		// TODO Auto-generated constructor stub
		this.ballActivity = ballActivity;
		this.mHandler = mHandler;
		this.gameMode = gameMode;
		this.detonate_num = 0;
		this.stone_unkown_num = 0;
		initGameWorld();
		this.mapData = selectMap(level);
		getHolder().addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.GREEN);
		paint.setTextSize(ConstantUtil.textSize);
	}

	private void initGameWorld() {
		// TODO Auto-generated method stub
		AABB worldAABB = new AABB();
		worldAABB.lowerBound.set(0.0f, 0.0f);// 设置包围盒的左、上边界
		worldAABB.upperBound.set(ConstantUtil.limit_right,
				ConstantUtil.limit_bottom);// 设置包围盒的右、下边界
		Vec2 gravity = new Vec2(0.0f, 0.0f);// 设置重力加速度
		boolean doSleep = true;
		world = new World(worldAABB, gravity, doSleep);
		ConstantUtil.world = world;
	}

	public void initBitmapData(BitmapManager bitmapManager,
			NumericalCalculation numericalCalculation) {
		// TODO Auto-generated method stub
		this.bitmapManager = bitmapManager;
		this.numericalCalculation = numericalCalculation;
		this.btn_pause = this.bitmapManager.ic_play;
		this.btn_back = this.bitmapManager.ic_back;
		createBody(mapData);
		initContactListener();
	}

	public void doDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (canvas == null) {
			return;
		}
		canvas.save();
		canvas.drawBitmap(this.bitmapManager.bg_gv, 0, 0, null);
		canvas.restore();
		mfb.drawSelf(canvas);
		for (MyBlankHoleBody mbhb : mbhbList) {
			mbhb.drawSelf(canvas);
		}
		for (int index = 0; index < this.bl.size(); index++) {
			MyBody mb = this.bl.get(index);
			if (mb.isSurvived && !mb.isDraw) {
				mb.isSurvived = false;
				this.world.destroyBody(mb.body);
				this.bl.remove(mb);
				index--;
			}
			if (mb instanceof MyPlayerBody) {
				ConstantUtil.player_death_location = mb.body.getPosition();
				continue;
			} else {
				if ((mb instanceof MyFinishBody)
						|| (mb instanceof MyBlankHoleBody)) {
					continue;
				} else {
					mb.drawSelf(canvas);
				}
			}
		}
		for (MyUnkownStoneBody sb : musbList) {
			sb.drawSelf(canvas);
		}
		myPlayer.drawSelf(canvas);
		if (myPlayer.isFail && !myPlayer.isSurvived) {
			myPlayer.isFail = false;
			this.mHandler.sendEmptyMessage(ConstantUtil.showFailView);
		} else if (myPlayer.isWin && !myPlayer.isSurvived) {
			myPlayer.isWin = false;
			usedTime = limitTime - (minute * 60 + second);
			this.mHandler.sendEmptyMessage(ConstantUtil.Vibrate);
			this.mHandler.sendEmptyMessage(ConstantUtil.showWinView);
		}
		if (gameMode == ConstantUtil.GameMode_Challenge) {
			canvas.save();
			canvas.drawText(Integer.toString(minute) + ":",
					numericalCalculation.tvMinute.left,
					numericalCalculation.tvMinute.bottom, paint);
			if (second > 9) {
				canvas.drawText(Integer.toString(second),
						numericalCalculation.tvSecond.left,
						numericalCalculation.tvSecond.bottom, paint);
			} else {
				canvas.drawText("0" + Integer.toString(second),
						numericalCalculation.tvSecond.left,
						numericalCalculation.tvSecond.bottom, paint);
			}
			canvas.restore();
		}
		canvas.save();
		canvas.drawBitmap(this.btn_pause,
				numericalCalculation.btnPauseRectC.left,
				numericalCalculation.btnPauseRectC.top, null);
		canvas.drawBitmap(this.btn_back,
				numericalCalculation.btnBackRectC.left,
				numericalCalculation.btnBackRectC.top, null);
		canvas.drawText(pointsBalance,
				numericalCalculation.tvPointsBalance.left,
				numericalCalculation.tvPointsBalance.bottom, paint);
		canvas.drawText(reward, numericalCalculation.tvPointsReward.left,
				numericalCalculation.tvPointsReward.bottom, paint);
		canvas.drawText(keyNumber, numericalCalculation.tvKeyNumber.left,
				numericalCalculation.tvKeyNumber.bottom, paint);
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
		case MotionEvent.ACTION_DOWN:
			if (numericalCalculation.btnPauseRectC.isInside(f1, f2)) {
				rectc = numericalCalculation.btnPauseRectC;
				btn_pause = this.bitmapManager.ic_down;
			} else if (numericalCalculation.btnBackRectC.isInside(f1, f2)) {
				rectc = numericalCalculation.btnBackRectC;
				btn_back = this.bitmapManager.ic_back_down;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if ((rectc == numericalCalculation.btnPauseRectC)
					&& (!rectc.isInside(f1, f2))) {
				rectc = null;
				if (isPause) {
					btn_pause = this.bitmapManager.ic_pause;
				} else {
					btn_pause = this.bitmapManager.ic_play;
				}
			} else if ((rectc == numericalCalculation.btnBackRectC)
					&& (!rectc.isInside(f1, f2))) {
				rectc = null;
				btn_back = this.bitmapManager.ic_back;
			}
			break;
		case MotionEvent.ACTION_UP:
			if ((rectc == numericalCalculation.btnPauseRectC)
					&& (rectc.isInside(f1, f2))) {
				rectc = null;
				if (!isPause) {
					isPause = true;
					btn_pause = this.bitmapManager.ic_pause;
					mHandler.sendEmptyMessage(ConstantUtil.btn_Pause);
				} else {
					isPause = false;
					ballActivity.pause = false;
					btn_pause = this.bitmapManager.ic_play;
				}
			} else if ((rectc == numericalCalculation.btnBackRectC)
					&& (rectc.isInside(f1, f2))) {
				rectc = null;
				btn_back = this.bitmapManager.ic_back;
				mHandler.sendEmptyMessage(ConstantUtil.showWelcomeView);
			}
			break;
		}
		rePaint();
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		ballActivity.pause = false;
		ConstantUtil.isStartGame = true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		this.mHandler.sendEmptyMessage(ConstantUtil.dismissProcessDialog);
		new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					dt = null;
					dt = new DrawThread(GameView.this, getHolder());
					dt.start();
					dt.isRunning = true;
					if (gameMode == ConstantUtil.GameMode_Challenge) {
						isTimerRunning = true;
						new Thread() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								while (isTimerRunning) {
									while (!ballActivity.pause) {
										try {
											sleep(1500);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (second > 0) {
											second--;
										} else {
											if (minute > 0) {
												minute--;
												second = 59;
											} else {
												ballActivity.pause = true;
												mHandler.sendEmptyMessage(ConstantUtil.showFailView);
											}
										}
									}
								}
							}
						}.start();
					}
				}
			};
		}.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		dt.isRunning = false;
		ballActivity.pause = true;
		isTimerRunning = false;
		ConstantUtil.isStartGame = false;
	}

	private void initContactListener() {
		// TODO Auto-generated method stub
		ContactListener cl = new ContactListener() {

			@Override
			public void result(ContactResult arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void remove(ContactPoint arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void persist(ContactPoint arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void add(ContactPoint arg0) {
				// TODO Auto-generated method stub
				BodySearchUtil.doAction(arg0.shape1.getBody(),
						arg0.shape2.getBody(), arg0.normal, arg0.velocity, bl,
						musbList, mHandler);
			}
		};
		world.setContactListener(cl);
	}

	private int[][] selectMap(int level) {
		// TODO Auto-generated method stub
		switch (level) {
		default:
		case 1:
			minute = 0;
			second = 30;
			limitTime = 30;
			return Map.MAP_DATA1;
		case 2:
			minute = 0;
			second = 45;
			limitTime = 45;
			return Map.MAP_DATA2;
		case 3:
			minute = 1;
			second = 0;
			limitTime = 60;
			return Map.MAP_DATA3;
		case 4:
			minute = 2;
			second = 0;
			limitTime = 120;
			return Map.MAP_DATA4;
		case 5:
			minute = 2;
			second = 30;
			limitTime = 150;
			return Map.MAP_DATA5;
		case 6:
			minute = 3;
			second = 0;
			limitTime = 180;
			return Map.MAP_DATA6;
		case 7:
			minute = 3;
			second = 30;
			limitTime = 210;
			return Map.MAP_DATA7;
		case 8:
			minute = 4;
			second = 0;
			limitTime = 240;
			return Map.MAP_DATA8;
		case 9:
			minute = 4;
			second = 30;
			limitTime = 270;
			return Map.MAP_DATA9;
		case 10:
			minute = 5;
			second = 0;
			limitTime = 300;
			isLevel10 = true;
			return Map.MAP_DATA10;
		}
	}

	private void createBody(int[][] mapData) {
		// TODO Auto-generated method stub
		int row = mapData.length;
		int column = mapData[0].length;
		Bitmap bitmap = null;
		Body body = null;
		MyCommonBody mcb = null;
		MyDetonateBody mdb = null;
		MyPolygonBody mpb = null;
		MyRotateBody mrb = null;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				boolean isStatic = true;
				switch (mapData[i][j]) {

				default:
				case 0:
					break;
				// 玩家
				case ConstantUtil.ic_ball:
					isStatic = false;
					bitmap = bitmapManager.ic_ball;
					ConstantUtil.player_start_location_x = j * Map.a + Map.a
							/ 2;
					ConstantUtil.player_start_location_y = i * Map.b + Map.b
							/ 2;
					body = Box2DUtil.createCircleBody(
							ConstantUtil.player_start_location_x,
							ConstantUtil.player_start_location_y,
							bitmap.getWidth() / 2, isStatic, world);
					myPlayer = new MyPlayerBody(body, bitmap.getWidth() / 2,
							bitmap.getWidth() / 2, bitmap);
					bl.add(myPlayer);
					// myPlayer.body.setLinearVelocity(new Vec2(-10, 0));
					break;
				// 黑洞刚体
				case ConstantUtil.ic_hole:
					bitmap = bitmapManager.ic_hole;
					body = Box2DUtil.createCircleBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 8,
							isStatic, world);
					MyBlankHoleBody mbhb = new MyBlankHoleBody(body,
							bitmap.getWidth() / 2, bitmap.getWidth() / 2,
							bitmap);
					mbhbList.add(mbhb);
					bl.add(mbhb);
					break;
				// 终点刚体
				case ConstantUtil.ic_finish:
					bitmap = bitmapManager.ic_finish;
					body = Box2DUtil.createCircleBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 8,
							isStatic, world);
					mfb = new MyFinishBody(body, bitmap.getWidth() / 2,
							bitmap.getWidth() / 2, bitmap);
					bl.add(mfb);
					break;
				// 钥匙
				case ConstantUtil.ic_key_not_static:
					isStatic = false;
				case ConstantUtil.ic_key:
					bitmap = bitmapManager.ic_key;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 4,
							bitmap.getHeight() / 4, isStatic, world);
					MyKeyBody mkb = new MyKeyBody(body, bitmap.getWidth() / 2,
							bitmap.getWidth() / 2, bitmap);
					bl.add(mkb);
					break;
				// 金币
				case ConstantUtil.ic_gold_not_static:
					isStatic = false;
				case ConstantUtil.ic_gold:
					bitmap = bitmapManager.ic_gold;
					body = Box2DUtil.createCircleBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 4,
							isStatic, world);
					MyGoldBody mgb = new MyGoldBody(body,
							bitmap.getWidth() / 2, bitmap.getWidth() / 2,
							bitmap);
					bl.add(mgb);
					break;
				// 多边形刚体（三角形）
				case ConstantUtil.ic_triangle_up_not_static:
					isStatic = false;
				case ConstantUtil.ic_triangle_up:
					bitmap = bitmapManager.ic_triangle_up;
					body = Box2DUtil.createPolygonBody(
							j * Map.a + Map.a / 2,
							i * Map.b + Map.b - bitmap.getHeight() / 2,
							new float[][] {
									{ -bitmap.getWidth() / 3,
											bitmap.getHeight() / 3 },
									{ 0, -bitmap.getHeight() / 3 },
									{ bitmap.getWidth() / 3,
											bitmap.getHeight() / 3 } },
							isStatic, world);
					mpb = new MyPolygonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mpb);
					break;
				case ConstantUtil.ic_triangle_right_not_static:
					isStatic = false;
				case ConstantUtil.ic_triangle_right:
					bitmap = bitmapManager.ic_triangle_right;
					body = Box2DUtil.createPolygonBody(
							j * Map.a + bitmap.getWidth() / 2,
							i * Map.b + Map.b / 2,
							new float[][] {
									{ -bitmap.getWidth() / 3,
											-bitmap.getHeight() / 3 },
									{ bitmap.getWidth() / 3, 0 },
									{ -bitmap.getWidth() / 3,
											bitmap.getHeight() / 3 } },
							isStatic, world);
					mpb = new MyPolygonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mpb);
					break;
				case ConstantUtil.ic_triangle_down_not_static:
					isStatic = false;
				case ConstantUtil.ic_triangle_down:
					bitmap = bitmapManager.ic_triangle_down;
					body = Box2DUtil.createPolygonBody(
							j * Map.a + Map.a / 2,
							i * Map.b + bitmap.getHeight() / 2,
							new float[][] {
									{ -bitmap.getWidth() / 3,
											-bitmap.getHeight() / 3 },
									{ bitmap.getWidth() / 3,
											-bitmap.getHeight() / 3 },
									{ 0, bitmap.getHeight() / 3 } }, isStatic,
							world);
					mpb = new MyPolygonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mpb);
					break;
				case ConstantUtil.ic_triangle_left_not_static:
					isStatic = false;
				case ConstantUtil.ic_triangle_left:
					bitmap = bitmapManager.ic_triangle_left;
					body = Box2DUtil.createPolygonBody(
							j * Map.a + Map.a - bitmap.getWidth() / 2,
							i * Map.b + Map.b / 2,
							new float[][] {
									{ -bitmap.getWidth() / 3, 0 },
									{ bitmap.getWidth() / 3,
											-bitmap.getHeight() / 3 },
									{ bitmap.getWidth() / 3,
											bitmap.getHeight() / 3 } },
							isStatic, world);
					mpb = new MyPolygonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mpb);
					break;
				// 石头刚体
				case ConstantUtil.ic_stone_not_static:
					isStatic = false;
				case ConstantUtil.ic_stone:
					bitmap = bitmapManager.ic_stone;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					break;
				// 3引线的石头刚体
				case ConstantUtil.ic_stone_3bent_down:
					bitmap = bitmapManager.ic_stone_3bent_down;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					break;
				// 有引线的石头刚体
				case ConstantUtil.ic_stone_bent1_not_static:
					isStatic = false;
				case ConstantUtil.ic_stone_bent1:
					bitmap = bitmapManager.ic_stone_bent1;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					break;
				case ConstantUtil.ic_stone_bent2_not_static:
					isStatic = false;
				case ConstantUtil.ic_stone_bent2:
					bitmap = bitmapManager.ic_stone_bent2;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					break;
				case ConstantUtil.ic_stone_bent3_not_static:
					isStatic = false;
				case ConstantUtil.ic_stone_bent3:
					bitmap = bitmapManager.ic_stone_bent3;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					break;
				case ConstantUtil.ic_stone_bent4_not_static:
					isStatic = false;
				case ConstantUtil.ic_stone_bent4:
					bitmap = bitmapManager.ic_stone_bent4;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					break;
				case ConstantUtil.ic_stone_horizontal_not_static:
					isStatic = false;
				case ConstantUtil.ic_stone_horizontal:
					bitmap = bitmapManager.ic_stone_horizontal;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					break;
				case ConstantUtil.ic_stone_vertical_not_static:
					isStatic = false;
				case ConstantUtil.ic_stone_vertical:
					bitmap = bitmapManager.ic_stone_vertical;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					break;
				// 未知的石头
				case ConstantUtil.ic_stone_unkown_not_static:
					isStatic = false;
				case ConstantUtil.ic_stone_unkown:
					stone_unkown_num++;
					bitmap = bitmapManager.ic_stone_unkown;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2
							- ConstantUtil.pw, bitmap.getHeight() / 2
							- ConstantUtil.ph, isStatic, world);
					MyUnkownStoneBody musb = new MyUnkownStoneBody(body,
							bitmap.getWidth() / 2, bitmap.getHeight() / 2,
							bitmap);
					musb.stone_unkown_num = this.stone_unkown_num;
					bl.add(musb);
					musbList.add(musb);
					break;
				// 锁
				case ConstantUtil.ic_lock_not_static:
					isStatic = false;
				case ConstantUtil.ic_lock:
					detonate_num++;
					bitmap = bitmapManager.ic_lock;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 4,
							bitmap.getHeight() / 4, isStatic, world);
					MyLockBody mlb = new MyLockBody(body,
							bitmap.getWidth() / 2, bitmap.getWidth() / 2,
							bitmap);
					mlb.detonate_num = this.detonate_num;
					bl.add(mlb);
					break;
				// 引爆器
				case ConstantUtil.ic_detonate_horizontal_up_not_static:
					isStatic = false;
				case ConstantUtil.ic_detonate_horizontal_up:
					detonate_num++;
					bitmap = bitmapManager.ic_detonate_horizontal;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + bitmap.getHeight() / 2,
							bitmap.getWidth() / 2, bitmap.getHeight() / 2,
							isStatic, world);
					mdb = new MyDetonateBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					mdb.detonate_num = this.detonate_num;
					bl.add(mdb);
					break;
				case ConstantUtil.ic_detonate_vertical_right_not_static:
					isStatic = false;
				case ConstantUtil.ic_detonate_vertical_right:
					detonate_num++;
					bitmap = bitmapManager.ic_detonate_vertical;
					body = Box2DUtil.createRectBody(
							(j + 1) * Map.a - bitmap.getWidth() / 2, i * Map.b
									+ Map.b / 2, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					mdb = new MyDetonateBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					mdb.detonate_num = this.detonate_num;
					bl.add(mdb);
					break;
				case ConstantUtil.ic_detonate_horizontal_down_not_static:
					isStatic = false;
				case ConstantUtil.ic_detonate_horizontal_down:
					detonate_num++;
					bitmap = bitmapManager.ic_detonate_horizontal;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2,
							(i + 1) * Map.b - bitmap.getHeight() / 2,
							bitmap.getWidth() / 2, bitmap.getHeight() / 2,
							isStatic, world);
					mdb = new MyDetonateBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					mdb.detonate_num = this.detonate_num;
					bl.add(mdb);
					break;
				case ConstantUtil.ic_detonate_vertical_left_not_static:
					isStatic = false;
				case ConstantUtil.ic_detonate_vertical_left:
					detonate_num++;
					bitmap = bitmapManager.ic_detonate_vertical;
					body = Box2DUtil.createRectBody(
							j * Map.a + bitmap.getWidth() / 2, i * Map.b
									+ Map.b / 2, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					mdb = new MyDetonateBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					mdb.detonate_num = this.detonate_num;
					bl.add(mdb);
					break;
				// 未知物体
				case ConstantUtil.ic_unkown:
					bitmap = bitmapManager.ic_unkown;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					MyUnkownBody mub = new MyUnkownBody(body,
							bitmap.getWidth() / 2, bitmap.getHeight() / 2,
							bitmap, mHandler);
					bl.add(mub);
					break;
				// 旋转挡板-方向上，顺时针旋转
				case ConstantUtil.ic_rotate_up:
					isStatic = false;
					stone_unkown_num++;
					bitmap = bitmapManager.ic_baffle_v;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					isStatic = true;
					bitmap = bitmapManager.ic_rotate;
					body = Box2DUtil.createCircleBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							isStatic, world);
					mrb = new MyRotateBody(body, mcb.body,
							bitmap.getWidth() / 2, bitmap.getHeight() / 2,
							bitmap, 0f, 1.57f);
					mrb.stone_unkown_num = stone_unkown_num;
					musbList.add(mrb);
					break;
				// 旋转挡板-方向下，逆时针旋转
				case ConstantUtil.ic_rotate_down:
					isStatic = false;
					stone_unkown_num++;
					bitmap = bitmapManager.ic_baffle_v;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					isStatic = true;
					bitmap = bitmapManager.ic_rotate;
					body = Box2DUtil.createCircleBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							isStatic, world);
					if (isLevel10) {
						isLevel10 = false;
						System.out.println("isLevel8==true");
						mrb = new MyRotateBody(body, mcb.body,
								bitmap.getWidth() / 2, bitmap.getHeight() / 2,
								bitmap, 0f, 1.57f);
						mrb.stone_unkown_num = stone_unkown_num;
						stone_unkown_num--;
					} else {
						System.out.println("isLevel8==false");
						mrb = new MyRotateBody(body, mcb.body,
								bitmap.getWidth() / 2, bitmap.getHeight() / 2,
								bitmap, -1.57f, 0f);
						mrb.stone_unkown_num = stone_unkown_num;
					}
					musbList.add(mrb);
					break;
				// 旋转挡板-方向左，顺时针旋转
				case ConstantUtil.ic_rotate_left:
					isStatic = false;
					stone_unkown_num++;
					bitmap = bitmapManager.ic_baffle_h;
					body = Box2DUtil.createRectBody(j * Map.a, i * Map.b
							+ Map.b / 2, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					isStatic = true;
					bitmap = bitmapManager.ic_rotate;
					body = Box2DUtil.createCircleBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							isStatic, world);
					mrb = new MyRotateBody(body, mcb.body,
							bitmap.getWidth() / 2, bitmap.getHeight() / 2,
							bitmap, 0f, 1.57f);
					mrb.stone_unkown_num = stone_unkown_num;
					musbList.add(mrb);
					break;
				// 旋转挡板-方向右，逆时针旋转
				case ConstantUtil.ic_rotate_right:
					isStatic = false;
					stone_unkown_num++;
					bitmap = bitmapManager.ic_baffle_h;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					bl.add(mcb);
					isStatic = true;
					bitmap = bitmapManager.ic_rotate;
					body = Box2DUtil.createCircleBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							isStatic, world);
					mrb = new MyRotateBody(body, mcb.body,
							bitmap.getWidth() / 2, bitmap.getHeight() / 2,
							bitmap, -1.57f, 0f);
					mrb.stone_unkown_num = stone_unkown_num;
					musbList.add(mrb);
					break;
				// 风车刚体
				case ConstantUtil.ic_windmill:
					isStatic = true;
					bitmap = bitmapManager.ic_baffle_long_h;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					mcb.isWindmill = true;
					bl.add(mcb);
					isStatic = true;
					bitmap = bitmapManager.ic_baffle_long_v;
					body = Box2DUtil.createRectBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, isStatic, world);
					mcb = new MyCommonBody(body, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					mcb.isWindmill = true;
					bl.add(mcb);
					isStatic = true;
					bitmap = bitmapManager.ic_rotate;
					body = Box2DUtil.createCircleBody(j * Map.a + Map.a / 2, i
							* Map.b + Map.b / 2, bitmap.getWidth() / 2,
							isStatic, world);
					Body body1 = Box2DUtil.createCircleBody(j * Map.a + Map.a
							/ 2, i * Map.b + Map.b / 2, bitmap.getWidth() / 2,
							false, world);
					mcb = new MyCommonBody(body1, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					mcb.isWindmill = true;
					mcb.isCentre = true;
					bl.add(mcb);
					mrb = new MyRotateBody(body, body1, bitmap.getWidth() / 2,
							bitmap.getHeight() / 2, bitmap);
					musbList.add(mrb);
					break;
				}
			}
		}
	}

	public void playerRevive() {
		// TODO Auto-generated method stub
		Bitmap bitmap = bitmapManager.ic_ball;
		Body body = Box2DUtil.createCircleBody(
				ConstantUtil.player_death_location.x * ConstantUtil.RATE,
				ConstantUtil.player_death_location.y * ConstantUtil.RATE,
				bitmap.getWidth() / 2, false, world);
		myPlayer = new MyPlayerBody(body, bitmap.getWidth() / 2,
				bitmap.getWidth() / 2, bitmap);
		bl.add(myPlayer);
		// myPlayer.body.setLinearVelocity(new Vec2(10, -10));
	}
}
