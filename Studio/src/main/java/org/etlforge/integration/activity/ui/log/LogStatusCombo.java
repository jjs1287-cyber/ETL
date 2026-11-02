package org.etlforge.integration.activity.ui.log;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

final class LogStatusCombo implements UiProvider {
   private static final Object ALL_ITEM = PackageResources.STATUS_COMBO_ALL;
   private final JComboBox comboBox = new JComboBox(createModel());

   public LogStatusCombo() {
      this.comboBox.setRenderer(new DefaultListDecorRenderer());
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.comboBox);
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocusInWindow();
   }

   public LogStatus getSelectedStatus() {
      Object var1 = this.comboBox.getSelectedItem();
      return var1 instanceof LogStatusCombo.StatusItem ? ((LogStatusCombo.StatusItem)var1).getStatus() : null;
   }

   public void setSelectedItem(Object var1) {
      this.comboBox.setSelectedItem(var1 != null ? var1 : ALL_ITEM);
   }

   private static ComboBoxModel createModel() {
      ArrayList var0 = new ArrayList();
      var0.add(ALL_ITEM);

      for (LogStatus var4 : LogStatus.values()) {
         var0.add(new LogStatusCombo.StatusItem(var4));
      }

      return new DefaultComboBoxModel<>(var0.toArray());
   }

   private static final class StatusItem implements ListDecorator {
      private final LogStatus status;

      public StatusItem(LogStatus var1) {
         this.status = var1;
      }

      public LogStatus getStatus() {
         return this.status;
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setIcon(PackageResources.getStatusIcon(this.status));
         var1.setText(this.status.toString());
      }
   }
}
