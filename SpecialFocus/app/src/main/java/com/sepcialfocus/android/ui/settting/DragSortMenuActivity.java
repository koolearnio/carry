/**
 * 工程名: SpecialFocus
 * 文件名: DragSortMenuActivity.java
 * 包名: com.sepcialfocus.android.ui.settting
 * 日期: 2015年9月8日上午11:15:51
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.settting;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.MD5Utils;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.NavBean;
import com.sepcialfocus.android.ui.MainActivity;
import com.sepcialfocus.android.ui.adapter.DragAdapter;
import com.sepcialfocus.android.ui.adapter.DragNavAdapter;
import com.sepcialfocus.android.ui.adapter.OtherAdapter;
import com.sepcialfocus.android.ui.widget.DragGridView;
import com.sepcialfocus.android.widgets.DragGrid;
import com.sepcialfocus.android.widgets.OtherGridView;

/**
 * 类名: DragSortMenuActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015年9月8日 上午11:15:51 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class DragSortMenuActivity extends BaseFragmentActivity implements View.OnClickListener,OnItemClickListener{
	
	 /** 用户栏目的GRIDVIEW */
    private DragGrid userGridView;
    /** 其它栏目的GRIDVIEW */
    private OtherGridView otherGridView;
    /** 用户栏目对应的适配器，可以拖动 */
    DragAdapter userAdapter;
    /** 其它栏目对应的适配器 */
    OtherAdapter otherAdapter;
    /** 其它栏目列表 */
    ArrayList<NavBean> otherChannelList = new ArrayList<NavBean>();
    /** 用户栏目列表 */
    ArrayList<NavBean> userChannelList = new ArrayList<NavBean>();
    /** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
    boolean isMove = false;
    
	ArrayList<NavBean> list = null;
	DragGridView mDragGridView;
	DragNavAdapter mDragAdapter = null;
	
	TextView mTitleTv;
	ImageView mBackImg;
	KJDB mDb;
	
	private boolean isFromMainActivity = false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_dragmenusoft);
		mDb = KJDB.create(this);
		if(getIntent().getExtras()!=null 
				&& getIntent().getExtras().getSerializable("key")!=null){
			isFromMainActivity = true;
			userChannelList = (ArrayList<NavBean>)getIntent().getSerializableExtra("key");
		}else{
			isFromMainActivity = false;
			userChannelList = getMenuList();
		}
		List<NavBean> list = mDb.findAllByWhere(NavBean.class, "show = \'0\'");
		if(list!=null && list.size()>0){
			otherChannelList = (ArrayList<NavBean>)list;
		}
		initView();
	}
	
	protected void initView(){
		userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
        
        
		mTitleTv = (TextView)findViewById(R.id.tv_title);
		mTitleTv.setText("栏目设置");
		mBackImg = (ImageView)findViewById(R.id.img_title_back);
		mBackImg.setOnClickListener(this);
		
		userAdapter = new DragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(otherAdapter);
        // 设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.img_title_back:
			goBack();
			break;
		}
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		
		 // 如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                // position为 0，1 的不可以进行任何操作
                if (position!=0) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final NavBean channel = ((DragAdapter) parent.getAdapter())
                                .getItem(position);// 获取点击的频道内容
                        otherAdapter.setVisible(false);
                        // 添加到最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    // 获取终点的坐标
                                    otherGridView
                                            .getChildAt(otherGridView.getLastVisiblePosition())
                                            .getLocationInWindow(endLocation);
                                    moveAnim(moveImageView, startLocation, endLocation, channel,
                                            userGridView);
                                    userAdapter.setRemove(position);
                                    updateChannel(channel,"0");
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final NavBean channel = ((OtherAdapter) parent.getAdapter())
                            .getItem(position);
                    userAdapter.setVisible(false);
                	NavBean channels = new NavBean();
                	if(channel!=null){
                		channels.setCategory(channel.getCategory());
                		channels.setShow("1");
                		channels.setMd5(channel.getMd5());
                		channels.setMenu(channel.getMenu());
                		channels.setMenuUrl(channel.getMenuUrl());
                	}
                    // 添加到最后一个
                    userAdapter.addItem(channels);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                // 获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition())
                                        .getLocationInWindow(endLocation);
                                moveAnim(moveImageView, startLocation, endLocation, channel,
                                        otherGridView);
                                otherAdapter.setRemove(position);
                                updateChannel(channel,"1");
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     * 
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void moveAnim(View moveView, int[] startLocation, int[] endLocation,
            final NavBean moveChannel,
            final GridView clickGridView) {
        // 将当前栏目增加到改变过的listview中 若栏目已经存在删除点，不存在添加进去

        int[] initLocation = new int[2];
        // 获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        // 得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        // 创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);// 动画时间
        // 动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     * 
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     * 
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /** 退出时候保存选择后数据库的设置 */
    void saveChannel() {
    	if(userChannelList!=null && userChannelList.size()>0){
    		int length = userChannelList.size();
    		for(int i =0 ; i < length ; i ++){
    			NavBean channel = userChannelList.get(i);
    			channel.setOrderId(i);
    			mDb.update(channel,"md5 = '"+MD5Utils.md5(channel.getMenu()+channel.getMenuUrl())+"'");
    		}
    	}
    }
	
    private void updateChannel(NavBean bean , String visible){
    	NavBean channel = new NavBean();
    	if(bean!=null){
    		channel.setCategory(bean.getCategory());
    		channel.setShow(visible);
    		channel.setMd5(bean.getMd5());
    		channel.setMenu(bean.getMenu());
    		channel.setMenuUrl(bean.getMenuUrl());
    	}
    	if(mDb!=null){
    		mDb.update(channel,"md5 = '"+MD5Utils.md5(channel.getMenu()+channel.getMenuUrl())+"'");
    	}
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){ 
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}
    
    private void goBack(){
    	if(userChannelList!=null && userChannelList.size()<1){
    		Toast.makeText(DragSortMenuActivity.this, "大哥，喜欢阅读的人运气都不会太差! \n您加一个栏目吧", Toast.LENGTH_LONG).show();
    		return;
    	}
    	saveChannel();
    	if(isFromMainActivity){
    		Intent intent = new Intent(DragSortMenuActivity.this,MainActivity.class);
    		startActivity(intent);
    		finish();
    	} else{
    		finish();
    	}
    }
    
    private ArrayList<NavBean> getMenuList(){
		List<NavBean> list  = mDb.findAllByWhere(NavBean.class, "show = \'1\'");
		if(list!=null && list.size()>0){
			return (ArrayList<NavBean>) list;
		}else{
			list = new ArrayList<NavBean>();
		}
		String[] menuName = getResources().getStringArray(R.array.menu_str);
		String[] menuUrl = getResources().getStringArray(R.array.menu_url);
		for(int i = 0 ; i<menuName.length ; i++){
			NavBean bean = new NavBean();
			bean.setMd5(MD5Utils.md5(menuName[i]+menuUrl[i]));
			bean.setMenu(menuName[i]);
			bean.setMenuUrl(menuUrl[i]);
			bean.setShow("1");
			bean.setCategory(1);
			list.add(bean);
			mDb.save(bean);
		}
		return (ArrayList<NavBean>)list;
	}
}

