package org.etlforge.integration.activity.ui.queue;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.etlforge.integration.activity.client.queue.QueueStatus;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

final class QueueStatusCombo implements UiProvider {
   private static final Object ALL_ITEM = PackageResources.STATUS_COMBO_ALL;
   private final JComboBox combo = new JComboBox(createModel());

   public QueueStatusCombo() {
      this.combo.setRenderer(new DefaultListDecorRenderer());
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.combo);
   }

   @Override
   public void requestFocus() {
      this.combo.requestFocusInWindow();
   }

   public QueueStatus getSelectedStatus() {
      Object var1 = this.combo.getSelectedItem();
      return var1 instanceof QueueStatusCombo.StatusItem ? ((QueueStatusCombo.StatusItem)var1).status : null;
   }

   public void setSelectedItem(Object var1) {
      this.combo.setSelectedItem(var1 != null ? var1 : ALL_ITEM);
   }

   private static ComboBoxModel createModel() {
      ArrayList var0 = Lists.newArrayList();
      var0.add(ALL_ITEM);

      for (QueueStatus var4 : QueueStatus.values()) {
         var0.add(new QueueStatusCombo.StatusItem(var4));
      }

      return new DefaultComboBoxModel<>(var0.toArray());
   }

   private static final class StatusItem implements ListDecorator {
      private final QueueStatus status;

      public StatusItem(QueueStatus var1) {
         this.status = var1;
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         switch (this.status) {
            case PENDING:
               var1.setIcon(PackageResources.PENDING_ICON);
               break;
            case RUNNING:
               var1.setIcon(PackageResources.RUNNING_ICON);
         }

         var1.setText(this.status.toString());
      }
   }
}
