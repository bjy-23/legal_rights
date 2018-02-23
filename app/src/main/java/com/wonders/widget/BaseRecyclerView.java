package com.wonders.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bjy on 2017/2/13.
 */

public class BaseRecyclerView extends RecyclerView {
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerView(Context context) {
        super(context);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(new ProxyAdapter(this,adapter));
    }

    private class ProxyAdapter<VH extends ViewHolder> extends Adapter<ViewHolder>{
        private RecyclerView recyclerView;
        private Adapter adapter;

        public ProxyAdapter(RecyclerView recyclerView, Adapter adapter) {
            this.recyclerView = recyclerView;
            this.adapter = adapter;

            adapter.registerAdapterDataObserver(new AdapterDataObserver() {
                @Override
                public void onChanged() {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    notifyItemRangeChanged(positionStart,itemCount);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    notifyItemRangeChanged(positionStart, itemCount, payload);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    notifyItemRangeRemoved(fromPosition, toPosition - fromPosition);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    notifyItemRangeInserted(positionStart, itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    notifyItemRangeRemoved(positionStart, itemCount);
                }
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return adapter.onCreateViewHolder(parent,viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            adapter.onBindViewHolder(holder,position);
            if (onItemClickListener!=null){
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(recyclerView,v,position);
                    }
                });
            }

            if (onItemLongClickListener!=null){
                holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return onItemLongClickListener.onItemLongClick(recyclerView,v,position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return adapter.getItemCount();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView parent, View clickedChild, int position);
    }

    public interface OnItemLongClickListener{
        boolean onItemLongClick(RecyclerView parent, View clickedChild, int position);
    }
}
