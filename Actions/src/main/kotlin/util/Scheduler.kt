package util

import actions.Action
import com.qualcomm.robotcore.hardware.HardwareDevice
import mechanism.Mechanism

/**
 * Scheduler class. Obtains private constructor.
 * @see Scheduler.Builder
 * @see Mechanism
 * @see Action
 * @throws NullPointerException
 * @param mechanisms LinkedHashSet of Mechanism that needs to be initialized
 * @param actions Array list of Actions that needs to be run
 * @property devices is a devices that has been initialized
 * @author Hkial
 * @since PhantomLib 2.0alpha
 */
class Scheduler private constructor(
    val mechanisms: LinkedHashSet<Mechanism>, val actions: ArrayList<Action>
) {
    var mechanismsInitializing: Boolean = false
    var actionsStarted: Boolean = false
    val telemetryMap: Map<String, Any?>
        get() = mutableMapOf<String, Any?>().apply {
            if (mechanismsInitializing){
                for (mechanism in mechanisms) {
                    putAll(mechanism.telemetryMap)
                }
            } else if (actionsStarted){
                for (action in actions) {
                    putAll(action.telemetryMap)
                }
            }
        }
    val devices: LinkedHashMap<String, HardwareDevice> = LinkedHashMap()

    /**
     * Method for initializing Mechanisms
     * @throws NullPointerException
     * @see Mechanism
     * @author Hkial
     * @since PhantomLib 2.0alpha
     */
    fun initMechanisms() {
        mechanismsInitializing = true
        require(mechanisms.isNotEmpty()) { "At least one MECHANISM is required" }
        for (mechanism in mechanisms) {
            mechanism.init()
            devices.putAll(mechanism.devices)
        }
    }

    /**
     * Method for running actions
     * @throws NullPointerException
     * @author Hkial
     * @since PhantomLin 2.0alpha
     * @see Action
     */
    suspend fun run() {
        mechanismsInitializing = false
        actionsStarted = true
        require(actions.isNotEmpty()) { "There must be at least one ACTION" }
        if (actions.size == 1) {
            actions[0].run()
        } else {
            for (action in actions) {
                action.run()
            }
        }
    }

    /**
     * Builder of a Scheduler. It has methods to add mechanisms and actions
     * @see Action
     * @see Mechanism
     * @see Scheduler
     * @since PhantomLib 2.0alpha
     * @author Hkial
     * @throws NullPointerException
     */
    class Builder {
        private val actions = ArrayList<Action>()
        private val mechanisms = LinkedHashSet<Mechanism>()

        /**
         * Method that builds an action
         * @see Scheduler
         * @author Hkial
         * @since PhantomLib 2.0alpha
         * @return new Scheduler
         */
        fun build(): Scheduler {
            return Scheduler(mechanisms, actions)
        }

        /**
         * Method that adds single mechanism. If it sees null, it will throw NullPointerException
         * @see Mechanism
         * @author Hkial
         * @since PhantomLib 2.0alpha
         * @throws NullPointerException
         * @return this builder again
         */
        fun addMechanism(mechanism: Mechanism?): Builder {
            val mechanismThatNotNull: Mechanism =
                mechanism ?: throw NullPointerException("Mechanism was null $mechanism")
            mechanisms.add(mechanismThatNotNull)
            return this
        }

        /**
         * Method that adds list of mechanisms. If it sees null, it will throw NullPointerException
         * @see Mechanism
         * @author Hkial
         * @since PhantomLib 2.0alpha
         * @throws NullPointerException
         * @return this builder again
         */
        fun addMechanisms(mechanisms: List<Mechanism?>): Builder {
            for (mech in mechanisms) {
                val mechanismThatNotNull: Mechanism = mech ?: throw NullPointerException("Mechanism was null $mech")
                this.mechanisms.add(mechanismThatNotNull)
            }
            return this
        }

        /**
         * Method that adds variable amount of mechanisms. If it sees null, it will throw NullPointerException
         * @see Mechanism
         * @author Hkial
         * @since PhantomLib 2.0alpha
         * @throws NullPointerException
         * @return this builder again
         */
        fun addMechanisms(vararg mechanism: Mechanism?): Builder {
            for (mech in mechanism) {
                val mechanismThatNotNull: Mechanism = mech ?: throw NullPointerException("Mechanism was null $mech")
                mechanisms.add(mechanismThatNotNull)
            }
            return this
        }

        /**
         * Method that adds single actions. If it sees null, it will throw NullPointerException
         * @see Action
         * @author Hkial
         * @since PhantomLib 2.0alpha
         * @throws NullPointerException
         * @return this builder again
         */
        fun addAction(action: Action?): Builder {
            val notNullAction = action ?: throw NullPointerException("Action was null $action")
            actions.add(notNullAction)
            return this
        }

        /**
         * Method that adds list of mechanisms. If it sees null, it will throw NullPointerException
         * @see Mechanism
         * @author Hkial
         * @since PhantomLib 2.0alpha
         * @throws NullPointerException
         * @return this builder again
         */
        fun addActions(vararg actions: Action?): Builder {
            for (act in actions) {
                val notNullAction = act ?: throw NullPointerException("Action was null $act")
                this.actions.add(notNullAction)
            }
            return this
        }

        /**
         * Method that adds variable amount of mechanisms. If it sees null, it will throw NullPointerException
         * @see Mechanism
         * @author Hkial
         * @since PhantomLib 2.0alpha
         * @throws NullPointerException
         * @return this builder again
         */
        fun addActions(actions: List<Action?>): Builder {
            for (act in actions) {
                val notNullAction = act ?: throw NullPointerException("Action was null $act")
                this.actions.add(notNullAction)
            }
            return this
        }
    }
}