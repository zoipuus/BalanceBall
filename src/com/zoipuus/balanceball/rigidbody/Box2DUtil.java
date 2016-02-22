package com.zoipuus.balanceball.rigidbody;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.zoipuus.balanceball.ConstantUtil;

//����������״�Ĺ�����
public class Box2DUtil {
	/**
	 * ������������(ͼƬ)
	 * 
	 * @param x
	 * @param y
	 * @param halfWidth
	 * @param halfHeight
	 * @param isStatic
	 * @param world
	 * @return
	 */
	public static Body createRectBody(float x,// x����
			float y,// y����
			float halfWidth,// ���
			float halfHeight,// ���
			boolean isStatic,// �Ƿ�Ϊ��ֹ��
			World world// ����
	) {
		// �����������������
		PolygonDef shape = new PolygonDef();
		// �����ܶ�
		if (isStatic) {
			shape.density = 0;
		} else {
			shape.density = 1.0f;
		}
		// ����Ħ��ϵ��
		shape.friction = 0.0f;
		// ����������ʧ�ʣ�������
		shape.restitution = 0.3f;
		shape.setAsBox(halfWidth / ConstantUtil.RATE, halfHeight
				/ ConstantUtil.RATE);
		// ����������������
		BodyDef bodyDef = new BodyDef();
		// ����λ��
		bodyDef.position.set(x / ConstantUtil.RATE, y / ConstantUtil.RATE);
		// �������д�������
		Body bodyTemp = world.createBody(bodyDef);
		// ָ��������״
		bodyTemp.createShape(shape);
		bodyTemp.setMassFromShapes();

		return bodyTemp;
	}

	/**
	 * ����Բ�Σ�ͼƬ��
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @param isStatic
	 * @param world
	 * @return
	 */
	public static Body createCircleBody(float x,// x����
			float y,// y����
			float radius,// �뾶
			boolean isStatic,// �Ƿ�Ϊ��ֹ��
			World world// ����
	) {
		// ����Բ��������
		CircleDef shape = new CircleDef();
		// �����ܶ�
		if (isStatic) {
			shape.density = 0;
		} else {
			shape.density = 1.0f;
		}
		// ����Ħ��ϵ��
		shape.friction = 0.0f;
		// ����������ʧ�ʣ�������
		shape.restitution = 0.3f;
		// ���ð뾶
		shape.radius = radius / ConstantUtil.RATE;

		// ����������������
		BodyDef bodyDef = new BodyDef();
		// ����λ��
		bodyDef.position.set(x / ConstantUtil.RATE, y / ConstantUtil.RATE);
		// �������д�������
		Body bodyTemp = world.createBody(bodyDef);
		// ָ��������״
		bodyTemp.createShape(shape);
		bodyTemp.setMassFromShapes();
		return bodyTemp;
	}

	/**
	 * �������������(ͼƬ)
	 * 
	 * @param x
	 * @param y
	 * @param points
	 * @param isStatic
	 * @param world
	 * @return
	 */
	public static Body createPolygonBody(float x,// x����
			float y,// y����
			float[][] points,// ������
			boolean isStatic,// �Ƿ�Ϊ��ֹ��
			World world// ����
	) {
		// �����������������
		PolygonDef shape = new PolygonDef();
		// �����ܶ�
		if (isStatic) {
			shape.density = 0;
		} else {
			shape.density = 1.0f;
		}
		// ����Ħ��ϵ��
		shape.friction = 0.0f;
		// ����������ʧ�ʣ�������
		shape.restitution = 0.3f;

		for (float[] fa : points) {
			shape.addVertex(new Vec2(fa[0] / ConstantUtil.RATE, fa[1]
					/ ConstantUtil.RATE));
		}

		// ����������������
		BodyDef bodyDef = new BodyDef();
		// ����λ��
		bodyDef.position.set(x / ConstantUtil.RATE, y / ConstantUtil.RATE);
		// �������д�������
		Body bodyTemp = world.createBody(bodyDef);
		// ָ��������״
		bodyTemp.createShape(shape);
		bodyTemp.setMassFromShapes();

		return bodyTemp;
	}
}