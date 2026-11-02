package org.etlforge.integration.client.ui.project.location;

import java.io.File;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;
import org.etlforge.util.version.Version;

public final class ProjectLocationsTableModel extends TypedAbstractKongaTableModel<ProjectLocation> {
   public static final TableColumnMetaData NAME = new TableColumnMetaData(
      ProjectUiResources.getString("Project.Name"), ProjectLocationsTableModel.ProjectNameTableItem.class, false, 200
   );
   public static final TableColumnMetaData LOCATION = new TableColumnMetaData(
      ProjectUiResources.getString("Project.Location"), ProjectLocationsTableModel.ProjectFileTableItem.class, false, 300
   );
   public static final TableColumnMetaData ACCESSED = new TableColumnMetaData(ProjectUiResources.getString("Project.LastAccessed"), DateCell.class, false, 100);
   public static final TableColumnMetaData REQUIRES_VERSION = new TableColumnMetaData(
      ProjectUiResources.getString("Project.RequiresVersion"), Version.class, false, 50
   );
   private static final TableColumnMetaData[] COLUMNS = new TableColumnMetaData[]{NAME, LOCATION, ACCESSED, REQUIRES_VERSION};

   public ProjectLocationsTableModel() {
      super(ProjectLocation.class, COLUMNS);
   }

   static int getTotalWidth() {
      int var0 = 0;

      for (TableColumnMetaData var4 : COLUMNS) {
         var0 += var4.getWidth();
      }

      return var0;
   }

   protected Object[] getColumnDataForTypedObject(ProjectLocation var1) {
      return new Object[]{
         new ProjectLocationsTableModel.ProjectNameTableItem(var1.getProjectName()),
         new ProjectLocationsTableModel.ProjectFileTableItem(var1.getLocation()),
         new DateCell(var1.getLastAccessed()),
         var1.getVersionRequired()
      };
   }

   private static class ProjectFileTableItem implements ListDecorator, Comparable<ProjectLocationsTableModel.ProjectFileTableItem> {
      private final File file;

      public ProjectFileTableItem(File var1) {
         this.file = var1.getParentFile();
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         String var3 = this.toString();
         var1.setText(var3);
         var1.setToolTip(var3);
         var1.setIcon(null);
      }

      public int compareTo(ProjectLocationsTableModel.ProjectFileTableItem var1) {
         return this.file.compareTo(var1.file);
      }

      @Override
      public String toString() {
         return this.file.toString();
      }
   }

   private static class ProjectNameTableItem implements ListDecorator, Comparable<ProjectLocationsTableModel.ProjectNameTableItem> {
      private final String name;

      public ProjectNameTableItem(String var1) {
         this.name = var1;
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.toString());
         var1.setIcon(ProjectUiResources.PROJECT_16);
         var1.setToolTip(this.toString());
      }

      public int compareTo(ProjectLocationsTableModel.ProjectNameTableItem var1) {
         return this.name.compareTo(var1.name);
      }

      @Override
      public String toString() {
         return this.name;
      }
   }
}
