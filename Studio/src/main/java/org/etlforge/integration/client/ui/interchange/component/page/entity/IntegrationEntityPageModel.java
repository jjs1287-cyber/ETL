package org.etlforge.integration.client.ui.interchange.component.page.entity;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import org.etlforge.application.ui.window.editor.service.EditorServiceUiMode;
import org.etlforge.application.ui.window.editor.service.EditorServiceUiModePreference;
import org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies.EntityDependenciesModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies.NoDependencies;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.system.Disposable;

public final class IntegrationEntityPageModel implements Disposable {
   private final IntegrationEntity entity;
   private final PropertyChangeListener entityListener;
   private final Multimap<String, BoundProperty> boundProperties;
   private EntityDependenciesModel dependencies;
   private boolean blockPropertyChangeEvents;

   public IntegrationEntityPageModel(IntegrationEntity var1) {
      this.entity = var1;
      this.dependencies = new NoDependencies();
      this.entityListener = new IntegrationEntityPageModel.EntityListener();
      this.boundProperties = HashMultimap.create();
   }

   public String getId() {
      return this.entity.getID().toString();
   }

   public IntegrationEntity getEntity() {
      return this.entity;
   }

   @Override
   public void dispose() {
      this.entity.removePropertyChangeListener(this.entityListener);
      this.boundProperties.clear();
      this.dependencies.dispose();
   }

   public EntityDependenciesModel getDependencies() {
      return this.dependencies;
   }

   public void setDependencies(EntityDependenciesModel var1) {
      this.dependencies = var1;
   }

   public String getMainTitle() {
      EditorServiceUiMode var1 = EditorServiceUiModePreference.get();
      return var1 == EditorServiceUiMode.MULTI_TABBED ? null : this.entity.getEntityType().getDisplayName();
   }

   public String getSubTitle() {
      return this.entity.getName();
   }

   public Icon getSmallIcon() {
      return this.getIconImpl();
   }

   private Icon getIconImpl() {
      return IconStore.defaultIcon(this.getEntity());
   }

   void prepareForDisplay() {
      this.entity.addPropertyChangeListener(this.entityListener);
   }

   public void bindProperty(BoundProperty var1) {
      this.boundProperties.put(var1.getName(), var1);
   }

   public void bindProperties(BoundProperty[] var1) {
      for (BoundProperty var5 : var1) {
         this.bindProperty(var5);
      }
   }

   public void bindProperties(Iterable<? extends BoundProperty> var1) {
      for (BoundProperty var3 : var1) {
         this.bindProperty(var3);
      }
   }

   public void removeBoundProperty(BoundProperty var1) {
      this.boundProperties.remove(var1.getName(), var1);
   }

   void setBlockPropertyChangeEvents(boolean var1) {
      this.blockPropertyChangeEvents = var1;
   }

   private class EntityListener implements PropertyChangeListener {
      private EntityListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!IntegrationEntityPageModel.this.blockPropertyChangeEvents) {
            String var2 = var1.getPropertyName();
            this.updateBoundPropertyControls(var2, var1.getNewValue());
         }
      }

      private void updateBoundPropertyControls(String var1, Object var2) {
         if (IntegrationEntityPageModel.this.boundProperties.containsKey(var1)) {
            for (BoundProperty var4 : IntegrationEntityPageModel.this.boundProperties.get(var1)) {
               var4.newValue(var2);
            }
         }
      }
   }
}
