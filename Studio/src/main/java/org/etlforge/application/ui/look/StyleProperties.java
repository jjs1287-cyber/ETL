package org.etlforge.application.ui.look;

import java.io.IOException;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.util.properties.ReadOnlyProperties;
import org.etlforge.util.properties.ReadOnlyPropertiesFactory;

public final class StyleProperties {
   private static StyleProperties INSTANCE;
   private final ReadOnlyProperties delegate;

   private StyleProperties() throws IOException {
      ReadOnlyPropertiesFactory var1 = ReadOnlyPropertiesFactory.newInstance();
      this.delegate = var1.load("org/etlforge/application/ui/look/style.properties");
   }

   public static ReadOnlyProperties getInstance() {
      return INSTANCE.delegate;
   }

   static {
      try {
         INSTANCE = new StyleProperties();
      } catch (IOException var1) {
         ApplicationLog.getMainLogger().warning("Failed to load the application UI properties: " + var1.getMessage());
      }
   }
}
