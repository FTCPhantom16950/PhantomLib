package groups

import actions.Action
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RepeatableLinearGroup(override vararg val actions: Action) : Group {
    override val telemetryMap: Map<String, Any?>
        get() = mutableMapOf<String, Any?>().apply {
            for (action in actions) {
                putAll(action.telemetryMap)
            }
        }


    var shouldRepeat: Boolean = false

    override suspend fun run() {
        while (shouldRepeat){
            for (action in actions) {
                if (!Thread.interrupted() && currentCoroutineContext().isActive) {
                    action.run()
                }
            }
        }
    }

    fun setState(state: Boolean): RepeatableLinearGroup {
        this.shouldRepeat = state
        return this
    }

}