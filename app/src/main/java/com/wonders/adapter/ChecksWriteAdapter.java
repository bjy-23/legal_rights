package com.wonders.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legal_rights.R;
import com.wonders.bean.Node;
import com.wonders.bean.PlanTitle;
import com.wonders.bean.SopListViewBean;

import java.util.List;

/**
 * Created by bjy on 2017/3/10.
 *
 */

public class ChecksWriteAdapter extends RecyclerView.Adapter {
    private List<Node> data;
    private Context context;
    private static final int LAYOUT_1 = 1;
    private static final int LAYOUT_2 = 2;
    private static final int LAYOUT_3 = 3;

    public ChecksWriteAdapter(List<Node> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (data.get(position).getLevel()){
            case 1:
                return LAYOUT_1;
            case 2:
                return LAYOUT_2;
            case 3:
                return LAYOUT_3;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.item_title,parent,false);
                return new ViewHolder1(view);
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.item_xclr_group,parent,false);
                return new ViewHolder2(view);
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.item_xclr_cell,parent,false);
                return new ViewHolder2(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1){
            ViewHolder1 viewHolder = (ViewHolder1) holder;
            PlanTitle planTitle = (PlanTitle)data.get(position).getT();
            viewHolder.tv.setText(planTitle.getName());
            if (1==planTitle.getType()) {
                viewHolder.igv.setVisibility(View.VISIBLE);
            }
        }else if (holder instanceof ViewHolder2){
            ViewHolder2 viewHolder = (ViewHolder2) holder;
            viewHolder.tvContent.setText((String)data.get(position).getT());
        }else if (holder instanceof ViewHolder3){
            ViewHolder3 viewHolder = (ViewHolder3) holder;
            SopListViewBean bean = (SopListViewBean)data.get(position).getT();
            viewHolder.tvContent.setText(bean.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder{
        private TextView tv;
        private ImageView igv;

        public ViewHolder1(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.text_title);
            igv = (ImageView) itemView.findViewById(R.id.img_add);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{
        private TextView tvContent;

        public ViewHolder2(View itemView) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.content);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder{
        private ImageView igvDelete,igvPic,igvPen;
        private TextView tvContent;

        public ViewHolder3(View itemView) {
            super(itemView);

            igvDelete = (ImageView) itemView.findViewById(R.id.del_btn);
            igvPic = (ImageView) itemView.findViewById(R.id.pic_img);
            igvPen = (ImageView) itemView.findViewById(R.id.pen_img);
            tvContent = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
