package dataFactories

import com.github.davidkleiven.avrodemo.Generator
import com.github.davidkleiven.avrodemo.Plan
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import java.time.Instant

data class GeneratorsWithPlans(
    val generators: List<Generator>,
    val plans: List<Plan>
)

val minDate = Instant.ofEpochSecond(0)!!
val maxDate = Instant.ofEpochSecond(24*3600)!!


val GeneratorArb: Arb<Generator> = Arb.bind(
    Arb.uuid(),
    Arb.float(0.0f, 1e8f),
    Arb.instant(minDate, maxDate),
    Arb.boolean()
) { mrid, power, startTime, isValid -> Generator(mrid.toString(), power, startTime, isValid)}

class PlanGenerator(private val componentMrids: List<String>) {
    val PlanArb = arbitrary {
        val planMrid = Arb.uuid().bind().toString()
        val power = Arb.float().bind()
        val componentMrid = Arb.element(componentMrids).bind()
        val startTime = Arb.instant(minDate, maxDate).bind()
        val stopTime = Arb.instant(startTime, maxDate).bind()
        Plan(planMrid, power, componentMrid, startTime, stopTime)
    }
}
val GeneratorWithPlansArb = arbitrary {
    val generators = Arb.list(GeneratorArb).bind()
    val genMrids = generators.map {g -> g.mrid}
    val plans = Arb.list(PlanGenerator(genMrids).PlanArb).bind()
    GeneratorsWithPlans(generators, plans)
}