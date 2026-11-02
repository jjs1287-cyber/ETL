/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef OPERATION_TEST_OPERATION_DATA_FILE_H
#define OPERATION_TEST_OPERATION_DATA_FILE_H

#include <string>

namespace konga {

	enum TestOperationDataFileType {
		TestOperationDataFileType_Source=1,
		TestOperationDataFileType_Target=2,
		TestOperationDataFileType_WSRequest=3,
		TestOperationDataFileType_WSResponse=4,
	};

	class TestOperationDataFile_deleteme {
	public:
		TestOperationDataFile_deleteme(TestOperationDataFileType type, const std::string& stLocalTmpFileName)
			: m_type(type), m_stLocalTmpFileName(stLocalTmpFileName) {}
		~TestOperationDataFile_deleteme(void) {}

		TestOperationDataFileType getType() const {
			return m_type;
		}

		std::string getLocalTmpFileName() const {
			return m_stLocalTmpFileName;
		}

	private:
		TestOperationDataFileType m_type;
		std::string m_stLocalTmpFileName;
	};

}

#endif                                        

