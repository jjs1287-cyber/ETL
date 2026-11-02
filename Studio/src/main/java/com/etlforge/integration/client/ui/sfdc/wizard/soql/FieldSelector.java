package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.collect.Sets;
import com.etlforge.integration.salesforce.ui.SchemaEntityNameFilterFunction;
import com.etlforge.integration.salesforce.ui.renderer.Renderers;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.border.TitledBorder;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.SelectorTable;
import org.etlforge.ui.widget.SelectorTableFilterField;

final class FieldSelector implements UiProvider {
   private final QueryBuilderModel model;
   private final TitledBorder border = new TitledBorder("Fields");
   private final SelectorTable<SObjectSchema.Field> fieldList;
   private final SelectorTableFilterField<SObjectSchema.Field> filterField;

   public FieldSelector(QueryBuilderModel var1) {
      this.model = var1;
      this.fieldList = this.createFieldList(var1);
      this.filterField = this.createFilterField();
      this.bindToModel(var1);
   }

   private SelectorTable<SObjectSchema.Field> createFieldList(QueryBuilderModel var1) {
      SelectorTable var2 = new SelectorTable<>(var1.getAllFields(), SObjectSchema.Field.class);
      var2.setVisibleRows(10);
      var2.setValueWidth(200);
      var2.setObjectCellRenderer(Renderers.forTable(var1.getFieldNameDisplayMode()));
      return var2;
   }

   private SelectorTableFilterField<SObjectSchema.Field> createFilterField() {
      SchemaEntityNameFilterFunction var1 = new SchemaEntityNameFilterFunction(this.model.getFieldNameDisplayMode());
      return SelectorTableFilterField.forTable(this.fieldList, var1).withLabel("&Filter: ");
   }

   private void bindToModel(QueryBuilderModel var1) {
      FieldSelector.FieldBinding var2 = new FieldSelector.FieldBinding(var1);
      this.fieldList.asSelectionSource().addSelectionListener(var2);
      var2.syncUi();
      new FieldSelector.ObjectBinding(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Vertical.borderLayout(5).of(this.filterField, this.fieldList, this.layoutButtons()).withBorder(this.border).decorate(var1);
   }

   private UiProvider layoutButtons() {
      FieldSelector.SelectAllAction var1 = new FieldSelector.SelectAllAction();
      FieldSelector.RemoveAllAction var2 = new FieldSelector.RemoveAllAction();
      Grid var3 = Horizontal.grid().of(var1, var2).withHGap(10);
      return Horizontal.Align.east(var3);
   }

   @Override
   public void requestFocus() {
      this.fieldList.requestFocus();
   }

   private class FieldBinding extends AbstractBinding<Set<SObjectSchema.Field>> implements SelectionListener {
      public FieldBinding(QueryBuilderModel var2) {
         super(var2, "fields");
      }

      protected void updateUiWithValue(Set<SObjectSchema.Field> var1) {
         FieldSelector.this.fieldList.setSelectedObjects(var1);
      }

      @Override
      protected void disposeUiListener() {
         FieldSelector.this.fieldList.asSelectionSource().removeSelectionListener(this);
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         if (this.isUiToModelEnabled()) {
            this.syncModel();
         }
      }

      protected Set<SObjectSchema.Field> getValueFromUi() {
         return Sets.newHashSet(FieldSelector.this.fieldList.getSelectedObjects());
      }
   }

   private class ObjectBinding extends AbstractBinding<SObjectSchema> {
      public ObjectBinding(QueryBuilderModel var2) {
         super(var2, "object");
      }

      protected void updateUiWithValue(SObjectSchema var1) {
         String var2 = FieldSelector.this.model.getObjectNameDisplayMode().getDisplayName(var1);
         FieldSelector.this.border.setTitle(var2 + " fields");
         FieldSelector.this.fieldList.replaceValues(FieldSelector.this.model.getAllFields());
      }
   }

   private class RemoveAllAction extends AbstractAction {
      public RemoveAllAction() {
         super("&Remove All");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (FieldSelector.this.filterField.isEmpty()) {
                  FieldSelector.this.model.removeAllFields();
               } else {
                  FieldSelector.this.fieldList.setInitialSelectionState(false);
               }
            }
         });
      }
   }

   private class SelectAllAction extends AbstractAction {
      public SelectAllAction() {
         super("Select &All");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (FieldSelector.this.filterField.isEmpty()) {
                  FieldSelector.this.model.selectAllFields();
               } else {
                  FieldSelector.this.fieldList.setInitialSelectionState(true);
               }
            }
         });
      }
   }
}
