package com.zoipuus.balanceball;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * 常量类
 * 
 * @author Administrator
 * 
 */
public class ConstantUtil {
	// 创建刚体时用到的常量
	public static final int ic_ball = 1;
	public static final int ic_hole = 2;
	public static final int ic_finish = 3;
	public static final int ic_triangle_up = 4;
	public static final int ic_triangle_right = 5;
	public static final int ic_triangle_down = 6;
	public static final int ic_triangle_left = 7;
	public static final int ic_stone = 8;
	public static final int ic_stone_bent1 = 9;
	public static final int ic_stone_bent4 = 10;
	public static final int ic_stone_bent2 = 11;
	public static final int ic_stone_bent3 = 12;
	public static final int ic_stone_horizontal = 13;
	public static final int ic_stone_vertical = 14;
	public static final int ic_stone_unkown = 15;
	public static final int ic_detonate_horizontal_up = 16;
	public static final int ic_detonate_vertical_right = 17;
	public static final int ic_triangle_up_not_static = 18;
	public static final int ic_triangle_right_not_static = 19;
	public static final int ic_triangle_down_not_static = 20;
	public static final int ic_triangle_left_not_static = 21;
	public static final int ic_stone_not_static = 22;
	public static final int ic_stone_bent1_not_static = 23;
	public static final int ic_stone_bent4_not_static = 24;
	public static final int ic_stone_bent2_not_static = 25;
	public static final int ic_stone_bent3_not_static = 26;
	public static final int ic_stone_horizontal_not_static = 27;
	public static final int ic_stone_vertical_not_static = 28;
	public static final int ic_stone_unkown_not_static = 29;
	public static final int ic_detonate_horizontal_up_not_static = 30;
	public static final int ic_detonate_vertical_right_not_static = 31;
	public static final int ic_detonate_horizontal_down = 32;
	public static final int ic_detonate_vertical_left = 33;
	public static final int ic_detonate_horizontal_down_not_static = 34;
	public static final int ic_detonate_vertical_left_not_static = 35;
	public static final int ic_unkown = 36;
	public static final int ic_key = 37;
	public static final int ic_lock = 38;
	public static final int ic_gold = 39;
	public static final int ic_key_not_static = 40;
	public static final int ic_lock_not_static = 41;
	public static final int ic_gold_not_static = 42;
	public static final int ic_stone_3bent_down = 43;
	public static final int ic_rotate_up = 50;
	public static final int ic_rotate_down = 51;
	public static final int ic_rotate_left = 52;
	public static final int ic_rotate_right = 53;
	public static final int ic_windmill = 54;

	// Handler用到的常量
	public static final int SOUND_AccessIntegral = 1;
	public static final int SOUND_Collision = 2;
	public static final int SOUND_InHole = 3;
	public static final int Vibrate = 4;
	public static final int showExitGameDialog = 15;
	public static final int showSelectLevelDialog = 16;
	public static final int showChallengeTimeDialog = 17;
	public static final int showAccessIntegralDialog = 18;
	public static final int showShareGameDialog = 44;
	public static final int showHelpGameDialog = 19;
	public static final int showGameSettingDialog = 20;
	public static final int showProcessDialog = 30;
	public static final int dismissProcessDialog = 31;
	public static final int getKey = 37;
	public static final int lostKey = 38;
	public static final int getGold = 39;
	public static final int openLock = 40;
	public static final int spendPoints = 41;
	public static final int AdvertisingView = 42;
	public static final int btn_Pause = 43;
	// 游戏模式
	public static final int GameMode_Normal = 50;
	public static final int GameMode_Challenge = 51;
	public static final int showProcessView = 52;
	public static final int showWelcomeView = 53;
	public static final int showGameView = 54;
	public static final int showWinView = 55;
	public static final int showFailView = 56;
	// 插入数据
	public static final int InsertData = 200;
	public static boolean haveInsertData;

	public static final int playerRevive = 1;
	public static final String ENCODING = "UTF-8";
	public static final String fileName = "gamedata.dat";

	// JBox2D游戏引擎需要用到的常量
	public static final float RATE = 10;// 屏幕与现实世界的比例
	public static float limit_right;// 世界的右边界
	public static float limit_bottom;// 世界的下边界
	public static final float TIME_STEP = 6.0f / 60.0f;// 模拟的频率
	public static final int ITERA = 10;// 迭代次数

	// 设置界面
	public static boolean isVibrate;
	/**
	 * 第一个参数为执行震动方法后多长时间开始震动 第二个参数为震动持续时间，两个参数必须为long类型
	 */
	public static long[] COLLISION_SOUND_PATTERN = { 0l, 30l };
	/**
	 * 当碰撞速度超过这个时发出声音
	 */
	public static final float COLLISIONVELOCITY = 3.0f;
	public static boolean isPlayMusic;
	// 是否开启有米广告
	public static boolean isOpenAds;
	public static int lingMinDu;
	public static Vec2 GRAVITYTEMP = new Vec2(0.0f, 0.0f);// 临时重力参考量，由重力传感器给他赋值

	public static int Window_Width;
	public static int Window_Height;
	public static int player_start_location_x;
	public static int player_start_location_y;
	public static Vec2 player_death_location;
	public static World world;
	public static boolean isStartGame;
	public static int textSize = 24;
	public static int pw = 3;
	public static int ph = 3;
	public static float WindmillSecondAngle;
	public static int KeyNumber = 0;
	
	//用户微博信息
	public static final String APP_KEY = "3760909850";
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
}
