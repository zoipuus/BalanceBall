package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

/**
 * ������ҿ��Ƶĸ���
 * 
 * @author zoipuus
 * 
 */
public class MyPlayerBody extends MyBody {

	public boolean isFail = false;
	public boolean isWin = false;

	public MyPlayerBody(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap) {
		super(body, halfWidth, halfHeight, bitmap);
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub

	}

}
