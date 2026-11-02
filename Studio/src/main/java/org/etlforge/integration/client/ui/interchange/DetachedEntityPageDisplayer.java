package org.etlforge.integration.client.ui.interchange;

import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.client.ui.interchange.entity.EntityPageFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class DetachedEntityPageDisplayer implements EntityPageDisplayer {
   private final EntityConfigLookup entityConfigLookup;
   private final EditorService editorService;
   private final Object lock = new Object();

   public DetachedEntityPageDisplayer(EntityConfigLookup var1, EditorService var2) {
      this.entityConfigLookup = var1;
      this.editorService = var2;
   }

   @Override
   public Editor findOpenPage(IntegrationEntity var1) {
      synchronized (this.lock) {
         for (Editor var6 : this.editorService.getOpenEditors()) {
            if (var6 instanceof IntegrationEntityPage) {
               IntegrationEntityPage var7 = (IntegrationEntityPage)var6;
               if (var7.getObject() == var1) {
                  return var6;
               }
            }
         }

         return null;
      }
   }

   @Override
   public Editor openPage(IntegrationEntity var1) {
      synchronized (this.lock) {
         Editor var3 = this.findOpenPage(var1);
         if (var3 == null) {
            var3 = this.getPageFactory(var1).createPage(var1);
         }

         this.editorService.openEditorDetached(var3);
         return var3;
      }
   }

   private EntityPageFactory getPageFactory(IntegrationEntity var1) {
      EntityConfig var2 = this.entityConfigLookup.getEntityConfig(var1.getEntityType());
      return var2.getPageFactory();
   }
}
