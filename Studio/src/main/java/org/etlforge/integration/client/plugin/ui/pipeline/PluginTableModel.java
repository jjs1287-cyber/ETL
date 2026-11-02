package org.etlforge.integration.client.plugin.ui.pipeline;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.etlforge.integration.client.plugin.PluginDisplayNameLookup;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.ui.pipeline.applied.AppliedPipelinePluginsModel;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.PluginPositionSelectorModel;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class PluginTableModel extends TypedAbstractKongaTableModel<PluginTableItem> {
   private static final TableColumnMetaData SELECTED = new TableColumnMetaData(" ", Boolean.class, true, 40, false);
   private static final TableColumnMetaData NAME = new TableColumnMetaData(
      Strings.get("PipelinePlugin.Table.Column.Name"), PluginNameTableEntry.class, false, 250
   );
   private static final TableColumnMetaData TYPE = new TableColumnMetaData(Strings.get("PipelinePlugin.Table.Column.Type"), String.class, false, 150);
   private static final TableColumnMetaData POSITION = new TableColumnMetaData(
      Strings.get("PipelinePlugin.Table.Column.Position"), PipelinePosition.class, false, 125
   );
   private final PluginTableModel.ColumnPolicy columnPolicy;
   private PluginTableModel.CheckedSelectionBinding checkedSelectionBinding;

   public static PluginTableModel emptyModel() {
      return new PluginTableModel(new PluginTableModel.StandardColumnPolicy());
   }

   private PluginTableModel(PluginTableModel.ColumnPolicy var1) {
      super(PluginTableItem.class, var1.getColumns());
      this.columnPolicy = var1;
   }

   public static PluginTableModel newAppliedPluginsModel(AppliedPipelinePluginsModel var0) {
      return new PluginTableModel(var0, new PluginTableModel.StandardColumnPolicy());
   }

   private PluginTableModel(AppliedPipelinePluginsModel var1, PluginTableModel.ColumnPolicy var2) {
      this(var2);
      PluginTableModel.AppliedPluginsBinding var3 = new PluginTableModel.AppliedPluginsBinding(var1);
      var3.syncUi();
   }

   public static PluginTableModel newSelectorModel(PluginPositionSelectorModel var0) {
      PluginTableModel.ColumnPolicy var1 = var0.isCheckSelectionEnabled()
         ? new PluginTableModel.CheckBoxSelectColumnPolicy()
         : new PluginTableModel.StandardColumnPolicy();
      return new PluginTableModel(var0, var1);
   }

   private PluginTableModel(PluginPositionSelectorModel var1, PluginTableModel.ColumnPolicy var2) {
      this(var2);
      Bindings var3 = new Bindings(new PluginTableModel.PluginPositionsBinding(var1));
      if (var1.isCheckSelectionEnabled()) {
         this.checkedSelectionBinding = new PluginTableModel.CheckedSelectionBinding(var1);
         var3.add(this.checkedSelectionBinding);
      }

      var3.syncUi();
   }

   public int getIndexOfNameColumn() {
      return this.columnPolicy.getIndexOfNameColumn();
   }

   public int getIndexOfPositionColumn() {
      return this.indexOfColumn(POSITION);
   }

   protected Object[] getColumnDataForTypedObject(PluginTableItem var1) {
      return this.columnPolicy.getColumnDataFromObject(var1);
   }

   @Override
   public void setValueAt(Object var1, int var2, int var3) {
      assert var3 == 0 : "Only the check box column should be editable";
      super.setValueAt(var1, var2, var3);
      if (this.checkedSelectionBinding != null && this.checkedSelectionBinding.listenToUi) {
         PipelinePosition var4 = this.getRowObjectAt(var2).getPipelinePosition();
         boolean var5 = (Boolean)var1;
         this.checkedSelectionBinding.selectionChanged(var4, var5);
      }
   }

   private void setCheckedSelection(Set<PipelinePosition> var1) {
      Preconditions.checkState(this.getColumnDescriptor(0) == SELECTED, "This model does not support check-selection");
      int var2 = 0;

      for (int var3 = this.getRowCount(); var2 < var3; var2++) {
         PipelinePosition var4 = this.getRowObjectAt(var2).getPipelinePosition();
         this.setValueAt(var1.contains(var4), var2, 0);
      }
   }

   private static String getTypeString(PluginTableItem var0) {
      return var0.isGlobal() ? Strings.get("PipelinePlugin.Table.Type.Global") : Strings.get("PipelinePlugin.Table.Type.Optional");
   }

   private class AppliedPluginsBinding implements PropertyChangeListener {
      private final AppliedPipelinePluginsModel model;

      public AppliedPluginsBinding(AppliedPipelinePluginsModel var2) {
         this.model = var2;
         var2.addPropertyChangeListener("plugins", this);
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.syncUi();
      }

      public void syncUi() {
         final List var1 = this.model.getPlugins();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               PluginTableModel.this.deleteAll();

               for (Object __etlItem129 : var1) {
                  AppliedPipelinePlugin var2 = (AppliedPipelinePlugin) __etlItem129;
                  PluginTableModel.this.addRow(new PluginTableItem(var2));
               }
            }
         });
      }
   }

   private static class CheckBoxSelectColumnPolicy implements PluginTableModel.ColumnPolicy {
      private CheckBoxSelectColumnPolicy() {
      }

      @Override
      public int getIndexOfNameColumn() {
         return 1;
      }

      @Override
      public Object[] getColumnDataFromObject(PluginTableItem var1) {
         return new Object[]{Boolean.FALSE, new PluginNameTableEntry(var1), PluginTableModel.getTypeString(var1), var1.getPipelinePosition()};
      }

      @Override
      public TableColumnMetaData[] getColumns() {
         return new TableColumnMetaData[]{PluginTableModel.SELECTED, PluginTableModel.NAME, PluginTableModel.TYPE, PluginTableModel.POSITION};
      }
   }

   private class CheckedSelectionBinding implements Binding, PropertyChangeListener {
      private final PluginPositionSelectorModel selectorModel;
      private boolean listenToModel = true;
      private boolean listenToUi = true;

      public CheckedSelectionBinding(PluginPositionSelectorModel var2) {
         this.selectorModel = var2;
         var2.addPropertyChangeListener("selectedPluginPositions", this);
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "selectedPluginPositions".equals(var1.getPropertyName());
         if (this.listenToModel) {
            this.syncUi();
         }
      }

      @Override
      public void syncUi() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckedSelectionBinding.this.listenToUi = false;
               PluginTableModel.this.setCheckedSelection(CheckedSelectionBinding.this.selectorModel.getSelectedPluginPositions());
               CheckedSelectionBinding.this.listenToUi = true;
            }
         });
      }

      public void selectionChanged(PipelinePosition var1, boolean var2) {
         if (this.listenToUi) {
            this.listenToModel = false;
            this.selectorModel.setPluginPositionSelected(var1, var2);
            this.listenToModel = true;
         }
      }

      @Override
      public void setUiToModelEnabled(boolean var1) {
         this.listenToUi = var1;
      }

      @Override
      public void setModelToUiEnabled(boolean var1) {
         this.listenToModel = var1;
      }

      @Override
      public void dispose() {
         this.selectorModel.removePropertyChangeListener("selectedPluginPositions", this);
      }

      @Override
      public void syncModel() {
         throw new UnsupportedOperationException();
      }
   }

   private interface ColumnPolicy {
      int getIndexOfNameColumn();

      TableColumnMetaData[] getColumns();

      Object[] getColumnDataFromObject(PluginTableItem var1);
   }

   private class PluginPositionsBinding extends AbstractBinding<List<PipelinePosition>> {
      private final PluginPositionSelectorModel selectorModel;

      public PluginPositionsBinding(PluginPositionSelectorModel var2) {
         super(var2.getProperty("pluginPositions"));
         this.selectorModel = var2;
      }

      protected void updateUiWithValue(List<PipelinePosition> var1) {
         PluginTableModel.this.deleteAll();
         PluginTableModel.this.addRows(this.createRows(var1));
         if (this.selectorModel.isCheckSelectionEnabled()) {
            this.updateCheckedSelections();
         }
      }

      private Collection<PluginTableItem> createRows(List<PipelinePosition> var1) {
         PluginDisplayNameLookup var2 = this.selectorModel.getNameLookup();
         TreeSet var3 = new TreeSet();

         for (PipelinePosition var5 : var1) {
            PluginIdentifier var6 = var5.getPluginId();
            var3.add(new PluginTableItem(var5, var2.getDisplayName(var6)));
         }

         return var3;
      }

      private void updateCheckedSelections() {
         if (this.selectorModel.isCheckSelectionEnabled()) {
            PluginTableModel.this.setCheckedSelection(this.selectorModel.getSelectedPluginPositions());
         }
      }
   }

   private static class StandardColumnPolicy implements PluginTableModel.ColumnPolicy {
      private StandardColumnPolicy() {
      }

      @Override
      public int getIndexOfNameColumn() {
         return 0;
      }

      @Override
      public Object[] getColumnDataFromObject(PluginTableItem var1) {
         return new Object[]{new PluginNameTableEntry(var1), PluginTableModel.getTypeString(var1), var1.getPipelinePosition()};
      }

      @Override
      public TableColumnMetaData[] getColumns() {
         return new TableColumnMetaData[]{PluginTableModel.NAME, PluginTableModel.TYPE, PluginTableModel.POSITION};
      }
   }
}
