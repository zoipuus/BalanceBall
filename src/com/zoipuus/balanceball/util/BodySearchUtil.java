package com.zoipuus.balanceball.util;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.os.Handler;

import com.zoipuus.balanceball.ConstantUtil;
import com.zoipuus.balanceball.rigidbody.MyBlankHoleBody;
import com.zoipuus.balanceball.rigidbody.MyBody;
import com.zoipuus.balanceball.rigidbody.MyCommonBody;
import com.zoipuus.balanceball.rigidbody.MyDetonateBody;
import com.zoipuus.balanceball.rigidbody.MyFinishBody;
import com.zoipuus.balanceball.rigidbody.MyGoldBody;
import com.zoipuus.balanceball.rigidbody.MyKeyBody;
import com.zoipuus.balanceball.rigidbody.MyLockBody;
import com.zoipuus.balanceball.rigidbody.MyPlayerBody;
import com.zoipuus.balanceball.rigidbody.MyPolygonBody;
import com.zoipuus.balanceball.rigidbody.MyRotateBody;
import com.zoipuus.balanceball.rigidbody.MyUnkownBody;
import com.zoipuus.balanceball.rigidbody.MyUnkownStoneBody;

public class BodySearchUtil {

	public static void doAction(Body body1, Body body2, Vec2 normal,
			Vec2 velocity, ArrayList<MyBody> bl,
			ArrayList<MyUnkownStoneBody> musbl, Handler mHandler) {
		// TODO Auto-generated method stub
		int firstType = 0;
		float angle = Vec2.dot(normal, velocity)
				/ (normal.length() * velocity.length());
		MyBody firstMyBody = null;
		int times = 0;
		for (MyBody mb : bl) {
			if (body1 == mb.body || body2 == mb.body) {
				times++;
				if (times == 1) {
					// �ж�mb�Ƿ�����MyPlayerBody��
					if (mb instanceof MyPlayerBody) {
						firstType = ConstantUtil.ic_ball;
					}
					// �ж�mb�Ƿ�����MyBlankHoleBody��
					else if (mb instanceof MyBlankHoleBody) {
						firstType = ConstantUtil.ic_hole;
					}
					// �ж�mb�Ƿ�����MyFinishBody��
					else if (mb instanceof MyFinishBody) {
						firstType = ConstantUtil.ic_finish;
					}
					// �ж�mb�Ƿ�����MyUnkownBody��
					else if (mb instanceof MyUnkownBody) {
						firstType = ConstantUtil.ic_unkown;
					}
					// �ж�mb�Ƿ�����MyPolygonBody��
					else if (mb instanceof MyPolygonBody) {
						firstType = ConstantUtil.ic_triangle_up;
					}
					// �ж�mb�Ƿ�����MyDetonateBody��
					else if (mb instanceof MyDetonateBody) {
						firstType = ConstantUtil.ic_detonate_horizontal_up;
					}
					// �ж�mb�Ƿ�����MyCommonBody��
					else if (mb instanceof MyCommonBody) {
						firstType = ConstantUtil.ic_stone;
					}
					// �ж�mb�Ƿ�����MyGoldBody��
					else if (mb instanceof MyGoldBody) {
						firstType = ConstantUtil.ic_gold;
					}
					// �ж�mb�Ƿ�����MyKeyBody��
					else if (mb instanceof MyKeyBody) {
						firstType = ConstantUtil.ic_key;
					}
					// �ж�mb�Ƿ�����MyLockBody��
					else if (mb instanceof MyLockBody) {
						firstType = ConstantUtil.ic_lock;
					}
					firstMyBody = mb;
				} else {
					// �ж�mb�Ƿ�����MyPlayerBody��
					if (mb instanceof MyPlayerBody) {
						boolean isPlay = false;
						if (velocity.length() > ConstantUtil.COLLISIONVELOCITY
								&& ConstantUtil.isPlayMusic
								&& Math.abs(angle) > 0.717// �Ƕ�����
						) {
							isPlay = true;
						} else {
							isPlay = false;
						}
						switch (firstType) {
						default:
							if (isPlay) {
								mHandler.sendEmptyMessage(ConstantUtil.SOUND_Collision);
							}
							break;
						case ConstantUtil.ic_hole:
							mb.isDraw = false;
							MyBlankHoleBody mbhb = (MyBlankHoleBody) firstMyBody;
							mbhb.function(mb);
							if (ConstantUtil.isVibrate) {
								mHandler.sendEmptyMessage(ConstantUtil.Vibrate);
							}
							break;
						case ConstantUtil.ic_triangle_up:
							mb.isDraw = false;
							firstMyBody.isDraw = false;
							MyPlayerBody myPlayer = (MyPlayerBody) mb;
							myPlayer.isFail = true;
							break;
						case ConstantUtil.ic_finish:
							mb.isDraw = false;
							firstMyBody.function(mb);
							break;
						case ConstantUtil.ic_unkown:
							if (isPlay) {
								mHandler.sendEmptyMessage(ConstantUtil.SOUND_Collision);
							}
							MyUnkownBody myBody4 = (MyUnkownBody) firstMyBody;
							myBody4.function(mb);
							break;
						case ConstantUtil.ic_key:
							firstMyBody.isDraw = false;
							mHandler.sendEmptyMessage(ConstantUtil.getKey);
							break;
						case ConstantUtil.ic_gold:
							firstMyBody.isDraw = false;
							mHandler.sendEmptyMessage(ConstantUtil.getGold);
							break;
						case ConstantUtil.ic_lock:
							MyLockBody myLock = (MyLockBody) firstMyBody;
							if (ConstantUtil.KeyNumber > 0) {
								mHandler.sendEmptyMessage(ConstantUtil.lostKey);
							} else {
								mHandler.obtainMessage(ConstantUtil.openLock,
										myLock.detonate_num, -1).sendToTarget();
								break;
							}
							myLock.isDraw = false;
							for (MyUnkownStoneBody musb : musbl) {
								if (musb instanceof MyUnkownStoneBody) {
									if (musb.stone_unkown_num == myLock.detonate_num) {
										musb.function(mb);
									}
								} else if (musb instanceof MyRotateBody) {
									MyRotateBody mrb = (MyRotateBody) musb;
									if (mrb.stone_unkown_num == myLock.detonate_num) {
										mrb.function(mb);
									}
								}
							}
							break;
						case ConstantUtil.ic_detonate_horizontal_up:
							MyDetonateBody myBody6 = (MyDetonateBody) firstMyBody;
							for (MyUnkownStoneBody musb : musbl) {
								if (musb instanceof MyUnkownStoneBody) {
									if (musb.stone_unkown_num == myBody6.detonate_num) {
										musb.function(mb);
									}
								} else if (musb instanceof MyRotateBody) {
									MyRotateBody mrb = (MyRotateBody) musb;
									if (mrb.stone_unkown_num == myBody6.detonate_num) {
										mrb.function(mb);
									}
								}
							}
							break;
						}
					}
					// �ж�mb�Ƿ�����MyBlankHoleBody��
					else if (mb instanceof MyBlankHoleBody) {
						firstMyBody.isDraw = false;
						mb.function(firstMyBody);
						if (firstMyBody instanceof MyPlayerBody
								&& ConstantUtil.isVibrate) {
							mHandler.sendEmptyMessage(ConstantUtil.Vibrate);
						}
					}
					// �ж�mb�Ƿ�����MyKeyBody��
					else if (mb instanceof MyKeyBody) {
						switch (firstType) {
						default:
							break;
						case ConstantUtil.ic_hole:
							mb.isDraw = false;
							MyBlankHoleBody mbhb = (MyBlankHoleBody) firstMyBody;
							mbhb.function(mb);
							break;
						case ConstantUtil.ic_ball:
							mb.isDraw = false;
							mHandler.sendEmptyMessage(ConstantUtil.getKey);
							break;
						}
					}
					// �ж�mb�Ƿ�����MyGoldBody��
					else if (mb instanceof MyGoldBody) {
						switch (firstType) {
						default:
							break;
						case ConstantUtil.ic_hole:
							mb.isDraw = false;
							MyBlankHoleBody mbhb = (MyBlankHoleBody) firstMyBody;
							mbhb.function(mb);
							break;
						case ConstantUtil.ic_ball:
							mb.isDraw = false;
							mHandler.sendEmptyMessage(ConstantUtil.getGold);
							break;
						}
					}
					// �ж�mb�Ƿ�����MyLockBody��
					else if (mb instanceof MyLockBody) {
						MyLockBody myLock = (MyLockBody) mb;
						switch (firstType) {
						default:
							break;
						case ConstantUtil.ic_ball:
							if (ConstantUtil.KeyNumber > 0) {
								mHandler.sendEmptyMessage(ConstantUtil.lostKey);
							} else {
								mHandler.obtainMessage(ConstantUtil.openLock,
										myLock.detonate_num, -1).sendToTarget();
								break;
							}
							myLock.isDraw = false;
							for (MyUnkownStoneBody musb : musbl) {
								if (musb instanceof MyUnkownStoneBody) {
									if (musb.stone_unkown_num == myLock.detonate_num) {
										musb.function(mb);
									}
								} else if (musb instanceof MyRotateBody) {
									MyRotateBody mrb = (MyRotateBody) musb;
									if (mrb.stone_unkown_num == myLock.detonate_num) {
										mrb.function(mb);
									}
								}
							}
							break;
						}
					}
					// �ж�mb�Ƿ�����MyFinishBody��
					else if (mb instanceof MyFinishBody) {
						switch (firstType) {
						default:
							break;
						case ConstantUtil.ic_ball:
							firstMyBody.isDraw = false;
							mb.function(firstMyBody);
							break;
						}
					}
					// �ж�mb�Ƿ�����MyUnkownBody��
					else if (mb instanceof MyUnkownBody) {
						MyUnkownBody myBody0 = (MyUnkownBody) mb;

						switch (firstType) {
						default:
							break;
						case ConstantUtil.ic_ball:
							MyPlayerBody myBody1 = (MyPlayerBody) firstMyBody;
							if (velocity.length() > ConstantUtil.COLLISIONVELOCITY
									&& ConstantUtil.isPlayMusic
									&& Math.abs(angle) > 0.717// �Ƕ�����
							) {
								mHandler.sendEmptyMessage(ConstantUtil.SOUND_Collision);
							}
							myBody0.function(myBody1);
							break;
						}
					}
					// �ж�mb�Ƿ�����MyPolygonBody��
					else if (mb instanceof MyPolygonBody) {
						MyPolygonBody myBody0 = (MyPolygonBody) mb;

						switch (firstType) {
						default:
							break;
						case ConstantUtil.ic_ball:
							MyPlayerBody myBody1 = (MyPlayerBody) firstMyBody;
							myBody1.isDraw = false;
							myBody1.isFail = true;
							myBody0.isDraw = false;
							break;
						case ConstantUtil.ic_hole:
							myBody0.isDraw = false;
							MyBlankHoleBody mbhb = (MyBlankHoleBody) firstMyBody;
							mbhb.function(myBody0);
							break;
						}
					}
					// �ж�mb�Ƿ�����MyDetonateBody��
					else if (mb instanceof MyDetonateBody) {
						MyDetonateBody myBody0 = (MyDetonateBody) mb;

						switch (firstType) {
						default:
							break;
						case ConstantUtil.ic_hole:
							myBody0.isDraw = false;
							MyBlankHoleBody mbhb = (MyBlankHoleBody) firstMyBody;
							mbhb.function(myBody0);
							break;
						case ConstantUtil.ic_ball:
							MyPlayerBody myBody1 = (MyPlayerBody) firstMyBody;
							for (MyUnkownStoneBody musb : musbl) {
								if (musb instanceof MyUnkownStoneBody) {
									if (musb.stone_unkown_num == myBody0.detonate_num) {
										musb.function(myBody1);
									}
								} else if (musb instanceof MyRotateBody) {
									MyRotateBody mrb = (MyRotateBody) musb;
									if (mrb.stone_unkown_num == myBody0.detonate_num) {
										mrb.function(myBody1);
									}
								}
							}
							break;
						}
					}
					// �ж�mb�Ƿ�����MyCommonBody��
					else if (mb instanceof MyCommonBody) {

						switch (firstType) {
						default:
							break;
						case ConstantUtil.ic_hole:
							mb.isDraw = false;
							MyBlankHoleBody mbhb = (MyBlankHoleBody) firstMyBody;
							mbhb.function(mb);
							break;
						}
					}
					break;
				}
			}
		}
	}
}
