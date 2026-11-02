package org.etlforge.integration.activity.ui.log;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.graphics.IconRow;

public final class LogStatusIcons {
   private static Map<LogStatus, Icon> ICON_MAP = Maps.newHashMap();

   public static Icon getIcon(LogStatus var0) {
      return ICON_MAP.get(var0);
   }

   private LogStatusIcons() {
   }

   static {
      ICON_MAP.put(LogStatus.SUCCESS, ClientIcons.getIcon("Operation.Success.16"));
      ICON_MAP.put(LogStatus.SUCCESS_WITH_WARNING, ClientIcons.getIcon("Operation.Partial.16"));
      ICON_MAP.put(LogStatus.ERROR, ClientIcons.getIcon("Operation.Error.16"));
      ICON_MAP.put(LogStatus.SUCCESS_WITH_INFO, new IconRow(ClientIcons.getIcon("Operation.Success.16"), CommonIcons.INFO_16));
   }
}
