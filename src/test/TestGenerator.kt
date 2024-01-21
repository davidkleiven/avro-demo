import com.github.avrokotlin.avro4k.Avro
import com.github.davidkleiven.avrodemo.Generator
import com.github.davidkleiven.avrodemo.generatorsFromBytes
import com.github.davidkleiven.avrodemo.generatorsToBytes
import org.apache.avro.Schema
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestGenerator {
    private val schemaOpt = Schema.Parser().parse(this::class.java.getResourceAsStream("generator.avsc"))
    private val schemaNoOpt =
        Schema.Parser().parse(this::class.java.getResourceAsStream("generatorWithoutOptional.avsc"))

    @Test
    fun `test schema equal`() {
        val schemaFromClass = Avro.default.schema(Generator.serializer())
        assertEquals(schemaOpt, schemaFromClass)
    }

    @Test
    fun `test serialization deserialization round trip`() {
        val generators = listOf(
            Generator("ae-0a", 2.0f, Instant.ofEpochSecond(1), true),
            Generator("ae-0b", 1.0f, Instant.ofEpochSecond(2), null)
        )

        val deserializedGenerators = generatorsFromBytes(generatorsToBytes(generators)).toList()
        assertEquals(generators, deserializedGenerators)
    }

    @TestFactory
    fun `test serialize deserialize without optional field`() = listOf(
        Pair(schemaNoOpt, schemaOpt),
        Pair(schemaOpt, schemaNoOpt),
        Pair(schemaNoOpt, schemaNoOpt),
        Pair(schemaOpt, schemaOpt)
    ).map { args -> DynamicTest.dynamicTest("${args.first} - ${args.second}") {
            val generators = listOf(Generator("ae-0a", 2.0f, Instant.ofEpochSecond(1), true))
            val serialized = generatorsToBytes(generators, args.first)

            // Deserialize with the schema that has optional
            val deserialized = generatorsFromBytes(serialized, args.first, args.second)
            assertEquals(generators, deserialized.toList())
        }
    }

    @Test
    fun `test fails if isValid is null`() {
        val generators = listOf(Generator("ae-0a", 2.0f, Instant.ofEpochSecond(1), null))
        assertFailsWith<java.lang.NullPointerException> { generatorsToBytes(generators, schemaNoOpt) }
    }
}