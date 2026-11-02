package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.etlforge.integration.client.ui.interchange.entity.document.page.SegmentFieldDialog;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.SegmentOccurrence;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableEnumEditor;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

public class SegmentTable extends DocDefTable {
   private final boolean isFixedWidth;
   private SegmentTable.DefineFieldsAction defineFieldsAction;
   private SegmentTable.CopySegmentAction copySegmentAction;

   public SegmentTable(SegmentTableModel var1, boolean var2) {
      super(var1);
      this.isFixedWidth = var2;
      this.getTable().setMaxWidth(SegmentTableModel.OCCURRENCE, 150);
      this.setNameOfDeleteAction(RESOURCES.getString("DeleteSegment"));
   }

   @Override
   protected TableColumnMetaData getColumnToEditWhenNewRowAdded() {
      return SegmentTableModel.NAME;
   }

   @Override
   protected void onDoubleClick(int var1) {
      if (var1 > 0 && var1 < this.getModel().getRowCount()) {
         this.showFieldDialog(var1);
      }
   }

   @Override
   protected JPopupMenu createPopupMenu() {
      JPopupMenu var1 = super.createPopupMenu();
      var1.insert(this.defineFieldsAction, 0);
      var1.add(new JSeparator(), 1);
      return var1;
   }

   @Override
   protected void setEditors(DocDefTableUi var1) {
      this.setOccurrenceEditor(var1);
      this.setParentEditor(var1);
   }

   @Override
   protected DocDefTableUi createTable() {
      return new SegmentTable.TableUi(this);
   }

   @Override
   protected void createActions(DocDefTableUi var1) {
      super.createActions(var1);
      this.defineFieldsAction = new SegmentTable.DefineFieldsAction();
      var1.getSelectionModel().addListSelectionListener(this.defineFieldsAction);
      this.copySegmentAction = new SegmentTable.CopySegmentAction();
      var1.getSelectionModel().addListSelectionListener(this.copySegmentAction);
   }

   @Override
   protected List<JButton> getButtons() {
      List var1 = super.getButtons();
      var1.add(1, new KongaButton(this.defineFieldsAction));
      var1.add(3, new KongaButton(this.copySegmentAction));
      return var1;
   }

   private void showFieldDialog(int var1) {
      Segment var2 = this.getSegment(var1);
      if (var2 != null && SegmentFieldDialog.show(var2, this.isFixedWidth)) {
         this.fireTableContentsChanged();
      }
   }

   private Segment getSegment(int var1) {
      SegmentTableModel var2 = (SegmentTableModel)this.getModel();
      return var2.getSegment(var1);
   }

   private void setOccurrenceEditor(DocDefTableUi var1) {
      var1.setCellEditorFor(SegmentTableModel.OCCURRENCE, new TableEnumEditor(SegmentOccurrence.class));
   }

   private void setParentEditor(DocDefTableUi var1) {
      TableColumn var2 = var1.getColumn(SegmentTableModel.PARENT);
      JComboBox var3 = new JComboBox();
      var3.setMaximumRowCount(16);
      var2.setCellEditor(new SegmentTable.ParentEditor(var3));
   }

   private class CopySegmentAction extends AbstractAction implements ListSelectionListener {
      public CopySegmentAction() {
         super(DocDefTable.RESOURCES.getString("Action.CopySegment"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.copySelectedSegment();
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting()) {
            int[] var2 = SegmentTable.this.getTable().getSelectedRows();
            if (var2.length == 1) {
               this.setEnabled(var2[0] != 0);
            } else {
               this.setEnabled(false);
            }
         }
      }

      private void copySelectedSegment() {
         int[] var1 = SegmentTable.this.getTable().getSelectedRows();
         if (var1.length == 1 && var1[0] > 0) {
            Segment var2 = SegmentTable.this.getSegment(var1[0]);
            Segment var3 = new Segment(var2);
            var3.setName(this.getNameOfNewSegment(var2));
            SegmentTable.this.getModel().addRow(var3);
            int var4 = SegmentTable.this.getModel().getRowCount() - 1;
            SegmentTable.this.getTable().setRowSelectionInterval(var4, var4);
         } else {
            throw new IllegalStateException("Exactly one copyable segment must be selected");
         }
      }

      private String getNameOfNewSegment(Segment var1) {
         String var2 = "Copy_Of_" + var1.getName();
         String var3 = var2;

         for (int var4 = 2; this.isNameTaken(var3); var4++) {
            var3 = var2 + "_" + var4;
         }

         return var3;
      }

      private boolean isNameTaken(String var1) {
         int var2 = 0;

         for (int var3 = SegmentTable.this.getModel().getRowCount(); var2 < var3; var2++) {
            if (var1.equalsIgnoreCase(SegmentTable.this.getSegment(var2).getName())) {
               return true;
            }
         }

         return false;
      }
   }

   private class DefineFieldsAction extends AbstractAction implements ListSelectionListener {
      private int selectedRow;

      public DefineFieldsAction() {
         super(DocDefTable.RESOURCES.getString("Action.DefineFields"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         SegmentTable.this.onDoubleClick(this.selectedRow);
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting()) {
            int[] var2 = SegmentTable.this.getTable().getSelectedRows();
            this.updateState(var2);
         }
      }

      private void updateState(int[] var1) {
         if (var1.length == 1 && var1[0] != 0) {
            this.selectedRow = var1[0];
            this.setEnabled(true);
         } else {
            this.selectedRow = -1;
            this.setEnabled(false);
         }
      }
   }

   private static class ParentEditor extends DefaultCellEditor {
      public ParentEditor(JComboBox var1) {
         super(var1);
      }

      @Override
      public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
         JComboBox var6 = (JComboBox)super.getTableCellEditorComponent(var1, var2, var3, var4, var5);
         this.updateModel(var6, (KongaTable)var1, var4);
         return var6;
      }

      private void updateModel(JComboBox var1, KongaTable var2, int var3) {
         Object var4 = var1.getSelectedItem();
         LinkedHashSet var5 = new LinkedHashSet();

         for (int var6 = 0; var6 < var3; var6++) {
            var5.add(var2.getValueAt(var6, SegmentTableModel.NAME));
         }

         DefaultComboBoxModel var7 = new DefaultComboBoxModel<>(var5.toArray());
         var1.setModel(var7);
         if (var5.contains(var4)) {
            var1.setSelectedItem(var4);
         } else {
            var1.setSelectedIndex(var5.isEmpty() ? -1 : 0);
         }
      }
   }

   private static final class RendererPreparer implements TableRendererPreparer {
      private final TableRendererPreparer firstPreparer;

      public RendererPreparer(TableRendererPreparer var1) {
         this.firstPreparer = var1;
      }

      @Override
      public boolean doDefaultPreparation() {
         return true;
      }

      @Override
      public Component prepare(JTable var1, TableCellRenderer var2, Component var3, int var4, int var5) {
         Component var6 = this.firstPreparer.prepare(var1, var2, var3, var4, var5);
         var6.setEnabled(var4 != 0 || var5 < ((KongaTable)var1).indexOfColumn(SegmentTableModel.PARENT));
         return var6;
      }
   }

   private static final class TableUi extends DocDefTableUi {
      public TableUi(DocDefTable var1) {
         super(var1);
         TableRendererPreparer var2 = this.getRendererPreparer();
         this.setRendererPreparer(new SegmentTable.RendererPreparer(var2));
      }
   }
}
