package com.etlforge.integration.client.ui.interchange.project.share;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiThreadSupplier;

final class EntityOverwriter {
   public void overwrite(IntegrationEntity var1, IntegrationEntity var2) {
      boolean var3 = this.closeTargetPageIfOpen(var2);
      this.updateTarget(var1, var2);
      this.checkName(var1, var2);
      if (var3) {
         this.openTargetPage(var2);
      }
   }

   private void updateTarget(IntegrationEntity var1, IntegrationEntity var2) {
      EntityType var3 = var2.getEntityType();
      if (!var3.isFolder()) {
         if (var3 == EntityType.Project) {
            throw new IllegalStateException("Not supposed to happen");
         }

         var2.importFrom(var1);
      }
   }

   private void checkName(IntegrationEntity var1, IntegrationEntity var2) {
      if (!var2.getName().equals(var1.getName())) {
      }
   }

   private boolean closeTargetPageIfOpen(final IntegrationEntity var1) {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            Editor var1x = UiCompanion.of(var1).findOpenPage();
            if (var1x != null) {
               var1x.close();
               return Boolean.TRUE;
            } else {
               return Boolean.FALSE;
            }
         }
      });
   }

   private void openTargetPage(final IntegrationEntity var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            UiCompanion.of(var1).openPage();
         }
      });
   }
}
