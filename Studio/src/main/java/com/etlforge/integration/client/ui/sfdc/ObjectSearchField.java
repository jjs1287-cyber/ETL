package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.DialogLayout;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.selection.SelectFirstAction;
import org.etlforge.ui.widget.text.KongaTextField;

final class ObjectSearchField implements UiProvider {
   private final KongaTextField searchField;
   private final ObjectSelectionWidget objectList;
   private final Function<String, ? extends Predicate<? super SObjectSchema>> filterFunction;

   public ObjectSearchField(ObjectSelectionWidget var1, Function<String, ? extends Predicate<? super SObjectSchema>> var2) {
      this.objectList = var1;
      this.filterFunction = var2;
      this.searchField = this.createSearchField();
   }

   private KongaTextField createSearchField() {
      KongaTextField var1 = new KongaTextField(Size.columns(10));
      ObjectSearchField.SearchStringHandler var2 = new ObjectSearchField.SearchStringHandler();
      var2.install(var1);
      SelectFirstAction.installInField(this.objectList, var1);
      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      JPanel var2 = new JPanel(new DialogLayout(5, 5));
      KongaLabel var3 = KongaLabel.withMnemonic("&Filter:");
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var3);
      var3.setLabelFor(this.searchField);
      var2.add(var3);
      var2.add(this.searchField);
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.searchField.requestFocusInWindow();
   }

   private class SearchStringHandler extends DelayedDocumentContentChangeReaction {
      private SearchStringHandler() {
      }

      @Override
      public void run() {
         String var1 = ObjectSearchField.this.searchField.getText();
         Predicate var2 = ObjectSearchField.this.filterFunction.apply(var1);
         ObjectSearchField.this.objectList.setNameFilter(var2);
      }
   }
}
