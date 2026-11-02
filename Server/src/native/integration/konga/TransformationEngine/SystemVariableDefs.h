/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRANSFORMATION_ENGINE_SYSTEM_VARIABLES_DEFS_H
#define KONGA_TRANSFORMATION_ENGINE_SYSTEM_VARIABLES_DEFS_H

                                                                               

namespace {
	const std::string DB_SRC_PRESERVE_CHAR_WHITESPACE("etlforge.source.db.preserve_char_whitespace");
	const std::string CSV_NULLABLE("etlforge.source.text.csv_nullable");

	const std::string TEXT_SOURCE_ENC("etlforge.source.text.character_encoding");
	const std::string TEXT_TARGET_ENC("etlforge.target.text.character_encoding");

	const std::string ODBC_SOURCE_ENC("etlforge.source.db.character_encoding");
	const std::string ODBC_TARGET_ENC("etlforge.target.db.character_encoding");
	const std::string DB_TARGET_PRE_SQL("etlforge.target.db.pre_target_sql");
}

#endif                                                       
