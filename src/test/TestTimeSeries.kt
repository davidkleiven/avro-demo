import com.github.davidkleiven.avrodemo.Timeseries
import com.github.davidkleiven.avrodemo.TimeseriesItem
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class TestTimeSeries {
    @Test
    fun `test sort`() {
        val v0 = TimeseriesItem(20.0, Instant.ofEpochSecond(1))
        val v1 = TimeseriesItem(30.0, Instant.ofEpochSecond(0))
        val ts = Timeseries(
            "ae-ae", listOf(v0, v1)
        )

        val sorted = ts.sort()
        val expect = Timeseries(ts.id, listOf(v1, v0))
        assertEquals(expect, sorted)
    }
}