/**
 * 工程名: MainActivity
 * 文件名: FeedbackActivity.java
 * 包名: com.sepcialfocus.android.ui.settting
 * 日期: 2015-9-30上午7:14:06
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.settting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mike.aframe.utils.DensityUtils;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.utils.SettingsManager;
import com.sepcialfocus.android.widgets.swiptlistview.SwipeListView;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

/**
 * 类名: FeedbackActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-30 上午7:14:06 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class FeedbackActivity extends BaseFragmentActivity implements View.OnClickListener ,
		SwipeRefreshLayout.OnRefreshListener{
	private SwipeRefreshLayout mSwipeLayout;
	private SwipeListView mListView;
	private FeedbackAgent mAgent;
	private Conversation mComversation;
	private Context mContext;
	private ReplyAdapter adapter;
	private Button sendBtn;
	private EditText inputEdit;
	private final int VIEW_TYPE_COUNT = 2;
	private final int VIEW_TYPE_USER = 0;
	private final int VIEW_TYPE_DEV = 1;
	private TextView mTitleTv; 
	private ImageView back_left;
	private Reply mReply = null;
	private List<Reply> mList ;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		mContext = this;
		mReply = new Reply(getResources().getString(R.string.dev_tips),
				"0",Reply.TYPE_DEV_REPLY,000);
		initView();
		mAgent = new FeedbackAgent(this);
		mComversation = new FeedbackAgent(this).getDefaultConversation();
		resetList();
		adapter = new ReplyAdapter();
		mListView.setAdapter(adapter);
		sync();

	}

	protected void initView() {
		back_left = (ImageView)findViewById(R.id.img_title_back);
		mTitleTv = (TextView)findViewById(R.id.tv_title);
		back_left.setOnClickListener(this);
		mTitleTv.setText(getResources().getString(R.string.mine_feedback_str));
		mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
		mListView = (SwipeListView) findViewById(R.id.article_listview);
		initSwapLayout();
		sendBtn = (Button) findViewById(R.id.fb_send_btn);
		inputEdit = (EditText) findViewById(R.id.fb_send_content);
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = inputEdit.getText().toString();
				inputEdit.getEditableText().clear();
				if (!TextUtils.isEmpty(content)) {
					// 将内容添加到会话列表
					mComversation.addUserReply(content);
					// 刷新新ListView
					mHandler.sendMessage(new Message());
					// 数据同步
					sync();
				}
			}
		});

	}

	// 数据同步
	private void sync() {

		mComversation.sync(new SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> replyList) {
			}

			@Override
			public void onReceiveDevReply(List<Reply> replyList) {
				// listview停止刷新
				mSwipeLayout.setRefreshing(false);
				// 发送消息，刷新ListView
				mHandler.sendMessage(new Message());
				// 如果开发者没有新的回复数据，则返回
				if (replyList == null || replyList.size() < 1) {
					return;
				}
			}
		});
		resetList();
		// 更新adapter，刷新ListView
		adapter.notifyDataSetChanged();
	}

	// adapter
	class ReplyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return mList!=null ? mList.size():0;
		}

		@Override
		public Object getItem(int arg0) {
			return mList!=null ? mList.get(arg0):null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public int getViewTypeCount() {
			// 两种不同的Tiem布局
			return VIEW_TYPE_COUNT;
		}

		@Override
		public int getItemViewType(int position) {
			// 获取单条回复
			Reply reply = mList.get(position);
			if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
				// 开发者回复Item布局
				return VIEW_TYPE_DEV;
			} else {
				// 用户反馈、回复Item布局
				return VIEW_TYPE_USER;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			// 获取单条回复
			Reply reply = mList.get(position);
			if (convertView == null) {
				// 根据Type的类型来加载不同的Item布局
				if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
					// 开发者的回复
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.custom_fb_dev_reply, null);
				} else {
					// 用户的反馈、回复
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.custom_fb_user_reply, null);
				}

				// 创建ViewHolder并获取各种View
				holder = new ViewHolder();
				holder.replyContent = (TextView) convertView
						.findViewById(R.id.fb_reply_content);
				holder.replyProgressBar = (ProgressBar) convertView
						.findViewById(R.id.fb_reply_progressBar);
				holder.replyStateFailed = (ImageView) convertView
						.findViewById(R.id.fb_reply_state_failed);
				holder.replyData = (TextView) convertView
						.findViewById(R.id.fb_reply_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 以下是填充数据
			// 设置Reply的内容
			holder.replyContent.setText(reply.content);
			// 在App应用界面，对于开发者的Reply来讲status没有意义
			if (!Reply.TYPE_DEV_REPLY.equals(reply.type)) {
				// 根据Reply的状态来设置replyStateFailed的状态
				if (Reply.STATUS_NOT_SENT.equals(reply.status)) {
					holder.replyStateFailed.setVisibility(View.VISIBLE);
				} else {
					holder.replyStateFailed.setVisibility(View.GONE);
				}

				// 根据Reply的状态来设置replyProgressBar的状态
				if (Reply.STATUS_SENDING.equals(reply.status)) {
					holder.replyProgressBar.setVisibility(View.VISIBLE);
				} else {
					holder.replyProgressBar.setVisibility(View.GONE);
				}
			}

			// 回复的时间数据，这里仿照QQ两条Reply之间相差100000ms则展示时间
			if ((position + 1) < mList.size()) {
				Reply nextReply = mList
						.get(position + 1);
				if (position!=0 && nextReply.created_at - reply.created_at > 100000) {
					Date replyTime = new Date(reply.created_at);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					holder.replyData.setText(sdf.format(replyTime));
					holder.replyData.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}

		class ViewHolder {
			TextView replyContent;
			ProgressBar replyProgressBar;
			ImageView replyStateFailed;
			TextView replyData;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.img_title_back:
			finish();
			break;
		}
		
	}
	
	private void resetList(){
		List<Reply> list = mComversation.getReplyList();
		int length = list.size();
		mList = new ArrayList<Reply>();
		mList.add(mReply);
		for(int i = 0 ; i < length ;i ++){
			mList.add(list.get(i));
		}
	}
	
	private void initSwapLayout(){
		mSwipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
		mSwipeLayout.setOnRefreshListener(this);
		
		 SettingsManager settings = SettingsManager.getInstance();
		 mListView.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
		 mListView.setSwipeActionLeft(settings.getSwipeActionLeft());
		 mListView.setSwipeActionRight(settings.getSwipeActionRight());
		 mListView.setOffsetLeft(DensityUtils.dip2px(mContext,
	                settings.getSwipeOffsetLeft()));
		 mListView.setOffsetRight(DensityUtils.dip2px(mContext,
	                settings.getSwipeOffsetRight()));
		 mListView.setAnimationTime(settings.getSwipeAnimationTime());
		 mListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
	}

	@Override
	public void onRefresh() {
		sync();
	}
}
