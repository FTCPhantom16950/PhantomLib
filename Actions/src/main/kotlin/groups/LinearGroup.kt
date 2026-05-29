package groups

import actions.Action
import com.sun.tools.javac.comp.Todo
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Linear group class. With adding to it Actions it will be going sequentially.
 * @see Action
 * @see Group
 * @author Hkial<
 * @since PhantomLib 2.0alpha
 * @param various number of actions
 */
class LinearGroup(override vararg val actions: Action) : Group {
    override val telemetryMap: Map<String, Any?>
        get() = mutableMapOf<String, Any?>().apply {
            for (action in actions) {
                putAll(action.telemetryMap)
            }
        }

    override suspend fun run() {
        for (action in actions) {
            if (!Thread.interrupted() && currentCoroutineContext().isActive) {
                action.run()
            }
        }

    }
}