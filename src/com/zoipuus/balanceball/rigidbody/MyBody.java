package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;

import com.zoipuus.balanceball.ConstantUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

//自定义刚体根类
public abstract class MyBody {
	public Body body;// 对应物理引擎中的刚体
	float halfWidth;// 半宽
	float halfHeight;// 半高
	Bitmap bitmap;// 刚体的图片
	public boolean isSurvived = true;
	public boolean isDraw = true;
	public boolean isCentre = false;
	public boolean isWindmill = false;

	public void drawSelf(Canvas canvas) {
		if (!isDraw) {
			return;
		}
		float x = body.getPosition().x * ConstantUtil.RATE;
		float y = body.getPosition().y * ConstantUtil.RATE;
		if (isWindmill && !isCentre) {
			body.setXForm(body.getPosition(), ConstantUtil.WindmillSecondAngle);
		} else if (isWindmill && isCentre) {
			ConstantUtil.WindmillSecondAngle = body.getAngle();
		}
		float angle = body.getAngle();

		canvas.save();
		Matrix m1 = new Matrix();
		m1.setRotate((float) Math.toDegrees(angle), x, y);
		canvas.setMatrix(m1);
		// 设置一个可视区域
		canvas.clipRect(x - halfWidth - 10, y - halfHeight - 10, x + halfWidth
				+ 10, y + halfHeight + 10);
		canvas.drawBitmap(bitmap, x - halfWidth, y - halfHeight, null);
		canvas.restore();
	};

	public MyBody(Body body, float halfWidth, float halfHeight, Bitmap bitmap) {
		this.body = body;
		this.halfWidth = halfWidth;
		this.halfHeight = halfHeight;
		this.bitmap = bitmap;
	}

	/**
	 * 赋予每个刚体特有功能
	 */
	public abstract void function(MyBody body);
}
