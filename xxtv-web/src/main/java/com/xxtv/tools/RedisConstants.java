package com.xxtv.tools;

/**
 * redis常量
 * 
 * @author zyz
 *
 */
public class RedisConstants
{

	/**
	 * 删除缓存时间(单位天)
	 */
	public static final int		DEL_CACHETIME					= 24;

	/**
	 * 默认缓存时间一天(单位秒)
	 */
	public static final int		CACHETIME_DAY					= 3600 * 24;

	/**
	 * 默认缓存时间二天(单位秒)
	 */
	public static final int		CACHETIME_2DAY					= 3600 * 24 * 2;

	/**
	 * 默认缓存时间一周(单位秒)
	 */
	public static final int		CACHETIME_WEEK					= 3600 * 24 * 7;

	/**
	 * 默认缓存时间一月(单位秒)
	 */
	public static final int		CACHETIME_MONTH					= 3600 * 24 * 30;

	/**
	 * 默认缓存时间三月(单位秒)
	 */
	public static final int		CACHETIME_3MONTH				= 3600 * 24 * 90;

	/**
	 * 默认缓存时间六月(单位秒)
	 */
	public static final int		CACHETIME_6MONTH				= 3600 * 24 * 180;

	/**
	 * 默认缓存时间年(单位秒)
	 */
	public static final int		CACHETIME_YEAR					= 3600 * 24 * 365;

	/**
	 * 无图标广告上报key
	 */
	public static final String	ADS_NOICON_REPORT				= "anr_";

	/**
	 * 无图标广告上报详情key
	 */
	public static final String	ADS_NOICON_REPORT_DETAIL		= "anrd_";

	/**
	 * 无图标广告上报key
	 */
	public static final String	ADS_NOICON_PUSH_REPORT			= "anpr_";

	/**
	 * 无图标广告上报详情key
	 */
	public static final String	ADS_NOICON_PUSH_REPORT_DETAIL	= "anprd_";

	/**
	 * 无图标app上报key
	 */
	public static final String	ADS_NOICON_APP_REPORT			= "anar_";

	/**
	 * 无图标app上报详情key
	 */
	public static final String	ADS_NOICON_APP_REPORT_DETAIL	= "anard_";

	/**
	 * 用户安装的广告
	 */
	public static final String	ADS_NOICON_INSTALL_DETAIL		= "anid_";

	/**
	 * 每条无图标广告每日限制展示数的key
	 */
	public static final String	ADS_NOICON_DAILYSHOWNUM			= "and_";

	/**
	 * app每日活跃key前缀
	 * 用于判断每日活跃量
	 */
	public static final String	APP_DAY_ACTIVE					= "ada_";

	/**
	 * app永久存活
	 * 用于判断新增量(永久去重)
	 */
	public static final String	APP_FOREVER_ACTIVE				= "afa_";

	/**
	 * update app每日活跃key前缀
	 * 用于判断每日活跃量
	 */
	public static final String	APP_UPDATE_DAY_ACTIVE			= "auda_";

	/**
	 * update app永久存活
	 * 用于判断新增量(永久去重)
	 */
	public static final String	APP_UPDATE_FOREVER_ACTIVE		= "aufa_";

	/**
	 * update app升级成功每日活跃key前缀
	 * 用于判断每日活跃量
	 */
	public static final String	APP_UPDATE_SUCCESS_DAY_ACTIVE	= "ausda_";

	/**
	 * 邀请页   浏览量统计
	 */
	public static final String	HASH_INVITE_PV					= "hash_invite_pv_";

	/**
	 * 邀请页  下载量统计
	 */
	public static final String	HASH_INVITE_DOWN				= "hash_invite_down_";

	/**
	 * 活跃统计
	 */
	public static final String	HASH_INVITE_ACTIVE				= "hash_invite_active_";

	/**
	 * 积分墙通信的密匙
	 */
	public static final String	HASH_ITGWALL_SECRET_KEY			= "hash_itgwall_secret_key";

	/**
	 * 排行榜    收入排行
	 */
	public static final String	ZSET_INCOME						= "zset_income_";

	/**
	 * 排行已经推送集合
	 */
	public static final String	SET_RANKPUSH					= "rankpushed";

	/**
	 * 用户基本信息key
	 */
	public static final String	USER_INFO						= "user_info";

	/**
	 * 用户排名key
	 */
	public static final String	USER_RANK						= "user_rank";

	/**
	 * 红包侠当前最新版本号key
	 */
	public static final String	APP_CURRENTVERSION				= "app_currentversion";

	/**
	 * 系统数据字典key
	 */
	public static final String	SYS_DICTKEY						= "sys_dictkey";

	/**
	 * 提现优惠百分比HTML标签key
	 */
	public static final String	DRAWAL_FAVORABLE_HTML			= "drawal_favorable_html";

	/**
	 * 静态资源key
	 */
	public static final String	STATIC_DATASOURCE				= "static_datasource";

	/**
	 * 昨日超过多少人完成任务key
	 */
	public static final String	STATIC_YES_TASK_COUNT			= "static_yes_task_count";

	/**
	 * 积分墙排行key
	 */
	public static final String	STATIC_YES_ITGWALLRANK			= "static_yes_itgwallrank";

	/**
	 * 每日点击红包集合
	 */
	public static final String	SET_DAYS_BONUS					= "set_days_bonus_";

	/**
	 * 每日抽奖集合
	 */
	public static final String	SET_DAYS_AWARD					= "set_days_award_";

	/**
	 * 每日签到集合
	 */
	public static final String	SET_DAYS_SIGN					= "set_days_sign_";

	/**
	 * 任务获得抽奖次数
	 */
	public static final String	HASH_TASK_AWARD					= "hash_task_award";

	/**
	 * 当月签到数
	 */
	public static final String	HASH_MONTH_SIGNNUM				= "hash_month_signnum_";

	/**
	 * 每日任务提示集合
	 */
	public static final String	SET_DAYS_TASK					= "set_days_task_";

	/**
	 * 每日快速任务提示集合
	 */
	public static final String	SET_DAYS_QUICKTASK				= "set_days_quicktask_";

}
