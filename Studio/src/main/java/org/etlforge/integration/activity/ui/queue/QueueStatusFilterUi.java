package org.etlforge.integration.activity.ui.queue;

import javax.swing.JComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.queue.QueueStatus;
import org.etlforge.integration.activity.client.queue.QueueStatusFilter;
import org.etlforge.integration.activity.ui.component.filter.AbstractActivityEntryFilterUi;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class QueueStatusFilterUi extends AbstractActivityEntryFilterUi<OperationLogEntry> {
   private QueueStatus status;
   private final QueueStatusCombo statusCombo = new QueueStatusCombo();

   public QueueStatusFilterUi() {
   }

   @Override
   public void apply() {
      QueueStatus var1 = this.status;
      this.status = this.statusCombo.getSelectedStatus();
      if (this.status != var1) {
         this.fireFilterChanged(this);
      }
   }

   @Override
   public void reset() {
      this.statusCombo.setSelectedItem(this.status);
   }

   @Override
   public boolean isActive() {
      return this.status != null;
   }

   public QueueStatusFilter getFilter() {
      return new QueueStatusFilter(this.status);
   }

   @Override
   public String getFilterString() {
      return PackageResources.QueueStatusFilter.getFilterString(this.status);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = InputFieldFactory.getLabeledComponent(
         InvisiblePanel.of(this.statusCombo), PackageResources.QueueStatusFilter.LABEL, this.getCapsuleOrientation()
      );
      var1.display(InvisiblePanel.of(var2));
   }

   @Override
   public void requestFocus() {
      this.statusCombo.requestFocus();
   }
}
