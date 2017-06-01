/**
 * 工程名: SpecialFocus
 * 文件名: OtherAdapter.java
 * 包名: com.sepcialfocus.android.ui.adapter
 * 日期: 2015年9月9日下午6:13:50
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.adapter;

import java.util.List;

import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.NavBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * 类名: OtherAdapter <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015年9月9日 下午6:13:50 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class OtherAdapter extends BaseAdapter {
    private final Context context;
    public List<NavBean> channelList;
    private TextView item_text;
    /** 是否可见 */
    boolean isVisible = true;
    /** 要删除的position */
    public int remove_position = -1;

    public OtherAdapter(Context context, List<NavBean> channelList) {
        this.context = context;
        this.channelList = channelList;
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public NavBean getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        NavBean channel = getItem(position);
        item_text.setText(channel.getMenu());
        if (!isVisible && (position == -1 + channelList.size())) {
            item_text.setText("");
        }
        if (remove_position == position) {
            item_text.setText("");
        }
        return view;
    }

    /** 获取频道列表 */
    public List<NavBean> getChannnelLst() {
        return channelList;
    }

    /** 添加频道列表 */
    public void addItem(NavBean channel) {
        channelList.add(channel);
        notifyDataSetChanged();
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
        // notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        notifyDataSetChanged();
    }

    /** 设置频道列表 */
    public void setListDate(List<NavBean> list) {
        channelList = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}

