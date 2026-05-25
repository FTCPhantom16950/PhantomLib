package groups

import actions.Action
/**
 * Interface for making new Groups for your robot
 * @author Hkial
 * @since PhantomLib 2.0alpha
 */
interface Group : Action {
    /**
     * Map that contains all your telemetry
     */
    override val telemetryMap: HashMap<String, Any?>
    override suspend fun run()

    /**
     * List that contains your Actions
     * @see Action
     */
    val actions: List<Action>
}