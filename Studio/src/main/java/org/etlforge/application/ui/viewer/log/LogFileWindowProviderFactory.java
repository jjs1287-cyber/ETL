package org.etlforge.application.ui.viewer.log;

import org.etlforge.application.log.LogFileRepository;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;

public final class LogFileWindowProviderFactory {
   private LogFileWindowProviderFactory() {
   }

   public static WindowSectionContentProvider newContentProvider(LogFileRepository var0) {
      return new LogFileWindowProviderFactory.ContentProviderImpl(var0);
   }

   private static class ContentProviderImpl implements WindowSectionContentProvider {
      private final LogFileRepository logs;
      private LogFileRepositoryViewFactory viewFactory;

      public ContentProviderImpl(LogFileRepository var1) {
         this.logs = var1;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         return this.getFactory().create(this.logs, var1);
      }

      @Override
      public String getContentId() {
         return LogFileRepositoryView.CONTENT_ID;
      }

      private synchronized LogFileRepositoryViewFactory getFactory() {
         if (this.viewFactory == null) {
            this.viewFactory = LogFileRepositoryViewFactory.newInstance();
         }

         return this.viewFactory;
      }
   }
}
