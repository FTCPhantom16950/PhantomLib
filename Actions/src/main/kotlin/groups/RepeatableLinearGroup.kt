package groups

import actions.Action
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

class RepeatableLinearGroup(vararg actions: Action?) : Group {
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
            for (action in this.actions) {
                if (!Thread.interrupted()) {
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