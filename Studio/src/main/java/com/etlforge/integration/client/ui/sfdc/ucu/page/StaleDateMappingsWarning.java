package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.awt.EventQueue;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.CopyToClipboardButton;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

final class StaleDateMappingsWarning {
   private final ImmutableList<String> targetFields;

   public StaleDateMappingsWarning(List<DateMapping> var1, List<DateMapping> var2) {
      SortedSet var3 = this.collectTargetFieldNames(var1, var2);
      this.targetFields = ImmutableList.copyOf(var3);
   }

   private SortedSet<String> collectTargetFieldNames(List<DateMapping> var1, List<DateMapping> var2) {
      TreeSet var3 = Sets.newTreeSet();

      for (DateMapping var5 : var1) {
         var3.add(var5.getTargetFieldName());
      }

      for (DateMapping var7 : var2) {
         var3.add(var7.getTargetFieldName());
      }

      assert !var3.isEmpty();
      return var3;
   }

   public void show() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            StaleDateMappingsWarning.this.showEDT();
         }
      });
   }

   private void showEDT() {
      StaleDateMappingsWarning.Ui var1 = new StaleDateMappingsWarning.Ui(this.targetFields);
      var1.openDialog();
   }

   private static class FieldListCopier implements Supplier<String> {
      private final List<String> fieldNames;

      public FieldListCopier(List<String> var1) {
         this.fieldNames = var1;
      }

      public String get() {
         return Joiner.on(SystemUtils.LINE_SEPARATOR).join(this.fieldNames);
      }
   }

   private static class Ui implements UiProvider {
      private final CopyToClipboardButton copy;
      private final KongaList list;

      public Ui(List<String> var1) {
         KongaListFactory var2 = ComponentFactories.newKongaListFactory();
         var2.setVisibleRowCount(8);
         var2.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
         var2.setLabel(new JLabel("The new date options could not be applied to the following target fields:"));
         this.list = var2.newList(var1);
         this.copy = new CopyToClipboardButton(new StaleDateMappingsWarning.FieldListCopier(var1));
         this.copy.setButtonLabel("&Copy Field Names to Clipboard");
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(10, 5);
         JLabel var3 = new JLabel(AlertIcons.WARNING);
         var3.setVerticalAlignment(1);
         var2.west(var3).center(this.layoutList()).decorate(var1);
      }

      private UiProvider layoutList() {
         return new BorderLayoutBuilder(0, 5).center(this.list).south("You will have to update the mappings of these fields by hand.");
      }

      @Override
      public void requestFocus() {
         this.copy.requestFocus();
      }

      public void openDialog() {
         KongaDialog var1 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Incomplete Date Option Mappings", true);
         BorderLayoutBuilder var2 = var1.standardLayout(this, null);
         JButton var3 = var1.createOKButton();
         BoxBuilder var4 = Horizontal.box().withEmptyBorder(10, 0, 10, 0).add(this.copy).glue().add(var3);
         var2.south(var4);
         var1.pack();
         var1.setOkButtonAsDefault();
         var1.setResizable(false);
         var1.setLocationRelativeTo(null);
         var1.setVisible(true);
      }
   }
}
