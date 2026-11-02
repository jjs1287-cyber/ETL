/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <memory>
#include <cassert>

#include <konga/Utils/util.h>
#include <konga/Utils/CsvUtils.h>

#include <konga/Debugger/Debuggee.h>

#include <konga/DataElement/DataElement.h>

#include <konga/Server/WDIPathFinder.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationUtils/IntegrationBreakpointLocation.h>

#include "ErrorCodes/resource.h"
#include "Transform.h"
#include "TreeMapper.h"
#include "RaiseErrorException.h"
#include "DataElementUtils.h"
#include "DETransform.h"

                                                                                   
#ifdef LoadString
#undef LoadString
#endif

using namespace konga;
using std::string;

#define DEBUGLOG(x)	if(getViewLog()->IsDebugLogEnabled()) { getViewLog()->DebugString(x); } else if(LOG) { fprintf(LOG, x); fflush(LOG); }

DETransform::DETransform(TreeMapper* pTreeMapper, long lScriptId, CTransform*pTrans) :
	m_pTreeMapper(pTreeMapper), m_pTransform(pTrans), m_pTranform_created(0), m_debug_log(0)
{
	assert(m_pTreeMapper);
	if(m_pTransform==0){
		if ( lScriptId > 0 ){
			m_pTransform=new CTransform(m_pTreeMapper, lScriptId);
			m_pTranform_created=m_pTransform;
		}
		else {
			m_pTransform=new CTransform(m_pTreeMapper, (CROM*)0, m_pTreeMapper->getSourceDeSet(), m_pTreeMapper->IsFlatSource());
			m_pTranform_created=m_pTransform;
		}
	}
}

DETransform::~DETransform(){
	if(m_pTranform_created)delete m_pTranform_created;
}

CErrorLog* DETransform::getErrorLog() const {
	return m_pTreeMapper->getErrorLog();
}

std::string DETransform::getLastError() const
{
	return (const char*)m_pTransform->m_lastErr;
}

namespace {

void parseNameValuePairs(const string& stNameValuePairs, std::map<string, string>& nameTypeMap, std::map<string, string>& nameValueMap)
{
	konga::StringTokenizer tk0(stNameValuePairs.c_str(), "=\"{};");
	const bool namePart=true, valuePart=false;
	bool status=namePart;
	string name, type, format, value;
	unsigned int pos=0;               
						                
						         
						                                       
						          
						                               
	while(tk0.NextToken()){
		std::string token0=tk0.token();
		unsigned char c=token0.at(0);
		if(c=='='){
			if(status==namePart){
				status=valuePart;
			}
			else {
				value+=token0;
			}
		}
		else if(c==';'){
			status=namePart;
			nameValueMap[name]=value;
			nameTypeMap[name]=type;
			name="";
			type="";
			format="";
			value="";
			pos=0;
		}
		else {
			switch(status){
			case namePart:
				{
					if(c=='"'){
						tk0.spanTo_CSV_String();
						tk0.NextToken();
						token0=tk0.token();                                
						konga::qualifyCsvString(token0, ",{}");
						switch(pos){
							case 0: name+=token0; break;
							case 1: type+=token0; break;
							case 2: format+=token0; break;
						}
					}
					else {
						konga::StringTokenizer tk(token0.c_str(), "(,)");
						while(tk.NextToken()){
							string token=tk.token();
							switch(c=token.at(0)){
								case '(': pos=1; break;
								case ',': pos=2; break;
								case ')': pos=3; break;
								default:
									switch(pos){
										case 0: name+=token; break;
										case 1: type+=token; break;
										case 2: format+=token; break;
									}
									break;
							}
						}
					}
				}
				break;
			case valuePart:
				{
					if(c=='"'){
						tk0.spanTo_CSV_String();
						tk0.NextToken();
						value=tk0.token();                                
						konga::qualifyCsvString(value, ",{}");
					}
					else if(c=='{'){
						int order=1;
						value=token0;
						while(tk0.NextToken()){
							string token=tk0.token();
							switch(c=token.at(0)){
								case '{': order++; break;
								case '}': order--; break;
								case '"':
								{
									tk0.spanTo_CSV_String();
									tk0.NextToken();                               
									token=tk0.token();
									if(token=="NULL") {
										token = "";    
									}
									else {
										konga::qualifyCsvString(token, "", true);
									}
								}
								break;
							}
							value+=token;
							if(order==0)break;
						}
					}
					else if(c=='}'){
						throw konga::InternalErrorException(
							string("Internal Error: misplaced '}' in the NameValue pairs:")+stNameValuePairs.c_str());
					}
					else {
						value=token0;
						while(tk0.NextToken()){
							string token=tk0.token();
							if(token.at(0)==';'){
								tk0.StepBack();
								break;
							}
							value+=token;
						}
					}
				}
				break;
			}
		} 
	}
}
}

string DETransform::defineInputDEs(const string& stNameValuePairs, int numValueToParse) {
	if(stNameValuePairs.empty())return "";
	parseNameValuePairs(stNameValuePairs, nameTypeMap, nameValueMap);

	std::string err_msg;
	for(int i=0;i<numValueToParse;i++){
        DataElement* dsi=m_pTransform->getInputDEOf(i);
		const string name(dsi->getName());
		string dataTypeStr(nameTypeMap[name]);
		string value(nameValueMap[name]);
		string formatStr;
		int j=dataTypeStr.find(',');
		if(j>0){
			formatStr=dataTypeStr.substr(j+1);
			int c1=formatStr.at(0),c2=formatStr.at(formatStr.size()-1);
			if(c1=='"'&&(c1==c2)){
				konga::CSV_restore(formatStr);
			}
			dataTypeStr=dataTypeStr.substr(0,j);
		}
		int sqlDataType=0;
		{
			int k1=dataTypeStr.find('<')+1, k2=dataTypeStr.find('>');
			if(k2>k1){
				std::string sqlDataTypeStr(dataTypeStr.substr(k1,k2-k1));
				dataTypeStr=dataTypeStr.substr(0, k1-1);
				sqlDataType=atoi(sqlDataTypeStr.c_str());
			}
		}
		DataElement::Type type=DataElement::get_type(dataTypeStr.c_str(),sqlDataType);

		if(m_debug_log){
			fprintf(m_debug_log," %s dataType:%s format:\"%s\", value:\"%s\"\n",
				(const char*)dsi->getName(), konga::DataElement::get_type_name(type),
				formatStr.c_str(), value.c_str());
			fflush(m_debug_log);
		}
		if(value=="NULL"){
			dsi->setNull(true);
		}
		else {
			char c1=0, c2=0;
			if(value.size()>=2){
				c1=value.at(0);
				c2=value.at(value.size()-1);
			}
			if(c1=='{'&&c2=='}'){
				                                                    
				                                                                           
				                                                                                                           
				err_msg=m_pTransform->readArrayString(dsi, value, type, formatStr);
			} else {                       
				if((c1=='"'&&c2=='"')){
					konga::CSV_restore(value);
				}
				err_msg=m_pTransform->readStringToDE(dsi, value, type, formatStr);
			}
			if(err_msg.length()>0){
				throw konga::CrException(err_msg);
			}
		}
	}
	return "";
}

string DETransform::testExpression(const string& stNameValuePairs, const string& stExpression, string& stWarnings)
{
	try {
		Debuggee* pDebuggee = m_pTreeMapper->getOperationContext()->getDebuggee();
		if ( pDebuggee ) {
			pDebuggee->getCallStack().push(
				new IntegrationBreakpointLocation(-1, -1, -1, 0, "")
				);
		}
		AutoPopCallStack pop_me(pDebuggee);
		std::auto_ptr<DataElement> de( runScript(stNameValuePairs, stExpression, stWarnings) );
		string sRetVal;
		if( de.get() != 0 ) {
			int err=0;
			sRetVal=de->GetString(err);
			if( err ) {
				stWarnings += "\nFailed to get return value of script.";
			}
		}
		return sRetVal;
	}
	catch( const RaiseErrorException& e ) {
		const string stError(e.GetError());
        m_pTransform->m_lastErr += stError;
		stWarnings=m_pTransform->m_lastErr;
        return string("");
	}
	catch(const CrFatalException& e)
	{
        m_pTransform->m_lastErr += e.GetErrorString();
        return string("");
	}
}

DataElement* DETransform::runScript(const string& sNameValuePairs, const string& sExpression, string& stWarnings)
{
    if(sExpression.find('\0')!=std::string::npos){stWarnings="Script source cannot contain NUL";return new DataElement();}
	if(m_debug_log){
		fprintf(m_debug_log,"---------------------\nScript name_value_pairs:%s\n",
			sNameValuePairs.c_str(),sExpression.c_str());
		fflush(m_debug_log);
	}

	int err=m_pTransform->TokenizeTemplate(sExpression.c_str());
	if(err){
		stWarnings = "Script parsing failed\n" + m_pTransform->m_lastErr;
		if(m_debug_log){
			fprintf(m_debug_log, stWarnings.c_str());
			fflush(m_debug_log);
		}
		return new DataElement();
	}
    TALIST*curTokenArrayList=m_pTransform->getCurrentTokenArrayList();

	const string sDeLoadErr( defineInputDEs(sNameValuePairs, (int)m_pTransform->getInputDEListSize()) );
	if( !sDeLoadErr.empty() ) {
		stWarnings=sDeLoadErr;
		return new DataElement();
	}

	std::string& targetDataType=nameValueMap["etlforge.target.target.ValueType"];
	if(targetDataType.empty())targetDataType="string";
	std::string& targetSqlTypeStr=nameValueMap["etlforge.target.target.SqlDataType"];
	int targetSqlType=targetSqlTypeStr.empty()?0:atoi(targetSqlTypeStr.c_str());
	std::string& sFormat=nameValueMap["etlforge.target.target.Format"];
	DataElement::Type target_data_type=DataElement::get_type(targetDataType.c_str(), targetSqlType);


	if(m_debug_log){
		fprintf(m_debug_log,"Script:%s\ntargetDataType=%s\ntargetSqlType=%d\nsFormat=%s\n",
			sExpression.c_str(),targetDataType.c_str(),targetSqlType,sFormat.c_str());
		fflush(m_debug_log);
	}


	KString sResult;                     
	m_pTransform->Evaluate(sExpression.c_str(), curTokenArrayList, target_data_type, sFormat.c_str(), 0, sResult, err);

	if(m_debug_log){
		fprintf(m_debug_log,"TestRun Result=%s\n",(const char*)sResult);
		fflush(m_debug_log);
	}
	stWarnings=m_pTransform->m_lastErr;
	return new DataElement(DataElement::t_string, sResult);
}
