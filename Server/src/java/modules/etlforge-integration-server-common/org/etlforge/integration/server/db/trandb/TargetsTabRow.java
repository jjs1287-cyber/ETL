package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.db.ColumnData;

public class TargetsTabRow implements TranDbTableRow {
   private final ColumnData<Integer> target_id = ColumnData.newIntColumn("target_id");
   private final ColumnData<String> target_name = ColumnData.newStringColumn("target_name");
   private final ColumnData<Integer> source_type_id = ColumnData.newIntColumn("source_type_id");
   private final ColumnData<Integer> document_id = ColumnData.newIntColumn("document_id");
   private final ColumnData<String> locator = ColumnData.newStringColumn("locator");
   private final ColumnData<String> path = ColumnData.newStringColumn("path");
   private final ColumnData<String> file_name = ColumnData.newStringColumn("file_name");
   private final ColumnData<Integer> timeout = ColumnData.newIntColumn("timeout");
   private final ColumnData<String> login = ColumnData.newStringColumn("login");
   private final ColumnData<String> password = ColumnData.newStringColumn("password");
   private final ColumnData<Integer> smtp_message = ColumnData.newIntColumn("smtp_message");
   private final ColumnData<Integer> transfer_type = ColumnData.newIntColumn("transfer_type");
   private final ColumnData<String> encrypt_text = ColumnData.newStringColumn("encrypt_text");
   private final ColumnData<Integer> encrypt_choice = ColumnData.newIntColumn("encrypt_choice");
   private final ColumnData<Integer> drivertype = ColumnData.newIntColumn("drivertype");
   private final ColumnData<String> user_client = ColumnData.newStringColumn("user_client");
   private final ColumnData<String> user_password = ColumnData.newStringColumn("user_password");
   private final ColumnData<String> user_id = ColumnData.newStringColumn("user_id");
   private final ColumnData<String> developer_password = ColumnData.newStringColumn("developer_password");
   private final ColumnData<String> developer_client = ColumnData.newStringColumn("developer_client");
   private final ColumnData<String> developer_id = ColumnData.newStringColumn("developer_id");
   private final ColumnData<String> system_number = ColumnData.newStringColumn("system_number");
   private final ColumnData<String> app_server = ColumnData.newStringColumn("app_server");
   private final ColumnData<String> lang = ColumnData.newStringColumn("lang");
   private final ColumnData<Integer> port = ColumnData.newIntColumn("port");
   private final ColumnData<Integer> passive_mode = ColumnData.newIntColumn("passive_mode");
   private final ColumnData<String> dbname = ColumnData.newStringColumn("dbname");
   private final ColumnData<Integer> target_target_id = ColumnData.newIntColumn("target_target_id");
   private final ColumnData<String> connectstring = ColumnData.newStringColumn("connectstring");
   private final ColumnData<Integer> secured = ColumnData.newIntColumn("secured");
   private final ColumnData<String> httpurl = ColumnData.newStringColumn("httpurl");
   private final ColumnData<String> certificatename = ColumnData.newStringColumn("certificatename");
   private final ColumnData<String> optional_connectstring = ColumnData.newStringColumn("optional_connectstring");
   private final ColumnData<String> odbc_unc_userid = ColumnData.newStringColumn("odbc_unc_userid");
   private final ColumnData<String> odbc_unc_password = ColumnData.newStringColumn("odbc_unc_password");
   private final ColumnData<Integer> http_verb = ColumnData.newIntColumn("http_verb");
   private final ColumnData<String> driver = ColumnData.newStringColumn("driver");
   private final ColumnData<String> dsn = ColumnData.newStringColumn("dsn");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.target_id.fetchValue(var1);
      this.target_name.fetchValue(var1);
      this.source_type_id.fetchValue(var1);
      this.document_id.fetchValue(var1);
      this.locator.fetchValue(var1);
      this.path.fetchValue(var1);
      this.file_name.fetchValue(var1);
      this.timeout.fetchValue(var1);
      this.login.fetchValue(var1);
      this.password.fetchValue(var1);
      this.smtp_message.fetchValue(var1);
      this.transfer_type.fetchValue(var1);
      this.encrypt_text.fetchValue(var1);
      this.encrypt_choice.fetchValue(var1);
      this.drivertype.fetchValue(var1);
      this.user_client.fetchValue(var1);
      this.user_password.fetchValue(var1);
      this.user_id.fetchValue(var1);
      this.developer_password.fetchValue(var1);
      this.developer_client.fetchValue(var1);
      this.developer_id.fetchValue(var1);
      this.system_number.fetchValue(var1);
      this.app_server.fetchValue(var1);
      this.lang.fetchValue(var1);
      this.port.fetchValue(var1);
      this.passive_mode.fetchValue(var1);
      this.dbname.fetchValue(var1);
      this.target_target_id.fetchValue(var1);
      this.connectstring.fetchValue(var1);
      this.secured.fetchValue(var1);
      this.httpurl.fetchValue(var1);
      this.certificatename.fetchValue(var1);
      this.optional_connectstring.fetchValue(var1);
      this.odbc_unc_userid.fetchValue(var1);
      this.odbc_unc_password.fetchValue(var1);
      this.http_verb.fetchValue(var1);
      this.driver.fetchValue(var1);
      this.dsn.fetchValue(var1);
   }

   public Integer get_target_id() {
      return this.target_id.getValue();
   }

   public String get_target_name() {
      return this.target_name.getValue();
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

   public Integer get_smtp_message() {
      return this.smtp_message.getValue();
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

   public String get_user_client() {
      return this.user_client.getValue();
   }

   public String get_user_password() {
      return this.user_password.getValue();
   }

   public String get_user_id() {
      return this.user_id.getValue();
   }

   public String get_developer_password() {
      return this.developer_password.getValue();
   }

   public String get_developer_client() {
      return this.developer_client.getValue();
   }

   public String get_developer_id() {
      return this.developer_id.getValue();
   }

   public String get_system_number() {
      return this.system_number.getValue();
   }

   public String get_app_server() {
      return this.app_server.getValue();
   }

   public String get_lang() {
      return this.lang.getValue();
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

   public Integer get_target_target_id() {
      return this.target_target_id.getValue();
   }

   public String get_connectstring() {
      return this.connectstring.getValue();
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

   public String get_driver() {
      return this.driver.getValue();
   }

   public String get_dsn() {
      return this.dsn.getValue();
   }
}
