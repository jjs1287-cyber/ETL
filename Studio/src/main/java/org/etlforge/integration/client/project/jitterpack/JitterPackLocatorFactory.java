package org.etlforge.integration.client.project.jitterpack;

import org.etlforge.application.Application;

public final class JitterPackLocatorFactory {
   public static JitterPackLocator newLocator() {
      return Application.getBean("jitterpack.locator", JitterPackLocator.class);
   }
}
