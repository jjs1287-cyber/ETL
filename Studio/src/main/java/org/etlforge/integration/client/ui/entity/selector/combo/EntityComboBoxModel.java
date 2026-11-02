package org.etlforge.integration.client.ui.entity.selector.combo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.name.NameSorter;

public final class EntityComboBoxModel extends DefaultComboBoxModel {
   private static final EntityComboBoxModel.NoneComboItem NONE_COMBO_ITEM = new EntityComboBoxModel.NoneComboItem();
   private final IntegrationProject project;
   private final EntityType entityType;
   private EntityFilter filter;
   private final List<IntegrationEntity> allItems;
   private final boolean includeNoneItem;
   private ProjectListener projectListener;
   private Object selectedObject;
   private EntityComboBoxModel.PropChangeSupport propChangeSupport;

   public EntityComboBoxModel(IntegrationProject var1, EntityType var2, EntityFilter var3, boolean var4) {
      this.project = var1;
      this.entityType = var2;
      this.filter = var3;
      this.includeNoneItem = var4;
      this.allItems = this.getInitialItems(var1, var2);
   }

   public void setDynamic() {
      if (this.projectListener == null) {
         this.installProjectListener();
      }

      if (this.propChangeSupport == null) {
         this.propChangeSupport = new EntityComboBoxModel.PropChangeSupport();
      }
   }

   private void installProjectListener() {
      this.projectListener = new EntityComboBoxModel.ProjectListenerImpl();
      this.project.addProjectListener(this.projectListener);
   }

   public void dispose() {
      if (this.projectListener != null) {
         this.project.removeProjectListener(this.projectListener);
         this.projectListener = null;
      }

      if (this.propChangeSupport != null) {
         this.propChangeSupport.dispose();
         this.propChangeSupport = null;
      }
   }

   public void setFilter(EntityFilter var1) {
      if (var1 != this.filter) {
         this.filter = var1;
         this.fireContentsChanged(this, -1, -1);
         IntegrationEntity var2 = this.getSelectedEntity();
         if (var2 != null && this.accept(var2)) {
            this.setSelectedItem(var2);
         } else {
            this.setSelectedItem(this.includeNoneItem ? NONE_COMBO_ITEM : null);
         }
      }
   }

   IntegrationEntity getSelectedEntity() {
      return this.selectedObject instanceof IntegrationEntity ? (IntegrationEntity)this.selectedObject : null;
   }

   @Override
   public void addElement(Object var1) {
      if (var1 instanceof IntegrationEntity && this.accept((IntegrationEntity)var1)) {
         this.allItems.add((IntegrationEntity)var1);
         this.fireContentsChanged(this, -1, -1);
      }
   }

   @Override
   public Object getElementAt(int var1) {
      int var2 = 0;
      if (this.includeNoneItem) {
         if (var1 == 0) {
            return NONE_COMBO_ITEM;
         }

         var2 = 1;
      }

      for (IntegrationEntity var4 : this.allItems) {
         if (this.accept(var4)) {
            if (var2 == var1) {
               return var4;
            }

            var2++;
         }
      }

      return null;
   }

   @Override
   public int getIndexOf(Object var1) {
      int var2 = 0;
      if (this.includeNoneItem) {
         if (var1 == NONE_COMBO_ITEM) {
            return 0;
         }

         var2 = 1;
      }

      if (var1 instanceof IntegrationEntity && this.accept((IntegrationEntity)var1)) {
         for (IntegrationEntity var4 : this.allItems) {
            if (var4 == var1) {
               return var2;
            }

            var2++;
         }
      }

      return -1;
   }

   @Override
   public Object getSelectedItem() {
      return this.selectedObject;
   }

   @Override
   public int getSize() {
      int var1 = 0;

      for (IntegrationEntity var3 : this.allItems) {
         if (this.accept(var3)) {
            var1++;
         }
      }

      return this.includeNoneItem ? var1 + 1 : var1;
   }

   @Override
   public void insertElementAt(Object var1, int var2) {
      this.checkValidObject(var1);
      this.checkValidIndex(var2);
      IntegrationEntity var3 = (IntegrationEntity)var1;
      if (this.accept(var3)) {
         int var4 = this.includeNoneItem ? 1 : 0;
         int var5 = 0;

         for (int var6 = this.allItems.size(); var5 < var6; var5++) {
            if (this.accept(this.allItems.get(var5))) {
               if (var2 == var4) {
                  this.allItems.add(var5, var3);
                  this.fireContentsChanged(this, -1, -1);
                  return;
               }

               var4++;
            }
         }
      }
   }

   private void checkValidObject(Object var1) {
      Preconditions.checkArgument(
         var1 instanceof IntegrationEntity, "anObject must be an IntgrationEntity (was " + (var1 != null ? var1.getClass().getName() : "null") + ")"
      );
   }

   private void checkValidIndex(int var1) {
      Preconditions.checkArgument(!this.includeNoneItem || var1 != 0, "Cannot insert an item at index 0 when the combo box has a NONE item.");
   }

   @Override
   public void removeAllElements() {
      if (!this.allItems.isEmpty()) {
         this.allItems.clear();
         this.fireContentsChanged(this, -1, -1);
      }
   }

   @Override
   public void removeElement(Object var1) {
      if (var1 instanceof IntegrationEntity) {
         IntegrationEntity var2 = (IntegrationEntity)var1;
         this.allItems.remove(var2);
         if (this.accept(var2)) {
            this.fireContentsChanged(this, -1, -1);
         }
      }
   }

   @Override
   public void removeElementAt(int var1) {
      if (this.includeNoneItem && var1 == 0) {
         throw new IllegalArgumentException("Cannot remove the NONE item.");
      }

      int var2 = this.includeNoneItem ? 1 : 0;
      int var3 = 0;

      for (int var4 = this.allItems.size(); var3 < var4; var3++) {
         if (this.accept(this.allItems.get(var3))) {
            if (var1 == var2) {
               this.allItems.remove(var3);
               this.fireContentsChanged(this, -1, -1);
               return;
            }

            var2++;
         }
      }
   }

   @Override
   public void setSelectedItem(Object var1) {
      if (var1 instanceof IntegrationEntity) {
         this.setSelectedItemImpl((IntegrationEntity)var1);
      } else if (var1 instanceof IntegrationEntityId) {
         this.setSelectedItemImpl((IntegrationEntityId)var1);
      } else {
         this.selectedObject = this.includeNoneItem ? NONE_COMBO_ITEM : null;
      }

      super.setSelectedItem(this.selectedObject);
   }

   private void setSelectedItemImpl(IntegrationEntity var1) {
      if (this.accept(var1)) {
         this.selectedObject = var1;
      } else {
         this.selectedObject = null;
      }
   }

   private void setSelectedItemImpl(IntegrationEntityId var1) {
      for (int var2 = 0; var2 < this.getSize(); var2++) {
         Object var3 = this.getElementAt(var2);
         if (var3 instanceof IntegrationEntity) {
            IntegrationEntity var4 = (IntegrationEntity)var3;
            if (var1.equals(var4.getID())) {
               this.selectedObject = var4;
               return;
            }
         }
      }
   }

   boolean accept(IntegrationEntity var1) {
      if (var1 == null) {
         return false;
      }

      EntityType var2 = var1.getEntityType();
      return var2 == this.entityType && (this.filter == null || this.filter.apply(var1));
   }

   String getToolTip() {
      IntegrationEntity var1 = this.getSelectedEntity();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : null;
   }

   private List<IntegrationEntity> getInitialItems(IntegrationProject var1, EntityType var2) {
      ArrayList var3 = Lists.newArrayList();
      this.addEntities(var1, var3, var2);
      Collections.sort(var3, NameSorter.ignoreCase());
      return var3;
   }

   private void addEntities(IntegrationProject var1, List<IntegrationEntity> var2, EntityType var3) {
      RootFolder var4 = var1.getRootFolder(var3);
      if (var4 == null) {
         throw new RuntimeException("Failed to find the root folder for the type " + var3);
      }

      addItemsToList(var4, var2, var3);
   }

   private static void addItemsToList(IntegrationEntity var0, List<IntegrationEntity> var1, EntityType var2) {
      if (accept(var0, var2)) {
         var1.add(var0);
      }

      for (IntegrationEntity var4 : var0.getAllChildren()) {
         addItemsToList(var4, var1, var2);
      }
   }

   private static boolean accept(IntegrationEntity var0, EntityType var1) {
      EntityType var2 = var0.getEntityType();
      return var2 == var1;
   }

   private class DeletedEntityHandler {
      private boolean update;
      private boolean deletedWasSelected;

      private DeletedEntityHandler() {
      }

      public void process(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            this.processEntity(var3);
         }

         if (this.deletedWasSelected) {
            EntityComboBoxModel.this.setSelectedItem(null);
         }

         if (this.update) {
            EntityComboBoxModel.this.fireContentsChanged(this, -1, -1);
         }
      }

      private void processEntity(IntegrationEntity var1) {
         this.checkSelectedItem(var1);
         EntityComboBoxModel.this.allItems.remove(var1);
         if (EntityComboBoxModel.this.accept(var1)) {
            this.update = true;
         }
      }

      private void checkSelectedItem(IntegrationEntity var1) {
         IntegrationEntity var2 = EntityComboBoxModel.this.getSelectedEntity();
         if (var2 != null) {
            this.deletedWasSelected = var1 == var2;
         }
      }
   }

   private static class NoneComboItem {
      private NoneComboItem() {
      }

      @Override
      public String toString() {
         return Strings.get("EntityCombo.NoneItem");
      }
   }

   private class ProjectListenerImpl implements ProjectListener {
      private ProjectListenerImpl() {
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.insertItems(var1);
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         EntityComboBoxModel.this.new DeletedEntityHandler().process(var1);
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         this.insertItems(var1);
      }

      private void insertItems(IntegrationProjectEvent var1) {
         boolean var2 = false;

         for (IntegrationEntity var4 : var1.getEntities()) {
            EntityComboBoxModel.this.allItems.add(var4);
            if (EntityComboBoxModel.this.accept(var4)) {
               var2 = true;
            }
         }

         if (var2) {
            EntityComboBoxModel.this.fireContentsChanged(this, -1, -1);
         }
      }
   }

   private class PropChangeSupport implements Disposable {
      private final ProjectPropertyChangeSupport impl = new ProjectPropertyChangeSupport(EntityComboBoxModel.this.project);

      public PropChangeSupport() {
         this.impl.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent var1) {
               PropChangeSupport.this.handleChange(var1);
            }
         });
      }

      @Override
      public void dispose() {
         this.impl.dispose();
      }

      private void handleChange(PropertyChangeEvent var1) {
         IntegrationEntity var2 = (IntegrationEntity)var1.getSource();
         if (var2.getEntityType() == EntityComboBoxModel.this.entityType) {
            UiUtils.runOnEventThread(new Runnable() {
               @Override
               public void run() {
                  EntityComboBoxModel.this.fireContentsChanged(PropChangeSupport.this, -1, -1);
                  if (EntityComboBoxModel.this.selectedObject != null && !EntityComboBoxModel.this.accept(EntityComboBoxModel.this.getSelectedEntity())) {
                     EntityComboBoxModel.this.setSelectedItem(null);
                  }
               }
            });
         }
      }
   }
}
