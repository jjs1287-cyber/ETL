package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.db.ColumnData;

public class SourcesTabRow implements TranDbTableRow {
   private final ColumnData<Integer> source_id = ColumnData.newIntColumn("source_id");
   private final ColumnData<String> source_name = ColumnData.newStringColumn("source_name");
   private final ColumnData<Integer> source_type_id = ColumnData.newIntColumn("source_type_id");
   private final ColumnData<Integer> document_id = ColumnData.newIntColumn("document_id");
   private final ColumnData<String> locator = ColumnData.newStringColumn("locator");
   private final ColumnData<String> path = ColumnData.newStringColumn("path");
   private final ColumnData<String> file_name = ColumnData.newStringColumn("file_name");
   private final ColumnData<Integer> timeout = ColumnData.newIntColumn("timeout");
   private final ColumnData<String> login = ColumnData.newStringColumn("login");
   private final ColumnData<String> password = ColumnData.newStringColumn("password");
   private final ColumnData<String> filter = ColumnData.newStringColumn("filter");
   private final ColumnData<Integer> ignore_first = ColumnData.newIntColumn("ignore_first");
   private final ColumnData<Integer> ignore_last = ColumnData.newIntColumn("ignore_last");
   private final ColumnData<Integer> transfer_type = ColumnData.newIntColumn("transfer_type");
   private final ColumnData<String> encrypt_text = ColumnData.newStringColumn("encrypt_text");
   private final ColumnData<Integer> encrypt_choice = ColumnData.newIntColumn("encrypt_choice");
   private final ColumnData<Integer> drivertype = ColumnData.newIntColumn("drivertype");
   private final ColumnData<Integer> dbtype_id = ColumnData.newIntColumn("dbtype_id");
   private final ColumnData<Integer> port = ColumnData.newIntColumn("port");
   private final ColumnData<Integer> passive_mode = ColumnData.newIntColumn("passive_mode");
   private final ColumnData<String> dbname = ColumnData.newStringColumn("dbname");
   private final ColumnData<String> connectstring = ColumnData.newStringColumn("connectstring");
   private final ColumnData<String> renamefileto = ColumnData.newStringColumn("renamefileto");
   private final ColumnData<Integer> secured = ColumnData.newIntColumn("secured");
   private final ColumnData<String> httpurl = ColumnData.newStringColumn("httpurl");
   private final ColumnData<String> certificatename = ColumnData.newStringColumn("certificatename");
   private final ColumnData<String> optional_connectstring = ColumnData.newStringColumn("optional_connectstring");
   private final ColumnData<String> odbc_unc_userid = ColumnData.newStringColumn("odbc_unc_userid");
   private final ColumnData<String> odbc_unc_password = ColumnData.newStringColumn("odbc_unc_password");
   private final ColumnData<Integer> http_verb = ColumnData.newIntColumn("http_verb");
   private final ColumnData<String> jms_message_selector = ColumnData.newStringColumn("jms_message_selector");
   private final ColumnData<String> driver = ColumnData.newStringColumn("driver");
   private final ColumnData<String> dsn = ColumnData.newStringColumn("dsn");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.source_id.fetchValue(var1);
      this.source_name.fetchValue(var1);
      this.source_type_id.fetchValue(var1);
      this.document_id.fetchValue(var1);
      this.locator.fetchValue(var1);
      this.path.fetchValue(var1);
      this.file_name.fetchValue(var1);
      this.timeout.fetchValue(var1);
      this.login.fetchValue(var1);
      this.password.fetchValue(var1);
      this.filter.fetchValue(var1);
      this.ignore_first.fetchValue(var1);
      this.ignore_last.fetchValue(var1);
      this.transfer_type.fetchValue(var1);
      this.encrypt_text.fetchValue(var1);
      this.encrypt_choice.fetchValue(var1);
      this.drivertype.fetchValue(var1);
      this.dbtype_id.fetchValue(var1);
      this.port.fetchValue(var1);
      this.passive_mode.fetchValue(var1);
      this.dbname.fetchValue(var1);
      this.connectstring.fetchValue(var1);
      this.renamefileto.fetchValue(var1);
      this.secured.fetchValue(var1);
      this.httpurl.fetchValue(var1);
      this.certificatename.fetchValue(var1);
      this.optional_connectstring.fetchValue(var1);
      this.odbc_unc_userid.fetchValue(var1);
      this.odbc_unc_password.fetchValue(var1);
      this.http_verb.fetchValue(var1);
      this.jms_message_selector.fetchValue(var1);
      this.driver.fetchValue(var1);
      this.dsn.fetchValue(var1);
   }

   public Integer get_source_id() {
      return this.source_id.getValue();
   }

   public String get_source_name() {
      return this.source_name.getValue();
   }

   public Integer get_source_type_id() {
      return this.source_type_id.getValue();
   }

   public Integer get_document_id() {
      return this.document_id.getValue();
   }

   public String get_locator() {
      return this.locator.getValue();
   }

   public String get_path() {
      return this.path.getValue();
   }

   public String get_file_name() {
      return this.file_name.getValue();
   }

   public Integer get_timeout() {
      return this.timeout.getValue();
   }

   public String get_login() {
      return this.login.getValue();
   }

   public String get_password() {
      return this.password.getValue();
   }

   public String get_filter() {
      return this.filter.getValue();
   }

   public Integer get_ignore_first() {
      return this.ignore_first.getValue();
   }

   public Integer get_ignore_last() {
      return this.ignore_last.getValue();
   }

   public Integer get_transfer_type() {
      return this.transfer_type.getValue();
   }

   public String get_encrypt_text() {
      return this.encrypt_text.getValue();
   }

   public Integer get_encrypt_choice() {
      return this.encrypt_choice.getValue();
   }

   public Integer get_drivertype() {
      return this.drivertype.getValue();
   }

   public Integer get_dbtype_id() {
      return this.dbtype_id.getValue();
   }

   public Integer get_port() {
      return this.port.getValue();
   }

   public Integer get_passive_mode() {
      return this.passive_mode.getValue();
   }

   public String get_dbname() {
      return this.dbname.getValue();
   }

   public String get_connectstring() {
      return this.connectstring.getValue();
   }

   public String get_renamefileto() {
      return this.renamefileto.getValue();
   }

   public Integer get_secured() {
      return this.secured.getValue();
   }

   public String get_httpurl() {
      return this.httpurl.getValue();
   }

   public String get_certificatename() {
      return this.certificatename.getValue();
   }

   public String get_optional_connectstring() {
      return this.optional_connectstring.getValue();
   }

   public String get_odbc_unc_userid() {
      return this.odbc_unc_userid.getValue();
   }

   public String get_odbc_unc_password() {
      return this.odbc_unc_password.getValue();
   }

   public Integer get_http_verb() {
      return this.http_verb.getValue();
   }

   public String get_jms_message_selector() {
      return this.jms_message_selector.getValue();
   }

   public String get_driver() {
      return this.driver.getValue();
   }

   public String get_dsn() {
      return this.dsn.getValue();
   }
}
