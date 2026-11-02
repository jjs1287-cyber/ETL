package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.button.ToggleButtonKeySelector;

public class AdvancedSearchOptionUi {
   private final AdvancedSearchPropertyModel model;
   private static final String SELECTEDKEY = "selected";
   private static final String titleTemplate = "Select Search Type";
   JLabel titleLabel = new JLabel("Select Search Type");
   KongaRadioButton searchBasic = new KongaRadioButton();
   KongaRadioButton search = new KongaRadioButton();
   KongaRadioButton searchAdvanced = new KongaRadioButton();
   KongaRadioButton searchSavedSearch = new KongaRadioButton();
   private final AdvancedSearchOptionUi.Helper helper;

   public AdvancedSearchOptionUi(AdvancedSearchPropertyModel var1) {
      this.model = var1;
      this.helper = new AdvancedSearchOptionUi.Helper();
      this.configure();
      this.bindWithModel();
   }

   private void configure() {
      this.setEnabled(null);
      WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeOver(this.titleLabel);
      WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeOver(this.searchBasic, this.search, this.searchAdvanced, this.searchSavedSearch);
      ButtonUtils.addActionListener(new AdvancedSearchOptionUi.Listener(), this.getButtons());
      ButtonUtils.makeMutuallyExclusive(this.getButtons());
   }

   private void setEnabled(AdvancedSearchOption.OptionType var1) {
      this.searchBasic.setSelected(true);
      this.helper.setSearchCategory();
      this.search.setEnabled(false);
      this.searchAdvanced.setEnabled(false);
      this.searchSavedSearch.setEnabled(false);
      if (var1 != null) {
         switch (var1) {
            case CustomObjectSearch:
               this.search.setEnabled(true);
               break;
            case All:
               this.search.setEnabled(true);
               this.searchAdvanced.setEnabled(true);
               this.searchSavedSearch.setEnabled(true);
         }
      }
   }

   private List<KongaRadioButton> getButtons() {
      return this.helper.choices;
   }

   private void bindWithModel() {
      Bindings var1 = new Bindings(new AdvancedSearchOptionUi.OptionBinding(), new AdvancedSearchOptionUi.TextBinding());
      var1.syncUi();
   }

   public JComponent getComponent() {
      Grid var1 = Vertical.grid(12).add(this.titleLabel).of(this.getButtons());
      JPanel var2 = var1.invisible();
      ToggleButtonKeySelector.install(this.getButtons(), var2);
      return var2;
   }

   private class Helper {
      private final List<KongaRadioButton> choices = Lists.newArrayList();
      private final Map<Integer, NetSuiteSearchCategory> choiceIndex = Maps.newHashMap();
      private final Map<NetSuiteSearchCategory, String> textTemplate = Maps.newHashMap();

      private Helper() {
         this.textTemplate.put(NetSuiteSearchCategory.NetSuiteSearchBasic, "&Basic Search (Filter only on selected)");
         this.textTemplate.put(NetSuiteSearchCategory.NetSuiteSearch, "&Expanded Search (Filter on selected and related objects)");
         this.textTemplate
            .put(NetSuiteSearchCategory.NetSuiteSearchAdvancedSavedSearch, "&Saved Search (Use reusable search definition previously saved in Netsuite)");
         this.put(NetSuiteSearchCategory.NetSuiteSearchBasic, AdvancedSearchOptionUi.this.searchBasic);
         this.put(NetSuiteSearchCategory.NetSuiteSearch, AdvancedSearchOptionUi.this.search);
         this.put(NetSuiteSearchCategory.NetSuiteSearchAdvancedSavedSearch, AdvancedSearchOptionUi.this.searchSavedSearch);
         this.update(null);
      }

      private void put(NetSuiteSearchCategory var1, KongaRadioButton var2) {
         this.choiceIndex.put(this.choices.size(), var1);
         this.choices.add(var2);
      }

      private void update(String var1) {
         String var2 = this.getNewText(var1, "Select Search Type");
         AdvancedSearchOptionUi.this.titleLabel.setText(var2);

         for (int var3 = 0; var3 < this.choices.size(); var3++) {
            KongaRadioButton var4 = this.choices.get(var3);
            var2 = this.textTemplate.get(this.choiceIndex.get(var3));
            var2 = this.getNewText(var1, var2);
            var4.setText(var2);
         }
      }

      private String getNewText(String var1, String var2) {
         return Strings.isNullOrEmpty(var1) ? var2 : var2.replace("selected", var1);
      }

      private void setSearchCategory() {
         for (int var1 = 0; var1 < this.choices.size(); var1++) {
            KongaRadioButton var2 = this.choices.get(var1);
            if (var2.isSelected()) {
               NetSuiteSearchCategory var3 = this.choiceIndex.get(var1);
               AdvancedSearchOptionUi.this.model.serSearchCategory(var3);
               return;
            }
         }

         AdvancedSearchOptionUi.this.model.serSearchCategory(NetSuiteSearchCategory.NetSuiteSearchBasic);
      }
   }

   private class Listener implements ActionListener {
      private Listener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         AdvancedSearchOptionUi.this.helper.setSearchCategory();
      }
   }

   private class OptionBinding extends AbstractBinding<AdvancedSearchOption.OptionType> {
      public OptionBinding() {
         super(AdvancedSearchOptionUi.this.model, "searchOption");
      }

      protected void updateUiWithValue(AdvancedSearchOption.OptionType var1) {
         AdvancedSearchOptionUi.this.setEnabled(var1);
      }
   }

   private class TextBinding extends AbstractBinding<String> {
      public TextBinding() {
         super(AdvancedSearchOptionUi.this.model, "selectedObject");
      }

      protected void updateUiWithValue(String var1) {
         AdvancedSearchOptionUi.this.helper.update(var1);
      }
   }
}
