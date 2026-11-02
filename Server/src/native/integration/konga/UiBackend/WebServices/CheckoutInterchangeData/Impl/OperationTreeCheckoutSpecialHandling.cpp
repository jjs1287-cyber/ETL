/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <konga/Utils/CrException.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/DbRelations.h>
#include <konga/DbRelations/TableKeyInfo.h>
#include <konga/DbRelations/KeyInfo.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include "OperationTreeCheckoutSpecialHandling.h"

using namespace konga;
using namespace konga::interchange_checkout;
using std::string;
using std::vector;

void OperationTreeCheckoutSpecialHandling::HandleColumn(vColumnValuesT& vColumnValues) {
	if ( m_stName.empty() )
		throw CrException("Internal Error: OperationTreeCheckoutSpecialHandling::HandleColumn : Name is empty.");

	const string stTableName(getTableName(m_stName));

	const TableKeyInfo& tableKeyInfo = m_dbRelations.GetTableKeyInfo(stTableName);

	const vector<KeyInfo>& vKeyInfo = tableKeyInfo.GetKeyInfo();

	                                            
	const vector<string>& vColumnNames = GetColumnNames();
	if ( vColumnNames.size() != vColumnValues.size() )
		throw CrException("Internal Error: The size of the column names and values are not the same for " + m_stName);

	vector<KeyInfo>::const_iterator citKey = vKeyInfo.begin();
	for(; citKey != vKeyInfo.end(); ++ citKey) {
		const KeyInfo& keyInfo = *citKey;
		if ( keyInfo.IsPrimary() ) {
			                                                   
			const int iEntityTypeId = keyInfo.GetEntityTypeId();
			NullableString& value = GetColumnValueRef(keyInfo.GetColumnName(), vColumnValues);
			if ( value != 0 ) {
				const string stValue(value.str());
				const long lItemId = atol(stValue.c_str());
				const string stGuid = m_guidToIdMapper.GetGuid(lItemId, iEntityTypeId);
				if ( stGuid.empty() )
					throw CrException("There is no GUID defined for " + m_stName + " for Id = " + stValue);
				value = stGuid;
			}
		}
		else {

			                                         
			const string stParentTable( keyInfo.GetParentTableName() );
			const TableKeyInfo& parentTableKeyInfo = m_dbRelations.GetTableKeyInfo(stParentTable);
                                                                                                               

			const KeyInfo* pParentPrimaryKey = parentTableKeyInfo.GetPrimaryKey();
			if ( pParentPrimaryKey == 0 )
				continue;

			const int iEntityTypeId = pParentPrimaryKey->GetEntityTypeId();
			if ( iEntityTypeId < 1 )
				continue;

			try {
				NullableString& value = GetColumnValueRef(pParentPrimaryKey->GetColumnName(), vColumnValues);
				if ( value != 0 ) {
					const string stValue(value.str());
					const long lItemId = atol(stValue.c_str());
					const string stGuid = m_guidToIdMapper.GetGuid(lItemId, iEntityTypeId);
					if ( stGuid.empty() )
						throw CrException("There is no GUID defined for " + stParentTable + " for Id = " + stValue, -12345);
					value = stGuid;
				}
			}
			catch( const CrException& ex) {
				if ( ex.GetErrorNumber() == -12345 )
					throw;
				                                                                                                                              
				                     
			}
		}
	}
}
