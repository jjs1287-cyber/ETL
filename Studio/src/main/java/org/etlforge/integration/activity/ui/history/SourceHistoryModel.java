package org.etlforge.integration.activity.ui.history;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.CompositeActivityEntryFilter;
import org.etlforge.integration.activity.client.filter.TransferSizeFilter;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.ui.AbstractActivityDisplayModel;
import org.etlforge.integration.activity.ui.DeleteSelectionListener;
import org.etlforge.lang.PrimitiveConversions;
import org.etlforge.util.Property;

public final class SourceHistoryModel extends AbstractActivityDisplayModel<SourceHistoryEntry> implements DeleteSelectionListener {
   public static final String HAVE_ITEMS_TO_DELETE = "deleteEnabled";
   private TransferSizeFilter sizeFilter;
   private final Set<Long> toDelete = Sets.newTreeSet();
   private final boolean supportsDelete;
   private final Property<Boolean> deleteEnabled = this.newProperty("deleteEnabled", Boolean.FALSE);

   public SourceHistoryModel(ActivitySubject var1, boolean var2) {
      super(var1);
      this.supportsDelete = var2;
   }

   @Override
   protected Predicate<ActivityEntry> createCompositeFilter() {
      CompositeActivityEntryFilter var1 = new CompositeActivityEntryFilter(this.getNameFilter(), this.getTimeFilter());
      if (this.sizeFilter != null) {
         var1.add(this.sizeFilter);
      }

      return var1;
   }

   public TransferSizeFilter getSizeFilter() {
      return this.sizeFilter;
   }

   public void setSizeFilter(TransferSizeFilter var1) {
      this.sizeFilter = var1;
   }

   public HistoryRequest createRequest() {
      return RequestBuilder.buildRequest(this, this.sizeFilter);
   }

   @Override
   protected void newEntriesReceived(List<SourceHistoryEntry> var1) {
      this.toDelete.clear();
      this.deleteEnabled.set(false);
   }

   public long[] getEntriesToDelete() {
      return PrimitiveConversions.toLongArray(this.toDelete);
   }

   public void setMarkForDeletion(SourceHistoryEntry var1, boolean var2) {
      if (var2) {
         this.toDelete.add(var1.getID());
      } else {
         this.toDelete.remove(var1.getID());
      }

      this.deleteEnabled.set(!this.toDelete.isEmpty());
   }

   @Override
   public void selectionChanged(ActivityEntry var1, boolean var2) {
      this.setMarkForDeletion((SourceHistoryEntry)var1, var2);
   }

   public boolean isDeleteSupported() {
      return this.supportsDelete;
   }
}
