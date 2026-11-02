package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Supplier;
import org.etlforge.application.extension.ExtensionObjectFactory;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityRenamer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.UiUtils;

final class UiCompanionExtensionFactory implements ExtensionObjectFactory {
   private final EntityConfigRegistry entityConfigs;
   private final ProjectContentViewerManager contentViewers;
   private final EntityPageDisplayer pageDisplayer;

   public UiCompanionExtensionFactory(EntityConfigRegistry var1, ProjectContentViewerManager var2, EntityPageDisplayer var3) {
      this.entityConfigs = var1;
      this.contentViewers = var2;
      this.pageDisplayer = var3;
   }

   @Override
   public Class[] getExtensionTypes() {
      return new Class[]{UiCompanion.class};
   }

   @Override
   public Object getExtensionObject(Object var1, Class var2) {
      if (UiCompanion.class.equals(var2)) {
         IntegrationEntity var3 = (IntegrationEntity)var1;
         EntityConfig var4 = this.entityConfigs.getEntityConfig(var3.getEntityType());
         return new UiCompanionExtensionFactory.Impl(var3, var4, this.contentViewers, this.pageDisplayer);
      } else {
         return null;
      }
   }

   private static class Impl extends UiCompanion {
      private final IntegrationEntity entity;
      private final EntityConfig config;
      private final ProjectContentViewerManager contentViewers;
      private final EntityPageDisplayer pageDisplayer;

      public Impl(IntegrationEntity var1, EntityConfig var2, ProjectContentViewerManager var3, EntityPageDisplayer var4) {
         this.entity = var1;
         this.config = var2;
         this.contentViewers = var3;
         this.pageDisplayer = var4;
      }

      @Override
      public boolean canOpenPage() {
         return true;
      }

      @Override
      public Editor openPage() {
         return UiThreadSupplier.call(new Supplier<Editor>() {
            public Editor get() {
               return Impl.this.pageDisplayer.openPage(Impl.this.entity);
            }
         });
      }

      @Override
      public Editor findOpenPage() {
         return UiThreadSupplier.call(new Supplier<Editor>() {
            public Editor get() {
               return Impl.this.pageDisplayer.findOpenPage(Impl.this.entity);
            }
         });
      }

      @Override
      public boolean rename(String var1) {
         EntityRenamer var2 = this.config.getRenamer();
         return var2.setNewName(this.entity, var1);
      }

      @Override
      public void displayInTree() {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               ProjectContentViewer var1 = Impl.this.contentViewers.getViewer();
               var1.selectEntity(Impl.this.entity);
            }
         });
      }
   }
}
