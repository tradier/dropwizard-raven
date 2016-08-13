package com.tradier.raven.logging;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple factory for a {@link UncaughtExceptionHandler} which logs the last uncaught exception with
 * a SLF4J {@link Logger} on ERROR level.
 *
 * Copied (and modified to use SLF4J) from Google Guava.
 */
public final class UncaughtExceptionHandlers {

  private UncaughtExceptionHandlers() { /* No instance methods */ }

  /**
   * Returns an exception handler that exits the system. This is particularly useful for the main
   * thread, which may start up other, non-daemon threads, but fail to fully initialize the
   * application successfully.
   *
   * <p>
   * Example usage:
   * </p>
   * 
   * <pre>
   * public static void main(String[] args) {
   *   Thread.currentThread().setUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit());
   *   ...
   * </pre>
   * 
   * @return UncaughtExceptionHandler An exception handler that will log before exiting
   */
  public static UncaughtExceptionHandler systemExit() {
    return new Exiter(Runtime.getRuntime());
  }

  private static final class Exiter implements UncaughtExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exiter.class);

    private final Runtime runtime;

    private Exiter(Runtime runtime) {
      this.runtime = runtime;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
      LOGGER.error(String.format("Caught an exception in %s.  Shutting down.", t), e);
      runtime.exit(1);
    }
  }
}
