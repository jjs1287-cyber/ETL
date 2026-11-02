package org.etlforge.integration.client.ui.structure.editor.db;

import javax.swing.Action;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.label.UiProviderLabel;
import org.etlforge.ui.widget.list.ListFilter;

final class SingleTableSelectionWidget implements TableSelectionWidget {
   private final TableSelectionModel model;
   private final AvailableTablesGrid availableTables;

   public SingleTableSelectionWidget(TableSelectionModel var1) {
      this.model = var1;
      this.availableTables = new AvailableTablesGrid(var1, 3);
      this.availableTables.addSelectionListener(new SingleTableSelectionWidget.SelectionHandler());
   }

   @Override
   public void setDefaultAction(Action var1) {
      this.availableTables.setDefaultAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JLabel var2 = this.createLabel();
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 3);
      var3.north(var2).center(this.availableTables);
      var3.decorate(var1);
   }

   private JLabel createLabel() {
      KongaLabel var1 = UiProviderLabel.create(PackageResources.ObjectSelectionPage.Labels.AVAILABLE_ITEMS, this.availableTables);
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var1);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.availableTables.requestFocus();
   }

   @Override
   public Receiver<ListFilter> getFilterReceiver() {
      return this.availableTables;
   }

   private class SelectionHandler implements SelectionListener {
      private SelectionHandler() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         OrderedSelection var2 = (OrderedSelection)var1.getSelection();
         SelectedTables var3 = new SelectedTables();
         if (!var2.isEmpty()) {
            DatabaseObject var4 = (DatabaseObject)var2.head();
            var3.setTableUsage(var4, 1);
         }

         SingleTableSelectionWidget.this.model.setUsageCount(var3);
      }
   }
}
