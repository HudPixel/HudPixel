package net.hypixel.api.adapters

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import java.io.IOException

/**
 * Credits: https://gist.github.com/dustin-graham/6469150#file-customizedtypeadapterfactory-java
 */
abstract class CustomizedTypeAdapterFactory<C>(private val customizedClass: Class<C>) : TypeAdapterFactory {

    @SuppressWarnings("unchecked") // we use a runtime check to guarantee that 'C' and 'T' are equal
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        return if (type.rawType == customizedClass) customizeMyClassAdapter(gson, type as TypeToken<C>) as TypeAdapter<T> else null
    }

    private fun customizeMyClassAdapter(gson: Gson, type: TypeToken<C>): TypeAdapter<C> {
        val delegate = gson.getDelegateAdapter(this, type)
        val elementAdapter = gson.getAdapter(JsonElement::class.java)
        return object : TypeAdapter<C>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: C) {
                val tree = delegate.toJsonTree(value)
                beforeWrite(value, tree)
                elementAdapter.write(out, tree)
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): C {
                val tree = elementAdapter.read(`in`)
                afterRead(tree)
                return delegate.fromJsonTree(tree)
            }
        }
    }

    /**
     * Override this to muck with `toSerialize` before it is written to
     * the outgoing JSON stream.
     */
    protected fun beforeWrite(source: C, toSerialize: JsonElement) {
    }

    /**
     * Override this to muck with `deserialized` before it parsed into
     * the application type.
     */
    protected open fun afterRead(deserialized: JsonElement) {
    }
}