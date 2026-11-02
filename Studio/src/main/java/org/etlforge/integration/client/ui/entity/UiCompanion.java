package org.etlforge.integration.client.ui.entity;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.data.entity.IntegrationEntity;

public abstract class UiCompanion {
   private static final UiCompanion NONE = new UiCompanion() {
      @Override
      public boolean canOpenPage() {
         return false;
      }

      @Override
      public Editor openPage() {
         return null;
      }

      @Override
      public Editor findOpenPage() {
         return null;
      }

      @Override
      public boolean rename(String var1) {
         return false;
      }

      @Override
      public void displayInTree() {
      }
   };

   public static UiCompanion of(IntegrationEntity var0) {
      UiCompanion var1 = var0.getExtensionObject(UiCompanion.class);
      if (var1 == null) {
         var1 = NONE;
      }

      return var1;
   }

   public abstract boolean canOpenPage();

   public abstract Editor openPage();

   public abstract Editor findOpenPage();

   public final boolean saveIfDirty() {
      Editor var1 = this.findOpenPage();
      if (var1 != null && var1.isDirty()) {
         try {
            var1.save();
            return true;
         } catch (SaveEditorException var3) {
            this.openPage();
            ErrorLog.attention(UiCompanion.class, null, var3);
            return false;
         }
      } else {
         return true;
      }
   }

   public abstract boolean rename(String var1);

   public abstract void displayInTree();
}
