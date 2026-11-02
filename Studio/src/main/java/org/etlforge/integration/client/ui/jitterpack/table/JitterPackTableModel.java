package org.etlforge.integration.client.ui.jitterpack.table;

import java.util.Date;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.ui.widget.table.FileSizeColumn;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;
import org.etlforge.util.version.Version;

public final class JitterPackTableModel extends TypedAbstractKongaTableModel<JitterPack> {
   public static final TableColumnMetaData LOCATION = new TableColumnMetaData(
      Strings.get("Table.Column.Location"), JitterPackLocationTableItem.class, false, 300
   );
   public static final TableColumnMetaData PROJECT = new TableColumnMetaData(Strings.get("Table.Column.Project"), String.class, false, 150);
   public static final TableColumnMetaData FILE_SIZE = new TableColumnMetaData(Strings.get("Table.Column.FileSize"), FileSizeColumn.class, false, 75);
   public static final TableColumnMetaData CREATED_AT = new TableColumnMetaData(Strings.get("Table.Column.Created"), Date.class, false, 100);
   public static final TableColumnMetaData VERSION = new TableColumnMetaData(Strings.get("Table.Column.Version"), Version.class, false, 100);
   public static final TableColumnMetaData ENCRYPTED = new TableColumnMetaData(Strings.get("Table.Column.Encrypted"), Boolean.class, false, 100);
   public static final TableColumnMetaData READ_ONLY = new TableColumnMetaData(Strings.get("Table.Column.ReadOnly"), Boolean.class, false, 100);
   private static final TableColumnMetaData[] COLUMNS_LARGE = new TableColumnMetaData[]{
      LOCATION, PROJECT, FILE_SIZE, CREATED_AT, VERSION, ENCRYPTED, READ_ONLY
   };
   private static final TableColumnMetaData[] COLUMNS_SMALL = new TableColumnMetaData[]{LOCATION, PROJECT, CREATED_AT, VERSION, ENCRYPTED, READ_ONLY};
   private final boolean small;

   public JitterPackTableModel(boolean var1) {
      super(JitterPack.class, createColumnMetaData(var1));
      this.small = var1;
   }

   private static TableColumnMetaData[] createColumnMetaData(boolean var0) {
      return var0 ? (TableColumnMetaData[])COLUMNS_SMALL.clone() : (TableColumnMetaData[])COLUMNS_LARGE.clone();
   }

   protected Object[] getColumnDataForTypedObject(JitterPack var1) {
      return this.small ? this.createSmallRow(var1) : this.createLargeRow(var1);
   }

   private Object[] createSmallRow(JitterPack var1) {
      JitterPackDescriptor var2 = var1.getDescriptor();
      return new Object[]{
         new JitterPackLocationTableItem(var1.getFile()),
         var2.getProjectName(),
         var2.createdAt(),
         var2.getVersionInfo().minimumRequired(),
         var2.isEncrypted(),
         var2.isPackedProjectReadOnly()
      };
   }

   private Object[] createLargeRow(JitterPack var1) {
      JitterPackDescriptor var2 = var1.getDescriptor();
      return new Object[]{
         new JitterPackLocationTableItem(var1.getFile()),
         var2.getProjectName(),
         var2.createdAt(),
         var2.getVersionInfo().minimumRequired(),
         var2.isEncrypted(),
         var2.isPackedProjectReadOnly()
      };
   }
}
