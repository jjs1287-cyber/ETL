/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TARGET_DATA_LOCATION_H
#define KONGA_TARGET_DATA_LOCATION_H

#include <string>

namespace konga {

	class OperationContext;
	class OperationDataElementHolder;
	class ProcessedFiles;
	class PipelinePluginSelector;
	class UserInfo;
	class DbConnection;

	class TargetDataLocation {
	public:
		TargetDataLocation(void);
		virtual ~TargetDataLocation(void);

		virtual void doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const = 0;

		virtual void doPreTargetTransfer(const OperationContext& context, OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		virtual void doPostTargetTransfer(const OperationContext& context, OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		std::string getTargetName() const {
			return m_TargetData.m_sTargetName;
		}

		long getTargetId() const {
			return m_TargetData.m_lTargetId;
		}

		bool userHasReadPermission(const UserInfo& user, DbConnection* pTranDb = 0) const;
		bool userHasExecutePermission(const UserInfo& user, DbConnection* pTranDb = 0) const;

		friend class LocationCreatorDb;
		friend class OperationObjectCreatorKeyValues;

	protected:
		struct TargetDataStruct {
			                            
			TargetDataStruct(void) :
				m_lTargetId(-1), m_lSmtpMessage(-1), m_lTarget_TargetId(-1) {}
			long m_lTargetId;
			std::string m_sTargetName;
			long m_lSmtpMessage;
			long m_lTarget_TargetId;
			                                                                                                         
		};

		PipelinePluginSelector* m_pPluginSelector;
		TargetDataStruct m_TargetData;
	};
}
#endif                                

