package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Strings;
import javax.swing.JLabel;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.util.Property;

public class SavedSearchUi implements UiProvider {
   public static final String savedSearchErrorMessage = "Please select a 'Saved Search' from the drop-down list.";
   private static final String SELECTEDKEY = "selected";
   private static final String titleTemplate = "Select a saved search for selected";
   private final SaveSearchModel model;
   private final KongaComboBox dropdown;
   private final JLabel titleLabel;
   private final PageSizeSelectorAndTestButtonUi testUi;

   public SavedSearchUi(SaveSearchModel var1, PageSizeSelectorAndTestButtonUi var2) {
      this.model = var1;
      this.titleLabel = new JLabel("Select a saved search for selected");
      this.dropdown = new KongaComboBox();
      WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeOver(this.titleLabel);
      WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeOver(this.dropdown);
      this.dropdown.setLabel("Please select saved query.");
      this.testUi = var2;
      this.bindToModel();
   }

   public final SaveSearchModel getModel() {
      return this.model;
   }

   boolean isValid() {
      String var1 = this.model.getSelectedSavedSearchId();
      boolean var2 = !Strings.isNullOrEmpty(var1);
      if (!var2) {
         NonBlockingAlert.error("Please select a 'Saved Search' from the drop-down list.", "Saved Search");
      }

      return var2;
   }

   private void bindToModel() {
      Bindings var1 = new Bindings(new SavedSearchUi.TitleBinding());
      var1.syncUi();
      Property var2 = this.model.getProperty("SavedSearchList");
      Property var3 = this.model.getProperty("SelectedSavedSearch");
      ComboBoxBinding var4 = new ComboBoxBinding(this.dropdown, var2, var3);
      var4.setSelectionPolicyOnContentChange(ComboBoxBinding.SelectionPolicy.NULL_OUT);
      var4.setNullSelectionMessage("Select a saved search...");
      var4.setDisableWhenEmpty(true);
      var4.syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = Grid.oneColumn();
      var2.add(this.titleLabel);
      var2.add(this.dropdown);
      Vertical.Anchor.north(var2).south(this.testUi.layoutPageSizeSelectorAndTestButton()).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.dropdown.requestFocus();
   }

   private class TitleBinding extends AbstractBinding<String> {
      public TitleBinding() {
         super(SavedSearchUi.this.model, "selectedObjectName");
      }

      protected void updateUiWithValue(String var1) {
         if (!Strings.isNullOrEmpty(var1)) {
            String var2 = "Select a saved search for selected".replace("selected", var1);
            SavedSearchUi.this.titleLabel.setText(var2);
         }
      }
   }
}
