package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.util.FileSizeUtil;
import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter;

import java.util.List;

/**
 * Created by wanhao on 2018/3/22.
 */

public class DocumentSectionAdapter extends SimpleSectionedAdapter<DocumentAdapter.Holder> {
    private static final String TAG = "DocumentSectionAdapter";

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private View mView;

    private List<List<Document>> lists;

    private SECTION now;


    public enum SECTION {
        COURSE, AUTHOR, TYPE, DATE,SIZE
    }

    public DocumentSectionAdapter(Context context, List<List<Document>> lists){
        this.context = context;
        this.lists = lists;
    }

    public void setData(List<List<Document>> list){
        this.lists = list;
        notifyDataSetChanged();
    }

    @Override
    protected String getSectionHeaderTitle(int section) {
//        switch (now){
//            case DATE:
//                break;
//            case SIZE:
//                break;
//            case COURSE:
//                break;
//            case AUTHOR:
//                break;
//            case TYPE:
//                break;
//        }
        return "什么都没有呢......";
//        if(lists==null||lists.get(0)==null||lists.get(0).get(0)==null)
//            return "什么都没有呢......";
//
//        return lists.get(section).get(0).getTitle();
    }

    @Override
    protected int getSectionCount() {
        return lists==null? 0:lists.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        if(lists==null||lists.get(section)==null)
            return 0;
        return lists.get(section).size();
    }

    @Override
    protected DocumentAdapter.Holder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_document, parent, false);
        mView =view;
        return new DocumentAdapter.Holder(view);
    }

    @Override
    protected void onBindItemViewHolder(DocumentAdapter.Holder holder, final int section, int position) {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(section,(int)view.getTag());
                }
            }
        });

        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onItemLongClickListener!=null){
                    onItemLongClickListener.onLongItemClick(section,(int)view.getTag());
                }
                return true;
            }
        });

        Document course = lists.get(section).get(position);

        holder.name.setText(course.getTitle());
        holder.size.setText(FileSizeUtil.FormetFileSize(Integer.valueOf(course.getSize()))+" 来自 "+course.getUser());
        holder.time.setText(course.getDate());
        String last = course.getTitle().substring(course.getTitle().length()-3);

        if(last.equals("pdf")){
            Glide.with(context).load(R.drawable.icon_pdf).into(holder.bck);
        }
        if(last.equals("txt")){
            Glide.with(context).load(R.drawable.icon_txt).into(holder.bck);
        }

        holder.itemView.setTag(position);
    }



    public interface OnItemClickListener {
        void onItemClick(int section, int position);
    }

    public void setOnItemClickListener(OnItemClickListener on){
        this.onItemClickListener = on;
    }

    public interface OnItemLongClickListener {
        void onLongItemClick(int section, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener on){
        this.onItemLongClickListener = on;
    }

}
