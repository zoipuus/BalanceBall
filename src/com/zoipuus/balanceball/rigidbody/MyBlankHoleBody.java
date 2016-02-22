package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;

import com.zoipuus.balanceball.ConstantUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * ´´½¨ºÚ¶´¸ÕÌå
 * 
 * @author zoipuus
 * 
 */
public class MyBlankHoleBody extends MyBody {

	private boolean isInHole = false;
	private Bitmap inHoleBitmap = null;

	public MyBlankHoleBody(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap) {
		super(body, halfWidth, halfHeight, bitmap);
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub
		MyBlankHoleThread mbht = new MyBlankHoleThread(this, body);
		mbht.start();
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

	private class MyBlankHoleThread extends Thread {
		private MyBlankHoleBody body1;
		private MyBody body2;

		public MyBlankHoleThread(MyBlankHoleBody body1, MyBody body2) {
			// TODO Auto-generated constructor stub
			this.body1 = body1;
			this.body2 = body2;
		}

		public void run() {
			int i = 0;
			float f = 0.9f;
			Matrix localMatrix = new Matrix();
			while (i < 10) {
				i++;
				f *= 0.9;
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
			if (body2 instanceof MyPlayerBody) {
				MyPlayerBody myPlayer = (MyPlayerBody) body2;
				myPlayer.isFail = true;
				this.body1.isDraw = false;
			}
			isInHole = false;
		};
	}
}
