package org.etlforge.application.ui.viewer.error.log;

import org.etlforge.application.ui.window.WindowSection;

public abstract class ErrorLogViewFactory {
   public abstract ErrorLogView create(WindowSection var1);

   public static ErrorLogViewFactory newInstance() {
      return new ErrorLogViewFactory() {
         @Override
         public ErrorLogView create(WindowSection var1) {
            return new ErrorLogViewImpl(var1);
         }
      };
   }
}
