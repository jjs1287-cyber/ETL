/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_LOCATION_CREATOR_H
#define KONGA_OPERATION_LOCATION_CREATOR_H

#include <konga/Db/TranDb/TRANSFORMATIONSTAB_V.h>

namespace konga {

	class Transformation;
	class SourceDataLocation;
	class TargetDataLocation;
	class DbConnection;
	class SourceDataStructure;
	class TargetDataStructure;
	class OperationDataElementHolder;
	class PipelinePluginSelector;
	class JmsSource;

	                                                                                                                  
                                                                                     
    
                                                        
    
    
	class LocationCreatorDb {
	public:
		LocationCreatorDb(void) {}
		virtual ~LocationCreatorDb(void) {}

		                                                          
		  

		Transformation* getTransformation(long lTransformationId, DbConnection& dbConnection,
			OperationDataElementHolder& dataElementHolder, PipelinePluginSelector* pPluginSelector);

		                                                                                                                                  
		SourceDataLocation* getSourceLocation(long lSourceId, DbConnection& dbConnection,
			OperationDataElementHolder& dataElementHolder, PipelinePluginSelector* pPluginSelector, bool bAllowOverride);
		TargetDataLocation* getTargetLocation(long lTargetId, DbConnection& dbConnection,
			OperationDataElementHolder& dataElementHolder, PipelinePluginSelector* pPluginSelector,
			const Transformation* const pTransformation);

		bool usesPreviousTargetAsSource(long lOperationId, DbConnection& dbConnection);

	private:
		SourceDataStructure* getSourceStructure(TranDb::TRANSFORMATIONSTAB_V* pTranTab,
			DbConnection& dbConnection, OperationDataElementHolder& dataElementHolder, PipelinePluginSelector* pPluginSelector);
		TargetDataStructure* getTargetStructure(TranDb::TRANSFORMATIONSTAB_V* pTranTab,
			DbConnection& dbConnection, OperationDataElementHolder& dataElementHolder, PipelinePluginSelector* pPluginSelector);

		JmsSource* getJmsSource(OperationDataElementHolder& dataElementHolder);
	};
}

#endif                                      

