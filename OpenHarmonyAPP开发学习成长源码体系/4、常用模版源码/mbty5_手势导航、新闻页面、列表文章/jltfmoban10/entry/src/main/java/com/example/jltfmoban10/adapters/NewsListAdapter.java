package com.example.jltfmoban10.adapters;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Text;
import ohos.app.Context;

import com.example.jltfmoban10.ResourceTable;
import com.example.jltfmoban10.been.NewsInfo;

import java.util.List;
import java.util.Optional;

/**
 * News list adapter
 */
public class NewsListAdapter extends BaseItemProvider {
    private List<NewsInfo> newsInfoList;
    private Context context;

    public NewsListAdapter(List<NewsInfo> listBasicInfo, Context context) {
        this.newsInfoList = listBasicInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return newsInfoList == null ? 0 : newsInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return Optional.of(this.newsInfoList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component componentP, ComponentContainer componentContainer) {
        ViewHolder viewHolder = null;
        Component component = componentP;
        if (component == null) {
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_news_layout, null, false);
            viewHolder = new ViewHolder();
            Component componentTitle = component.findComponentById(ResourceTable.Id_item_news_title);
            Component componentImage = component.findComponentById(ResourceTable.Id_item_news_image);
            if (componentTitle instanceof Text) {
                viewHolder.title = (Text) componentTitle;
            }
            if (componentImage instanceof Image) {
                viewHolder.image = (Image) componentImage;
            }
            component.setTag(viewHolder);
        } else {
            if (component.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) component.getTag();
            }
        }
        if (null != viewHolder) {
            viewHolder.title.setText(newsInfoList.get(position).getTitle());
            viewHolder.image.setScaleMode(Image.ScaleMode.STRETCH);
        }
        return component;
    }

    /**
     * ViewHolder which has title and image
     */
    private static class ViewHolder {
        Text title;
        Image image;
    }
}
