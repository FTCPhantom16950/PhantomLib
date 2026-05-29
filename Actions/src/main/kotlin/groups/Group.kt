package groups

import actions.Action
/**
 * Interface for making new Groups for your robot
 * @author Hkial
 * @since PhantomLib 2.0alpha
 */
interface Group : Action {
    /**
     * List that contains your Actions
     * @see Action
     */
    val actions: Array<out Action>
}