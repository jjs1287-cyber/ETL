/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_DATA_STORE_INSTANCE_H
#define TRANSFORMATION_DATA_STORE_INSTANCE_H

#include <string>

namespace konga {

	                                     
	enum TransformationDataStoreInstanceType {
		TransformationDataStoreInstanceType_SOURCE = 0,
		TransformationDataStoreInstanceType_LINK = 1,
		TransformationDataStoreInstanceType_TARGET = 2,
		TransformationDataStoreInstanceType_INVALID = 3,
	};

	std::string dataStoreInstanceTypeName(TransformationDataStoreInstanceType t);

	int dataStoreInstanceTypeAsInt(TransformationDataStoreInstanceType t);

	TransformationDataStoreInstanceType fromInt(int t);

	                                                                                   
	class TransformationDataStoreInstance {
	public:
		TransformationDataStoreInstance(void);
		~TransformationDataStoreInstance(void) {};

		long transformationId, operationId;
		long originalSourceNumber, chunkNumber;

		std::string GUID;
		std::string sourceFile, targetFile, linkFile;

		std::string getFileOfType(TransformationDataStoreInstanceType t) const;

	};
}

#endif                                        

