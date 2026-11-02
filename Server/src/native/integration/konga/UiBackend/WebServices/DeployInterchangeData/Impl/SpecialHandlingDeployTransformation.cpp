/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <vector>
#include <sstream>		               

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             
#include <konga/Utils/PortDefs.h>		                   

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>

#include "../InterchangeDataRow.hpp"
#include "SpecialHandlingDeployTransformation.h"

using namespace std;
using namespace konga;
using namespace konga::interchange_deploy;

bool SpecialHandlingTransformation::HandleRow(InterchangeDataRowStruct& row) {
	bool bRowModified = false;
	vColPosT::const_iterator citColPos = m_vDocumentColPosToHandle.begin();

	vFieldsT& fields = row.m_vFields;
	const int nFields = fields.size();

	                                                      
	for(; citColPos != m_vDocumentColPosToHandle.end(); ++citColPos) {

		                       
		if( nFields <= *citColPos )
			throw CrException("Cannot modify column #" + toString(*citColPos) +
				" because there are only " + toString(nFields) + " columns in total.");

		const NullableString& value = fields.at(*citColPos);
		if( value==0 || value.str().empty() )
			continue;	                                     

		                           
		string stDocId( value.str() );
		long lDocId = m_guidToIdMapper.GetId(stDocId, DocumentEntityId);
		if ( lDocId == GuidToIdMapper::invalid_id )
			throw CrException("Bad Data: There is no Document_ID defined on the system for GUID = " + stDocId);

		                         
		stDocId = toString(lDocId) + ",";

		fields[*citColPos] = stDocId;
		bRowModified = true;
	}

	                                                                                               
	                                                                             
	                                                                                 

	if ( m_vSourceTargetIdColPos.size() != 2 )
		throw CrException("Internal Error: The size of the m_vSourceTargetIdColPos array is not 2!");

	const int iSource_SourceIdColPos = m_vSourceTargetIdColPos[0];
	if ( nFields <= iSource_SourceIdColPos )
		throw CrException("Bad Data: There are not enough fields for transformation.");

	const NullableString& sourceSourceId = fields.at(iSource_SourceIdColPos);
	if ( sourceSourceId != 0 && !sourceSourceId.str().empty() ) {
		const string& stSourceId = sourceSourceId.str();
		const int iSourceId = m_guidToIdMapper.GetId(stSourceId, SourceEntityId);
		if ( iSourceId == GuidToIdMapper::invalid_id )
			throw CrException("Failed to find SourceID for GUID = " + stSourceId);

		row.m_vFields[iSource_SourceIdColPos] = toString(iSourceId);
	}

	const int iTarget_TargetIdColPos = m_vSourceTargetIdColPos[1];
	if ( nFields <= iTarget_TargetIdColPos )
		throw CrException("Bad Data: There are not enough fields for transformation.");

	const NullableString& targetTargetId = fields.at(iTarget_TargetIdColPos);
	if ( targetTargetId != 0 && !targetTargetId.str().empty() ) {
		const string& stTargetId = targetTargetId.str();
		const int iTargetId = m_guidToIdMapper.GetId(stTargetId, TargetEntityId);
		if ( iTargetId == GuidToIdMapper::invalid_id )
			throw CrException("Failed to find TargetID for GUID = " + stTargetId);

		row.m_vFields[iTarget_TargetIdColPos] = toString(iTargetId);
	}

	return bRowModified;
}

