/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ITEM_PROPERTIES_DEFINITIONS_H
#define KONGA_ITEM_PROPERTIES_DEFINITIONS_H

#include <string>

namespace {
	const std::string OPERATION_PROPERTY_SCHEDULE_POLICY("schedule_policy");
	const std::string OPERATION_PROPERTY_CHUNK_SIZE("chunk_size");
	const std::string OPERATION_PROPERTY_SOURCE_CHUNK_NODE("source_chunk_node");
	const std::string OPERATION_PROPERTY_TARGET_CHUNK_NODE("target_chunk_node");
	const std::string OPERATION_PROPERTY_WRITE_LOG("write_log");
	const std::string OPERATION_PROPERTY_MAX_THREADS("max_number_of_threads");
	const std::string OPERATION_PROPERTY_LABEL("label");
	const std::string OPERATION_SCHEDULE_ENABLED("schedule_enabled");
	const std::string OPERATION_PROPERTY_HISTORY_LOG("enable_history_log");
	const std::string OPERATION_PROPERTY_LOG_LEVEL("operation_log_level");

	const std::string TRANSFORMATION_DO_SOURCE_XML_VALIDATION("DoSourceXmlValidation");
	const std::string TRANSFORMATION_DO_TARGET_XML_VALIDATION("DoTargetXmlValidation");
	const std::string TRANSFORMATION_SCHEMA_LOCATION("SchemaLocation");
	const std::string TRANSFORMATION_TRUNCATE_DB_CHAR_TARGETS("TruncateDbCharTargets");
	const std::string TRANSFORMATION_PROPERTY_WRITE_LOG("write_log");
	const std::string TRANSFORMATION_SOURCE_BATCH_NODE_PATH("SourceBatchNodePath");
	const std::string TRANSFORMATION_SOURCE_BATCH_SIZE("SourceBatchSize");
	const std::string NO_SOURCE_DATA_USED_IN_MAPPING("NoSourceDataUsedInMapping");
	const std::string TRANSFORMATION_DO_BATCH_TARGET("doBatchTarget");

	const std::string SCHEDULE_ENABLED("enabled");

	const std::string FTP_SOURCE_PROPERTY_NLST("ftp_nlst");

	const std::string SOURCE_CASE_INSENSITIVE_FILE_FILTER("case_insensitive_file_filter");
	const std::string SOURCE_REGULAR_EXPRESSION_FILE_FILTER("regular_expression_file_filter");

	const std::string DB_LOCATION_TRANSACTION_ISOLATION_LEVEL("db_transaction_isolation_level");
	const std::string DB_LOCATION_BEGIN_QUOTE("db_begin_quote");
	const std::string DB_LOCATION_END_QUOTEKEY("db_end_quote");
	const std::string DB_LOCATION_DRIVER_BEGIN_QUOTE("driver_begin_quote");
	const std::string DB_LOCATION_DRIVER_END_QUOTEKEY("driver_end_quote");
	const std::string DB_FETCH_SIZE("fetch_size");

	const std::string HTTP_LOCATION_ENABLE_CONTENT_ENCODING("enable_content_encoding");
	const std::string HTTP_LOCATION_ENABLE_CHUNKED_TRANSFER_ENCODING("enable_chunked_transfer_encoding");
	const std::string HTTP_LOCATION_SEND_EXPECT_100_CONTINUE("send_expect_100_continue");
	const std::string HTTP_LOCATION_CONTENT_TYPE("http_content_type_header");
	const std::string HTTP_LOCATION_REQUEST_HEADERS("http_request_headers");

	const std::string TEXT_DOCUMENT_QUALIFIER_MODE("qualifier_mode");
	const std::string TEXT_DOCUMENT_EDIFACT_MODE("edifact");

	const std::string WIZARD_SALESFORCE_OPERATION("wizard.salesforce.operation");
}

#endif                                       
