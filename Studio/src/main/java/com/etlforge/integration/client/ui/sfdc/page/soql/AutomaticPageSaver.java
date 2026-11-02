package com.etlforge.integration.client.ui.sfdc.page.soql;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.lang.exception.ExceptionHandlers;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;

public final class AutomaticPageSaver implements Disposable {
   private final SoqlModel model;
   private final Editor editor;
   private final AutomaticPageSaver.Listener listener;
   private volatile ExceptionHandler<SaveEditorException> errorReporter = ExceptionHandlers.toStandardErr();

   public static AutomaticPageSaver install(SoqlModel var0, Editor var1) {
      AutomaticPageSaver var2 = new AutomaticPageSaver(var0, var1);
      var0.addPropertyChangeListener("soql", var2.listener);
      return var2;
   }

   private AutomaticPageSaver(SoqlModel var1, Editor var2) {
      this.model = var1;
      this.editor = var2;
      this.listener = new AutomaticPageSaver.Listener();
   }

   public void setErrorReporter(ExceptionHandler<SaveEditorException> var1) {
      this.errorReporter = var1;
   }

   @Override
   public void dispose() {
      this.model.removePropertyChangeListener("soql", this.listener);
   }

   private class Listener implements PropertyChangeListener {
      private Listener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         UiUtils.nestedInvokeLater(2, new Runnable() {
            @Override
            public void run() {
               Listener.this.save();
            }
         });
      }

      private void save() {
         try {
            if (AutomaticPageSaver.this.editor instanceof GenericEditor) {
               ((GenericEditor)AutomaticPageSaver.this.editor).setDirty();
            }

            AutomaticPageSaver.this.editor.save();
         } catch (SaveEditorException var2) {
            AutomaticPageSaver.this.errorReporter.caught(var2);
         }
      }
   }
}
