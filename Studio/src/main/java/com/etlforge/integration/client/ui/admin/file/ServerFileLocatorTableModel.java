package com.etlforge.integration.client.ui.admin.file;

import com.etlforge.integration.client.admin.server.ServerFileLocator;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.io.File;
import java.util.Date;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public class ServerFileLocatorTableModel extends TypedAbstractKongaTableModel<ServerFileLocator> {
   public static final TableColumnMetaData PATH = new TableColumnMetaData(EnterpriseAdminResources.getString("ServerFile.Table.Path"), String.class, false, 300);
   public static final TableColumnMetaData MODIFIED = new TableColumnMetaData(
      EnterpriseAdminResources.getString("ServerFile.Table.Modified"), Date.class, false, 150
   );
   public static final TableColumnMetaData SIZE = new TableColumnMetaData(EnterpriseAdminResources.getString("ServerFile.Table.Size"), Long.class, false, 75);
   private boolean showFullPath;

   public ServerFileLocatorTableModel() {
      super(ServerFileLocator.class, PATH, MODIFIED, SIZE);
   }

   public void setShowFullPath(boolean var1) {
      this.showFullPath = var1;
   }

   protected Object[] getColumnDataForTypedObject(ServerFileLocator var1) {
      String var2 = this.showFullPath ? var1.getPath() : new File(var1.getPath()).getName();
      return new Object[]{var2, var1.getModifiedDate(), var1.getSizeInBytes()};
   }
}
