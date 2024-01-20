import com.github.avrokotlin.avro4k.Avro
import com.github.davidkleiven.avrodemo.Generator
import com.github.davidkleiven.avrodemo.generatorsFromBytes
import com.github.davidkleiven.avrodemo.generatorsToBytes
import org.apache.avro.Schema
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class TestGenerator {

    @Test
    fun `test schema equal`() {
        val schema = Schema.Parser().parse(this::class.java.getResourceAsStream("generator.avsc"))
        val schemaFromClass = Avro.default.schema(Generator.serializer())
        assertEquals(schema, schemaFromClass)
    }

    @Test
    fun `test serialization deserialization round trip`() {
        val generators = listOf(
            Generator(
                "ae-0a", 2.0f, Instant.ofEpochSecond(1), true
            ),
            Generator("ae-0b", 1.0f, Instant.ofEpochSecond(2), null)
        )

        val deserializedGenerators = generatorsFromBytes(generatorsToBytes(generators)).toList()
        assertEquals(generators, deserializedGenerators)
    }
}