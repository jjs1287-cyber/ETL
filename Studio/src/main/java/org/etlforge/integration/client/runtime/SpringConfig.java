package org.etlforge.integration.client.runtime;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.io.FilteredLineProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

final class SpringConfig {
   public static ApplicationContext loadContext() {
      try {
         String[] var0 = getFileList();
         log(var0);
         return new ClassPathXmlApplicationContext(var0);
      } catch (Exception var1) {
         FatalConfigurationError.throwError(9, SpringConfig.class, var1, "The Spring configuration is incorrect. Please contact ETLForge Support.");
         return null;
      }
   }

   private static void log(String[] var0) {
      ApplicationLog.getMainLogger().info("Loading the Spring application context from " + Arrays.toString(var0));
   }

   private static String[] getFileList() throws IOException {
      URL var0 = Resources.getResource("Spring.config");
      return Resources.readLines(var0, Charsets.UTF_8, new SpringConfig.FileNameCollector());
   }

   private SpringConfig() {
   }

   private static class FileNameCollector extends FilteredLineProcessor<String[]> {
      private final List<String> files = Lists.newArrayList();

      public FileNameCollector() {
         super(HASHMARK_COMMENT, true);
      }

      public String[] getResult() {
         return this.files.toArray(new String[this.files.size()]);
      }

      @Override
      protected boolean handleAcceptedLine(String var1) throws IOException {
         this.files.add(var1);
         return true;
      }
   }
}
