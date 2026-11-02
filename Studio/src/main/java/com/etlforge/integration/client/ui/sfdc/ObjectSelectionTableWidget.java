package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.etlforge.integration.salesforce.ui.renderer.Renderers;
import java.util.List;
import org.etlforge.application.ui.widget.SelectionGrid;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.salesforce.wsdl.ObjectCategory;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class ObjectSelectionTableWidget implements ObjectSelectionWidget {
   private final ObjectSelectionModel objectModel;
   private final ObjectSelectionTableWidget.TableImpl table;
   private Predicate<? super SObjectSchema> nameFilter;

   public ObjectSelectionTableWidget(ObjectSelectionModel var1) {
      this(var1, 3);
   }

   public ObjectSelectionTableWidget(ObjectSelectionModel var1, int var2) {
      this.objectModel = var1;
      this.table = new ObjectSelectionTableWidget.TableImpl(var2);
      this.table.setRenderer(Renderers.forTable(var1.getObjectNameDisplayMode()));
      this.bindToModel(var1);
   }

   private void bindToModel(ObjectSelectionModel var1) {
      ListProperty var2 = var1.getAvailableObjectsProperty();
      Property var3 = var1.getProperty("selectedObject");
      Bindings.of(this.table.bindWithModel(var2, var3), new ObjectSelectionTableWidget.ObjectCategoryBinding(var1)).syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   @Override
   public void setNameFilter(Predicate<? super SObjectSchema> var1) {
      this.nameFilter = var1;
      this.table.setValues(this.objectModel.getAvailableObjects());
   }

   private Predicate<SObjectSchema> getEffectiveFilter() {
      ObjectCategory var1 = this.objectModel.getCategory();
      return this.nameFilter == null ? var1 : Predicates.and(var1, this.nameFilter);
   }

   @Override
   public void selectFirst() {
      this.table.selectFirst();
   }

   @Override
   public boolean isEmpty() {
      return this.table.isEmpty();
   }

   private class ObjectCategoryBinding extends AbstractBinding<ObjectCategory> {
      public ObjectCategoryBinding(ObjectSelectionModel var2) {
         super(var2, "category");
      }

      protected void updateUiWithValue(ObjectCategory var1) {
         ObjectSelectionTableWidget.this.table.setValues(ObjectSelectionTableWidget.this.objectModel.getAvailableObjects());
      }
   }

   private class TableImpl extends SelectionGrid<SObjectSchema> {
      public TableImpl(int var2) {
         super(SObjectSchema.class, var2);
      }

      @Override
      protected List<SObjectSchema> getFilteredList(ImmutableList<SObjectSchema> var1) {
         List var2 = ObjectSelectionTableWidget.this.objectModel.alphabeticalCopy(var1);
         Predicate var3 = ObjectSelectionTableWidget.this.getEffectiveFilter();
         return KongaListUtils.filter(var2, var3);
      }
   }
}
