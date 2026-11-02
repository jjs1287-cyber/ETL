package org.etlforge.integration.client.ui.interchange;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.Map;
import org.etlforge.application.extension.ExtensionObjectFactory;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.event.EntitySavedEvent;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;
import org.etlforge.system.Disposable;

final class EntityToolTipExtensionFactory implements ExtensionObjectFactory, Disposable {
   private final EntityConfigRegistry entityConfigs;
   private final Map<IntegrationEntity, IntegrationEntityToolTip> tooltips = Maps.newConcurrentMap();
   private final EventBus eventBus;
   private final EntityToolTipExtensionFactory.ToolTipRefresher refresher = new EntityToolTipExtensionFactory.ToolTipRefresher();
   private boolean disposed;

   public EntityToolTipExtensionFactory(EntityConfigRegistry var1, ManagedProject var2) {
      this.entityConfigs = var1;
      this.eventBus = var2.getEventBus();
      this.eventBus.register(this.refresher);
   }

   @Override
   public void dispose() {
      if (!this.disposed) {
         this.eventBus.unregister(this.refresher);
         this.tooltips.clear();
         this.disposed = true;
      }
   }

   @Override
   public Class[] getExtensionTypes() {
      return new Class[]{IntegrationEntityToolTip.class};
   }

   @Override
   public Object getExtensionObject(Object var1, Class var2) {
      if (IntegrationEntityToolTip.class.equals(var2)) {
         IntegrationEntity var3 = (IntegrationEntity)var1;
         IntegrationEntityToolTip var4 = this.tooltips.get(var3);
         if (var4 == null) {
            var4 = this.createAndStoreToolTip(var3);
         }

         return var4;
      } else {
         return null;
      }
   }

   private IntegrationEntityToolTip createAndStoreToolTip(IntegrationEntity var1) {
      EntityConfig var2 = this.entityConfigs.getEntityConfig(var1.getEntityType());
      IntegrationEntityToolTip var3 = var2.getToolTip(var1);
      this.tooltips.put(var1, var3);
      return var3;
   }

   private class ToolTipRefresher {
      private ToolTipRefresher() {
      }

      @Subscribe
      public void handle(EntitySavedEvent var1) {
         EntityToolTipExtensionFactory.this.createAndStoreToolTip(var1.getEntity());
      }
   }
}
