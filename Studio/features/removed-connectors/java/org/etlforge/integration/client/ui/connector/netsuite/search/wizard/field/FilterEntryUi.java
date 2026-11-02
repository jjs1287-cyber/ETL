package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.EnumSet;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.QueryBuilderModel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.Property;

public final class FilterEntryUi implements UiProvider {
   private final EnumComboBox<NetSuiteSearchOperator> operatorBox = EnumComboBox.of(EnumSet.noneOf(NetSuiteSearchOperator.class));
   private final JComponent layout;
   private final BorderLayoutBuilder valueEntryPanel = new BorderLayoutBuilder().withBorder(StyleSheet.getDefaultTitleBorder(null));
   private final FilterEntryUi.AddFilterAction addFilterAction;
   private final TwoStringsValueUi disabledValueUi = new TwoStringsValueUi();
   private ValueUi valueUi;

   public FilterEntryUi(QueryBuilderModel var1) {
      this.operatorBox.setMaximumRowCount(16);
      this.operatorBox.setPrototypeDisplayValue(NetSuiteSearchOperator.notGreaterThanOrEqualTo);
      this.addFilterAction = new FilterEntryUi.AddFilterAction(var1);
      this.disabledValueUi.setEnabled(false);
      this.valueEntryPanel.center(this.disabledValueUi);
      this.layout = this.createLayout();
      this.bindWithModel(var1);
   }

   private JComponent createLayout() {
      KongaButton var1 = new KongaButton(this.addFilterAction);
      WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeOver(var1);
      NColumnPanel var2 = new NColumnPanel(3);
      var2.setAnchors(GridBagLayoutPanel.Anchor.WEST, GridBagLayoutPanel.Anchor.WEST, GridBagLayoutPanel.Anchor.EAST);
      var2.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE);
      var2.setXWeights(0.0, 1.0, 0.0);
      var2.setYWeights(0.0, 0.0, 0.0);
      var2.setInsets(0, 0, 10, 8);
      var2.addRow("Operator:", InvisiblePanel.of(this.operatorBox), var1);
      var2.setAnchors(GridBagLayoutPanel.Anchor.NORTHWEST, GridBagLayoutPanel.Anchor.NORTHWEST, GridBagLayoutPanel.Anchor.NORTHEAST);
      var2.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.NONE);
      var2.setXWeights(0.0, 1.0, 0.0);
      var2.setYWeights(0.0, 1.0, 0.0);
      var2.setInsets(0, 0, 0, 8);
      var2.add("Value(s):");
      var2.add(this.valueEntryPanel, 2);
      return var2.container();
   }

   private void bindWithModel(QueryBuilderModel var1) {
      Property var2 = var1.getProperty("operators");
      Property var3 = var1.getProperty("selectedOperator");
      ComboBoxBinding var4 = new ComboBoxBinding(this.operatorBox, var2, var3);
      Bindings.of(var4, new FilterEntryUi.SelectedFieldBinding(var1), new FilterEntryUi.SelectedFilterBinding(var1)).syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.layout);
   }

   @Override
   public void requestFocus() {
      this.operatorBox.requestFocusInWindow();
   }

   private void showValueUi(ValueUi var1) {
      var1.addActionListener(this.addFilterAction);
      this.valueEntryPanel.removeAll();
      this.valueEntryPanel.center(var1);
      UiUtils.revalidate(this.layout);
      this.valueEntryPanel.deeplyNonOpaque();
      this.addFilterAction.setEnabled(true);
      this.valueUi = var1;
   }

   private void clearValueUi() {
      this.valueEntryPanel.removeAll();
      this.valueEntryPanel.center(this.disabledValueUi);
      UiUtils.revalidate(this.layout);
      this.addFilterAction.setEnabled(false);
      this.valueUi = null;
   }

   private class AddFilterAction extends AbstractAction {
      private final QueryBuilderModel model;

      public AddFilterAction(QueryBuilderModel var2) {
         super("Add / Update Filter");
         this.model = var2;
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddFilterAction.this.addFilter();
            }
         });
      }

      private void addFilter() {
         if (FilterEntryUi.this.valueUi != null) {
            String var1 = this.model.getSelectedObjectName();
            String var2 = this.model.getSelectedFieldName();
            if (var2 != null) {
               SearchField var3 = this.model.getFieldMetaData(var1, var2);
               NetSuiteSearchOperator var4 = FilterEntryUi.this.operatorBox.getSelectedItem();
               if (var4 != null) {
                  SearchFieldValue var5 = FilterEntryUi.this.valueUi.getValue(var4);
                  Filter var6 = new Filter(var1, var2, var3, var5);
                  this.model.addOrReplaceFilter(var6);
               }
            }
         }
      }
   }

   private class SelectedFieldBinding extends AbstractBinding<String> {
      private final QueryBuilderModel model;

      public SelectedFieldBinding(QueryBuilderModel var2) {
         super(var2, "selectedField");
         this.model = var2;
      }

      protected void updateUiWithValue(String var1) {
         if (var1 == null) {
            FilterEntryUi.this.clearValueUi();
         } else {
            String var2 = this.model.getSelectedObjectName();
            SearchField var3 = this.model.getFieldMetaData(var2, var1);
            ValueUi var4 = ValueUi.fromField(var3);
            FilterEntryUi.this.showValueUi(var4);
         }
      }
   }

   private class SelectedFilterBinding extends AbstractBinding<List<Filter>> {
      public SelectedFilterBinding(QueryBuilderModel var2) {
         super(var2, "selectedFilters");
      }

      protected void updateUiWithValue(final List<Filter> var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               this.handleSelectedFilters(var1);
            }

            private void handleSelectedFilters(List<Filter> var1x) {
               switch (var1x.size()) {
                  case 0:
                     break;
                  case 1:
                     Filter var2 = (Filter)var1x.get(0);
                     ValueUi var3 = ValueUi.fromFilter(var2);
                     FilterEntryUi.this.showValueUi(var3);
                     break;
                  default:
                     FilterEntryUi.this.clearValueUi();
               }
            }
         });
      }
   }
}
