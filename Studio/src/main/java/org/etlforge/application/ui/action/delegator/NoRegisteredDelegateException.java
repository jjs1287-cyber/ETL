package org.etlforge.application.ui.action.delegator;

import org.etlforge.application.config.ApplicationConfiguration;

public class NoRegisteredDelegateException extends Exception {
   public NoRegisteredDelegateException(String var1) {
      super(
         "A configuration error has occurred: no action handler has been registered for the action with ID = ["
            + var1
            + "]. Please contact "
            + ApplicationConfiguration.getCompanyName()
            + " Support."
      );
   }
}
