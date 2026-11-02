/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include "NoSource.h"

using namespace std;
using namespace konga;

namespace {
	void AddEmpty(ProcessedFiles& files) {
		                                                                                   
		if ( files.empty() ) {
			files.add(ProcessedFile());
		}
	}
}

void NoSource::doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
	AddEmpty(files);
}

void NoSource::getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
	AddEmpty(files);
}
