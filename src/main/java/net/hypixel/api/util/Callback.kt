package net.hypixel.api.util

import net.hypixel.api.reply.AbstractReply

abstract class Callback<T : AbstractReply>(val clazz: Class<T>) {

    abstract fun callback(failCause: Throwable?, result: T?)
}
