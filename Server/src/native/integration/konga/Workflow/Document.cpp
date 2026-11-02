/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <sstream>

#include "Process.h"
#include "Document.h"
#include "DataElement.h"

using namespace std;
using namespace konga;
using namespace konga::workflow;

std::string Document::GetToDocDataElementsAsString() const {
	if ( m_vToDocDataElementList.empty() ) return string();

	stringstream retval;
	for( vector<ToDocDataElement>::const_iterator it = m_vToDocDataElementList.begin();
		it != m_vToDocDataElementList.end(); ++it ) {
			retval << it->m_stName << '=';
			DataElement de = m_process.GetDataElement(it->m_DataElementId);
			retval << '[' << de.GetName() << "];";
	}
	return retval.str();
}
std::string Document::GetFromDocDataElementsAsString() const {
	if ( m_vFromDocDataElementList.empty() ) return string();

	stringstream retval;
	for( vector<FromDocDataElement>::const_iterator it = m_vFromDocDataElementList.begin();
		it != m_vFromDocDataElementList.end(); ++it ) {
			retval << it->m_stName << ';';
	}
	return retval.str();
}
