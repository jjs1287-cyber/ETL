package org.etlforge.integration.activity.ui.queue;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.CompositeActivityEntryFilter;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.queue.QueueStatusFilter;
import org.etlforge.integration.activity.ui.AbstractOperationLogModel;
import org.etlforge.integration.activity.ui.DeleteSelectionListener;
import org.etlforge.lang.PrimitiveConversions;
import org.etlforge.util.Property;

public final class OperationQueueModel extends AbstractOperationLogModel implements DeleteSelectionListener {
   public static final String HAVE_ITEMS_TO_DELETE = "deleteEnabled";
   private QueueStatusFilter statusFilter = new QueueStatusFilter(null);
   private final Set<Long> toDelete = Sets.newTreeSet();
   private final Property<Boolean> deleteEnabled = this.newProperty("deleteEnabled", Boolean.FALSE);

   public OperationQueueModel(ActivitySubject var1) {
      super(var1);
   }

   @Override
   protected void newEntriesReceived(List<OperationLogEntry> var1) {
      this.toDelete.clear();
      this.deleteEnabled.set(false);
   }

   @Override
   protected Predicate<ActivityEntry> createCompositeFilter() {
      return new CompositeActivityEntryFilter(this.statusFilter, this.getNameFilter());
   }

   public QueueStatusFilter getStatusFilter() {
      return this.statusFilter;
   }

   public void setStatusFilter(QueueStatusFilter var1) {
      this.statusFilter = var1;
   }

   public long[] getEntriesToDelete() {
      return PrimitiveConversions.toLongArray(this.toDelete);
   }

   public void setMarkForDeletion(OperationLogEntry var1, boolean var2) {
      if (var2) {
         this.toDelete.add(var1.getID());
      } else {
         this.toDelete.remove(var1.getID());
      }

      this.deleteEnabled.set(!this.toDelete.isEmpty());
   }

   public boolean isDeleteEnabled() {
      return this.deleteEnabled.get();
   }

   @Override
   public void selectionChanged(ActivityEntry var1, boolean var2) {
      this.setMarkForDeletion((OperationLogEntry)var1, var2);
   }
}
