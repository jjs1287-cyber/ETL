/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                       
  
                                                                      

#ifdef DS_USE_SP

#include <iostream>

#include <boost/algorithm/string/case_conv.hpp>

                      
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Utils/DateUtils.h>
#include <konga/Utils/date.h>

#include "engine_util.h"					                   

#include "IncomingMsg.h"

#include "SpCaller.h"
#include "SpException.h"
#include "BindParam.h"

using namespace std;

                                                                      
                           
                                                                      

CSpCaller::CSpCaller(const char* scriptStr):
m_script(scriptStr?scriptStr:""), m_argList(0), m_connectStr(""),
m_dbConnectStr(""), m_numParams(0), m_spStr(""), m_lTimeOut(-1)
{
}

CSpCaller::CSpCaller(std::string& scriptStr):
m_script(scriptStr), m_argList(0), m_connectStr(""), m_dbConnectStr(""),
m_numParams(0), m_spStr(""), m_lTimeOut(-1)
{
}


CSpCaller::~CSpCaller()
{
	FreeResources();
}


                                                                            
                                                                          
                                                   

void CSpCaller::InvokeStoredProcedure()
{
	if(m_script.empty() )
	{
		string err = "Script not defined or empty.\n";
		err += m_script;
		throw CSpException(CSpException::ERR_BAD_SCRIPT, err);
	}
	if ((m_script.substr(0, 5)).compare("SQLSP"))
	{
		string err = "This script does not begin with keyword 'SQLSP'.\n";
		err += m_script;
		throw CSpException(CSpException::ERR_NO_SP_CALL, err);
	}
	string script(m_script.substr(6, std::string::npos));              
	ParseScript(script);
	InvokeSP();
	return;
}

void CSpCaller::ParseScript(std::string& scriptStr)
{
	int sqlPos = scriptStr.find("SQL");
	if (sqlPos == std::string::npos)
	{
		string err = "No SQL statement found to call the stored procedure.\n";
		err += m_script;
		throw CSpException(CSpException::ERR_NO_SQL_STMNT, err);
	}

	string argString(scriptStr.substr(0, sqlPos));

	int retVal = string::npos;
	int numArgs = 0;
	while ((retVal = argString.find(';',++retVal)) != std::string::npos)
		++numArgs;

	if (numArgs > 0)
	{
		FreeResources();
		m_argList = new spArgs[numArgs];
	}

	string script(scriptStr);

	bool isRetValReqd = false;
	if(!(script.substr(0, 4)).compare("RET/"))                          
	{
		script.erase(0,4);
		isRetValReqd = true;
	}

	m_numParams = m_numFields = 0;
	for(int i = 0; i < numArgs; ++i)
	{
		int k = script.find(';');
		string argInfo = script.substr(0, k);

		int pos = -1, numTokens = 1;
		while ((pos = argInfo.find(',',++pos)) != std::string::npos)
			++numTokens;

		                                                   
		                                                
		                                                        
		                                                       
		                                                       

		if (numTokens < 4)
		{
			string err = "Syntax error in script. ";
			err += "Required number of tokens not found.\n";
			err += argInfo;
			throw CSpException(CSpException::ERR_BAD_SCRIPT, err);
		}

		m_argList[i].dataSize = 1;
		m_argList[i].data = 0;                                                            
		m_argList[i].argDataSubType = 0;
		m_argList[i].bIsNull = false;
		int strtIndex = 0, endIndex = 0;

		endIndex = argInfo.find(',', strtIndex);

		                                                                          
		                                                                          
		            
		m_argList[i].argDataType = argInfo.substr(strtIndex, endIndex - strtIndex);
		boost::to_upper(m_argList[i].argDataType);

		if ((m_argList[i].argDataType.find("STR")) != std::string::npos)
		{
			string strSize = m_argList[i].argDataType.substr(3, m_argList[i].argDataType.length() - 3);
			m_argList[i].dataSize = atoi(strSize.c_str());
			m_argList[i].argDataType = "STR";
		}
		strtIndex = endIndex;
		
		endIndex = argInfo.find(',', ++strtIndex);
		m_argList[i].argName = argInfo.substr(strtIndex, endIndex - strtIndex);
		strtIndex = endIndex;

		endIndex = argInfo.find(',', ++strtIndex);

		                                                                          
		                                                                          
		            
		m_argList[i].argParamType = argInfo.substr(strtIndex, endIndex - strtIndex);
		boost::to_upper(m_argList[i].argDataType);

		strtIndex = endIndex;

		if (i == 0 && isRetValReqd)
			m_argList[i].argParamType = "R";
	
		if (m_argList[i].argParamType == "F")
			++m_numFields;
		else
			++m_numParams;


		string varValue = argInfo.substr(++strtIndex, argInfo.length() - strtIndex);

		                                                                            
		          
		                      
		if (!(m_argList[i].argDataType.compare("DTE"))) {
			std::string::size_type indexOfSubtype = varValue.find(']');
			if (indexOfSubtype == std::string::npos)
				indexOfSubtype = varValue.rfind('\'');
			if (indexOfSubtype == std::string::npos)
				indexOfSubtype = varValue.rfind('"');

			if (indexOfSubtype != std::string::npos) {
				m_argList[i].argDataSubType = atoi(varValue.substr(indexOfSubtype+1).c_str());
				varValue = varValue.substr(0, indexOfSubtype+1);
			}
		}

		if (stricmp(varValue.c_str(), "NULL") == 0) {
			varValue = "''";
			m_argList[i].bIsNull = true;
		}

		if (!(m_argList[i].argDataType.compare("INT")))
			m_argList[i].data = new int(atoi(varValue.c_str()));
		else if (!(m_argList[i].argDataType.compare("STR")))
		{
			if( varValue.length() > m_argList[i].dataSize )
				m_argList[i].dataSize = varValue.length();
			m_argList[i].data = new char[ m_argList[i].dataSize + 1];
			memset(m_argList[i].data, 0, m_argList[i].dataSize + 1);
			varValue = ::UnquoteString(varValue);
			strcpy((char*)m_argList[i].data, varValue.c_str());
		}
		else if (!(m_argList[i].argDataType.compare("LNG")))
			m_argList[i].data = new long(atol(varValue.c_str()));
		else if (!(m_argList[i].argDataType.compare("DBL")))
			m_argList[i].data = new double(atof(varValue.c_str()));
		else if (!(m_argList[i].argDataType.compare("FLT")))
			m_argList[i].data = new float(static_cast<float>(atof(varValue.c_str())));
		else if (!(m_argList[i].argDataType.compare("SRT")))
			m_argList[i].data = new short(static_cast<short>(atoi(varValue.c_str())));
		#ifdef WIN32
		else if (!(m_argList[i].argDataType.compare("VAR")))
		{
			m_argList[i].data = new VARIANT;
			::VariantInit(static_cast<VARIANT*>(m_argList[i].data));
			static_cast<VARIANT*>(m_argList[i].data)->vt = VT_BSTR;
			static_cast<VARIANT*>(m_argList[i].data)->bstrVal = AsciiToBstr(varValue.c_str());
		}
		#endif         
		else if (!(m_argList[i].argDataType.compare("BOL")))
		{
			if (!(varValue.compare("0")) || !(varValue.compare("FALSE")))
				m_argList[i].data = new bool(false);
			else
				m_argList[i].data = new bool(true);
		}
		#ifdef DS_USE_SP_DATE                                                                   
		                                                        
		                      
		else if (!(m_argList[i].argDataType.compare("DTE")))
		{ 
			if ( (m_argList[i].argParamType == "I") || (m_argList[i].argParamType == "B") ) {
				varValue = ::UnquoteString(varValue);
				KString csErr;
				COleDateTime objDT = ::GetDate(varValue.c_str(), csErr);
				if (!csErr.IsEmpty()) {
					string strErr = "An error occurred while converting string '" + varValue + 
						"' to date/datetime. ";
					strErr += (LPCTSTR)csErr;
					throw CSpException(CSpException::ERR_BAD_DATE_FORMAT, strErr);
				}
				TIMESTAMP_STRUCT* timeObj = new TIMESTAMP_STRUCT;
				timeObj->year = objDT.GetYear();
				timeObj->month = objDT.GetMonth();
				timeObj->day = objDT.GetDay();
				timeObj->hour = objDT.GetHour();
				timeObj->minute = objDT.GetMinute();
				timeObj->second = objDT.GetSecond();
				timeObj->fraction = 0;
				m_argList[i].data = timeObj;
			} else {
				COleDateTime oleDateObj = COleDateTime::GetCurrentTime();
				TIMESTAMP_STRUCT* timeObj = new TIMESTAMP_STRUCT;
				timeObj->year = oleDateObj.GetYear();
				timeObj->month = oleDateObj.GetMonth();
				timeObj->day = oleDateObj.GetDay();
				timeObj->hour = oleDateObj.GetHour();
				timeObj->minute = oleDateObj.GetMinute();
				timeObj->second = oleDateObj.GetSecond();
				timeObj->fraction = 0;
				m_argList[i].data = timeObj;
			}
		}
		#endif                  
		else
		{
			m_argList[i].dataSize = 4096;                                                                
			m_argList[i].data = new char[m_argList[i].dataSize + 1];
			memset(m_argList[i].data, 0, m_argList[i].dataSize + 1);
			strcpy((char*)m_argList[i].data, varValue.c_str());
		}
		script.erase(0, k+1);
	}

	int callPos = scriptStr.find("CALL", 4);
	if (callPos == std::string::npos)
	{
		string err = "No stored procedure call found in the SQL statement.\n";
		err += m_script;
		throw CSpException(CSpException::ERR_NO_SP_CALL, err);
	}

	m_connectStr = scriptStr.substr(sqlPos , callPos - sqlPos -1);

	int spPos = scriptStr.find(';', callPos);
	m_spStr = ::TrimString(scriptStr.substr(callPos, spPos - callPos));

	return;
}

void CSpCaller::InvokeSP()
{
	SmartDbPtr<DbConnection> pDataSource(GetDbConnection());

	string connect("ODBC;");
	connect += m_connectStr;

	try
	{
		pDataSource->Open(m_connectStr.substr(4).c_str());

		  
                                                               
                                                                                                                
   
                                                               
                                  
                  
                                                            
   
                                                      
                                
                                                                                          
      
                                              
    
	}
	catch(DbException& e)
	{
		string err = "Unable to open the connection to database.\n Error returned from ODBC engine is: " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "]";
		
		throw CSpException(CSpException::ERR_DB_CONNECTION, err);
	}

	string procCallString;
	if (m_numFields + m_numParams > 0) 
	{
		if (m_argList[0].argParamType == "R")
		{
			procCallString += "{? = " + m_spStr;
			if (m_numParams > 1) procCallString += "(";
			for (int i = 1; i < (m_numParams + m_numFields); ++i)
				if (m_argList[i].argParamType != "F")
					procCallString += "?,";
		}
		else
		{
			procCallString += "{" + m_spStr;
			if (m_numParams > 0) procCallString += "(";
			for (int i = 0; i < (m_numParams + m_numFields); ++i)
				if (m_argList[i].argParamType != "F")
					procCallString += "?,";
		}

		if (procCallString.substr(procCallString.length()-1) == ",")
			procCallString.erase(procCallString.end()-1);

		if (procCallString.find("(") != std::string::npos) 
			procCallString += ")}";
		else
			procCallString += "}";
	}
	else
	{
		procCallString += "{" + m_spStr + "}";
	}

	pDataSource->SetQueryTimeout(m_lTimeOut > 0 ? m_lTimeOut : GetODBCTimeOut());
	SmartDbPtr<CBindParam> rs = new CBindParam(m_numFields, m_numParams, m_argList, pDataSource);
	BOOL b=false;
	try
	{
		rs->SetSql(procCallString);
		rs->Open();
	}
	catch(DbException& e)
	{
		string err = "An error occurred while calling stored procedure '" + procCallString + "'.\n Error returned from ODBC engine is: " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "]";

		rs->Close();
		pDataSource->Close();
		
		throw CSpException(CSpException::ERR_DB_CONNECTION, err);
	}

	while( rs->Next() )
		;
	rs->Close();
	pDataSource->Close();
	return;
}

  
                                                 
 
                                 
          

                                          
                                                                        
                                                 
                        
                 
           
                   
 

                                            
 
                                        
          
                                      
                                                                                
                    
 
  

void CSpCaller::Dumpdata()
{
	std::cout << "Script string is: " << m_script << std::endl;
	std::cout << "Number of arguments to stored procedure in the script is/are: " << m_numParams << std::endl;
	std::cout << "Database connection string is: " << m_connectStr << std::endl;
	std::cout << "Stored procedure call is: " << m_spStr << std::endl;

	if (m_argList)
	{
		std::cout << "Iterating over the available parameters." << std::endl;
		for (int i = 0; i < (m_numParams + m_numFields); i++)
		{
			std::cout << "Argument " << i+1 << ":" << std::endl;

			std::cout << "Data Type: " << m_argList[i].argDataType << std::endl;
			std::cout << "Argument Name: " << m_argList[i].argName << std::endl;
			std::cout << "Parameter Type: " << m_argList[i].argParamType << std::endl;

			if (m_argList[i].argDataType == "INT")
				std::cout << "Data: " << *(int*)(m_argList[i].data) << std::endl;
			else if(m_argList[i].argDataType == "STR")
				std::cout << "Data: " << (char*)(m_argList[i].data) << std::endl;
			else if (m_argList[i].argDataType == "LNG")
				std::cout << "Data: " << *(long*)(m_argList[i].data) << std::endl;
			else if (m_argList[i].argDataType == "FLT")
				std::cout << "Data: " << *(float*)(m_argList[i].data) << std::endl;
			else if (m_argList[i].argDataType == "SRT")
				std::cout << "Data: " << *(short*)(m_argList[i].data) << std::endl;
			else if (m_argList[i].argDataType == "DBL")
				std::cout << "Data: " << *(double*)(m_argList[i].data) << std::endl;
			else if (m_argList[i].argDataType == "BOL")
				std::cout << "Data: " << *(bool*)(m_argList[i].data) << std::endl;
			else if (m_argList[i].argDataType == "DTE") {
				TIMESTAMP_STRUCT timeObj = *(TIMESTAMP_STRUCT*)(m_argList[i].data);
				std::cout << "Data: " << timeObj.month << "/" << timeObj.day << "/" <<
					timeObj.year;
				if (timeObj.hour != 0 || timeObj.minute != 0 || timeObj.second != 0)
				{
					std::cout << " " << timeObj.hour << ":" << timeObj.minute <<
						":" << timeObj.second << std::endl;
				}
			}
			else 
				std::cout << "Data: " << (char*)(m_argList[i].data) << std::endl;

			std::cout << std::endl;
			                                            
		}
	}
}

string CSpCaller::GetSPResults(IncomingMsg* pmsg)
{
	string retVal(""), argName(""), argVal("");
	if (!m_argList)
		return retVal;

	for (int i = 0; i < (m_numParams + m_numFields); ++i)
	{
		if ( (m_argList[i].argParamType == "O") ||
				(m_argList[i].argParamType == "R") ||
				(m_argList[i].argParamType == "B") ||
				(m_argList[i].argParamType == "F"))
		{
			if (m_argList[i].argName.empty())
				continue;

			argName = m_argList[i].argName;
			
			if (m_argList[i].argDataType == "INT")
			{
				argVal = toString(*(int*)(m_argList[i].data));
			}
			else if (m_argList[i].argDataType == "STR") {
				argVal = (char*)(m_argList[i].data);
			}
			else if (m_argList[i].argDataType == "LNG")
			{
				argVal = toString(*(long*)(m_argList[i].data));
			}
			else if (m_argList[i].argDataType == "FLT")
			{
				char temp[MAX_BUFFER_SIZE_FOR_CONVERSION];
				argVal = gcvt(static_cast<double>(*(float*)(m_argList[i].data)), FLOAT_SIZE_MAX, temp);
			}
			else if (m_argList[i].argDataType == "SRT")
			{
				char temp[MAX_BUFFER_SIZE_FOR_CONVERSION];
				argVal = itoa(static_cast<int>(*(short*)(m_argList[i].data)), temp, 10);               
			}
			else if (m_argList[i].argDataType == "DBL")
			{
				char temp[MAX_BUFFER_SIZE_FOR_CONVERSION];
				argVal = gcvt(*(double*)(m_argList[i].data), FLOAT_SIZE_MAX, temp);
			}
			else if (m_argList[i].argDataType == "BOL")
			{
				if (*(bool*)(m_argList[i].data))
					argVal = "1";
				else
					argVal = "0";
			}
			else if (m_argList[i].argDataType == "DTE")
			{
				TIMESTAMP_STRUCT* timeObj = 0;
				timeObj = static_cast<TIMESTAMP_STRUCT*>(m_argList[i].data);
				if (!timeObj)
					throw CSpException(CSpException::ERR_BAD_DATE_FORMAT, "An error occurred retrieving datetime data.");

				argVal = toString(timeObj->month);			argVal += "/";
				argVal += toString(timeObj->day);			argVal += "/";
				argVal += toString(timeObj->year);			
				if (timeObj->hour != 0 || timeObj->minute != 0 || timeObj->second != 0)
				{
					argVal += " ";
					argVal += toString(timeObj->hour);			argVal += ":";
					argVal += toString(timeObj->minute);		argVal += ":";
					argVal += toString(timeObj->second);
				}				
			}
			else  {
				argVal = (char*)(m_argList[i].data);
			}

			if (pmsg) {
				pmsg->SetDataElementValue(argName.c_str(), argVal.c_str());
			}

			retVal += argName + "=" + argVal + ";";
		}
	}

	return retVal;
}

void CSpCaller::FreeResources()
{
	if (!m_argList)
		return;

	for (int i = 0; i < (m_numParams + m_numFields); ++i)
	{
		if (m_argList[i].argDataType == "STR")
			delete[] (char*)m_argList[i].data;
		else if (m_argList[i].argDataType == "VAR")
			VariantClear(static_cast<VARIANT*>(m_argList[i].data));
		if (!(m_argList[i].argDataType.compare("INT")))
			delete (int*)m_argList[i].data;
		else if (!(m_argList[i].argDataType.compare("LNG")))
			delete (long*)m_argList[i].data;
		else if (!(m_argList[i].argDataType.compare("DBL")))
			delete (double*)m_argList[i].data;
		else if (!(m_argList[i].argDataType.compare("FLT")))
			delete (float*)m_argList[i].data;
		else if (!(m_argList[i].argDataType.compare("SRT")))
			delete (short*)m_argList[i].data;
        
                                 
	}

	delete[] m_argList;
}

#endif             

