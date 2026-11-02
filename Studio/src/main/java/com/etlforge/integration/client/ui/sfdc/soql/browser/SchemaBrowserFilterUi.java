package com.etlforge.integration.client.ui.sfdc.soql.browser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;
import org.etlforge.util.ObservableValue;

final class SchemaBrowserFilterUi implements UiProvider {
   private final SimpleTreeFilterField nameField;
   private final FilterFunction filterFunction;
   private final KongaCheckBox includeParents = new KongaCheckBox("Show &parents", true);
   private final KongaCheckBox includeChildren = new KongaCheckBox("Show &children", true);

   public SchemaBrowserFilterUi(KongaTree var1, ObservableValue<SObjectSchema> var2) {
      this.filterFunction = new FilterFunction(var2);
      this.nameField = new SimpleTreeFilterField(var1, this.filterFunction);
      this.nameField.setLabel("&Filter");
      ButtonUtils.addActionListener(new SchemaBrowserFilterUi.CheckBoxListener(), this.includeParents, this.includeChildren);
      var2.addListener(new ObservableValue.Listener<SObjectSchema>() {
         public void valueChanged(ObservableValue<? extends SObjectSchema> var1, SObjectSchema var2x, SObjectSchema var3) {
            SchemaBrowserFilterUi.this.nameField.clear();
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      Horizontal.box().addAllWithSpace(10, this.nameField, this.includeParents, this.includeChildren).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.nameField.requestFocus();
   }

   private class CheckBoxListener implements ActionListener {
      private CheckBoxListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() == SchemaBrowserFilterUi.this.includeParents) {
            SchemaBrowserFilterUi.this.filterFunction.setIncludeParents(SchemaBrowserFilterUi.this.includeParents.isSelected());
         } else {
            SchemaBrowserFilterUi.this.filterFunction.setIncludeChildren(SchemaBrowserFilterUi.this.includeChildren.isSelected());
         }

         SchemaBrowserFilterUi.this.nameField.updateTree();
      }
   }
}
