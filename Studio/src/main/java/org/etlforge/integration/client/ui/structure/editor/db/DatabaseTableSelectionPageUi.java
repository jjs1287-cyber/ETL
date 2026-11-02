package org.etlforge.integration.client.ui.structure.editor.db;

import javax.swing.Action;
import javax.swing.JCheckBox;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class DatabaseTableSelectionPageUi implements UiProvider {
   private final TableSelectionModel model;
   private final KongaButton downloadButton;
   private final TableFilterDefiner filterDefiner;
   private final TableSelectionWidget selectionWidget;
   private final JCheckBox includeSchemaBox;
   private Action refreshColumnsAction;

   public DatabaseTableSelectionPageUi(TableSelectionModel var1, Action var2, boolean var3) {
      this.model = var1;
      this.downloadButton = new KongaButton(var2);
      this.filterDefiner = new TableFilterDefiner(var1);
      this.filterDefiner.setFieldListener(var2);
      this.selectionWidget = this.createSelectionWidget(var3);
      this.filterDefiner.setListFilterReceiver(this.selectionWidget.getFilterReceiver());
      this.includeSchemaBox = this.createSchemaSwitch();
      this.bindProperties();
   }

   private TableSelectionWidget createSelectionWidget(boolean var1) {
      return var1 ? new SingleTableSelectionWidget(this.model) : new MultipleTableSelectionWidget(this.model);
   }

   private JCheckBox createSchemaSwitch() {
      KongaCheckBox var1 = new KongaCheckBox(PackageResources.ObjectSelectionPage.Schema.SWITCH_TEXT);
      var1.setOpaque(false);
      return var1;
   }

   private void bindProperties() {
      BooleanBinding var1 = BooleanBinding.bind(this.model, "merry", this.includeSchemaBox);
      var1.syncUi();
   }

   public void setDefaultAction(Action var1) {
      this.selectionWidget.setDefaultAction(var1);
   }

   public void setRefreshColumnsAction(Action var1) {
      this.refreshColumnsAction = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      DatabaseTableSelectionPageUi.Layout var2 = new DatabaseTableSelectionPageUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.downloadButton.requestFocusInWindow();
   }

   private class Layout {
      private final OneColumnPanel components = new OneColumnPanel();

      public Layout() {
         this.components.setWeightX(1.0);
         this.components.withInsets(0, 0, 10, 0);
         this.layoutDownloadButtonAndFilterField();
         this.components.add(DatabaseTableSelectionPageUi.this.includeSchemaBox);
         this.layoutSelectionWidget();
         this.layoutRefreshColumnsButton();
      }

      private void layoutDownloadButtonAndFilterField() {
         TwoColumnPanel var1 = TwoColumnPanel.forFormEntry(10, 0);
         var1.addRow(UiProviderAdapter.of(DatabaseTableSelectionPageUi.this.downloadButton), DatabaseTableSelectionPageUi.this.filterDefiner);
         this.components.add(var1);
      }

      private void layoutSelectionWidget() {
         this.components.fillOut(DatabaseTableSelectionPageUi.this.selectionWidget);
      }

      private void layoutRefreshColumnsButton() {
         if (DatabaseTableSelectionPageUi.this.refreshColumnsAction != null) {
            this.components.setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
            this.components.setWeightY(0.0);
            BoxBuilder var1 = BoxBuilder.horizontal();
            var1.glue().add(DatabaseTableSelectionPageUi.this.refreshColumnsAction);
            this.components.add(var1);
         }
      }

      public void decorate(UiCanvas var1) {
         this.components.decorate(var1);
      }
   }
}
