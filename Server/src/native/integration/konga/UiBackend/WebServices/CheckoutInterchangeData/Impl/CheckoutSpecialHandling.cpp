/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>

#include "CheckoutSpecialHandling.h"

using namespace konga;
using namespace konga::interchange_checkout;
using std::string;
using std::vector;

NullableString& CheckoutSpecialHandling::GetColumnValueRef(const string& stColumnName,
																  const vector<string>& vColumnNames,
																  vColumnValuesT& vColumnValues)
{
	if ( vColumnNames.size() != vColumnValues.size() )
		throw InternalErrorException("Column Names array of different size than Column Values.");
	
	vector<string>::const_iterator citNames = vColumnNames.begin();
	vColumnValuesT::iterator itValues = vColumnValues.begin();
	for(; citNames != vColumnNames.end(); ++citNames, ++itValues) {
		const string& stThisColumnName = *citNames;

		if ( strcasecmp(stColumnName.c_str(), stThisColumnName.c_str()) == 0 ) {
			return *itValues;
		}
	}

	throw InternalErrorException("No value defined for column " + stColumnName);
}

NullableString& CheckoutSpecialHandling::GetColumnValueRef(const string& stColumnName,
														   vColumnValuesT& vColumnValues)
{
	const vector<string>& vColumnNames = GetColumnNames();
	return GetColumnValueRef(stColumnName, vColumnNames, vColumnValues);
}
