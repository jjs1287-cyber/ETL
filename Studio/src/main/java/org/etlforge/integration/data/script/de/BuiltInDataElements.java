package org.etlforge.integration.data.script.de;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class BuiltInDataElements {
   public static final String WS_RETRIES = "etlforge.web_service_call.number_of_retries";
   public static final String WS_RETRY_INTERVAL = "etlforge.web_service_call.retry_wait_seconds";
   private static final SortedSet<String> dataElements = load();
   private static final SortedSet<String> dataElementsWithPrefix = withPrefix();

   public static SortedSet<String> getBuiltInDataElements() {
      return Sets.newTreeSet(dataElements);
   }

   public static SortedSet<String> getBuiltInDataElementsWithPrefix() {
      return Sets.newTreeSet(dataElementsWithPrefix);
   }

   public static boolean isBuiltInDataElement(String var0) {
      return dataElements.contains(var0);
   }

   public static String getDocumentation(String var0) {
      throw new NotImplementedYetException();
   }

   private static SortedSet<String> load() {
      return Sets.newTreeSet(
         Arrays.asList(
            "etlforge.http.enable_cookies",
            "etlforge.networking.http.query.*",
            "etlforge.networking.http.request.header.*",
            "etlforge.networking.http.request.method",
            "etlforge.networking.http.response.header.*",
            "etlforge.networking.http.response.content_type",
            "etlforge.networking.http.response.status_code",
            "etlforge.networking.peer.ip",
            "etlforge.operation.chunking.warn_on_error",
            "etlforge.operation.error",
            "etlforge.operation.guid",
            "etlforge.operation.last_error",
            "etlforge.operation.log_level",
            "etlforge.operation.name",
            "etlforge.operation.previous.error",
            "etlforge.operation.previous.success",
            "etlforge.operation.write_history",
            "etlforge.scripting.db.auto_commit",
            "etlforge.scripting.db.max_rows",
            "etlforge.scripting.db.rows_affected",
            "etlforge.scripting.db.transaction",
            "etlforge.scripting.ldap.include_dn_in_results",
            "etlforge.scripting.ldap.max_search_results",
            "etlforge.scripting.ldap.return_null_if_no_results",
            "etlforge.scripting.ldap.scope",
            "etlforge.scripting.ldap.use_paged_search",
            "etlforge.scripting.nesting.max",
            "etlforge.scripting.while.max_iterations",
            "etlforge.sfdc.auto_fieldsToNull",
            "etlforge.sfdc.query.record_count",
            "etlforge.sfdc.failure_record_count",
            "etlforge.sfdc.success_record_count",
            "etlforge.source.db.character_encoding",
            "etlforge.source.db.preserve_char_whitespace",
            "etlforge.source.db.schema_name_delimiter",
            "etlforge.source.file_limit",
            "etlforge.source.file_share.file_list_limit",
            "etlforge.source.filename",
            "etlforge.source.filenames",
            "etlforge.source.ftp.transfer_timeout",
            "etlforge.source.sftp.ssh_key_id",
            "etlforge.source.http.ssl_cert_id",
            "etlforge.source.http.transfer_timeout",
            "etlforge.source.http.response.header.*",
            "etlforge.source.locator",
            "etlforge.source.locators",
            "etlforge.source.preserve_char_whitespace",
            "etlforge.source.size",
            "etlforge.source.sizes",
            "etlforge.source.text.character_encoding",
            "etlforge.source.text.csv_nullable",
            "etlforge.source.db.trim",
            "etlforge.target.chunk_size",
            "etlforge.target.chunk_node_name",
            "etlforge.target.chunking_uncombined",
            "etlforge.target.file_count",
            "etlforge.target.db.character_encoding",
            "etlforge.target.db.commit_chunks",
            "etlforge.target.db.include_null_in_sql_statement",
            "etlforge.target.db.no_data_action",
            "etlforge.target.db.pre_target_sql",
            "etlforge.target.db.schema_name_delimiter",
            "etlforge.target.db.transaction",
            "etlforge.target.file_share.create_directories",
            "etlforge.target.ftp.transfer_timeout",
            "etlforge.target.http.form_data",
            "etlforge.target.http.form_data.name",
            "etlforge.target.http.form_data.filename",
            "etlforge.target.http.form_data.ContentType",
            "etlforge.target.http.remove_trailing_linebreaks",
            "etlforge.target.http.ssl_cert_id",
            "etlforge.target.http.transfer_timeout",
            "etlforge.target.http.response.header.*",
            "etlforge.target.sftp.ssh_key_id",
            "etlforge.target.text.character_encoding",
            "etlforge.target.xml.prettify",
            "etlforge.target.xml.include_null_xml",
            "etlforge.target.xml.include_empty_xml",
            "etlforge.target.xml.num_for_bool",
            "etlforge.target.xml.nsprefix",
            "etlforge.text.qualifier_required",
            "etlforge.transformation.auto_streaming",
            "etlforge.transformation.chunk_number",
            "etlforge.transformation.chunking",
            "etlforge.transformation.jbxmlparser",
            "etlforge.transformation.name",
            "etlforge.transformation.source.check_null_characters",
            "etlforge.transformation.thread_number",
            "etlforge.transformation.timing_on",
            "etlforge.transformation.total_chunks",
            "etlforge.transformation.total_threads",
            "etlforge.transformation.trim_extra_linebreaks",
            "etlforge.transformation.disable_normalization",
            "etlforge.web_service_call.max_redirs",
            "etlforge.web_service_call.ssl_cert_id",
            "etlforge.web_service_call.number_of_retries",
            "etlforge.web_service_call.retry_wait_seconds",
            "etlforge.web_service_call.sync_response",
            "etlforge.web_service_call.time_out"
         )
      );
   }

   private static SortedSet<String> withPrefix() {
      TreeSet var0 = Sets.newTreeSet();

      for (String var2 : dataElements) {
         var0.add(GlobalDE.forScript(var2));
      }

      return var0;
   }

   private BuiltInDataElements() {
   }
}
