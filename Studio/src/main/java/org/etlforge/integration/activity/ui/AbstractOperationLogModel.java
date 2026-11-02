package org.etlforge.integration.activity.ui;

import java.util.Comparator;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.log.LogDisplayMode;
import org.etlforge.integration.activity.ui.log.SortStatus;
import org.etlforge.integration.activity.ui.log.SortStatusTranslator;
import org.etlforge.util.Property;

public abstract class AbstractOperationLogModel extends AbstractActivityDisplayModel<OperationLogEntry> {
   public static final String DISPLAY_MODE = "displayMode";
   private final Property<LogDisplayMode> displayMode = this.newProperty("displayMode", LogDisplayMode.getPreferredMode());

   public AbstractOperationLogModel(ActivitySubject var1) {
      super(var1);
      this.displayMode.setAllowsNull(false);
   }

   public final LogDisplayMode getDisplayMode() {
      return this.displayMode.get();
   }

   public final void setDisplayMode(LogDisplayMode var1) {
      this.displayMode.set(var1);
   }

   public final Comparator<OperationLogEntry> getTopLevelOrder() {
      SortStatus var1 = this.getSortStatus();
      return var1 == null ? null : SortStatusTranslator.toComparator(var1);
   }
}
