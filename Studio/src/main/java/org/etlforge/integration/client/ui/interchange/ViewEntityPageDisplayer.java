package org.etlforge.integration.client.ui.interchange;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ViewEntityPageDisplayer implements EntityPageDisplayer {
   private final ApplicationWindow appWin;
   private final EntityConfigLookup entityConfigs;
   private final PageManager pageManager;
   private final Object lock = new Object();

   public ViewEntityPageDisplayer(ApplicationWindow var1, EntityConfigLookup var2) {
      this.appWin = var1;
      this.entityConfigs = var2;
      this.pageManager = new PageManager(var1, var2);
   }

   @Override
   public Editor findOpenPage(IntegrationEntity var1) {
      synchronized (this.lock) {
         return this.pageManager.findOpenPage(var1);
      }
   }

   @Override
   public Editor openPage(IntegrationEntity var1) {
      synchronized (this.lock) {
         boolean var3 = this.checkIfEditorShouldBeDetached(var1);
         if (var3) {
            DetachedEntityPageDisplayer var4 = new DetachedEntityPageDisplayer(this.entityConfigs, this.appWin.getEditorService());
            return var4.openPage(var1);
         } else {
            return this.pageManager.openPage(var1);
         }
      }
   }

   private boolean checkIfEditorShouldBeDetached(IntegrationEntity var1) {
      boolean var2 = false;

      for (Editor var6 : this.appWin.getEditorService().getOpenEditors()) {
         if (var6.getState().contains(WindowElementState.DETACHED) && var6.getObject() != var1) {
            var2 = true;
            break;
         }
      }

      return var2;
   }
}
