package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import com.zoipuus.balanceball.ConstantUtil;

import android.graphics.Bitmap;

/**
 * ��ת�ĸ���
 * 
 * @author zoipuus
 * 
 */
public class MyRotateBody extends MyUnkownStoneBody {

	private RevoluteJointDef rjd;
	private RevoluteJoint rj;
	private float lowerAngle;
	private float upperAngle;
	private boolean isFirst = true;

	public MyRotateBody(Body body, Body body1, float halfWidth,
			float halfHeight, Bitmap bitmap, float lowerAngle, float upperAngle) {
		super(body, halfWidth, halfHeight, bitmap);
		// TODO Auto-generated constructor stub
		this.lowerAngle = lowerAngle + 0.05f;
		this.upperAngle = upperAngle - 0.05f;
		rjd = new RevoluteJointDef();
		rjd.initialize(body, body1, body.getWorldCenter());
		rjd.lowerAngle = lowerAngle;
		rjd.upperAngle = upperAngle;
		rjd.enableLimit = true;// �򿪽Ƕ�����
		rjd.maxMotorTorque = 100000;// �������
		rjd.enableMotor = true;// �����
		rj = (RevoluteJoint) ConstantUtil.world.createJoint(rjd);
	}

	public MyRotateBody(Body body, Body body1, int halfWidth, int halfHeight,
			Bitmap bitmap) {
		super(body, halfWidth, halfHeight, bitmap);
		// TODO Auto-generated constructor stub
		RevoluteJointDef rjd = new RevoluteJointDef();
		rjd.initialize(body, body1, body.getWorldCenter());
		rjd.maxMotorTorque = 100000;// �������
		rjd.motorSpeed = (float) (Math.PI / 18);// ת��
		rjd.enableMotor = true;// �����
		ConstantUtil.world.createJoint(rjd);
	}

	@Override
	public void function(MyBody body) {
		// TODO Auto-generated method stub
		// super.function(body);
		if (isFirst) {
			isFirst = false;
			new Thread() {
				public void run() {
					while (ConstantUtil.isStartGame) {
						rjd.body2.wakeUp();
						if (rj.getJointAngle() <= lowerAngle) {
							rj.m_motorSpeed = (float) (Math.PI / 7);// ת��
						} else if (rj.getJointAngle() >= upperAngle) {
							rj.m_motorSpeed = (float) (-Math.PI / 7);// ת��
						}
						try {
							sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			}.start();
		}
	}
}
