package net.hypixel.api.request

import com.google.common.collect.Maps
import net.hypixel.api.exceptions.ParamTypeException

@SuppressWarnings("unused")
class RequestBuilder private constructor(requestType: RequestType) {
    private var requestType: RequestType? = null
    private var params = Maps.newHashMap<RequestParam, Any>()

    init {
        this.requestType = requestType
    }

    fun addParam(param: RequestParam, value: Any): RequestBuilder {
        if (!validate(param, value)) {
            throw ParamTypeException(param, value)
        }
        this.params.put(param, value)
        return this
    }

    /**
     * Validate value for param, making sure it is of
     * the correct RequestParam type and
     * can be used for given RequestType
     */
    private fun validate(param: RequestParam, value: Any?): Boolean {
        if (value != null && value.javaClass == param.valueClass) {
            if (param.requestType == requestType) {
                return true
            }
        }
        return false
    }

    /**
     * Builds a request from the builder
     */
    fun createRequest(): Request {
        return Request(requestType, params)
    }

    companion object {

        fun newBuilder(requestType: RequestType): RequestBuilder {
            return RequestBuilder(requestType)
        }
    }
}