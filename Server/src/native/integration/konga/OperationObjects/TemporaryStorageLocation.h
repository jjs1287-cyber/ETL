/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_TEMPORARY_STORAGE_LOCATION_H
#define KONGA_OPERATION_TEMPORARY_STORAGE_LOCATION_H

#include "DataLocation.h"

#include <string>
#include <vector>

namespace konga {

	struct direntry {
		                                                         
		std::string absolutePath;
		std::string relativePath;
	};
	typedef std::vector<direntry> vdirentry;

	class TemporaryStorageLocation : public DataLocation {
	public:
		TemporaryStorageLocation(void) {
		}
		virtual ~TemporaryStorageLocation(void) {
		}

		static std::string ToCanonicPath(const std::string&);

	protected:
		void ValidatePath(const std::string& path) const;

		std::string GetTmpDirectoryWithSlash() const;

		std::string GetRelativePath() const;

		                                                                                                    
		                                                 
		                                                         
		std::string GetAbsoluteRootPathWithSlash() const;

		void CreateParentDirectory(const std::string& file_path) const;
	};
}
#endif                                                

