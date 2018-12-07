package com.rayhahah.library.interceptor

import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit



/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Zhou│Alt │         Space         │ Alt│ Li │Feng│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 *
 * Author: Zachary46
 * Time: 2018/12/7
 *
 */
class HttpLogInterceptor : Interceptor {
    constructor(){
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    companion object {
        val TAG = "http"
        val UTF8 = Charset.forName("UTF-8")
    }


    override fun intercept(chain: Interceptor.Chain): Response? {
        var request = chain.request()
        request?.let {
            logForRequest(request)
            var startNs = System.nanoTime()
            var response = chain.proceed(request)
            response?.let {
                var tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()- startNs)
                return logForRespone(response, tookMs)
            } ?: return response
        }

    }

    /**
     * 打印请求日志
     **/
    private fun logForRequest(request: Request) {
        var requestBody = request.body()
        Log.i(TAG, " =====================================请求开始=====================================")
        Log.i(TAG, "\t" + "======请求方法======" + request.method())
        Log.i(TAG, "\t" + "======请求地址======" + request.url())
        var headers = request.headers()
        /*for (i in 0..headers.size()-1) {
            Log.i(TAG,"\t"+"=====请求头部======" + headers.name(i) + ": " + headers.value(i))
        }*/
        requestBody?.let {
            if (isPlaintext(requestBody.contentType())) {
                bodyToString(request)
            } else {
                Log.i(TAG, "\tbody: maybe [file part] , too large too print , ignored!")
            }
        }
        Log.i(TAG, " =====================================请求结束=====================================")

    }

    private fun bodyToString(request: Request){
        val copy = request.newBuilder().build()
        val buffer = Buffer()
        copy.body()?.writeTo(buffer)
        var contentType = copy.body()?.contentType()
        contentType?.let {
            Log.i(TAG, "\t" + "======请求参数======" + buffer.readString(contentType.charset(UTF8)))
        }
    }

    /**
     * 打印响应日志
     **/
    private fun logForRespone(response: Response, tookMs: Long): Response {
        var builder = response.newBuilder()
        var clone = builder.build()
        var responseBody = clone.body()
        Log.i(TAG, " =====================================响应开始=====================================")
        Log.i(TAG, "\t" + "======状态码=======" + clone.code())
        Log.i(TAG, "\t" + "======状态=========" + clone.message())
        Log.i(TAG, "\t" + "======响应时间======" + "(" + tookMs + "ms）")
        var headers = clone.headers()
        /*for (i in 0..headers.size()-1) {
            Log.i(TAG,"\t"+"=====响应头部======" + headers.name(i) + ": " + headers.value(i))
        }*/
        if (HttpHeaders.hasBody(clone)) {
            if (isPlaintext(responseBody?.contentType())){
                var body = responseBody?.string()
                Log.i(TAG, "\t" + "=================================响应数据======================================" + "\t")
                Logger.t(TAG).json(body)
                responseBody = ResponseBody.create(responseBody?.contentType(), body)
                return response.newBuilder().body(responseBody).build()
            } else {
                Log.i(TAG, "\tbody: maybe [file part] , too large too print , ignored!")
            }
        }
        Log.i(TAG, " =====================================响应结束=====================================")
        return response
    }

    private fun isPlaintext(mediaType: MediaType?): Boolean {
        mediaType?.let {
            mediaType.type()?.let {
                if (mediaType.type().equals("text")) return true
            }
            var subtype = mediaType.subtype()
            subtype?.let {
                subtype = subtype.toLowerCase()
                if (subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html")){
                    return true
                }
            }
        }
        return false

    }

}