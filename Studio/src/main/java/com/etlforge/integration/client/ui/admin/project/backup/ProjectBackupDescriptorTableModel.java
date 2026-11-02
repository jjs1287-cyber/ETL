package com.etlforge.integration.client.ui.admin.project.backup;

import java.util.Date;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public class ProjectBackupDescriptorTableModel extends TypedAbstractKongaTableModel<ProjectBackupDescriptor> {
   public static final TableColumnMetaData WHEN = new TableColumnMetaData("Created", Date.class, false, 2);
   public static final TableColumnMetaData WHO = new TableColumnMetaData("User", String.class, false, 2);
   public static final TableColumnMetaData TAG = new TableColumnMetaData("Tag", String.class, false, 5);

   public ProjectBackupDescriptorTableModel(ProjectBackupDescriptor... var1) {
      super(ProjectBackupDescriptor.class, new TableColumnMetaData[]{WHEN, WHO, TAG}, var1);
   }

   protected Object[] getColumnDataForTypedObject(ProjectBackupDescriptor var1) {
      return new Object[]{var1.getDateOfBackup(), var1.getCreatedBy(), var1.getTag()};
   }
}
