package org.etlforge.integration.activity.ui;

import java.text.DateFormat;
import javax.swing.table.TableCellRenderer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.widget.table.renderer.DateRenderer;

public final class ActivityViewUtils {
   private static final DateFormat tableDateFormat = DateFormat.getDateTimeInstance(3, 2);

   public static DateFormat getTableDateFormat() {
      return tableDateFormat;
   }

   public static TableCellRenderer getDateCellRenderer() {
      return new DateRenderer(tableDateFormat);
   }

   public static boolean isValidType(IntegrationEntity var0) {
      return ActivityUtils.isValidType(var0);
   }

   private ActivityViewUtils() {
   }
}
