package org.etlforge.application.ui.groovy;

import com.google.common.base.Supplier;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.UiProviderEditor;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.Enablable;

public final class GroovyConsole {
   private static final String EDITOR_ID = "GroovyEditor";
   private final ConsoleUi ui = new ConsoleUi();
   private final Executor executor = this.createExecutor();

   public GroovyConsole() {
      this.bindExecutorToUi();
   }

   private Executor createExecutor() {
      Executor var1 = new Executor();
      var1.setCallback(new Enablable() {
         @Override
         public void setEnabled(boolean var1) {
            GroovyConsole.this.ui.setEnabled(var1);
            if (var1) {
               GroovyConsole.this.giveFocusToUi();
            }
         }

         @Override
         public boolean isEnabled() {
            return false;
         }
      });
      return var1;
   }

   private void bindExecutorToUi() {
      ExecuteAction var1 = new ExecuteAction(this.executor, new Supplier<String>() {
         public String get() {
            return GroovyConsole.this.ui.getScript();
         }
      });
      this.ui.setExecuteAction(var1);
   }

   public void setCommonImports(List<CommonImport> var1) {
      this.ui.setCommonImports(var1);
   }

   public void open(ApplicationWindow var1) {
      Editor var2 = this.getEditor(var1);
      var1.getEditorService().openEditor(var2);
      this.giveFocusToUi();
   }

   private Editor getEditor(ApplicationWindow var1) {
      for (Editor var5 : var1.getEditorService().getOpenEditors()) {
         if (var5.getId().equals("GroovyEditor")) {
            return var5;
         }
      }

      UiProviderEditor var6 = new UiProviderEditor(this.ui, "GroovyEditor", "Groovy Console");
      var6.setUseScroll(false);
      return var6;
   }

   private void giveFocusToUi() {
      DelayedJob.createAndSchedule(100, new Runnable() {
         @Override
         public void run() {
            GroovyConsole.this.ui.requestFocus();
         }
      });
   }
}
