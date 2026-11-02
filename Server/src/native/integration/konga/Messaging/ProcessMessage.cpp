/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>	                      

#include "ProcessMessage.h"

using namespace konga;
using std::string;

void ProcessMessage::SetProcessName(const std::string& stName) { m_sProcessName = stName; }
void ProcessMessage::SetTaskName(const std::string& stName) { m_sTaskName = stName; }

std::string ProcessMessage::GetProcessName() const { return m_sProcessName; }
std::string ProcessMessage::GetTaskName() const { return m_sTaskName; }

void ProcessMessage::SetProcessVariables(const std::string& stProcessVariables) { m_sProcessVariables = stProcessVariables; }
std::string ProcessMessage::GetProcessVariables() const { return m_sProcessVariables; }

void ProcessMessage::SetOwner(const std::string& sOwner) { m_sOwner = sOwner; }
std::string ProcessMessage::GetOwner() const { return m_sOwner; }

void ProcessMessage::AddDataElement(const std::string& stName, const std::string& stValue, DE_TYPE deType) {
	m_vDataElements.push_back( DataElement(stName, stValue, deType) );
}

const ProcessMessage::DEVector& ProcessMessage::GetDataElements() const {
	return m_vDataElements;
}

const char* const ProcessMessage::DataTypeStrings[] = { "undefined", "bool", "int", "long", "float", "double", "string", "date", "text", 0 };
                                                                                                                                                                         

std::string konga::ProcessMessage::DataElement::DataTypeAsString() const {
	return ProcessMessage::DataTypeStrings[this->type];
}

                                                                                  
DE_TYPE ProcessMessage::TypeStringToEnum(const std::string& sTypeName) {
	for(int i=0; DataTypeStrings[i]; ++i) {
		if( strcasecmp(sTypeName.c_str(), DataTypeStrings[i]) == 0 )
			return DE_TYPE(i);
	}
	throw CrException("Unknown type '" + sTypeName + 
		"'. Valid type names are: bool, int, long, float, double, string, date, text.");
}

void ProcessMessage::Validate() const {
	string stValidationError;

	if( m_sProcessName.empty() && m_sProcessVariables.empty() ) {
		stValidationError += "Must specify Process Name.\n";
	}

	if( m_sTaskName.empty() && m_sProcessVariables.empty() ) {
		stValidationError += "Must specify Task Name.\n";
	}

	if ( !stValidationError.empty() )
		throw CrException(stValidationError);
}
