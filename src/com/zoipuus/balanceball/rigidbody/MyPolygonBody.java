package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;
import android.graphics.Bitmap;

//自定义的矩形类(图片)

/**
 * 创建多边形图片
 * 
 * @author zoipuus
 * 
 */
public class MyPolygonBody extends MyBody {

	public MyPolygonBody(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap) {
		super(body, halfWidth, halfHeight, bitmap);
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub

	}
}
