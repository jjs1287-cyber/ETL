package org.etlforge.application.ui.job;

import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;

public final class JobConsoleWindowProviderFactory {
   private JobConsoleWindowProviderFactory() {
   }

   public static WindowSectionContentProvider newContentProvider() {
      return new JobConsoleWindowProviderFactory.ContentProviderImpl();
   }

   private static class ContentProviderImpl implements WindowSectionContentProvider {
      private ContentProviderImpl() {
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         UiJobConsole var2 = UiJobConsole.getConsole(ApplicationUi.getJobManager());
         return var2.getAsWindowSectionContent(var1);
      }

      @Override
      public String getContentId() {
         return "ui_job_console";
      }
   }
}
