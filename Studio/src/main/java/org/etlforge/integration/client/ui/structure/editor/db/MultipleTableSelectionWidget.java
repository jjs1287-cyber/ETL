package org.etlforge.integration.client.ui.structure.editor.db;

import java.awt.Dimension;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.label.UiProviderLabel;
import org.etlforge.ui.widget.list.ListFilter;

final class MultipleTableSelectionWidget implements TableSelectionWidget {
   private final TableSelectionModel model;
   private final AvailableTablesList availableTables;
   private final TableUseCountTable selectedTables;
   private final SelectionAction selectTablesAction;
   private final SelectionAction removeTablesAction;

   public MultipleTableSelectionWidget(TableSelectionModel var1) {
      this.model = var1;
      this.availableTables = new AvailableTablesList(var1);
      this.availableTables.setDisableSelectedItems(true);
      this.selectedTables = new TableUseCountTable(var1);
      this.selectTablesAction = new SelectTablesAction(var1);
      this.removeTablesAction = new MultipleTableSelectionWidget.RemoveTables();
      this.availableTables.setDefaultAction(this.selectTablesAction);
      this.selectedTables.installAction(this.removeTablesAction);
   }

   @Override
   public void setDefaultAction(Action var1) {
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = new NColumnPanel(3);
      var2.setInsets(0, 0, 2, 0);
      var2.setXWeights(0.5, 0.0, 0.5);
      KongaLabel var3 = createLabel(PackageResources.ObjectSelectionPage.Labels.AVAILABLE_ITEMS, this.availableTables);
      KongaLabel var4 = createLabel(PackageResources.ObjectSelectionPage.Labels.SELECTED_ITEMS, this.selectedTables);
      var2.addRow(var3, null, var4);
      var2.setYWeights(1.0, 1.0, 1.0);
      var2.setFills(GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.VERTICAL, GridBagLayoutPanel.Fill.BOTH);
      var2.setInsets(1, 0, 10, 0, 10);
      var2.addRow(wrap(this.availableTables), this.layoutButtons(), wrap(this.selectedTables));
      var2.decorate(var1);
   }

   private static JComponent wrap(UiProvider var0) {
      JPanel var1 = BorderLayoutBuilder.fillWith(var0);
      var1.setPreferredSize(new Dimension(20, 20));
      return var1;
   }

   private static KongaLabel createLabel(String var0, UiProvider var1) {
      KongaLabel var2 = UiProviderLabel.create(var0, var1);
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var2);
      return var2;
   }

   private JComponent layoutButtons() {
      BoxBuilder var1 = BoxBuilder.vertical();
      var1.glue().addAllWithSpace(10, this.selectTablesAction, this.removeTablesAction).glue();
      return var1.container();
   }

   @Override
   public void requestFocus() {
      this.availableTables.requestFocus();
   }

   @Override
   public Receiver<ListFilter> getFilterReceiver() {
      return this.availableTables;
   }

   private class RemoveTables extends AbstractSingleTypeSelectionAction<DatabaseObject> {
      public RemoveTables() {
         super(DatabaseObject.class, null, UiResourceBundle.getIcon("LeftArrow"));
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
      }

      @Override
      protected void run(List<DatabaseObject> var1) {
         MultipleTableSelectionWidget.this.model.removeTables(var1);
      }
   }
}
