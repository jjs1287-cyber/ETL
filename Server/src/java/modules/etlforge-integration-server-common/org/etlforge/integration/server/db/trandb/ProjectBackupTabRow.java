package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.etlforge.integration.server.db.ColumnData;

public class ProjectBackupTabRow implements TranDbTableRow {
   private final ColumnData<Integer> projectbackup_id = ColumnData.newIntColumn("projectbackup_id");
   private final ColumnData<String> interchangeproject_guid = ColumnData.newStringColumn("interchangeproject_guid");
   private final ColumnData<String> uploadedby_user_guid = ColumnData.newStringColumn("uploadedby_user_guid");
   private final ColumnData<Timestamp> uploadtime = ColumnData.newTimestampColumn("uploadtime");
   private final ColumnData<Long> filesize = ColumnData.newLongColumn("filesize");
   private final ColumnData<String> tag = ColumnData.newStringColumn("tag");
   private final ColumnData<String> uploadedfilename = ColumnData.newStringColumn("uploadedfilename");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.projectbackup_id.fetchValue(var1);
      this.interchangeproject_guid.fetchValue(var1);
      this.uploadedby_user_guid.fetchValue(var1);
      this.uploadtime.fetchValue(var1);
      this.filesize.fetchValue(var1);
      this.tag.fetchValue(var1);
      this.uploadedfilename.fetchValue(var1);
   }

   public Integer get_projectbackup_id() {
      return this.projectbackup_id.getValue();
   }

   public String get_interchangeproject_guid() {
      return this.interchangeproject_guid.getValue();
   }

   public String get_uploadedby_user_guid() {
      return this.uploadedby_user_guid.getValue();
   }

   public Timestamp get_uploadtime() {
      return this.uploadtime.getValue();
   }

   public Long get_filesize() {
      return this.filesize.getValue();
   }

   public String get_tag() {
      return this.tag.getValue();
   }

   public String get_uploadedfilename() {
      return this.uploadedfilename.getValue();
   }
}
