package com.yc.yyc.bean

import java.io.Serializable

/**
 * Created by yc on 2017/8/17.
 */

class BaseListBean<T> : Serializable {

    var totalCount: Int = 0
    var totalPage: Int = 0
    var page: Int = 0
    var size: Int = 0
    var content: List<T>? = null

}
