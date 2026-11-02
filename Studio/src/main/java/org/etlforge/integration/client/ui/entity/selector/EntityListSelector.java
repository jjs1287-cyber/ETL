package org.etlforge.integration.client.ui.entity.selector;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionSourceWithDefaultAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.util.name.NameSorter;

public class EntityListSelector<T extends IntegrationEntity> implements EntitySelector<T> {
   private final Class<T> clazz;
   private final KongaList entityList;
   private final FilteredListModel listModel;

   public EntityListSelector(IntegrationProject var1, Class<T> var2) {
      this(var1.getAllEntitiesOfClass(var2), var2);
   }

   public EntityListSelector(Iterable<T> var1, Class<T> var2) {
      this.clazz = var2;
      this.listModel = this.createListModel(var1);
      this.entityList = this.createList(this.listModel);
   }

   @Override
   public void setSelectedEntity(T var1) {
      Selection var2 = var1 != null ? SelectionFactory.newOrderedSelection(var1) : Selection.EMPTY;
      this.entityList.setSelection(var2);
   }

   @Override
   public T getSelectedEntity() {
      IndexedSelection var1 = this.entityList.getSelection();
      if (var1.size() == 1) {
         IntegrationEntity var2 = (IntegrationEntity)var1.head();
         return this.clazz.cast(var2);
      } else {
         return null;
      }
   }

   @Override
   public List<T> getSelectedEntities() {
      return Lists.newArrayList(FluentIterable.from(this.entityList.getSelection().items()).filter(this.clazz));
   }

   public SelectionSourceWithDefaultAction getSelectionSource() {
      return this.entityList;
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.entityList.setDefaultAction(var1);
   }

   @Override
   public void setFilter(EntityFilter var1) {
      final EntityListSelector.ListFilterImpl var2 = var1 != null ? new EntityListSelector.ListFilterImpl(var1) : null;
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            EntityListSelector.this.listModel.setFilter(var2);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.entityList.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.entityList.requestFocus();
      if (this.entityList.getSelection().isEmpty()) {
         this.entityList.selectFirst();
      }
   }

   private FilteredListModel createListModel(Iterable<T> var1) {
      List var2 = Iterables2.sortedCopy(var1, NameSorter.ignoreCase());
      DefaultListModel var3 = new DefaultListModel();

      for (Object __etlItem101 : var2) {
         IntegrationEntity var5 = (IntegrationEntity) __etlItem101;
         var3.addElement(var5);
      }

      return new FilteredListModel(var3);
   }

   private KongaList createList(ListModel var1) {
      KongaListFactory var2 = ComponentFactories.newKongaListFactory();
      var2.setCellRenderer(RendererUtils.getIntegrationEntityListCellRenderer());
      var2.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var2.setVisibleRowCount(15);
      return var2.newList(var1);
   }

   private static class ListFilterImpl implements ListFilter {
      private final EntityFilter delegate;

      public ListFilterImpl(EntityFilter var1) {
         this.delegate = var1;
      }

      @Override
      public boolean test(Object var1) {
         return this.delegate.apply((IntegrationEntity)var1);
      }
   }
}
