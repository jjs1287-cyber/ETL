/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_SOURCE_TARGET_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_SOURCE_TARGET_H

#include <string>
#include <vector>
#include <map>

#include "SpecialHandlingDeployCrypto.h"
#include "ItemWarningHolder.h"

namespace konga {
	class GuidToIdMapper;

namespace interchange_deploy {

	class DatabaseDriverManager;

	class SpecialHandlingDeploySourceTarget : public Crypto, public ItemWarningHolder {
	public:
		SpecialHandlingDeploySourceTarget(const xsd__string_Array& columnNames,
			const DatabaseDriverManager& databaseDriverManager,
			const InterchangeDataTableStruct& jmsLocationProperties,
			GuidToIdMapper& guidToIdMapper,
			DbConnection& dbConnection,
			bool bIsSource);
		virtual ~SpecialHandlingDeploySourceTarget(void);

		virtual bool HandleRow(InterchangeDataRowStruct& row);

		virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row);

		void UpdateJmsLocationProperties();

		bool JmsLocationsWereUpdated() const;

	private:
		void HandleJms();

	private:
		const DatabaseDriverManager& m_databaseDriverManager;
		const xsd__string_Array& m_columnNames;
		bool m_bIsSource;
		bool m_bDidEncryption;
		bool m_bJmsLocationsWereUpdated;
		int m_iEntityTypeId;
		int m_iLocationTypeId;	                     
		unsigned int m_iConnectStringColPos;
		unsigned int m_iPwdStringColPos;
		unsigned int m_iIdColPos;
		unsigned int m_iLoginColPos;
		unsigned int m_iDriverColPos;
		unsigned int m_iLocationTypeColPos;	                                  
		unsigned int m_iDriverTypeColPos;
		std::string m_stGuid;
		std::string m_stLogin;
		typedef std::map<std::string, std::vector<const InterchangeDataRowStruct*> > JmsLocationMapT;
		JmsLocationMapT m_jmsLocationPropertiesMap;	                                            
		GuidToIdMapper& m_guidToIdMapper;
		DbConnection& m_dbConnection;
		std::vector< std::vector<std::string> > m_vJmsLocationProperties;
	};

	class SpecialHandlingDeploySource : public SpecialHandlingDeploySourceTarget {
	public:
		SpecialHandlingDeploySource(const xsd__string_Array& columnNames,
			const DatabaseDriverManager& databaseDriverManager,
			const InterchangeDataTableStruct& jmsLocationProperties,
			GuidToIdMapper& guidToIdMapper,
			DbConnection& dbConnection) :
		SpecialHandlingDeploySourceTarget(columnNames, databaseDriverManager,
			jmsLocationProperties, guidToIdMapper, dbConnection, true) {}
		virtual ~SpecialHandlingDeploySource() {}
	};

	class SpecialHandlingDeployTarget : public SpecialHandlingDeploySourceTarget {
	public:
		SpecialHandlingDeployTarget(const xsd__string_Array& columnNames,
			const DatabaseDriverManager& databaseDriverManager,
			const InterchangeDataTableStruct& jmsLocationProperties,
			GuidToIdMapper& guidToIdMapper,
			DbConnection& dbConnection);
		virtual ~SpecialHandlingDeployTarget() {}

	virtual bool HandleRow(InterchangeDataRowStruct& row);

	                                                                   
	virtual bool Update(DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

	private:
		int m_iTargetIdColPos;
		int m_iTargetTargetIdColPos;
		typedef std::map<std::string, std::string> TargetGuid_TargetTargetGuidMapT;
		TargetGuid_TargetTargetGuidMapT m_targetsToUpdate;
	};
}
}

#endif                                                    

