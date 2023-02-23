package com.citi.cloudlab.response

data class BaseResponse<T>(
        val resultCode: String? = "000",
        val resultMsg: String? = "success",
        var content: T
){

    constructor(content: T) : this("000","success",content)


}