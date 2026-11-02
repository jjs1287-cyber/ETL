package org.etlforge.integration.client.server.ui.info;

import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;
import org.etlforge.util.version.Version;

final class KnownServersTableModel extends TypedAbstractKongaTableModel<KnownServer> {
   public static final TableColumnMetaData NAME = new TableColumnMetaData(PackageResources.Viewer.NAME, String.class, false, 100);
   public static final TableColumnMetaData VERSION = new TableColumnMetaData(PackageResources.Viewer.VERSION, Version.class, false, 100);
   public static final TableColumnMetaData OS = new TableColumnMetaData(PackageResources.Viewer.OS, String.class, false, 200);
   public static final TableColumnMetaData ID = new TableColumnMetaData(PackageResources.Viewer.ID, ServerGuid.class, false, 250);
   public static final TableColumnMetaData FIRST_LOGIN = new TableColumnMetaData(PackageResources.Viewer.FIRST_LOGIN, DateCell.class, false, 170);
   public static final TableColumnMetaData LATEST_LOGIN = new TableColumnMetaData(PackageResources.Viewer.LATEST_LOGIN, DateCell.class, false, 170);
   public static final TableColumnMetaData LATEST_USER = new TableColumnMetaData(PackageResources.Viewer.LATEST_USER, String.class, false, 100);
   private final KnownServersTableModel.ColumnPolicy policy;

   private KnownServersTableModel(KnownServersTableModel.ColumnPolicy var1) {
      super(KnownServer.class, var1.getColumns());
      this.policy = var1;
   }

   public static KnownServersTableModel fullModel() {
      return new KnownServersTableModel(new KnownServersTableModel.AllColumnsPolicy());
   }

   public static KnownServersTableModel minimalModel() {
      return new KnownServersTableModel(new KnownServersTableModel.MinimalColumnPolicy());
   }

   protected Object[] getColumnDataForTypedObject(KnownServer var1) {
      return this.policy.getColumnDataFromObject(var1);
   }

   private static class AllColumnsPolicy implements KnownServersTableModel.ColumnPolicy {
      private AllColumnsPolicy() {
      }

      @Override
      public Object[] getColumnDataFromObject(KnownServer var1) {
         ServerInfo var2 = var1.getServerInfo();
         ServerGuid var3 = var2.getGuid();
         return new Object[]{
            var2.getName(),
            var2.getVersion(),
            var2.getPlatform().getOS(),
            var3,
            new DateCell(var1.getDateOfFirstLogin()),
            new DateCell(var1.getDateOfLatestLogin()),
            var1.getLatestUser()
         };
      }

      @Override
      public TableColumnMetaData[] getColumns() {
         return new TableColumnMetaData[]{
            KnownServersTableModel.NAME,
            KnownServersTableModel.VERSION,
            KnownServersTableModel.OS,
            KnownServersTableModel.ID,
            KnownServersTableModel.FIRST_LOGIN,
            KnownServersTableModel.LATEST_LOGIN,
            KnownServersTableModel.LATEST_USER
         };
      }
   }

   private interface ColumnPolicy {
      TableColumnMetaData[] getColumns();

      Object[] getColumnDataFromObject(KnownServer var1);
   }

   private static class MinimalColumnPolicy implements KnownServersTableModel.ColumnPolicy {
      private MinimalColumnPolicy() {
      }

      @Override
      public Object[] getColumnDataFromObject(KnownServer var1) {
         ServerInfo var2 = var1.getServerInfo();
         return new Object[]{var2.getName(), var2.getVersion(), var2.getPlatform().getOS(), new DateCell(var1.getDateOfLatestLogin()), var1.getLatestUser()};
      }

      @Override
      public TableColumnMetaData[] getColumns() {
         return new TableColumnMetaData[]{
            KnownServersTableModel.NAME,
            KnownServersTableModel.VERSION,
            KnownServersTableModel.OS,
            KnownServersTableModel.LATEST_LOGIN,
            KnownServersTableModel.LATEST_USER
         };
      }
   }
}
