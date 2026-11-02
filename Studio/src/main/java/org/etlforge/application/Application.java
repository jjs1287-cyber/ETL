package org.etlforge.application;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.etlforge.application.appdir.ApplicationDirectory;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.application.cache.DefaultDataCacheService;
import org.etlforge.application.config.ApplicationConfigurationException;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.application.worker.ApplicationWorkerFactory;
import org.etlforge.system.annotations.InternalMethod;
import org.springframework.context.ApplicationContext;

public abstract class Application {
   private static volatile ApplicationDirectory appDir;
   private static volatile ApplicationWorker worker;
   private static volatile DataCacheService cacheService = new DefaultDataCacheService();
   private static volatile ApplicationContext appContext;
   private static final Date startTime = new Date();
   private static volatile boolean exiting;

   @InternalMethod
   public static void setAppContext(ApplicationContext var0) {
      appContext = var0;
      String var1 = "platform.DataCacheService";
      if (var0.containsBean(var1)) {
         cacheService = (DataCacheService)var0.getBean(var1);
      }
   }

   public static <T> T getBean(String var0, Class<T> var1) {
      return (T)var1.cast(appContext.getBean(var0));
   }

   public static boolean containsBean(String var0) {
      return appContext != null && appContext.containsBean(var0);
   }

   public static <T> List<T> getBeansOfType(Class<T> var0) {
      ArrayList var1 = Lists.newArrayList();
      if (appContext != null) {
         Map var2 = appContext.getBeansOfType(var0);

         for (Object var4 : var2.values()) {
            var1.add(var0.cast(var4));
         }
      }

      return var1;
   }

   @InternalMethod
   public static void setApplicationDirectory(ApplicationDirectory var0) {
      Preconditions.checkNotNull(var0, "appDir");
      Preconditions.checkState(appDir == null, "The application director has already been set");
      appDir = var0;
   }

   public static ApplicationDirectory getApplicationDirectory() {
      return appDir;
   }

   public static DataCacheService getCacheService() {
      return cacheService;
   }

   public static synchronized ApplicationWorker getWorker() {
      if (worker == null) {
         worker = createWorker();
      }

      return worker;
   }

   private static ApplicationWorker createWorker() {
      try {
         return ApplicationWorkerFactory.createWorker();
      } catch (ApplicationConfigurationException var2) {
         String var1 = "Failed to create the UI worker. This is most probably a configuration error. The application will exit.";
         FatalConfigurationError.throwError(9, Application.class, var2, var1);
         return null;
      }
   }

   public static boolean exit() {
      exiting = true;
      ExitHandler.handleExitRequest();
      exiting = false;
      return false;
   }

   public static boolean isExiting() {
      return exiting;
   }

   public static void addExitListener(ExitListener var0) {
      ExitHandler.addListener(var0);
   }

   public static void removeExitListener(ExitListener var0) {
      ExitHandler.removeListener(var0);
   }

   public static Date getStartupTime() {
      return new Date(startTime.getTime());
   }
}
