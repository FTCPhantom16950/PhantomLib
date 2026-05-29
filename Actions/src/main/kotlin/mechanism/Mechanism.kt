package mechanism

import com.qualcomm.robotcore.hardware.HardwareDevice

/**
 * Interface that holds all your HardwareDevices and initializing it
 * @property devices you need to fulfill it with you devices to use it in the Actions easily
 * @author Hkial
 * @since PhantomLib 2.0alpha
 */
interface Mechanism {
    /**
     * LinkedHashMap of String and HardwareDevice
     * @see HardwareDevice
     */
    val devices: LinkedHashMap<String, HardwareDevice>
    val telemetryMap: Map<String, Any?>
    /**
     * Method for initializing, it can be automatized by scheduler
     * @author Hkial
     * @since PhantomLib 2.0alpha
     */
    fun init()
}