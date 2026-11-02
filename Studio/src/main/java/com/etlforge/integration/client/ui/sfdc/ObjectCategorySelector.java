package com.etlforge.integration.client.ui.sfdc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.salesforce.wsdl.ObjectCategory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.label.KongaLabel;

final class ObjectCategorySelector implements UiProvider {
   private final EnumComboBox<ObjectCategory> dropdown = EnumComboBox.of(ObjectCategory.class);

   public ObjectCategorySelector(ObjectSelectionModel var1) {
      ObjectCategorySelector.CategoryBinding var2 = new ObjectCategorySelector.CategoryBinding(var1);
      var2.syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      JLabel var2 = this.createLabel();
      Horizontal.box().of(var2, this.dropdown).decorate(var1);
   }

   private JLabel createLabel() {
      KongaLabel var1 = KongaLabel.withMnemonic("&Objects to show: ");
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var1);
      var1.setLabelFor(this.dropdown);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.dropdown.requestFocusInWindow();
   }

   private class CategoryBinding extends AbstractBinding<ObjectCategory> implements ActionListener {
      public CategoryBinding(ObjectSelectionModel var2) {
         super(var2, "category");
         ObjectCategorySelector.this.dropdown.addActionListener(this);
      }

      protected void updateUiWithValue(ObjectCategory var1) {
         ObjectCategorySelector.this.dropdown.setSelectedItem(var1);
      }

      protected ObjectCategory getValueFromUi() {
         return ObjectCategorySelector.this.dropdown.getSelectedItem();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.syncModel();
      }
   }
}
