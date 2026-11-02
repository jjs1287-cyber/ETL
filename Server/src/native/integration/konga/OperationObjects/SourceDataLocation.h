/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_SOURCE_DATA_LOCATION_H
#define KONGA_OPERATION_SOURCE_DATA_LOCATION_H

#include <string>

namespace konga {
	class OperationEngineContext;
	class OperationContext;
	class OperationDataElementHolder;
	class OperationProgress;
	class ProcessedFiles;
	class PipelinePluginSelector;
	class UserInfo;
	class DbConnection;
	class KeyValues;

	class SourceDataLocation {
	public:
		SourceDataLocation(void);
		virtual ~SourceDataLocation(void);
        void finishSourceFiles(const OperationEngineContext& context, ProcessedFiles& files) const;

		                                                                                    
		virtual void doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const = 0;

		                                                                                    
		virtual void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const = 0;

		                                                                             
		virtual void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const = 0;

		virtual void doPreSourceTransfer(OperationContext& context, OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		virtual void doPostSourceTransfer(OperationContext& context, OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		long getSourceId() const {
			return m_SourceData.m_lSourceId;
		}

		std::string getSourceName() const {
			return m_SourceData.m_sSourceName;
		}

		bool userHasReadPermission(const UserInfo& user, DbConnection* pTranDb = 0) const;
		bool userHasExecutePermission(const UserInfo& user, DbConnection* pTranDb = 0) const;

	protected:
		
		long getIgnoreFirstLines() const;
		long getIgnoreLastLines() const;

	protected:
		friend class LocationCreatorDb;
		friend class OperationObjectCreatorKeyValues;

		struct SourceDataStruct  {
			SourceDataStruct(void) : m_lSourceId(0), m_lIgnoreFirst(0),
				m_lIgnoreLast(0), m_lDBTypeId(-1) {}

			                       
			long m_lSourceId;
			std::string m_sSourceName;
			long m_lIgnoreFirst;
			long m_lIgnoreLast;
			long m_lDBTypeId;
			std::string m_stRenameFileTo;

			                                         
			KeyValues* m_pExtendedProperties;
		};

		PipelinePluginSelector* m_pPluginSelector;
		SourceDataStruct m_SourceData;
	};
}

#endif                                          

