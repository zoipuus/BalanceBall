package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

/**
 * 创建引爆器刚体
 * 
 * @author zoipuus
 * 
 */
public class MyDetonateBody extends MyBody {

	public int detonate_num;

	public MyDetonateBody(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap) {
		super(body, halfWidth, halfHeight, bitmap);
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub

	}

}
