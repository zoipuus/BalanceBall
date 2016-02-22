package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;

import com.zoipuus.balanceball.ConstantUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * 创建终点刚体
 * 
 * @author zoipuus
 * 
 */
public class MyFinishBody extends MyBody {

	private boolean isInHole;
	private Bitmap inHoleBitmap;

	public MyFinishBody(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap) {
		super(body, halfWidth, halfHeight, bitmap);
	}

	@Override
	public void drawSelf(Canvas canvas) {
		// TODO Auto-generated method stub
		super.drawSelf(canvas);
		if (isInHole) {
			canvas.save();
			canvas.drawBitmap(inHoleBitmap, this.body.getPosition().x
					* ConstantUtil.RATE - inHoleBitmap.getWidth() / 2,
					this.body.getPosition().y * ConstantUtil.RATE
							- inHoleBitmap.getHeight() / 2, null);
			canvas.restore();
		}
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub
		MyPlayerBody myBody = (MyPlayerBody) body;
		MyFinishThread mbht = new MyFinishThread(this, myBody);
		mbht.start();
	}

	private class MyFinishThread extends Thread {
		private MyFinishBody body1;
		private MyPlayerBody body2;

		public MyFinishThread(MyFinishBody body1, MyPlayerBody body2) {
			// TODO Auto-generated constructor stub
			this.body1 = body1;
			this.body2 = body2;
		}

		public void run() {
			int i = 0;
			float f = 0.9f;
			Matrix localMatrix = new Matrix();
			while (i < 3) {
				i++;
				f *= 0.95;
				localMatrix.setScale(f, f);
				this.body1.inHoleBitmap = Bitmap.createBitmap(body2.bitmap, 0,
						0, body2.bitmap.getWidth(), body2.bitmap.getHeight(),
						localMatrix, true);
				isInHole = true;
				try {
					sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.body2.isWin = true;
		};
	}
}
