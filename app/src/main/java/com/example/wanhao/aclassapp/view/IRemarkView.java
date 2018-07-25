package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.base.IBaseTokenView;
import com.example.wanhao.aclassapp.bean.requestbean.Remark;

import java.util.List;

/**
 * Created by wanhao on 2018/3/28.
 */

public interface IRemarkView extends IBaseTokenView<List<Remark>> {
    void sendRemarkSucess();
}
