/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include "SourceXmlDataStructure.h"
#include "OperationEngineContext.h"

using namespace std;
using namespace konga;

void SourceXmlDataStructure::doPreTransformation(const OperationEngineContext& context) const {
	CopySourceSchemaFilesToTmpDir(context.getLocalTmpDirForOperation(), context.getTranDbConnection());
}

