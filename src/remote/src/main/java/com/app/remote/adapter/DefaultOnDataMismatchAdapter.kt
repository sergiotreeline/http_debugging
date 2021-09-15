package com.app.remote.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.io.IOException
import java.lang.reflect.Type

// Este adapter se usa cuando el server devuelve un tipo no esperado
//por ejemplo en lugar de un array o en vez de mandar null envia un diccionario vacio

class DefaultOnDataMismatchAdapter<T> private constructor(
    private val delegate: JsonAdapter<T>,
    private val defaultValue: T?
) : JsonAdapter<T>() {

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): T? {
        return try {
            delegate.fromJsonValue(reader.readJsonValue())
        } catch (e: Exception) {
            defaultValue
        }
    }

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: T?) {
        delegate.toJson(writer, value)
    }

    companion object {
        @JvmStatic
        fun <T> newFactory(type: Class<T>, defaultValue: T?): Factory {
            return object : Factory {
                override fun create(requestedType: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
                    if (type != requestedType) {
                        return null
                    }
                    val delegate = moshi.nextAdapter<T>(this, type, annotations)
                    return DefaultOnDataMismatchAdapter(delegate, defaultValue)
                }
            }
        }
    }
}