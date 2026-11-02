/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <sstream>	               
#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h> 	          

#include "RFC822HeadersWithParameters.h"

using namespace std;
using namespace konga;

                    
namespace {
	const string stHWSP = " \t";
    const string stCRLF = "\r\n";                      
}

HeaderValue::HeaderValue(const string& stHeaderValue) {
	Parse(stHeaderValue);
}

string HeaderValue::Print() const
{
	string stRes(GetValue());
	Parameters::const_iterator cIt = m_Parameters.begin();
	for (; cIt != m_Parameters.end(); ++cIt )
	{
		stRes.append("; ");
		stRes.append(cIt->first);
		stRes.append(1, '=');
		stRes.append(cIt->second);
	}
	return stRes;
}

string HeaderValue::GetValue() const { return m_stValue; }

string HeaderValue::GetParameter(const string& stParameterName) const
{
	Parameters::const_iterator cIt = m_Parameters.begin();
	for (; cIt != m_Parameters.end(); ++cIt )
	{
		if ( stricmp(stParameterName.c_str(), (cIt->first).c_str()) == 0 )
			return cIt->second;
	}
	return string("");
}

HeaderValue::Parameters HeaderValue::GetParameters() const { return m_Parameters; }

bool HeaderValue::empty() { return (m_stValue.empty() && m_Parameters.empty()); }

void HeaderValue::Parse(const string& stHeaderValue)
{
	string st(stHeaderValue);
	Unfold(st);

	const string::size_type iSize = st.size();
	if ( st.empty() )
		return;
	string::size_type iBegin = st.find_first_not_of(stHWSP);
	if ( iBegin == string::npos )
		return;

	string::size_type iPos = st.find(';', iBegin), iEnd = 0;
	if ( iPos == string::npos )                  
	{
		iEnd = st.find_last_not_of(stHWSP, iSize-1);
		m_stValue = st.substr(iBegin, iEnd-iBegin+1);
		return;
	}
	else
	{
		iEnd = st.find_last_not_of(stHWSP, iPos-1);
		m_stValue = st.substr(iBegin, iEnd-iBegin+1);
	}

                                   
	iBegin = st.find_first_not_of(stHWSP, iPos+1);
	if ( iBegin == string::npos )
		return;                                

	const string stTokens("=;");
	string stName;
	bool bName = true;
	iPos = st.find_first_of(stTokens, iBegin);
	while ( true )
	{
		if ( iPos == string::npos || st[iPos] == ';')
		{
			if ( bName )                          
			{
				m_stValue = st;
				m_Parameters.clear();
				return;
			}
			if ( iPos == string::npos )
				iPos = iSize;
			iEnd = st.find_last_not_of(stHWSP, iPos-1);
			if ( st[iBegin] == '\"' && st[iEnd] == '\"' )
				m_Parameters.push_back(Parameter(stName, st.substr(iBegin+1, iEnd-iBegin-1)));
			else
				m_Parameters.push_back(Parameter(stName, st.substr(iBegin, iEnd-iBegin+1)));
			if ( iPos == iSize )
				return;
			iBegin = st.find_first_not_of(stHWSP, iPos+1);
			bName = true;
			iPos = st.find_first_of(stTokens, iBegin);
		}
		else if ( st[iPos] == '=' )
		{
			if ( !bName )                          
			{
				m_stValue = st;
				m_Parameters.clear();
				return;
			}
			iEnd = st.find_last_not_of(stHWSP, iPos-1);
			stName = st.substr(iBegin, iEnd-iBegin+1);
			iBegin = st.find_first_not_of(stHWSP, iPos+1);
			bName = false;
			iPos = st.find_first_of(stTokens, iBegin);
		}
	}
}

                                                                                                                          
void HeaderValue::Unfold(string& st)
{
	const string::size_type iSize = st.size();
	string::size_type iPos = st.find(stCRLF), iEnd = 0;
	while ( iPos != string::npos )
	{
		if ( iPos >= iSize-2 )                  
		{
			st.erase(iPos, iSize);
			return;
		}
		iEnd = st.find_first_not_of(stHWSP, iPos+2);
		if ( iEnd == string::npos || iEnd == iPos+2)                                                                 
		{
			st.erase(iPos, iSize);
			return;
		}
		else
			st.replace(iPos, iEnd-iPos, " ");
		iPos = st.find(stCRLF, iEnd);
	}
}

                                                           
  

RFC822HeadersWithParameters::RFC822HeadersWithParameters(const string& stHeaders) { Parse(stHeaders); }

void RFC822HeadersWithParameters::PrintHeaders(string& stHeaders) const
{
	Headers::const_iterator cIt = m_Headers.begin();
	for (; cIt != m_Headers.end(); ++cIt )
	{
		stHeaders.append(cIt->first);
		stHeaders.append(": ");
		stHeaders.append((cIt->second).Print());
		stHeaders.append(stCRLF);
	}
}

string RFC822HeadersWithParameters::GetHeaderValueString(const string& stFieldName) const
{
	Headers::const_iterator cIt =  m_Headers.begin();
	for (; cIt != m_Headers.end(); ++cIt )
	{
		if ( stricmp(stFieldName.c_str(), (cIt->first).c_str()) == 0 )
			return (cIt->second).Print();
	}
	return string("");
}

HeaderValue RFC822HeadersWithParameters::GetHeaderValue(const string& stFieldName) const
{
	Headers::const_iterator cIt =  m_Headers.begin();
	for (; cIt != m_Headers.end(); ++cIt )
	{
		if ( stricmp(stFieldName.c_str(), (cIt->first).c_str()) == 0 )
			return cIt->second;
	}
	return HeaderValue();
}

string RFC822HeadersWithParameters::GetHeader(const string& stFieldName) const
{
	Headers::const_iterator cIt =  m_Headers.begin();
	for (; cIt != m_Headers.end(); ++cIt )
	{
		if ( stricmp(stFieldName.c_str(), (cIt->first).c_str()) == 0 )
		{
			stringstream sRes;
			sRes << cIt->first << ": " << cIt->second.Print();
			return sRes.str();
		}
	}
	return string("");
}

vector<HeaderValue> RFC822HeadersWithParameters::GetHeaderValues(const std::string& stFieldName) const {
	vector<HeaderValue> retVal;
	Headers::const_iterator cIt =  m_Headers.begin();
	for (; cIt != m_Headers.end(); ++cIt )
	{
		if ( stricmp(stFieldName.c_str(), (cIt->first).c_str()) == 0 )
			retVal.push_back(cIt->second);
	}
	return retVal;
}

void RFC822HeadersWithParameters::Parse(const string& stHeaders)
{
	if ( stHeaders.empty() )
		return;

	string::size_type iBegin = 0, iEnd = 0;
	string::size_type iPos = stHeaders.find(stCRLF, iBegin);
	if ( iPos != string::npos )
	{
		while ( iPos != string::npos )
		{
			iEnd = stHeaders.find_first_not_of(stHWSP, iPos+2);
			if ( iEnd == string::npos )                
			{
				AddHeader(stHeaders.substr(iBegin, iPos-iBegin));
				return;
			}
			else if ( iEnd == iPos+2 )                  
			{
				AddHeader(stHeaders.substr(iBegin, iPos-iBegin));
				iBegin = iPos = iEnd;
			}
			else                  
			{
				iPos = iEnd;
			}
			iPos = stHeaders.find(stCRLF, iPos );
		}
	}
	else
		throw CrException("The headers are not terminated by a CRLF sequence.");
}

void RFC822HeadersWithParameters::AddHeader(const string& stHeader)
{
	if ( stHeader.empty() )
		return;

	string::size_type iBegin = stHeader.find_first_not_of(stHWSP);
	if ( iBegin == string::npos )
		return;

	string stName, stValue;
	string::size_type iEnd = 0;
	string::size_type iPos = stHeader.find_first_of(':', iBegin);
	if ( iPos != string::npos )
	{
		iEnd = stHeader.find_last_not_of(stHWSP, iPos-1);
		stName = stHeader.substr(iBegin, iEnd-iBegin+1);
		iBegin = stHeader.find_first_not_of(stHWSP, iPos+1);
		if ( iBegin == string::npos )
			throw CrException("Failed to parse the header\n\n" + stHeader + "\n\nIt has no value part");

		iEnd = stHeader.find_last_not_of(stHWSP, stHeader.size()-1);
		stValue = stHeader.substr(iBegin, iEnd-iBegin+1);
	}
	else
		throw CrException("Failed to parse the header\n\n" + stHeader + "\n\nIt contains no \":\"");

	if ( stName.empty() || stValue.empty() )
		throw CrException("The header\n\n" + stHeader + "\n\nis malformed.");
	else
		m_Headers.push_back( Header(stName, HeaderValue(stValue)) );
}
