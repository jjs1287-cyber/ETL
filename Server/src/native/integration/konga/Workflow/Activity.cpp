/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include "Activity.h"

#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>
#include <konga/Utils/StringUtils.h>	             

#include "DataElement.h"
#include "Process.h"

using namespace std;
using namespace konga;
using namespace konga::workflow;

const char argsep = ',';


                                                                    
                                                               
                                            
                                                               
    

std::string FileRetrievalActivity::GetName() const {
	stringstream name;
	name << "FileXfer("
		<< m_stUrl << argsep
		<< m_stFileOrString << argsep
		<< m_iFileMode << argsep
		<< m_iResponseMode << argsep
		<< m_stResponseName << argsep
		<< m_stUserName << argsep
		<< m_stPassword << argsep
		<< m_stCertName << argsep
		<< m_lTimeOut << ")";
	return name.str();
}

std::string Activity::GetActivityListItemAsString() const {
	stringstream s;
	s << GetId();
	return s.str();
}

std::string RaiseErrorActivity::GetName() const {
	stringstream name;
	name << "RaiseError(" << (m_bTerminateTask ? "1":"0") << "," << m_stArgument << ")";
	return name.str();
}

std::string CreateScheduledEventActivity::GetName() const {
	stringstream name;
	name << "CreateScheduledEvent(" << m_TaskId << "," << m_stTime << "," << m_stDataElementName << ",0)";
	return name.str();
}

void OdbcActivity::SetStatementType(const std::string& stStatementType) {
	if ( strcasecmp(stStatementType.c_str(), "select") == 0 )
		m_StatementType = stSELECT;
	else if ( strcasecmp(stStatementType.c_str(), "insert") == 0 )
		m_StatementType = stINSERT;
	else if ( strcasecmp(stStatementType.c_str(), "update") == 0 )
		m_StatementType = stUPDATE;
	else if ( strcasecmp(stStatementType.c_str(), "delete") == 0 )
		m_StatementType = stDELETE;
	else
		m_StatementType = stNONE;
}

bool OdbcActivity::HasOutput() const {
	return ( m_StatementType == stSELECT );
}

                    
namespace {
	string FormatValue(const string& stValue, const string& stDataType) {
		string st;
		if ( stDataType == "string" ) st = "'";
		st += stValue;
		if ( stDataType == "string" ) st += "'";
		return st;
	}

	string FormatOutputValue(const string& stValue_in) {
		string stValue(stValue_in);
		if ( stValue.size() > 2 && stValue.at(0) == '[' ) {
			stValue.erase(0,1);
			stValue.erase(stValue.size(),1);
		}
		return stValue;
	}

	void AddWhereClause(string& stWhereClause, const string& stColName, const string& stFilter, const string& stDataType) {
		if ( !stFilter.empty() ) {
			if ( !stWhereClause.empty() )
				stWhereClause += " AND ";
			stWhereClause += stColName;
			stWhereClause += "=";
			stWhereClause += FormatValue( stFilter, stDataType );
		}
	}
}

string OdbcActivity::GetName() const {
	if ( m_stEntityType == "StoredProcedure" )
		return GetNameCall();
	else
		return GetNameStatement();
}

string OdbcActivity::GetNameCall() const {
	stringstream name;
	name << "SQLSP:";

	string stParameters;
	for( vector<DBAttribute>::const_iterator it = m_vDbAttribute.begin();
		it != m_vDbAttribute.end(); ++it )
	{
		const DBAttribute& attr(*it);
		if ( !stParameters.empty() )
			stParameters += ";";
		const string& stIO(attr.m_stIO);
		if ( stIO == "R" ) {
			stParameters += "RET/" + attr.m_stDataType;
			stParameters += "," + FormatOutputValue(attr.m_stValue) + ",R";
			stParameters += attr.m_stValue;
		}
		else if ( stIO == "i" ) {
			stParameters += attr.m_stDataType + ",,i," + FormatValue(attr.m_stValue, attr.m_stDataType) + ",";
		}
		else if ( stIO == "F" ) {
			stParameters += attr.m_stDataType + "," + FormatOutputValue(attr.m_stValue) + ",F,";
		}
	}
	name << "SQL:" << m_stConnectString << ":CALL ";
	string::size_type ipos = m_stEntityName.find(';');
	if ( ipos == string::npos )
		name << m_stEntityName;
	else {
		name << m_stEntityName.substr(0, ipos+1);
	}

	return name.str();
}

string OdbcActivity::GetNameStatement() const {

	stringstream name;
	for( vector<data_element_id_t>::const_iterator it = m_vOutputDataElement.begin(); it != m_vOutputDataElement.end(); ++it ) {
		DataElement& de = m_process.GetDataElement(*it);
		name << de.GetName() << ";";
	}
	name << "SQL:" << m_stConnectString << ":";

	string stWhereClause;
	if ( m_StatementType == stSELECT ) {
		string stSelectedColumns;
		for ( vector<DBAttribute>::const_iterator it = m_vDbAttribute.begin(); it != m_vDbAttribute.end(); ++it ) {
			const DBAttribute& dbAttr(*it);
			if ( !dbAttr.m_stValue.empty() ) {
				if ( !stSelectedColumns.empty() )
					stSelectedColumns +=  ",";
				stSelectedColumns += dbAttr.m_stName;
			}
			AddWhereClause(stWhereClause, dbAttr.m_stName, dbAttr.m_stFilter, dbAttr.m_stDataType);
		}
		name << "SELECT " << stSelectedColumns << " FROM " << m_stEntityName;
	}
	else if ( m_StatementType == stINSERT ) {
		name << "INSERT INTO " << m_stEntityName;
		string stColumns, stValues;
		for ( vector<DBAttribute>::const_iterator it = m_vDbAttribute.begin(); it != m_vDbAttribute.end(); ++it ) {
			const DBAttribute& dbAttr(*it);
			if ( !dbAttr.m_stValue.empty() ) {
				if ( !stColumns.empty() ) {
					stColumns += ",";
					stValues += ",";
				}
				stColumns += dbAttr.m_stName;
				stValues += FormatValue(dbAttr.m_stValue, dbAttr.m_stDataType);
			}
		}
		name << " (" << stColumns << ") VALUES (" << stValues << ")";
	}
	else if ( m_StatementType == stUPDATE ) {
		name << "UPDATE " << m_stEntityName << " SET ";
		string stNameValues;
		for ( vector<DBAttribute>::const_iterator it = m_vDbAttribute.begin(); it != m_vDbAttribute.end(); ++it ) {
			const DBAttribute& dbAttr(*it);
			if ( !dbAttr.m_stValue.empty() ) {
				if ( !stNameValues.empty() )
					stNameValues += ",";
				stNameValues += dbAttr.m_stName + "=" + FormatValue(dbAttr.m_stValue, dbAttr.m_stDataType);
			}

			AddWhereClause(stWhereClause, dbAttr.m_stName, dbAttr.m_stFilter, dbAttr.m_stDataType);
		}
		name << stNameValues;
	}
	else if ( m_StatementType == stDELETE ) {
		name << "DELETE FROM " << m_stEntityName;
		for ( vector<DBAttribute>::const_iterator it = m_vDbAttribute.begin(); it != m_vDbAttribute.end(); ++it ) {
			const DBAttribute& dbAttr(*it);
			AddWhereClause(stWhereClause, dbAttr.m_stName, dbAttr.m_stFilter, dbAttr.m_stDataType);
		}
	}

	if ( m_bIsAdvancedWhere )
		name << " WHERE " << m_stAdvancedWhere;
	else {
		if ( !stWhereClause.empty() )
			name << " WHERE " << stWhereClause;
	}

	return name.str();
}

string OdbcActivity::GetLocation() const {
	return toString(m_lTimeOut_Seconds);
}

