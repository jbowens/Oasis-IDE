package camel;

/**
 * This thread is run when the application is closing. It cleans up and prepares
 * for the shutdown.
 */
public class ShutdownCleaner extends Thread {
  
  /* The application to clean up */
  protected Application app;

  /**
   * Creates a new shutdown cleaner, given the application it should clean up.
   *
   * @param app the application to clean up
   */
  public ShutdownCleaner(Application app) {
    this.app = app;
  }

  /**
   * Clean up
   */
  public void run() {
    try {
      app.getConfig().save();
    } catch(SettingsSaveException ex) {
      // Oh well
    }
    //app.gracefulClose();
  }

}