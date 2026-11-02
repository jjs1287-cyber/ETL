/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FILE_TYPE_TARGET_LOCATION_H
#define KONGA_OPERATION_FILE_TYPE_TARGET_LOCATION_H

#include <string>

#include "TargetDataLocation.h"

namespace konga {

	class ProcessedFile;
	class RenameFile;

	class FileTypeTargetLocation : public TargetDataLocation {
	public:
		FileTypeTargetLocation(void) {}
		virtual ~FileTypeTargetLocation(void) {}

		                                                                      
		static bool doNotTransfer(const ProcessedFile& file);

		virtual std::string getFileName() const = 0;

		virtual void setOverrideFileName(const std::string& stOverrideFilename) {
			m_stOverrideFileName = stOverrideFilename;
		}

		                                                                       
		                                                                                  
		                          
		virtual void testFileTargetConnection(bool bCopyTestFile) const = 0;

	protected:

		std::string getRenamedRemoteName(const RenameFile& renamer, const ProcessedFile& file) const;

		std::string getOverrideFileName() const {
			return m_stOverrideFileName;
		}

	private:
		std::string m_stOverrideFileName;
	};
}
#endif                                               

