/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATIONS_LOG_PROGRESS_H
#define KONGA_OPERATIONS_LOG_PROGRESS_H

#include <string>
#include <map>

namespace konga {

	class DbConnection;

	class OperationProgress {
	public:
		OperationProgress(DbConnection& dbConnection, long long llOperationInstanceId);
		~OperationProgress(void);

		void UpdateProgress(const std::string& stSection, const std::string& stMessage, int percentage_done, long long estimated_time_left);

	private:
		bool EntryKnownToExists();

	private:
		bool m_bEntryExists;
		DbConnection& m_dbConnection;
		const long long m_llOperationInstanceId;
	};
}

#endif                                   

