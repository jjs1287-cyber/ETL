package org.etlforge.integration.client.ui.structure.editor.db;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;

final class AvailableTablesList implements UiProvider, Receiver<ListFilter> {
   private final TableSelectionModel selectionModel;
   private FilteredListModel filteredListModel;
   private final KongaList list;
   private ListFilter filter;
   private boolean disableSelectedItems;

   public AvailableTablesList(TableSelectionModel var1) {
      this.selectionModel = var1;
      this.filteredListModel = new FilteredListModel(new DefaultListModel());
      this.list = this.createList();
      this.bindProperties();
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(4);
      var1.setCellRenderer(new AvailableTablesList.Renderer());
      var1.setSelectionConverter(new SelectionConverter() {
         @Override
         public Object toSelection(Object var1) {
            return var1;
         }

         @Override
         public Object fromSelection(Object var1) {
            return var1 instanceof DatabaseObject ? var1 : null;
         }
      });
      return var1.newList(this.filteredListModel);
   }

   private void bindProperties() {
      new AvailableTablesList.AvailableObjectsBinding();
      this.selectionModel.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            AvailableTablesList.this.list.repaint();
         }
      });
   }

   public void setDisableSelectedItems(boolean var1) {
      this.disableSelectedItems = var1;
   }

   public void handle(ListFilter var1) {
      this.filteredListModel.removeFilter(this.filter);
      this.filteredListModel.addFilter(var1);
      this.filter = var1;
   }

   public void addSelectionListener(SelectionListener var1) {
      this.list.addSelectionListener(var1);
   }

   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
      if (this.list.getSelection().isEmpty()) {
         this.list.selectFirst();
      }
   }

   private class AvailableObjectsBinding extends AbstractBinding<DatabaseObjectContainer[]> {
      public AvailableObjectsBinding() {
         super(AvailableTablesList.this.selectionModel, "availab...");
      }

      protected void updateUiWithValue(DatabaseObjectContainer[] var1) {
         this.createNewModel(var1);
         AvailableTablesList.this.list.setModel(AvailableTablesList.this.filteredListModel);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AvailableTablesList.this.requestFocus();
            }
         });
      }

      private void createNewModel(DatabaseObjectContainer[] var1) {
         ObjectListPopulator var2 = new ObjectListPopulator(var1);
         ListModel var3 = var2.getListModel();
         AvailableTablesList.this.filteredListModel = new FilteredListModel(var3);
         if (AvailableTablesList.this.filter != null) {
            AvailableTablesList.this.filteredListModel.addFilter(AvailableTablesList.this.filter);
         }
      }
   }

   private class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         this.setForeground(var4 ? var1.getSelectionForeground() : var1.getForeground());
         if (var2 instanceof DatabaseObject) {
            DatabaseObject var6 = (DatabaseObject)var2;
            String var7 = AvailableTablesList.this.selectionModel.isSchemaNameIncluded()
               ? AvailableTablesList.this.selectionModel.getQualifiedName(var6)
               : var6.getName();
            this.setText(var7);
            this.setToolTipText(var7);
            BorderBuilder var8 = new BorderBuilder().empty(0, 10, 0, 0).add(this.getBorder());
            this.setBorder(var8);
            if (AvailableTablesList.this.disableSelectedItems && AvailableTablesList.this.selectionModel.isTableIncluded(var6)) {
               this.setForeground(Color.LIGHT_GRAY);
            }
         } else {
            this.setFont(Fonts.bold(var1));
         }

         return this;
      }
   }
}
