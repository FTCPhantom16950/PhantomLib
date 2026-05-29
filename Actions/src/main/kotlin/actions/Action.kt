package actions

/**
 * Interface for making new Actions for your robot
 * @author Hkial
 * @since PhantomLib 2.0alpha
 */
interface Action {
    /**
     * Map that contains all your telemetry
     */
    val telemetryMap: Map<String, Any?>

    /**
     * Method that contains all of your acts
     * @author Hkial
     * @since PhantomLib 2.0alpha
     * @see Action
     */
    suspend fun run(): Void
}