package org.etlforge.integration.client.runtime;

import java.awt.EventQueue;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.etlforge.application.Application;
import org.etlforge.application.NonInteractiveMode;
import org.etlforge.application.appdir.AppDirLocationProvider;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.appdir.LogDirectory;
import org.etlforge.application.commandline.CommandLineParser;
import org.etlforge.application.commandline.CommandLineParsingException;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.application.errorlog.ErrorDetailsGenerator;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.runtime.appdir.ClientApplicationDirectory;
import org.etlforge.integration.client.runtime.lock.ApplicationInstanceLock;
import org.etlforge.integration.client.runtime.lock.ApplicationInstanceLockFactory;
import org.etlforge.integration.client.ui.ClientUi;
import org.etlforge.system.SystemUtils;
import org.etlforge.system.annotations.InternalMethod;
import org.springframework.context.ApplicationContext;

public final class ClientPlatform {
   private static Logger LOGGER;
   private static final ClientPlatform.StartupUncaughtExceptionHandler startupExceptionHandler = new ClientPlatform.StartupUncaughtExceptionHandler();
   private static ClientId id;
   private static volatile File appDirLocation;
   private static volatile ApplicationContext appContext;
   private static final CountDownLatch initLatch = new CountDownLatch(1);

   private ClientPlatform() {
   }

   @InternalMethod
   public static void start(StartupProgressMonitor var0, String[] var1) throws Exception {
      startupExceptionHandler.install();
      ApplicationTempFolder.replaceJavaIoTmpDir();
      var0.publishMessage("Locating application directory");
      initializeAppDirLocation(var1);
      if (appDirLocation != null) {
         ApplicationLog.setAppDirLocation(appDirLocation);
         var0.publishMessage("Loading application configuration");
         appContext = SpringConfig.loadContext();
         Application.setApplicationDirectory(getApplicationDirectory());
         readClientId();
         createMainLogger();
         initializeErrorLog();
         logStartup(var1);
         if (testApplicationLock(var1)) {
            Application.setAppContext(appContext);

            parseCommandLineArgumentsAtStartUp(var1);
            var0.publishMessage("Loading project meta data");
            loadProjectMetaData();
            runPostPlatformInitializedJobs(var0);
            initLatch.countDown();
            ClientUi.publishUi(var0);
            startupExceptionHandler.restore();
         }
      }
   }

   private static void initializeAppDirLocation(String[] var0) {
      appDirLocation = getApplicationDirectoryLocation(var0);
      if (appDirLocation != null) {
         ;
      }
   }

   private static File getApplicationDirectoryLocation(String[] var0) {
      File var1 = getAppDirLocationFromCommandLine(var0);
      if (var1 == null) {
         AppDirLocationProvider var2 = ApplicationConfiguration.getAppDirLocationProvider();
         var1 = var2.getLocation();
      }

      return var1;
   }

   private static File getAppDirLocationFromCommandLine(String[] var0) {
      int var1 = ArrayUtils.indexOf(var0, "-d", 0);
      if (var1 < 0) {
         return null;
      }

      if (var1 == var0.length - 1) {
         throw new RuntimeException("Invalid use of the -d option: must provide the path to a folder.");
      }

      String var2 = var0[var1 + 1];
      return new File(var2);
   }

   public static ClientId getId() {
      return id;
   }

   private static void initializeErrorLog() {
      ErrorDetailsGenerator var0 = null;
      String var1 = "platform.ErrorDetailsGenerator";
      if (appContext.containsBean(var1)) {
         var0 = (ErrorDetailsGenerator)appContext.getBean(var1);
      } else {
         var0 = ErrorDetailsGenerator.DEFAULT;
      }

      ErrorLog.setDetailsGenerator(var0);
   }

   public static ApplicationContext getAppContext() {
      return appContext;
   }

   public static ClientApplicationDirectory getApplicationDirectory() {
      return (ClientApplicationDirectory)appContext.getBean("platform.AppDir");
   }

   public static File getApplicationHome() {
      return appDirLocation;
   }

   public static void parseCommandLineArguments(String[] var0) throws CommandLineParsingException {
      ApplicationLog.getMainLogger().info("ClientPlatform received command line arguments: " + Arrays.toString(var0));
      if (var0.length > 0) {
         CommandLineParser var1 = (CommandLineParser)getAppContext().getBean("platform.CommandLineParser");
         var1.parse(var0);
      }
   }

   private static void parseCommandLineArgumentsAtStartUp(String[] var0) {
      try {
         parseCommandLineArguments(var0);
      } catch (CommandLineParsingException var2) {
         FatalConfigurationError.throwError(7, ClientPlatform.class, var2, "Failed to parse the command line arguments.");
      }
   }

   private static void loadProjectMetaData() {
      CommonInitialization var0 = new CommonInitialization(new InitializationErrorHandler() {
         @Override
         public void handle(String var1, int var2, Throwable var3) {
            FatalConfigurationError.throwError(var2, ClientPlatform.class, var3, null);
         }
      });
      var0.initializeObjectStructure();
      var0.loadMetaData();
   }

   private static void createMainLogger() {
      LogDirectory var0 = getApplicationDirectory().getLogDirectory();
      ApplicationLog.setFolder(var0);
      ApplicationLog.loadPreconfiguredLoggers();
      LOGGER = ApplicationLog.getMainLogger();
   }

   private static void logStartup(String[] var0) {
      StringBuilder var1 = new StringBuilder("The ETLForge Integration Client was started @");
      var1.append(new Date()).append("\n");
      var1.append("Current version is: ").append(ClientInfo.getCurrentVersion()).append("\n");
      var1.append("Startup arguments: ").append(Arrays.toString(var0)).append("\n");
      var1.append("System properties: \n").append(SystemUtils.getSystemProperties("\n"));
      LOGGER.info(var1.toString());
   }

   private static boolean testApplicationLock(String[] var0) {
      ApplicationInstanceLock var1 = ApplicationInstanceLockFactory.getLock(appContext, appDirLocation);
      boolean var2 = var1.lock(var0);
      if (!var2) {
         showAnotherInstanceAlreadyRunningMessage();
      }

      return var2;
   }

   private static void showAnotherInstanceAlreadyRunningMessage() {
      if (!NonInteractiveMode.on()) {
         NonBlockingAlert.info(ApplicationConfiguration.getProductName() + " is already running.", "Already running");
      }
   }

   public static void waitForInitialization() throws InterruptedException {
      checkWaitForInitializationIsDoneFromBackgroundThread();
      initLatch.await();
   }

   private static void checkWaitForInitializationIsDoneFromBackgroundThread() {
      if (EventQueue.isDispatchThread()) {
         ErrorLog.log(
            ClientPlatform.class,
            null,
            new Exception("ClientPlatform.waitForInitialization should not be called from the EDT. It blocks the calling thread and may lead to deadlocks.")
         );
      }
   }

   public static boolean waitForInitialization(long var0, TimeUnit var2) throws InterruptedException {
      checkWaitForInitializationIsDoneFromBackgroundThread();
      return initLatch.await(var0, var2);
   }

   private static void readClientId() {
      assert getApplicationDirectory() != null : "This method cannot be called if the ApplicationDirectory has not been set";

      try {
         id = new ClientIdHandler().getId(getApplicationDirectory());
      } catch (ClientPlatformException var1) {
         FatalConfigurationError.throwError(9, ClientPlatform.class, var1, "Failed to read/generate a unique identifier for this client");
      }
   }

   private static void runPostPlatformInitializedJobs(StartupProgressMonitor var0) {
      for (PostPlatformInitializedJob var2 : Application.getBeansOfType(PostPlatformInitializedJob.class)) {
         var2.run(var0);
      }
   }

   private static class StartupUncaughtExceptionHandler implements UncaughtExceptionHandler {
      private UncaughtExceptionHandler originalHandler;

      private StartupUncaughtExceptionHandler() {
      }

      public void install() {
         Thread var1 = Thread.currentThread();
         this.originalHandler = var1.getUncaughtExceptionHandler();
         var1.setUncaughtExceptionHandler(this);
      }

      public void restore() {
         Thread.currentThread().setUncaughtExceptionHandler(this.originalHandler);
      }

      @Override
      public void uncaughtException(Thread var1, Throwable var2) {
         FatalConfigurationError.throwError(9, ClientPlatform.class, var2, "An unexpected error occurred.");
      }
   }
}
