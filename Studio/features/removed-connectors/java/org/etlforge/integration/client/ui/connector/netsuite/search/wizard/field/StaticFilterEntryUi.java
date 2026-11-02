package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import com.google.common.base.Objects;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.QueryBuilderModel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.text.KongaTextField;

public final class StaticFilterEntryUi implements UiProvider {
   private final QueryBuilderModel model;
   private final KongaTextField selectedObject = new KongaTextField(20);
   private final KongaTextField selectedField = new KongaTextField(20);
   private final StaticFilterEntryUi.AddFilterAction addFilterAction = new StaticFilterEntryUi.AddFilterAction();
   private final KongaButton addFilterButton = new KongaButton(this.addFilterAction);
   private final JLabel caption = new JLabel(" ");

   public StaticFilterEntryUi(QueryBuilderModel var1) {
      this.model = var1;
      this.selectedField.setEditable(false);
      this.selectedObject.setEditable(false);
      WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeOver(this.addFilterButton);
      this.bindWithModel();
   }

   private void bindWithModel() {
      Bindings.of(new StaticFilterEntryUi.SelectedFieldBinding(this.model), new StaticFilterEntryUi.SelectedFilterBinding(this.model)).syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = new NColumnPanel(3)
         .setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE)
         .setXWeights(0.0, 1.0, 0.0)
         .setYWeights(0.0, 0.0, 0.0)
         .setInsets(0, 0, 5, 8);
      var2.addRow("Selected join:", this.selectedObject, null);
      var2.addRow("Selected field:", this.selectedField, this.addFilterButton);
      var2.emptyColumn().add(this.caption, 2);
      Vertical.Anchor.north(var2).decorate(var1);
   }

   public Action getAddFilterAction() {
      return this.addFilterAction;
   }

   @Override
   public void requestFocus() {
      this.addFilterButton.requestFocusInWindow();
   }

   private void updateStateForFieldName(String var1) {
      boolean var2 = false;
      String var3 = " ";
      if (var1 != null) {
         Filter var4 = this.model.getFilterFor(this.model.getSelectedObjectName(), var1);
         if (var4 == null) {
            var2 = true;
         } else {
            var3 = getEditFilterCaptionText();
         }
      }

      this.selectedObject.setText(this.model.getSelectedObjectName());
      this.selectedField.setText(var1);
      this.addFilterAction.setEnabled(var2);
      this.caption.setText(var3);
   }

   private static String getEditFilterCaptionText() {
      return "The selected field already has a condition. Use the Edit button below to modify it.";
   }

   private class AddFilterAction extends AbstractAction {
      public AddFilterAction() {
         super("Add Condition");
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddFilterAction.this.addFilter(var1);
            }
         });
      }

      private void addFilter(ActionEvent var1) {
         String var2 = StaticFilterEntryUi.this.model.getSelectedFieldName();
         if (var2 != null) {
            String var3 = StaticFilterEntryUi.this.model.getSelectedObjectName();
            SearchField var4 = StaticFilterEntryUi.this.model.getFieldMetaData(var3, var2);
            FilterUi var5 = FilterUi.newFilter(var3, var2, var4);
            Window var6 = Objects.firstNonNull(UiUtils.getWindowAncestor(var1).orNull(), ApplicationUi.getAppWinFrame());
            Filter var7 = var5.openInAddFilterDialog(var6);
            if (var7 != null) {
               StaticFilterEntryUi.this.model.addOrReplaceFilter(var7);
            }
         }
      }
   }

   private class SelectedFieldBinding extends AbstractBinding<String> {
      public SelectedFieldBinding(QueryBuilderModel var2) {
         super(var2, "selectedField");
      }

      protected void updateUiWithValue(String var1) {
         StaticFilterEntryUi.this.updateStateForFieldName(var1);
      }
   }

   private class SelectedFilterBinding extends AbstractBinding<List<Filter>> {
      public SelectedFilterBinding(QueryBuilderModel var2) {
         super(var2, "selectedFilters");
      }

      protected void updateUiWithValue(List<Filter> var1) {
         if (var1.isEmpty()) {
            String var2 = StaticFilterEntryUi.this.model.getSelectedFieldName();
            StaticFilterEntryUi.this.updateStateForFieldName(var2);
         } else {
            StaticFilterEntryUi.this.addFilterAction.setEnabled(false);
            StaticFilterEntryUi.this.caption.setText(StaticFilterEntryUi.getEditFilterCaptionText());
         }
      }
   }
}
