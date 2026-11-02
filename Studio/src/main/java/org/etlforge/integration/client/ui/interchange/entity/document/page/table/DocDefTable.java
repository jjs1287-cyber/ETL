package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.Container;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentPage;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.util.PropertyChangeManager;
import org.etlforge.util.event.PropertyChangeEventSender;

public abstract class DocDefTable implements PropertyChangeEventSender, TableModelListener {
   public static final String TABLE_CONTENT_PROPERTY = "DocDefTable#TableContents";
   protected static final ResourceBundle RESOURCES = TextDocumentPage.getResources();
   private final DocDefTableUi ui;
   private final DocDefTableModel model;
   private Action addNewAction;
   private Action deleteAction;
   private Action moveUpAction;
   private Action moveDownAction;
   private final JPopupMenu popupMenu;
   private final int visibleRowRount;
   private final PropertyChangeManager propChangeManager = new PropertyChangeManager(this);

   public DocDefTable(DocDefTableModel var1) {
      this.model = var1;
      this.visibleRowRount = 4;
      this.ui = this.createTable();
      this.createActions(this.ui);
      this.popupMenu = this.createPopupMenu();
      var1.addTableModelListener(this);
   }

   public final JComponent getDisplayer() {
      return CaptionLayout.of(this.getButtonPanel(), this.ui).deeplyNonOpaque().container();
   }

   public final void selectFirstRow() {
      this.ui.selectFirstRow();
   }

   public final void requestFocus() {
      this.ui.requestFocusInWindow();
   }

   public final void stopEditing() {
      if (this.ui.isEditing()) {
         this.ui.getCellEditor().stopCellEditing();
      }
   }

   protected DocDefTableUi createTable() {
      return new DocDefTableUi(this);
   }

   protected final KongaTable getTable() {
      return this.ui;
   }

   protected final DocDefTableModel getModel() {
      return this.model;
   }

   protected final void setNameOfDeleteAction(String var1) {
      this.deleteAction.putValue("Name", var1);
   }

   protected abstract TableColumnMetaData getColumnToEditWhenNewRowAdded();

   protected void setEditors(DocDefTableUi var1) {
   }

   protected void createActions(DocDefTableUi var1) {
      TableActions var2 = new TableActions(this);
      this.createAddNewAction(var2);
      this.createDeleteAction(var2);
      this.createMoveUpAction(var2);
      this.createMoveDownAction(var2);
   }

   private void createAddNewAction(TableActions var1) {
      this.addNewAction = var1.getAddNew();
   }

   private void createDeleteAction(TableActions var1) {
      this.deleteAction = var1.getDelete();
      this.deleteAction.setEnabled(false);
   }

   private void createMoveUpAction(TableActions var1) {
      this.moveUpAction = var1.getMoveUp();
      this.moveUpAction.setEnabled(false);
   }

   private void createMoveDownAction(TableActions var1) {
      this.moveDownAction = var1.getMoveDown();
      this.moveDownAction.setEnabled(false);
   }

   private Container getButtonPanel() {
      Box var1 = Box.createHorizontalBox();
      List var2 = this.getButtons();

      for (int var3 = 0; var3 < var2.size(); var3++) {
         JButton var4 = (JButton)var2.get(var3);
         if (var4 == null) {
            var1.add(Box.createHorizontalGlue());
         } else {
            if (var3 > 0) {
               var1.add(Box.createHorizontalStrut(10));
            }

            var1.add(var4);
         }
      }

      return var1;
   }

   protected List<JButton> getButtons() {
      Action[] var1 = new Action[]{this.addNewAction, null, this.moveUpAction, this.moveDownAction, this.deleteAction};
      return KongaListUtils.transform(var1, ButtonUtils.ACTION_TO_BUTTON);
   }

   @Override
   public final void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeManager.addPropertyChangeListener(var1);
   }

   @Override
   public final void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeManager.removePropertyChangeListener(var1);
   }

   @Override
   public final void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeManager.addPropertyChangeListener(var1, var2);
   }

   @Override
   public final void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeManager.removePropertyChangeListener(var1, var2);
   }

   @Override
   public final void tableChanged(TableModelEvent var1) {
      this.fireTableContentsChanged();
   }

   protected final void fireTableContentsChanged() {
      this.propChangeManager.firePropertyChange("DocDefTable#TableContents", "0", "1");
   }

   protected void onAddNew() {
      this.model.addRow(this.model.createNewObject());
      this.ui.clearSelection();
      int var1 = this.ui.getRowCount() - 1;
      this.ui.scrollRectToVisible(this.ui.getCellRect(var1, 0, false));
      TableColumnMetaData var2 = this.getColumnToEditWhenNewRowAdded();
      if (this.ui.editCellAt(var1, var2)) {
         int var3 = this.ui.indexOfColumn(var2);
         this.ui.changeSelection(var1, var3, false, false);
      }

      this.requestFocus();
   }

   protected void onDelete() {
      int[] var1 = this.ui.getSelectedRows();
      this.stopEditing();
      this.deleteRows(var1);
   }

   private void deleteRows(int[] var1) {
      this.model.startCompoundEdit();

      for (int var2 = var1.length - 1; var2 >= 0; var2--) {
         if (this.model.canRowBeDeleted(var1[var2])) {
            this.model.deleteRow(var1[var2]);
         }
      }

      this.model.stopCompoundEdit();
   }

   protected void onDoubleClick(int var1) {
   }

   protected JPopupMenu createPopupMenu() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      var1.addActions(this.moveUpAction, this.moveDownAction, null, this.deleteAction);
      return var1;
   }

   final JPopupMenu getPopupMenu() {
      return this.popupMenu;
   }

   final Action getDeleteAction() {
      return this.deleteAction;
   }

   public final Action getAddNewAction() {
      return this.addNewAction;
   }

   final void registerKeyBindings(DocDefTableUi var1) {
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1);
      var2.addBinding(KeyUtils.getCtrlDown(78), "add", this.addNewAction);
      var2.addBinding(KeyUtils.getCtrlDown(85), "up", this.moveUpAction);
      var2.addBinding(KeyUtils.getCtrlDown(68), "down", this.moveDownAction);
      var2.install();
   }

   final int getVisibleRows() {
      return this.visibleRowRount;
   }

   public final int[] getSelectedRows() {
      return this.ui.getSelectedRows();
   }
}
