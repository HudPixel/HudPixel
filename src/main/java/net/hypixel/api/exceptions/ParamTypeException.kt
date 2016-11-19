package net.hypixel.api.exceptions

import net.hypixel.api.request.RequestParam

class ParamTypeException(requestParam: RequestParam, value: Any?) : HypixelAPIException(requestParam.name + " is not of correct type! " +
        "Expected " +
        "'" + requestParam.valueClass.simpleName + "'" +
        " but got " +
        "'" + (if (value == null) "null" else value.javaClass.simpleName) + "'")
