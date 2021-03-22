// To assist development, double-check and confirm RSSI values by using
// https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer or Xirrus Wi-Fi Inspector.
// package com.example.findmytag.algorithms.neuralnetwork;

import java.lang.Math;

/**
 * This class is used to generate the RSSI signal strength values for the data training. There are
 * several static methods/formulas that can be invoked for the sake of convenience. All of these
 * different formulas were collected from different sources on the Internet. Each formula is
 * applicable in a specific context and they have their own advantages and disadvantages. Other
 * factors related to indoor environments such as attenuation and signal-to-noise ratio thresholds
 * might only be taken into consideration by some/several formulas (i.e., not all methods).
 * 
 * RSSI values closer to 0 would imply a stronger and better signal strength. Common RSSI values are
 * usually negative (unless their signal power is somehow larger than 1 mW).
 * 
 * Please do take note that it is highly desirable for the indoor building environment that we are
 * testing the neural network algorithm in to have the same WiFi router/modem's chipset model
 * everywhere.
 * 
 * For reference, SUTD's Office of IT informed us that they use the Aruba AP-225 (220 Series).
 * However, independent personal OSINT-assisted research indicates that they (referring to the SUTD
 * university school side) are using the Alcatel-Lucent OmniAccess OAW-AP114/115 (110 Series).
 * Perhaps the latter model is already discontinued from commercial enterprise-grade production for
 * some reason? No one knows. Yet, the SUTD hostel's resident rooms utilize either the Aruba 205H AP
 * or the Alcatel-Lucent OmniAccess AP205H model. It seems that Aruba OEMs its products to and
 * conducts extended joint development with Alcatel-Lucent (as one among many)? Is Alcatel-Lucent a
 * vendor partner of Aruba and basically just resells Aruba products and technologies? I am not a
 * business person so I do not know or understand anything about this strange arrangement. It feels
 * alien and unfamiliar to me, and I mean that in a wholesome kind of way. Anyway, all possibly
 * relevant technical specifications are included in the `docs/ap_specs` directory. Don't ask how I
 * found the German one. I don't even know how to read or speak German.
 * 
 * NOTE: This aspect is the weak-point link of this entire neural network algorithm since it has so
 * many assumptions. A good neural network should be able to handle significantly variable
 * fluctuations of RSSI values in a fairly resistant behavior (i.e., should not overfit).
 */
public class RSSICalculator {
    // Speed of light in metres per second
    private static final int SPEED_OF_LIGHT = 299792458;

    // Auto-generated stub constructor
    public RSSICalculator() {

    }

    /**
     * This is the conventional formula method. Calibrated against a known distance.
     * 
     * @param d - distance from WiFi AP in metres
     * @param n - propagation constant or path-loss exponent/index (2 for free space, normal range
     *          is 2.7-4.3 for indoor but can reach 10 if outdoor)
     * @param A - RSSI in dBm at 1 metre away from WiFi AP
     * @param v - Log-normal shadow fading effects (usually follows a normal distribution with
     *          mean/expectation value of 0 and whose standard deviation depends on the
     *          characteristics of the environment)
     * @return received signal strength at distance d metres away from WiFi AP
     */
    public static int conventional(double d, double n, double A, double v) {
        return (int) Math.round(A - (10 * n * Math.log10(d)) - v);
    }

    /**
     * This uses the naive theoretical free space path loss method. Derived from Friis transmission
     * equation.
     * 
     * @param d  - distance from WiFi AP in metres
     * @param f  - frequency of WiFi signal in Hz (usually either 2.4 GHz or 5 GHz)
     * @param gt - transmitter gain in dB (usually 0)
     * @param gr - receiver gain in dB (usually 0)
     * @return received signal strength at distance d metres away from WiFi AP
     */
    public static int modifiedFSPL(double d, double f, double gt, double gr) {
        double fspl = (20 * Math.log10(d)) + (20 * Math.log10(f))
                + (20 * Math.log10((4 * Math.PI) / SPEED_OF_LIGHT)) - gt - gr;
        return (int) Math.round(30 - fspl); // 1 Watt is +30 dBm
    }

    /**
     * This formula was derived from here: http://www.ece.lsu.edu/scalzo/Mega%20Hurtz%20FDR.pptx. It
     * seems like an academic study conducted by some researchers from the Division/Department of
     * Electrical & Computer Engineering (School of Electrical Engineering and Computer Science) of
     * Louisiana State University. Looks slightly similar to the conventional formula.
     * 
     * @param d  - distance from WiFi AP in metres
     * @param n  - propagation constant or path-loss exponent (2 for free space, normal range is
     *           2.7-4.3)
     * @param f  - frequency of WiFi signal in Hz (usually either 2.4 GHz or 5 GHz)
     * @param fm - fade margin in dB (typical values are 10-22 dB)
     * @param po - signal power in dB at zero distance from WiFi AP
     * @return received signal strength at distance d metres away from WiFi AP
     */
    public static int ecelcu(double d, double n, double f, double fm, double po) {
        return (int) Math.round(
                po - fm - (10 * n * Math.log10(f)) + (30 * n) - 32.44 - ((10 * Math.log10(d)) / n));
    }
}
