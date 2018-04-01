//package com.example.wanhao.aclassapp.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.example.wanhao.aclassapp.R;
//import com.example.wanhao.aclassapp.bean.Remark;
//import com.example.wanhao.aclassapp.config.ApiConstant;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by wanhao on 2018/3/28.
// */
//
//public class RemarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{
//    private static final String TAG = "MessageItemAdapter";
//
//
//    private List<Remark> list;
//    private OnItemClickListener mOnItemClickListener = null;
//    private OnLongItemClickListener mOnLongItemClickListener = null;
//    View view;
//    Context context;
//
//    public RemarkAdapter(Context context){
//        list = new ArrayList<>();
//        this.context = context;
//    }
//
//    public void setData(List<Remark> list) {
//        this.list = list;
//        this.notifyDataSetChanged();
//    }
//
//    public interface OnLongItemClickListener {
//        void onLongItemClick(View view, int position);
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        if(viewType== ApiConstant.REMARK_NORMAL){
//            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_remark, viewGroup, false);
//            HolderImage vh = new HolderImage(view);
//            view.setOnClickListener(this);
//            return vh;
//        }
//        if(viewType==ApiConstant.REMARK_REMARK){
//            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_remark_remark, viewGroup, false);
//            HolderNormal vh = new HolderNormal(view);
//            view.setOnClickListener(this);
//            return vh;
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if(holder instanceof HolderImage){
//            Message message =list.get(position);
//
//            ((HolderImage)holder).name.setText(message.getUser());
//            ((HolderImage)holder).time.setText(message.getTime());
//            ((HolderImage)holder).contant.setText(message.getContantUrl());
//            ((HolderImage)holder).title.setText(message.getTitle());
//            Glide.with(context).load(message.getImageUrl()).into(((HolderImage)holder).image);
//        }
//        if(holder instanceof HolderNormal){
//            Message message =list.get(position);
//
//            ((HolderNormal)holder).title.setText(message.getTitle());
//            ((HolderNormal)holder).name.setText(message.getUser());
//            ((HolderNormal)holder).time.setText(message.getTime());
//            ((HolderNormal)holder).contant.setText(message.getContantUrl());
//        }
//        holder.itemView.setTag(position);
//    }
//
//    @Override
//    public int getItemViewType (int position) {
//        return list.get(position).getType();
//    }
//    @Override
//    public void onClick(View v) {
//        if (mOnItemClickListener != null) {
//            mOnItemClickListener.onItemClick(v, (int) v.getTag());
//        }
//    }
//
//    @Override
//    public boolean onLongClick(View v) {
//        if (mOnLongItemClickListener != null) {
//            mOnLongItemClickListener.onLongItemClick(v, (int) v.getTag());
//        }
//        return true;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//
//    public void setOnLongItemClickListener(OnLongItemClickListener listener) {
//        this.mOnLongItemClickListener = listener;
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    //自定义的ViewHolder，持有每个Item的的所有界面元素
//    public static class HolderNormal extends RecyclerView.ViewHolder {
//        public CircleImageView head;
//        public TextView time;
//        public TextView name;
//        public TextView title;
//        public TextView contant;
//
//        public HolderNormal(View view) {
//            super(view);
//            time = (TextView) view.findViewById(R.id.ad_message_time);
//            head = (CircleImageView) view.findViewById(R.id.ad_message_head);
//            name = (TextView) view.findViewById(R.id.ad_message_name);
//            title = (TextView) view.findViewById(R.id.ad_message_title);
//            contant = (TextView) view.findViewById(R.id.ad_message_contant);
//
//        }
//    }
//
//    public static class HolderImage extends RecyclerView.ViewHolder {
//        public CircleImageView head;
//        public TextView name;
//        public TextView time;
//        public TextView title;
//        public TextView contant;
//        public ImageView image;
//
//        public HolderImage(View view) {
//            super(view);
//            time = (TextView) view.findViewById(R.id.ad_message_image_time);
//            head = (CircleImageView) view.findViewById(R.id.ad_message_image_head);
//            name = (TextView) view.findViewById(R.id.ad_message_image_name);
//            title = (TextView) view.findViewById(R.id.ad_message_image_title);
//            contant = (TextView) view.findViewById(R.id.ad_message_image_contant);
//            image = (ImageView) view.findViewById(R.id.ad_message_image_image);
//        }
//    }
//}