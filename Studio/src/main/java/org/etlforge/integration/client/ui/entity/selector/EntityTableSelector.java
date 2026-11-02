package org.etlforge.integration.client.ui.entity.selector;

import java.util.Collection;
import java.util.List;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityTableCellRenderer;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.SelectorTable;

public final class EntityTableSelector<T extends IntegrationEntity> implements EntitySelector<T> {
   private final SelectorTable<T> table;
   private EntitySelector.SelectionMode mode = EntitySelector.SelectionMode.MULTIPLE;

   public EntityTableSelector(Collection<T> var1, Class<T> var2) {
      this.table = new SelectorTable<>(var1, var2);
      this.table.setStyle(TableStyles.selector());
      this.table.setObjectCellRenderer(new IntegrationEntityTableCellRenderer());
   }

   @Override
   public List<T> getSelectedEntities() {
      return this.table.getSelectedObjects();
   }

   public void setSelectedEntities(Collection<? extends T> var1) {
      this.table.setSelectedObjects(var1);
   }

   public List<T> getUnselectedEntities() {
      return this.table.getUnselectedObjects();
   }

   public void setSelectionState(boolean var1) {
      this.table.setInitialSelectionState(var1);
   }

   @Override
   public void setSelectedEntity(T var1) {
      throw new NotImplementedYetException();
   }

   @Override
   public T getSelectedEntity() {
      List var1 = this.getSelectedEntities();
      if (this.mode == EntitySelector.SelectionMode.SINGLE && var1.size() == 1) {
         return (T)var1.get(0);
      } else {
         return (T)(this.mode == EntitySelector.SelectionMode.MULTIPLE && !var1.isEmpty() ? var1.get(0) : null);
      }
   }

   @Override
   public SelectionSource getSelectionSource() {
      return this.table.asSelectionSource();
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
   }

   @Override
   public void setFilter(EntityFilter var1) {
      throw new NotImplementedYetException();
   }

   public boolean isEverythingSelected() {
      return this.table.isEverythingSelected();
   }

   public void setSelectionMode(EntitySelector.SelectionMode var1) {
      this.mode = var1;
      this.table.setSelectionMode(var1.asSelectorTableMode());
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }
}
