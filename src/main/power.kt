package com.github.davidkleiven.avrodemo

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.AvroDefault
import com.github.avrokotlin.avro4k.AvroName
import com.github.avrokotlin.avro4k.AvroNamespace
import com.github.avrokotlin.avro4k.io.AvroDecodeFormat
import com.github.avrokotlin.avro4k.io.AvroEncodeFormat
import com.github.avrokotlin.avro4k.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import org.apache.avro.Schema
import java.io.ByteArrayOutputStream
import java.time.Instant

@Serializable
@AvroName("generator")
@AvroNamespace("com.external.schema")
data class Generator(
    val mrid: String,
    val power: Float,
    @Serializable(with= InstantSerializer::class)
    val startTime: Instant,
    @AvroDefault(Avro.NULL)
    private val isValid: Boolean? = null,
) {
    fun valid(): Boolean {
        return isValid ?: true
    }
}

fun generatorsToBytes(generators: List<Generator>, serializationSchema: Schema): ByteArray {
    val stream = ByteArrayOutputStream()
    val os = Avro.default.openOutputStream(Generator.serializer()) {
        encodeFormat = AvroEncodeFormat.Binary
        schema = serializationSchema
    }.to(stream)

    os.write(generators)
    os.flush()
    return stream.toByteArray()
}

fun generatorsToBytes(generators: List<Generator>): ByteArray {
    val generatorSchema = Avro.default.schema(Generator.serializer())
    return generatorsToBytes(generators, generatorSchema)
}

fun generatorsFromBytes(data: ByteArray): Sequence<Generator> {
    // Schema which is generated from out dataclass
    val schema = Avro.default.schema(Generator.serializer())
    return generatorsFromBytes(data, schema)
}

fun generatorsFromBytes(data: ByteArray, schema: Schema): Sequence<Generator> {
    return generatorsFromBytes(data, schema, schema)
}

fun generatorsFromBytes(data: ByteArray, writerSchema: Schema, readerSchema: Schema): Sequence<Generator> {
    return Avro.default.openInputStream(Generator.serializer()) {
        decodeFormat = AvroDecodeFormat.Binary(writerSchema, readerSchema)
    }.from(data).iterator().asSequence()
}