package com.zoipuus.balanceball.view;

import com.zoipuus.balanceball.ConstantUtil;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
	private SurfaceHolder holder;
	public boolean isRunning;
	private GameView gameView;

	public DrawThread(GameView gameView, SurfaceHolder holder) {
		// TODO Auto-generated constructor stub
		this.gameView = gameView;
		this.holder = holder;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Canvas canvas = null;
		while (isRunning) {
			while (!this.gameView.ballActivity.pause) {
				if (this.gameView.myPlayer != null) {
					this.gameView.myPlayer.body.wakeUp();
					this.gameView.myPlayer.body.applyForce(
							ConstantUtil.GRAVITYTEMP,
							this.gameView.myPlayer.body.getWorldCenter());
				}
				this.gameView.world.step(ConstantUtil.TIME_STEP,
						ConstantUtil.ITERA);
				canvas = this.holder.lockCanvas();
				try {
					synchronized (holder) {
						this.gameView.doDraw(canvas);
					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}
				}
				try {
					sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// for (int index = 0; index < this.gameView.bl.size(); index++)
				// {
				// MyBody mb = this.gameView.bl.get(index);
				// if (mb.isSurvived && !mb.isDraw) {
				// if (mb instanceof MyPlayerBody) {
				// MyPlayerBody myPlayer = (MyPlayerBody) mb;
				// if (myPlayer.isFail) {
				// myPlayer.isSurvived = false;
				// this.gameView.mHandler
				// .sendEmptyMessage(ConstantUtil.showFailView);
				// }
				// } else {
				// mb.isSurvived = false;
				// this.gameView.world.destroyBody(mb.body);
				// // this.gameView.bl.remove(mb);
				// // index--;
				// }
				// }
				// }
				// try {
				// sleep(3);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}
	}
}
