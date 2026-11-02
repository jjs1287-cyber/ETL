package org.etlforge.application.ui.viewer.error.dialog;

import org.etlforge.application.errorlog.ErrorLogEntry;

public abstract class ErrorDisplayer {
   private static final ErrorDisplayer instance = new DefaultErrorDisplayer();
   private static final ErrorDisplayer minimal = new MinimalErrorDisplayer();

   public abstract void show(ErrorLogEntry var1);

   public abstract void showAndWait(ErrorLogEntry var1) throws InterruptedException;

   public static ErrorDisplayer defaultDisplayer() {
      return instance;
   }

   public static ErrorDisplayer minimalDisplayer() {
      return minimal;
   }
}
