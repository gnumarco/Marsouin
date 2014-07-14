/**
 *
 * @author  Marc Segond
 */
public class Splasher {
    /**
     * Shows the splash screen, launches the application and then disposes
     * the splash screen.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SplashWindow.splash(Splasher.class.getResource("splash.jpg"));
        SplashWindow.invokeMain("Marsouin", args);
        SplashWindow.disposeSplash();
    }
    
}
