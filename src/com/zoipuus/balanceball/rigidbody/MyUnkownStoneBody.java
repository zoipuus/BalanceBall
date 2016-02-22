package com.zoipuus.balanceball.rigidbody;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

/**
 * 创建未知刚体（碰撞后有奖罚）
 * 
 * @author zoipuus
 * 
 */
public class MyUnkownStoneBody extends MyBody {

	public int stone_unkown_num;

	public MyUnkownStoneBody(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap) {
		super(body, halfWidth, halfHeight, bitmap);
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int r = random.nextInt(4);
		switch (r) {
		default:
		case 0:
			this.body.setLinearVelocity(new Vec2(0, -30));
			break;
		case 1:
			this.body.setLinearVelocity(new Vec2(0, 30));
			break;
		case 2:
			this.body.setLinearVelocity(new Vec2(-30, 0));
			break;
		case 3:
			this.body.setLinearVelocity(new Vec2(30, 0));
			break;
		}
	}

}
