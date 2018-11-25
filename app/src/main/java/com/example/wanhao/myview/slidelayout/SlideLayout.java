package com.example.wanhao.myview.slidelayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

public class SlideLayout extends FrameLayout {

    private static final String TAG = "SlideLayout";

    View itemView;
    List<View> viewList;
    List<Integer> viewWidthList;

    int menuWidth;
    int height;
    int mScaledTouchSlop;

    boolean isOpen = false;
    Scroller scroller;

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        viewList = new ArrayList<>();
        viewWidthList = new ArrayList<>();
        scroller = new Scroller(context);

        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        //Log.i(TAG, "SlideLayout: mScaledTouchSlop = "+mScaledTouchSlop);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setClickable(true);

        height = itemView.getMeasuredHeight();
        menuWidth = 0;

        for(int x=0;x<viewList.size();x++){
            viewWidthList.add(getChildAt(x).getMeasuredWidth());
            menuWidth += getChildAt(x + 1).getMeasuredWidth();
        }

        //Log.i(TAG, "onMeasure: height = "+ height +"  menuWidth = " + menuWidth );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int startX = 0;
        int startY = 0;

        itemView.layout(0,0,itemView.getWidth(),itemView.getHeight());

        startX+=itemView.getWidth();

        for(View view:viewList){

            view.layout(startX,0,startX+view.getWidth(),view.getHeight());
            startX+=view.getWidth();
        }

    }

    int startX,startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.i(TAG, "onMeasure  menuWidth = " + menuWidth );

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //Log.i(TAG, "ACTION_DOWN: ");
                startX = (int) event.getX();
                startY =  (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //Log.i(TAG, "ACTION_MOVE: ");

                float nowX = event.getX();
                float nowY = event.getY();

                //一次滑动的位移
                float disX = nowX - startX;
                int toScrollX = (int) (getScrollX() - disX);

                if(toScrollX < 0){
                    toScrollX = 0;
                } else if(toScrollX > menuWidth){
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX,getScrollY());

                startX = (int) event.getX();
                startY =  (int) event.getY();

                break;
            }
            case MotionEvent.ACTION_UP:
                //Log.i(TAG, "ACTION_UP: ");

            case MotionEvent.ACTION_CANCEL: {
                //Log.i(TAG, "ACTION_CANCEL: ");
                if( getScrollX() < menuWidth / 2){
                    closeMenuSmooth();
                }else {
                    openMenuSmooth();
                }
            }

        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(ev);
        //Log.i(TAG, "onInterceptTouchEvent: interceped = "+ intercepted);
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //Log.i(TAG, "onInterceptTouchEvent ACTION_DOWN: ");
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                //Log.i(TAG, "onInterceptTouchEvent ACTION_MOVE: ");

                if( Math.abs(startX - x) > mScaledTouchSlop){
                    //Log.i(TAG, "onInterceptTouchEvent: retuen trrrrrrrrrrrrrrrrrrrrue");
                    return true;
                }

                break;

            }
            case MotionEvent.ACTION_UP:
                //Log.i(TAG, "onInterceptTouchEvent ACTION_UP: ");

            case MotionEvent.ACTION_CANCEL: {
                //Log.i(TAG, "onInterceptTouchEvent ACTION_CANCEL: ");

            }

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //Log.i(TAG, "dispatchTouchEvent ACTION_DOWN: ");
                //getParent().requestDisallowInterceptTouchEvent(false);
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //Log.i(TAG, "dispatchTouchEvent ACTION_MOVE: ");
                int deltaX = x - startX;
                int deltaY = y - startY;
                if(Math.abs(deltaX) > Math.abs(deltaY) ){
                    //Log.i(TAG, "dispatchTouchEvent: requestDisallowInterceptTouchEvent false");
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;

            }
            case MotionEvent.ACTION_UP:
                //Log.i(TAG, "dispatchTouchEvent ACTION_UP: ");

            case MotionEvent.ACTION_CANCEL: {
                //Log.i(TAG, "dispatchTouchEvent ACTION_CANCEL: ");

            }

        }

        return super.dispatchTouchEvent(ev);
    }

    public void openMenuSmooth(){
        scroller.startScroll(getScrollX(),getScrollY(),menuWidth - getScrollX(),getScrollY());
        invalidate();
        if(listener != null){
            listener.onStateChangeListener(true);
        }
        isOpen = true;
    }

    public void closeMenuSmooth(){
        scroller.startScroll(getScrollX(),getScrollY(),- getScrollX(),getScrollY());
        invalidate();
        if(listener != null){
            listener.onStateChangeListener(false);
        }
        isOpen =false;
    }

    public void setMenuState(boolean isOpen){
        if(isOpen){
            scrollTo(menuWidth - getScrollX(),getScrollY());
        }else {
            scrollTo(0,0);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if( scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        int childCount = getChildCount();
        Log.i(TAG, "onFinishInflate: clildCount = "+childCount);
        itemView = getChildAt(0);

        for(int x = 1;x<childCount;x++){
            viewList.add(getChildAt(x));
        }

        super.onFinishInflate();
    }

    public void setOnStateChangeListener(OnStateChangeListener listener){
        this.listener = listener;
    }
    
    private OnStateChangeListener listener;

    public interface OnStateChangeListener{
        void onStateChangeListener(boolean isOpen);
    }

}
