/**
 * 工程�?: MainActivity
 * 文件�?: AppConstant.java
 * 包名: com.sepcialfocus.android.config
 * 日期: 2015-9-6下午9:07:02
 * 类名: AppConstant <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-6 下午9:07:02 <br/>
 *
 * @author   leixun
 * @version  	 
 */
package com.sepcialfocus.android;
public class AppConstant {
	
	public static final String UPYUN = "http://xmpigai.b0.upaiyun.com";
	public static final String SHARE_ICON = "http://xmpigai.b0.upaiyun.com/test/img/enjoyread.png";
	
	// 空间名
	public static final String bucket = "xmpigai"; 
	// 表单密钥
	public static final String formApiSecret = "/LfWYSGfXOD1md3eIcXB9pHUBPE=";
	
	public static final String SHARE_HTML = "footer.mp3";
	
	/** 下一页的存放地址 */
	public static final String URL_NEXT_PAGE_FILE = "next_file";
	public static final String MENU_FILE = "menu_file";
	
	/** webview 字体大小 */
	public static final String TEXTSIZE = "text_size";
	
	/** 无图模式 �?�? */
	public static final String FLAG_IMG = "no_img";
	
	/** 无悬浮窗�?�? */
	public static final String FLAG_WINDOW = "no_window";
	
	/** 文章推送 */
	public static final String FLAG_NOTIFICATION = "notify_artile";
	

	public static final int WEBVIEW_WIDTH = 320;
	public static final int JUMP_CODE = 0x02;
	
	public static final String YOUMI_APPID = "d6ed809e39ffe869";
	public static final String YOUMI_APPSECRET = "08420089c668f93d";
	
	  /**
     * QQ登录ID
     */
    public static final String QQ_APP_ID = "1104890846";
    /**
     * QQ登录KEY
     */
    public static final String QQ_APP_KEY = "eLw7U5MvXUca3Pj7";
    
    /**
     * 人人网appid
     */
    public static final String Renren_APP_ID = "481449";
    
    /**
     * 人人网key
     */
    public static final String Renren_APP_KEY = "c8cd26ef88d246f8afd8e6cf3dc7056a";
    
    /**
     * 人人网secret
     */
    public static final String Renren_APP_SECRET = "97b04877255f44c4be202b9f45d15931";
    
    /**
     * 微信登录ID
     */
    public static final String WEIXIN_APP_ID = "wxb300c4ed29347222";
    /**
     * 微信登录KEY
     */
    public static final String WEIXIN_APP_SECRET = "e34fb6efaa490060754459b6f369503e";
    
    
    public static final String DESCRIPTOR = "com.umeng.share";
    
	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页�?
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，�?以定义为何种形式都将不影响， 但是没有定义将无法使�? SDK 认证登录�?
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String SINA_REDIRECT_URL = "http://www.xiaoma.com";  //http://www.xiaoma.com


	/**
	 * Scope �? OAuth2.0 授权机制�? authorize 接口的一个参数�?��?�过 Scope，平台将�?放更多的微博
	 * 核心功能给开发�?�，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权�? 选择赋予应用的功能�??
	 * 
	 * 我们通过新浪微博�?放平�?-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请�??
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔�?
	 * 
	 * 有关哪些 OpenAPI �?要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SIAN_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
    
    /**
     * 新浪登录的APPKEY
     */
    public static String SIAN_APP_KEY = "1295897724";
    
    
    /**
     * 友盟在线参数
     */
    public static final String umeng_share_content = "share_content";
    public static final String umeng_share_title = "share_title";
    public static final String umeng_share_targetUrl = "share_targetUrl";
}

