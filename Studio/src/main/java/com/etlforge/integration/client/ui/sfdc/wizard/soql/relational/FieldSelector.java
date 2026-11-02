package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.ChildToParentField;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderModel;
import com.etlforge.integration.salesforce.ui.SchemaEntityNameFilterFunction;
import com.etlforge.integration.salesforce.ui.renderer.Renderers;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.SelectorTable;
import org.etlforge.ui.widget.SelectorTableFilterField;
import org.etlforge.util.Property;

final class FieldSelector implements UiProvider {
   private final RelationalFieldSelectorModel model;
   private final SelectorTable<SObjectSchema.Field> fieldList;
   private final SelectorTableFilterField<SObjectSchema.Field> filterField;
   private Optional<Binding> fieldBinding = Optional.absent();

   public FieldSelector(RelationalFieldSelectorModel var1) {
      this.model = var1;
      this.fieldList = this.createFieldList(var1);
      this.filterField = this.createFilterField();
      this.bindToModel(var1);
   }

   private SelectorTable<SObjectSchema.Field> createFieldList(RelationalFieldSelectorModel var1) {
      SelectorTable var2 = new SelectorTable<>(Collections.EMPTY_LIST, SObjectSchema.Field.class);
      var2.setVisibleRows(6);
      var2.setValueWidth(200);
      var2.setObjectCellRenderer(Renderers.forTable(var1.getFieldNameDisplayMode()));
      return var2;
   }

   private SelectorTableFilterField<SObjectSchema.Field> createFilterField() {
      SchemaEntityNameFilterFunction var1 = new SchemaEntityNameFilterFunction(this.model.getFieldNameDisplayMode());
      return SelectorTableFilterField.forTable(this.fieldList, var1).withLabel("&Filter: ");
   }

   private void bindToModel(RelationalFieldSelectorModel var1) {
      FieldSelector.ObjectBinding var2 = new FieldSelector.ObjectBinding(var1);
      var2.syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      new BorderLayoutBuilder(0, 5).center(this.fieldList).south(this.layoutFilterAndButtons()).decorate(var1);
   }

   private UiProvider layoutFilterAndButtons() {
      return Vertical.box().leftAligned().addAllWithSpace(5, this.filterField, this.layoutButtons());
   }

   private UiProvider layoutButtons() {
      Action var1 = this.getSelectAllAction();
      Action var2 = this.getRemoveAllAction();
      Grid var3 = Horizontal.grid().of(var1, var2).withHGap(10);
      return Horizontal.Align.east(var3);
   }

   private Action getSelectAllAction() {
      Action var1 = this.fieldList.getSelectAllAction();
      var1.putValue("Name", "Select &All");
      return var1;
   }

   private Action getRemoveAllAction() {
      Action var1 = this.fieldList.getUnselectAllAction();
      var1.putValue("Name", "&Remove All");
      return var1;
   }

   @Override
   public void requestFocus() {
      this.fieldList.requestFocus();
   }

   private abstract class AbstractFieldBinding<T> extends AbstractBinding<Set<T>> implements SelectionListener {
      public AbstractFieldBinding(Property<Set<T>> var2) {
         super(var2);
         FieldSelector.this.fieldList.asSelectionSource().addSelectionListener(this);
      }

      @Override
      public final void selectionChanged(SelectionChangedEvent var1) {
         if (this.isUiToModelEnabled()) {
            this.syncModel();
         }
      }

      @Override
      protected final void disposeUiListener() {
         FieldSelector.this.fieldList.asSelectionSource().removeSelectionListener(this);
      }
   }

   private class ChildToParentFieldBinding extends FieldSelector.AbstractFieldBinding<ChildToParentField> {
      private final ParentRelationship parent;

      public ChildToParentFieldBinding(ParentRelationship var2, Property<Set<ChildToParentField>> var3) {
         super(var3);
         this.parent = var2;
      }

      protected void updateUiWithValue(Set<ChildToParentField> var1) {
         HashSet var2 = Sets.newHashSet();

         for (ChildToParentField var4 : var1) {
            if (this.parent.isOwnerOf(var4)) {
               String var5 = var4.getName();
               Optional var6 = this.parent.getField(var5);
               if (var6.isPresent()) {
                  var2.add(var6.get());
               }
            }
         }

         FieldSelector.this.fieldList.setSelectedObjects(var2);
      }

      protected Set<ChildToParentField> getValueFromUi() {
         HashSet var1 = Sets.newHashSet();

         for (SObjectSchema.Field var3 : FieldSelector.this.fieldList.getSelectedObjects()) {
            var1.add(this.parent.getChildToParentField(var3.getName()));
         }

         return var1;
      }
   }

   private class FieldBinding extends FieldSelector.AbstractFieldBinding<SObjectSchema.Field> {
      public FieldBinding(Property<Set<SObjectSchema.Field>> var2) {
         super(var2);
      }

      protected void updateUiWithValue(Set<SObjectSchema.Field> var1) {
         FieldSelector.this.fieldList.setSelectedObjects(var1);
      }

      protected Set<SObjectSchema.Field> getValueFromUi() {
         return Sets.newHashSet(FieldSelector.this.fieldList.getSelectedObjects());
      }
   }

   private class ObjectBinding extends AbstractBinding<FieldOwner> {
      public ObjectBinding(RelationalFieldSelectorModel var2) {
         super(var2, "selectedObject");
      }

      protected void updateUiWithValue(FieldOwner var1) {
         this.updateFieldList(var1);
         this.installFieldBinding(var1);
      }

      private void updateFieldList(FieldOwner var1) {
         List var2 = FieldSelector.this.model.alphabeticalOrder(var1.getFields());
         FieldSelector.this.fieldList.replaceValues(var2);
      }

      private void installFieldBinding(FieldOwner var1) {
         if (FieldSelector.this.fieldBinding.isPresent()) {
            FieldSelector.this.fieldBinding.get().dispose();
         }

         Binding var2 = this.createNewFieldBinding(var1, FieldSelector.this.model.getQueryBuilderModel());
         if (var2 != null) {
            var2.syncUi();
         }

         FieldSelector.this.fieldBinding = Optional.fromNullable(var2);
      }

      private Binding createNewFieldBinding(FieldOwner var1, QueryBuilderModel var2) {
         if (var1 instanceof RootObject) {
            Property var6 = var2.getProperty("fields");
            return FieldSelector.this.new FieldBinding(var6);
         } else if (var1 instanceof ChildRelationship) {
            Property var5 = var2.getNestedFieldsProperty(var1.getName());
            return FieldSelector.this.new FieldBinding(var5);
         } else {
            assert var1 instanceof ParentRelationship;
            ParentRelationship var3 = (ParentRelationship)var1;
            Property var4 = var2.getChildToParentFieldsProperty(var3);
            return FieldSelector.this.new ChildToParentFieldBinding(var3, var4);
         }
      }
   }
}
