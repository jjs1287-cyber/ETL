/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>

#include "TransformationDataStoreInstance.h"

using namespace konga;
using std::string;

string konga::dataStoreInstanceTypeName(TransformationDataStoreInstanceType t) {
	switch(t) {
		case TransformationDataStoreInstanceType_SOURCE:
			return string("Source");
		case TransformationDataStoreInstanceType_LINK:
			return string("Link");
		case TransformationDataStoreInstanceType_TARGET:
			return string("Target");
		default:
			throw InternalErrorException("Invalid TransformationDataStoreInstanceType sent to dataStoreInstanceTypeName.");
	};
}

int konga::dataStoreInstanceTypeAsInt(TransformationDataStoreInstanceType t) {
	if ( t < TransformationDataStoreInstanceType_SOURCE || t >= TransformationDataStoreInstanceType_INVALID )
		throw InternalErrorException("Invalid TransformationDataStoreInstanceType sent to dataStoreInstanceTypeAsInt.");

	return static_cast<int>(t);
}

TransformationDataStoreInstanceType konga::fromInt(int t) {
	if ( t < TransformationDataStoreInstanceType_SOURCE || t>= TransformationDataStoreInstanceType_INVALID )
		throw InternalErrorException("Invalid TransformationDataStoreInstanceType number passed to konga::fromInt: " + toString(t));
	return static_cast<TransformationDataStoreInstanceType>(t);
}

TransformationDataStoreInstance::TransformationDataStoreInstance(void) : transformationId(0), operationId(0), originalSourceNumber(0), chunkNumber(0) {}

string TransformationDataStoreInstance::getFileOfType(TransformationDataStoreInstanceType t) const {
	switch(t) {
		case TransformationDataStoreInstanceType_SOURCE:
			return this->sourceFile;
		case TransformationDataStoreInstanceType_LINK:
			return this->linkFile;
			break;
		case TransformationDataStoreInstanceType_TARGET:
			return this->targetFile;
			break;
		default:
			throw InternalErrorException("Error: Unknown data file type: " + toString(dataStoreInstanceTypeAsInt(t)));
	};
}

