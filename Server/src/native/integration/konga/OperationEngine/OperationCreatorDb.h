/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_ENGINE_OPERATION_CREATOR_H
#define KONGA_OPERATION_ENGINE_OPERATION_CREATOR_H

#include <konga/OperationObjects/LocationCreatorDb.h>

namespace konga {
	class Operation;
	class OperationDataElementHolder;
	class PipelinePluginSelector;
	class EmailMessage;
	class OperationContext;

	                                                                                                                  
                                                                                     
    
                                                        
    
    
	class OperationCreatorDb : public LocationCreatorDb {
	public:
		OperationCreatorDb(void) {}
		~OperationCreatorDb(void) {}

		                                                          
		  

		Operation* getOperation(long lOperationId, DbConnection& dbConnection, OperationDataElementHolder& deHolder, OperationContext& context,
			PipelinePluginSelector* pPluginSelector, bool bDeep = true);
	};
}

#endif                                              

