import actions.Action
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mechanism.Mechanism
import org.jetbrains.annotations.Async
import util.Scheduler

class PhantomOpMode: OpMode() {
    private var scheduler: Scheduler? = null
    val mechanisms: ArrayList<Mechanism> = ArrayList()

    lateinit var action: Action


    override fun init() {
        scheduler = Scheduler.Builder()
            .addMechanisms(mechanisms)
            .addActions(action)
            .build()
        scheduler?.initMechanisms()
    }

    override fun init_loop() {
        super.init_loop()
    }

    override fun start() {
        super.start()
        runBlocking {
            launch {
                while (coroutineContext.isActive) {
                    action.telemetryMap.forEach {
                        telemetry.addData(it.key, it.value)
                    }
                    telemetry.update()
                }
            }
            if (coroutineContext.isActive) scheduler?.run()
        }
    }

    override fun loop() {

    }

    override fun stop() {
        super.stop()
        runBlocking {
            currentCoroutineContext().cancelChildren()
            currentCoroutineContext().cancel()
        }
    }
}