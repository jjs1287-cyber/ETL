package org.etlforge.application.ui.util;

import com.google.common.base.Preconditions;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import org.apache.commons.lang.SystemUtils;

public abstract class NativeFileEditor {
   public static NativeFileEditor getEditor() {
      return SystemUtils.IS_OS_WINDOWS ? new NativeFileEditor.WindowsEditor() : new NativeFileEditor.DesktopEditor();
   }

   public abstract boolean isSupported();

   public abstract void edit(File var1) throws Exception;

   private static class DesktopEditor extends NativeFileEditor {
      private final Desktop desktop;

      public DesktopEditor() {
         if (Desktop.isDesktopSupported()) {
            this.desktop = Desktop.getDesktop();
         } else {
            this.desktop = null;
         }
      }

      @Override
      public boolean isSupported() {
         return this.desktop != null && this.desktop.isSupported(Action.EDIT);
      }

      @Override
      public void edit(File var1) throws Exception {
         Preconditions.checkState(
            this.desktop != null,
            "This editor is not supported. You are not supposed to call this method if isSupported returns false (which it did (or at least would have done, had you called it))."
         );
         this.desktop.edit(var1);
      }
   }

   private static class WindowsEditor extends NativeFileEditor {
      private WindowsEditor() {
      }

      @Override
      public boolean isSupported() {
         return true;
      }

      @Override
      public void edit(File var1) throws Exception {
         String var2 = "rundll32 url.dll,FileProtocolHandler " + var1.getCanonicalPath();
         Runtime.getRuntime().exec(var2);
      }
   }
}
