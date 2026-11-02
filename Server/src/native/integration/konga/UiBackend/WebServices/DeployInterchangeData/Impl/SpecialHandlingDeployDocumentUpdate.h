/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SPECIAL_HANDLING_DOCUMENT_UPDATE
#define KONGA_WS_DEPLOY_SPECIAL_HANDLING_DOCUMENT_UPDATE

#include <vector>
#include <string>

#include <konga/Utils/CrException.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "DeployUtils.h"
#include "SpecialHandlingDeploy.h"


namespace konga {
namespace interchange_deploy {

	class SpecialHandlingDeployDocumentUpdate : public SpecialHandlingDeploy {
	public:
		SpecialHandlingDeployDocumentUpdate(const xsd__string_Array& columnNames,
			GuidToIdMapper& guidToIdMapper) : m_guidToIdMapper(guidToIdMapper) {
			std::vector<std::string> vColumnNamesToGet(1);
			vColumnNamesToGet[0] = "document_id";
			vColPosT tmp = GetFieldIndexes(columnNames, vColumnNamesToGet);
			m_iColPos = tmp[0];
			UIBACKEND_TRACE("SpecialHandlingDeployDocumentUpdate() finished");
		}
		~SpecialHandlingDeployDocumentUpdate(void) {}

		bool HandleRow(InterchangeDataRowStruct& row) {
			const NullableString& docId = row.m_vFields.at(m_iColPos);
			if ( docId != 0 ) {
				const string& stGuid = docId.str();
				long lDocId = m_guidToIdMapper.GetId(stGuid, DocumentEntityId);
				if ( lDocId == GuidToIdMapper::invalid_id )
					throw konga::CrException("Internal error: Invalid GUID passed to Text Document Update code. This should not happen!");
				m_vUpdatedDocumentIds.push_back(lDocId);
			}

			return true;
		}

		std::vector<long> GetUpdatedDocumentIds() const {
			return m_vUpdatedDocumentIds;
		}

	private:
		unsigned int m_iColPos;
		std::vector<long> m_vUpdatedDocumentIds;
		GuidToIdMapper& m_guidToIdMapper;
	};
}
}

#endif                                                    

