package com.zoipuus.balanceball.rigidbody;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.zoipuus.balanceball.ConstantUtil;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * ����δ֪���壨��ײ���н�����
 * 
 * @author zoipuus
 * 
 */
public class MyUnkownBody extends MyBody {

	private Handler mHandler;

	public MyUnkownBody(Body body, float halfWidth, float halfHeight,
			Bitmap bitmap, Handler mHandler) {
		super(body, halfWidth, halfHeight, bitmap);
		this.mHandler = mHandler;
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub
		MyPlayerBody myPlayer = (MyPlayerBody) body;
		Random random = new Random();
		int r = random.nextInt(6);
		switch (r) {
		// ��Ҵ���ʧ��
		default:
		case 0:
		case 3:
			myPlayer.isDraw = false;
			myPlayer.isFail = true;
			break;
		// û��
		case 1:
		case 4:
			break;
		// ��һص���ʼλ��
		case 5:
			body.body.setXForm(new Vec2(ConstantUtil.player_start_location_x
					* ConstantUtil.RATE, ConstantUtil.player_start_location_y
					* ConstantUtil.RATE), body.body.getAngle());
			break;
		// ��һ�ý�ҽ���
		case 2:
			mHandler.sendEmptyMessage(ConstantUtil.getGold);
			break;
		}
		this.isDraw = false;
	}
}
