package org.etlforge.application.ui.groovy;

import java.awt.EventQueue;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListUtils;

final class ImportsArea implements UiProvider {
   private final KongaList commonImportsList;
   private final IndividualPackageField packageField;
   private final ScriptArea scriptArea;

   public ImportsArea(ScriptArea var1) {
      this.scriptArea = var1;
      this.commonImportsList = this.createCommonImportsList();
      this.packageField = new IndividualPackageField(var1);
   }

   private KongaList createCommonImportsList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setLabel(KongaLabel.withMnemonic("Common &imports (double-click to insert):"));
      var1.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      KongaList var2 = var1.newList();
      var2.setDefaultAction(new ImportsArea.InsertCommonImportsActions());
      return var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      Vertical.borderLayout().of(this.packageField, this.commonImportsList).withGap(10).withEmptyBorder(3).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.commonImportsList.requestFocus();
   }

   public void setCommonImports(List<CommonImport> var1) {
      Collections.sort(var1);
      final DefaultListModel var2 = ListUtils.createModel(var1);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ImportsArea.this.commonImportsList.setModel(var2);
         }
      });
   }

   public void setEnabled(boolean var1) {
      this.commonImportsList.setEnabled(var1);
      this.packageField.setEnabled(var1);
   }

   private final class InsertCommonImportsActions extends AbstractSingleTypeSelectionAction<CommonImport> {
      public InsertCommonImportsActions() {
         super(CommonImport.class, "Insert Selected Imports");
      }

      @Override
      protected void run(List<CommonImport> var1) {
         for (CommonImport var3 : var1) {
            this.insertImport(var3);
         }

         ImportsArea.this.requestFocus();
      }

      private void insertImport(CommonImport var1) {
         ImportsArea.this.scriptArea.insertImport(var1.getPath());
      }
   }
}
