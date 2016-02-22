package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

/**
 * 创建普通刚体
 * 
 * @author zoipuus
 * 
 */
public class MyCommonBody extends MyBody {

	public MyCommonBody(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap) {
		super(body, halfWidth, halfHeight, bitmap);
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub

	}

}
