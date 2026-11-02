package org.etlforge.integration.client.project;

import java.lang.reflect.Constructor;
import org.etlforge.application.config.FatalConfigurationError;

final class ProjectPersistorFactory {
   public static ExtendedProjectPersistor newPersistor(ManagedProject var0) {
      try {
         Class var1 = getPersistorClass();
         return instantiatePersistor(var1, var0);
      } catch (Exception var2) {
         FatalConfigurationError.throwError(9, ProjectPersistorFactory.class, var2, "The client configuration for saving project data is incorrect.");
         return null;
      }
   }

   private static Class<? extends ExtendedProjectPersistor> getPersistorClass() throws Exception {
      Class var0 = Class.forName("org.etlforge.integration.client.project.persistor.v20.ProjectXmlPersistor");
      if (ExtendedProjectPersistor.class.isAssignableFrom(var0)) {
         return var0.asSubclass(ExtendedProjectPersistor.class);
      } else {
         throw new Exception("The assigned project persistor class does not implement the ExtendedProjectPersistor interface");
      }
   }

   private static ExtendedProjectPersistor instantiatePersistor(Class<? extends ExtendedProjectPersistor> var0, ManagedProject var1) throws Exception {
      Constructor var2 = var0.getConstructor(ManagedProject.class);
      return (ExtendedProjectPersistor)var2.newInstance(var1);
   }

   private ProjectPersistorFactory() {
   }
}
