package org.etlforge.application.ui.viewer.error.log;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;

public final class ErrorLogWindowProviderFactory {
   private ErrorLogWindowProviderFactory() {
      throw new UnsupportedOperationException();
   }

   public static WindowSectionContentProvider newContentProvider() {
      return new ErrorLogWindowProviderFactory.ContentProviderImpl();
   }

   private static class ContentProviderImpl implements WindowSectionContentProvider {
      private ErrorLogViewFactory viewFactory;

      private ContentProviderImpl() {
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         ErrorLogViewFactory var2 = this.getViewFactory();
         return var2.create(var1);
      }

      @Override
      public String getContentId() {
         return "error_log";
      }

      private ErrorLogViewFactory getViewFactory() {
         if (this.viewFactory == null) {
            this.viewFactory = ErrorLogViewFactory.newInstance();
         }

         return this.viewFactory;
      }
   }
}
