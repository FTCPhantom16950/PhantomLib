package groups

import actions.Action
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RepeatableParallelGroup(vararg actions: Action?) : Group {
    override val telemetryMap: HashMap<String, Any?> = HashMap<String, Any?>().apply {
        for (action in actions) {
            val notNullAction = action?.telemetryMap ?: throw NullPointerException("Action must not be null")
            this.putAll(notNullAction)
        }
    }


    override val actions: ArrayList<Action> = ArrayList<Action>().apply {
        for (action in actions) {
            val notNullAction = action ?: throw NullPointerException("Action must not be null")
            this.add(notNullAction)
        }
    }
    var shouldRepeat: Boolean = false

    override suspend fun run() {
        while (shouldRepeat && currentCoroutineContext().isActive) {
            coroutineScope {
                for (action in actions) {
                    if (!Thread.interrupted()) {
                        this.launch { action.run() }
                    } else {
                        this.cancel()
                    }
                }
            }
        }
    }

    fun setState(state: Boolean): RepeatableParallelGroup {
        this.shouldRepeat = state
        return this
    }

}