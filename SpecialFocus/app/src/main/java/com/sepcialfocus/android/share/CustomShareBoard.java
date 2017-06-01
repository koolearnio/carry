/**
 * 工程名: IeltsCode
 * 文件名: CustomShareBoard.java
 * 包名: com.xiaoma.ieltstone.widgets
 * 日期: 2015-6-16上午11:55:17
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.sepcialfocus.android.AppConstant;
import com.sepcialfocus.android.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 类名: CustomShareBoard <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-6-16 上午11:55:17 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

    private UMSocialService mController;
    private Activity mActivity;
    private LinearLayout wechat,friends,sina,qq,qzone,renren;
    
    private boolean isSetShareFlag = false;
    public CustomShareBoard(Activity activity) {
        super(activity);
        this.mActivity = activity;
        mController = UMServiceFactory.getUMSocialService(AppConstant.DESCRIPTOR);
		configPlatforms();
        initView(activity);
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.share_uipopupwindow, null);
        wechat=(LinearLayout) rootView.findViewById(R.id.ll_wechat);
		friends=(LinearLayout) rootView.findViewById(R.id.ll_friends);
		sina=(LinearLayout) rootView.findViewById(R.id.ll_sina);
		qq=(LinearLayout) rootView.findViewById(R.id.ll_qq);
		qzone=(LinearLayout) rootView.findViewById(R.id.ll_qzone);
		renren=(LinearLayout) rootView.findViewById(R.id.ll_renren);
		
		wechat.setOnClickListener(this);
		friends.setOnClickListener(this);
		sina.setOnClickListener(this);
		qq.setOnClickListener(this);
		qzone.setOnClickListener(this);
		renren.setOnClickListener(this);
		
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
//        setAnimationStyle(R.style.AnimBottom);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.ll_wechat){
        // 微信分享
        	performShare(SHARE_MEDIA.WEIXIN);
        }else if(id == R.id.ll_friends){
        	// 朋友圈分享
        	performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
        }else if(id == R.id.ll_sina){
        	performShare(SHARE_MEDIA.SINA);
        }else if(id == R.id.ll_qq){
        	performShare(SHARE_MEDIA.QQ);
        }else if(id == R.id.ll_qzone){
        	performShare(SHARE_MEDIA.QZONE);
        }else if(id == R.id.ll_renren){
        	performShare(SHARE_MEDIA.RENREN);
        }
        dismiss();
    }

    private void performShare(SHARE_MEDIA platform) {
    	mController.getConfig().closeToast();
        mController.postShare(mActivity, platform, new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
            	String showText = "";
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享成功";
                    Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
                } else {
//                    showText = "分享失败";
                }
//                Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
    
    public final String getString(int resId) {
        return mActivity.getResources().getString(resId);
    }
    
    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加QQ、QZone平台
        addQQQZonePlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }
    
    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = AppConstant.WEIXIN_APP_ID;
        String appSecret = AppConstant.WEIXIN_APP_SECRET;
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform() {
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity,
                AppConstant.QQ_APP_ID, AppConstant.QQ_APP_KEY);
//        qqSsoHandler.setTargetUrl(getString(R.string.share_target_url_str));
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, 
        		AppConstant.QQ_APP_ID, AppConstant.QQ_APP_KEY);
        qZoneSsoHandler.addToSocialSDK();
    }
    
    public void setShareContent(String shareContent,String shareTitle,
    		String shareTargetUrl,String shareTargetIcon){
    	
    	mController.getConfig().closeToast();
    	
    	// 配置SSO
        new SinaSsoHandler().addToSocialSDK();
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity,
                AppConstant.QQ_APP_ID, AppConstant.QQ_APP_KEY);
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent(shareContent);

        // APP ID：201874, API
        // * KEY：28401c0964f04a72a14c812d6132fcef, Secret
        // * Key：3bf66e42db1e4fa9829b955cc300b737.
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(mActivity,
                AppConstant.Renren_APP_ID, AppConstant.Renren_APP_KEY,
                AppConstant.Renren_APP_SECRET);
        renrenSsoHandler.addToSocialSDK();

        UMImage localImage = new UMImage(mActivity, R.drawable.icon);


        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent(shareContent);
        weixinContent.setTitle(shareTitle);
        weixinContent.setTargetUrl(shareTargetUrl);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia
                .setShareContent(shareContent);
        circleMedia.setTitle(shareTitle);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(shareTargetUrl);
        mController.setShareMedia(circleMedia);

        // 设置renren分享内容
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent(shareTargetUrl+shareContent);
        UMImage image = new UMImage(mActivity,shareTargetIcon);
        image.setTitle(shareTargetUrl+shareTitle);
        image.setThumb(shareTargetIcon);
        image.setTargetUrl(shareTargetIcon);
        renrenShareContent.setShareMedia(image);
        renrenShareContent.setTargetUrl(shareTargetUrl);
        renrenShareContent.setTitle(shareTargetUrl+shareTitle);
        mController.setShareMedia(renrenShareContent);
        mController.setAppWebSite(SHARE_MEDIA.RENREN,shareTargetUrl);

        UMImage qzoneImage = new UMImage(mActivity,
                shareTargetIcon);
        qzoneImage.setThumb(shareTargetIcon);
        qzoneImage.setTargetUrl(shareTargetIcon);
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(shareContent);
        qzone.setTargetUrl(shareTargetUrl);
        qzone.setTitle(shareTitle);
//        qzone.setShareImage(new UMImage(mActivity,R.drawable.share_share));
        mController.setShareMedia(qzone);


        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(shareContent);
        qqShareContent.setTitle(shareTitle);
        qqShareContent.setShareMedia(image);
        qqShareContent.setTargetUrl(shareTargetUrl);
        mController.setShareMedia(qqShareContent);


        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent
                .setShareContent(shareTargetUrl+shareContent);
        sinaContent.setTitle(shareTitle);
        sinaContent.setTargetUrl(shareTargetUrl);
        sinaContent.setShareImage(image);
        mController.setShareMedia(sinaContent);
    }
}

