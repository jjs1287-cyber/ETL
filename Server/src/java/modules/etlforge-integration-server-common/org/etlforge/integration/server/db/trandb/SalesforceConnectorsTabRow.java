package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.db.ColumnData;

public final class SalesforceConnectorsTabRow implements TranDbTableRow {
   private final ColumnData<Integer> salesforceconnector_id = ColumnData.newIntColumn("salesforceconnector_id");
   private final ColumnData<String> salesforceconnector_name = ColumnData.newStringColumn("salesforceconnector_name");
   private final ColumnData<String> api_version = ColumnData.newStringColumn("api_version");
   private final ColumnData<String> server_host = ColumnData.newStringColumn("server_host");
   private final ColumnData<String> user_name = ColumnData.newStringColumn("user_name");
   private final ColumnData<String> salesforce_password = ColumnData.newStringColumn("salesforce_password");
   private final ColumnData<String> security_token = ColumnData.newStringColumn("security_token");
   private final ColumnData<String> auth_type = ColumnData.newStringColumn("auth_type"); public String get_auth_type() { return this.auth_type.getValue(); }
private final ColumnData<String> instance_url = ColumnData.newStringColumn("instance_url"); public String get_instance_url() { return this.instance_url.getValue(); }
private final ColumnData<String> client_id = ColumnData.newStringColumn("client_id"); public String get_client_id() { return this.client_id.getValue(); }
private final ColumnData<String> client_secret = ColumnData.newStringColumn("client_secret"); public String get_client_secret() { return this.client_secret.getValue(); }
private final ColumnData<Integer> sandbox = ColumnData.newIntColumn("sandbox");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.salesforceconnector_id.fetchValue(var1);
      this.salesforceconnector_name.fetchValue(var1);
      this.api_version.fetchValue(var1);
      this.server_host.fetchValue(var1);
      this.user_name.fetchValue(var1);
      this.salesforce_password.fetchValue(var1);
      this.security_token.fetchValue(var1);
      this.sandbox.fetchValue(var1);this.auth_type.fetchValue(var1);
this.instance_url.fetchValue(var1);
this.client_id.fetchValue(var1);
this.client_secret.fetchValue(var1);

   }

   public Integer get_salesforceconnector_id() {
      return this.salesforceconnector_id.getValue();
   }

   public String get_salesforceconnector_name() {
      return this.salesforceconnector_name.getValue();
   }

   public String get_api_version() {
      return this.api_version.getValue();
   }

   public String get_server_host() {
      return this.server_host.getValue();
   }

   public String get_user_name() {
      return this.user_name.getValue();
   }

   public String get_salesforce_password() {
      return this.salesforce_password.getValue();
   }

   public String get_security_token() {
      return this.security_token.getValue();
   }

   public Integer get_sandbox() {
      return this.sandbox.getValue();
   }
}
