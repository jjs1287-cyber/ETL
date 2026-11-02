package org.etlforge.integration.client.ui.interchange;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.entity.ProjectPageGroup;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageLog;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.client.ui.interchange.entity.EntityPageFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.wait.WaitLock;

final class PageManager implements EntityPageDisplayer {
   private final ApplicationWindow appWin;
   private final EntityConfigLookup entityConfigs;
   private final BiMap<IntegrationEntity, Editor> entityToPage = HashBiMap.create();

   public PageManager(ApplicationWindow var1, EntityConfigLookup var2) {
      this.entityConfigs = var2;
      this.appWin = var1;
   }

   @Override
   public Editor openPage(IntegrationEntity var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "open");
      logRequest(var1);
      WaitLock var2 = this.appWin.startWait();
      PageManager.Job var3 = new PageManager.Job(var1, var2);
      return var3.run();
   }

   private static void logRequest(IntegrationEntity var0) {
      IntegrationEntityPage.LOG.info("Asked to create a page for the " + var0.getEntityType().getDisplayName() + " \"" + var0.getName() + "\"");
   }

   @Override
   public Editor findOpenPage(IntegrationEntity var1) {
      Editor var2 = this.entityToPage.get(var1);
      if (var2 == null) {
         EditorService var3 = this.appWin.getEditorService();

         for (Editor var7 : var3.getOpenEditors()) {
            if (var7.getObject() == var1) {
               var2 = var7;
               break;
            }
         }
      }

      return var2;
   }

   private final class Job {
      private final IntegrationEntity entity;
      private final WaitLock waitLock;

      public Job(IntegrationEntity var2, WaitLock var3) {
         this.entity = var2;
         this.waitLock = var3;
      }

      public Editor run() {
         try {
            Editor var1 = this.getPage();
            if (var1 != null) {
               this.displayPage(var1);
            }

            return var1;
         } catch (Exception var6) {
            this.handleError(var6);
         } finally {
            this.waitLock.release();
         }

         return null;
      }

      private void handleError(Exception var1) {
         ErrorLog.attention(PageManager.class, PackageResources.PageManager.OPEN_PAGES_FAILED, var1);
      }

      private Editor getPage() {
         Editor var1 = PageManager.this.findOpenPage(this.entity);
         if (var1 == null) {
            EntityConfig var2 = PageManager.this.entityConfigs.getEntityConfig(this.entity.getEntityType());
            EntityPageFactory var3 = var2.getPageFactory();
            var1 = var3.createPage(this.entity);
         }

         return var1;
      }

      private void displayPage(Editor var1) {
         String var2 = ProjectPageGroup.get(this.entity);
         PageManager.this.appWin.getEditorService().openEditor(var1, var2);
         PageManager.this.entityToPage.put(this.entity, var1);
         var1.addResource(new PageManager.Job.PageRemover());
         this.logPageOpened();
      }

      private void logPageOpened() {
         IntegrationEntityPageLog.LOG.info("The page is open.");
      }

      private class PageRemover extends AbstractWindowElementResource {
         private PageRemover() {
         }

         @Override
         public void dispose(WindowElement var1) {
            IntegrationEntityPage var2 = (IntegrationEntityPage)var1;
            PageManager.this.entityToPage.inverse().remove(var2);
         }
      }
   }
}
