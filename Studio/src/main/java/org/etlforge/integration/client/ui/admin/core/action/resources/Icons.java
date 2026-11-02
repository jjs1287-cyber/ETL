package org.etlforge.integration.client.ui.admin.core.action.resources;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.activity.ui.resources.ActivityIcons;
import org.etlforge.integration.client.ui.admin.resources.AdminIcons;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public final class Icons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {getSmallActionIconKey("RunScheduledOperations"), ClientIcons.RUN_OPERATION_16},
      {getSmallActionIconKey("SchedulerConsole"), AdminIcons.SCHEDULE_CONSOLE_16},
      {getLargeActionIconKey("SchedulerConsole"), AdminIcons.SCHEDULE_CONSOLE_32},
      {getSmallActionIconKey("SystemConsole"), AdminIcons.SYSTEM_CONSOLE_16},
      {getLargeActionIconKey("SystemConsole"), AdminIcons.SYSTEM_CONSOLE_32},
      {getSmallActionIconKey("JmsConsole"), AdminIcons.JMS_CONSOLE_16},
      {getLargeActionIconKey("JmsConsole"), AdminIcons.JMS_CONSOLE_32},
      {getSmallActionIconKey("OperationQueue"), ActivityIcons.QUEUE_16},
      {getLargeActionIconKey("OperationQueue"), ActivityIcons.QUEUE_32},
      {getSmallActionIconKey("DeleteQueue"), CommonIcons.DELETE_16},
      {getMediumActionIconKey("DeleteQueue"), CommonIcons.DELETE_24},
      {getLargeActionIconKey("DeleteQueue"), CommonIcons.DELETE_32},
      {getSmallActionIconKey("OperationLog"), ActivityIcons.LOG_16},
      {getLargeActionIconKey("OperationLog"), ActivityIcons.LOG_32},
      {getSmallActionIconKey("AuditLog"), ActivityIcons.HISTORY_16},
      {getSmallActionIconKey("ProjectComparison"), AdminIcons.PROJECT_CONSOLE_16},
      {getMediumActionIconKey("ProjectComparison"), AdminIcons.PROJECT_CONSOLE_24},
      {getLargeActionIconKey("ProjectComparison"), AdminIcons.PROJECT_CONSOLE_32},
      {getSmallActionIconKey("SshKeys"), ActivityIcons.LOG_16},
      {getLargeActionIconKey("SshKeys"), ActivityIcons.LOG_32},
      {getSmallActionIconKey("Cluster"), AdminIcons.SYSTEM_CONSOLE_16},
      {getLargeActionIconKey("Cluster"), AdminIcons.SYSTEM_CONSOLE_32},
      {getSmallActionIconKey("CloudLogging"), ActivityIcons.LOG_16},
      {getLargeActionIconKey("CloudLogging"), ActivityIcons.LOG_32},
      {getLargeActionIconKey("AuditLog"), ActivityIcons.HISTORY_32},
      {getSmallActionIconKey("OperationHistory"), ActivityIcons.HISTORY_16},
      {getLargeActionIconKey("OperationHistory"), ActivityIcons.HISTORY_32},
      {getSmallActionIconKey("UserConsole"), AdminIcons.USER_CONSOLE_16},
      {getLargeActionIconKey("UserConsole"), AdminIcons.USER_CONSOLE_32},
      {getSmallActionIconKey("ProjectConsole"), AdminIcons.PROJECT_CONSOLE_16},
      {getLargeActionIconKey("ProjectConsole"), AdminIcons.PROJECT_CONSOLE_32},
      {getSmallActionIconKey("ServerProxyConsole"), ClientIcons.PROXY_16},
      {getLargeActionIconKey("ServerProxyConsole"), ClientIcons.PROXY_32}
   };

   private static String getSmallActionIconKey(String var0) {
      return "admin.action." + var0 + ".Icon.Small";
   }

   private static String getMediumActionIconKey(String var0) {
      return "admin.action." + var0 + ".Icon.Medium";
   }

   private static String getLargeActionIconKey(String var0) {
      return "admin.action." + var0 + ".Icon.Large";
   }

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
