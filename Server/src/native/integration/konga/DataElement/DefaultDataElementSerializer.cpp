/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifdef WIN32
#include <windows.h>
#endif
#include <sqltypes.h>
#include <sqlext.h>

#include <memory>	                
#include "DataElement.h"
#include "DefaultDataElementSerializer.h"

using namespace konga;
using std::string;

DefaultDataElementSerializer::DefaultDataElementSerializer(void) {}
DefaultDataElementSerializer::~DefaultDataElementSerializer(void) {}

std::string DefaultDataElementSerializer::Serialize(const std::string& deName, const DataElement& de) const
{
	string retVal(de.getTypeStr());
	retVal.append(".");
	int err = 0;
	retVal.append((const char*)de.GetString(err));
	return retVal;
}

DataElement* DefaultDataElementSerializer::DeSerialize(const std::string& stDe) const {
	if ( stDe.empty() )
		return new DataElement(DataElement::t_string, stDe.c_str());

	string::size_type iDot = stDe.find('.');
	if ( iDot == string::npos )
		return new DataElement(DataElement::t_string, stDe.c_str());

	const string& sDataType = stDe.substr(0,iDot);
	const string sValue( iDot==stDe.size()-1 ? "" : stDe.substr(iDot+1) );

	std::auto_ptr<DataElement> de(new DataElement());
	                                                                                                  
	de->setTypedValue(sValue.c_str(), sDataType.c_str(), SQL_UNKNOWN_TYPE);
	return de.release();
}

