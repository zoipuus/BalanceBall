package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;

import com.zoipuus.balanceball.ConstantUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

//�Զ���������
public abstract class MyBody {
	public Body body;// ��Ӧ���������еĸ���
	float halfWidth;// ���
	float halfHeight;// ���
	Bitmap bitmap;// �����ͼƬ
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
		// ����һ����������
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
	 * ����ÿ���������й���
	 */
	public abstract void function(MyBody body);
}
