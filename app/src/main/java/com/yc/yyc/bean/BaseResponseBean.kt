package com.yc.yyc.bean

import java.io.Serializable

class BaseResponseBean<T> : Serializable {

    var code: Int = 0
    var description: String? = null
    var result: T? = null

}
