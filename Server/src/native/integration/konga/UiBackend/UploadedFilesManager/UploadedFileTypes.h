/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_UPLOADED_FILES_MANAGER_UPLOADED_FILES_TYPES_H
#define ETLFORGE_UPLOADED_FILES_MANAGER_UPLOADED_FILES_TYPES_H

namespace konga {
	enum UploadedFileType {
		INVALID_FILE_TYPE = -1,
		XML_SCHEMA_FILE_TYPE = 0,
		XML_DTD_FILE_TYPE = 1,
		WSDL_FILE_TYPE = 2
	};

	enum UploadedWsdlFileType {
		INVALID_WSDL_FILE_TYPE = -1,
		WSDL_WSDL_FILE_TYPE = 0,
		INCLUDED_SCHEMA_WSDL_FILE_TYPE = 1
	};
}

#endif                                                          

