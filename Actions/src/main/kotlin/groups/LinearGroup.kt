package groups

import actions.Action

/**
 * Linear group class. With adding to it Actions it will be going sequentially.
 * @see Action
 * @see Group
 * @author Hkial<
 * @since PhantomLib 2.0alpha
 * @param various number of actions
 */
class LinearGroup(vararg actions: Action?) : Group {


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

    override suspend fun run() {
        for (action in this.actions) {
            if (!Thread.interrupted()) {
                action.run()
            }
        }
    }
}