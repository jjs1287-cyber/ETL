/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                             
  
                                                                      

#ifdef DS_USE_LDAP

#include "ErrorResolver.h"
#include "LDAPWrapper.h"

                                                                      
                           
                                                                      

CLDAPWrapper::CLDAPWrapper() :
	m_pLDAPSessionHandle(0), m_pLDAPMsgResults(0)
{
}

CLDAPWrapper::~CLDAPWrapper()
{
	if( m_pLDAPSessionHandle ) UnBind();
	if( m_pLDAPMsgResults ) FreeSrchResults();
}

                                                                                              
              
  
                    
                                                                                                           
                                      
                                                                                                         
                              
                                                                                      
  
                  
                                                        
                                                    
                                                                   

                                                                        
bool CLDAPWrapper::InitConnection(PCHAR pchHostName, ULONG ulPortNum)
{
	m_pLDAPSessionHandle = ldap_init(pchHostName, ulPortNum);
	if (!m_pLDAPSessionHandle)   
	{       
		m_strErrMsg=ErrorResolver().Resolve(GetLastError());
		return false;
	}
	else
		return true;         
}


                                                                          

                                                                                
                                                                                
bool CLDAPWrapper::ConnectOnly(LDAP* pLDAPSessionHandle, int nTimeOutSeconds)
{
	LDAP_TIMEVAL structLDAPTimeVal;
	structLDAPTimeVal.tv_sec=nTimeOutSeconds;
	structLDAPTimeVal.tv_usec = 0;
	                                          
	                                                                                      
	ULONG ulErrCode=ldap_connect(pLDAPSessionHandle, &structLDAPTimeVal);
	if (LDAP_SUCCESS!=ulErrCode)
	{
		m_strErrMsg = ldap_err2string(ulErrCode);
		m_strErrMsg += ErrorResolver().Resolve(LdapGetLastError());                                                           
		return false;
	}
	else
		return true;
}


                                                                                                        
            
  
                   
                                                                                                
                                                             
                                                               
                                                                                         
                                                                            
                                                 
  
                  
                                                        
                                                    
                                                                   
                                                                       
bool CLDAPWrapper::OpenConnection(PCHAR pcHostName, ULONG ulPortNum)
{
	m_pLDAPSessionHandle = ldap_open(pcHostName, ulPortNum);
	                                       
	                                                               
	if (m_pLDAPSessionHandle)
		                      
		return true;
	else 
	{
	                                                                                   
		m_strErrMsg = _T("Failed to get LDAP session handle.\n");
		ULONG ulErrCode = LdapGetLastError();
		if (ulErrCode)
			m_strErrMsg += ErrorResolver().Resolve(LdapGetLastError());                                                           
		return false;
	}
}


bool CLDAPWrapper::ValidSessionHandle()
{
	if (!m_pLDAPSessionHandle)	
	{
		m_strErrMsg = _T("LDAP session handle does not exist.");
		return false;
	}
	return true;
}


                                                                                                          
bool CLDAPWrapper::SimpleSyncBind(PCHAR pcUserName, PCHAR psPasswd)
{
	                                                             
	ULONG ulRetVal = ldap_simple_bind_s(m_pLDAPSessionHandle, pcUserName, psPasswd); 
	if (LDAP_SUCCESS == ulRetVal)
		return true;
	else
	{
		m_strErrMsg = ldap_err2string(ulRetVal);
		return false;
	}
}


                                                         
             
  
                       
                                                                                       
                                                               
  
                    
                                                                                  
                                                    

bool CLDAPWrapper::SyncSearch(PCHAR pcEntryToSrch, PCHAR pcSrchFilter, ULONG bAttrsOnly       , 
							   PCHAR pcArrayOfAttr[]      , ULONG ulScopeOfSrch                      )
{
	ULONG ulErrCode = ldap_search_s(m_pLDAPSessionHandle, pcEntryToSrch, ulScopeOfSrch, pcSrchFilter, 
						pcArrayOfAttr, bAttrsOnly, &m_pLDAPMsgResults); 	
	if (ulErrCode!=LDAP_SUCCESS)
	{
		FreeSrchResults();
		m_strErrMsg = ldap_err2string(ulErrCode);
		                                                           
		                                                          
		                                                             
		return false;
	}

                                                                            
	return true;
}


                                                                                  
                                                                                      

void CLDAPWrapper::FreeSrchResults()
{
	                                                                                   
	if(m_pLDAPMsgResults)
	{
		ldap_msgfree(m_pLDAPMsgResults);                                                                               
		m_pLDAPMsgResults = 0;
	}
}



                                                                                                       
                         

ULONG CLDAPWrapper::ConfgNumOfSrchResults(int* nNumOfSrchResults                 )
{
	                     
	return ldap_set_option(m_pLDAPSessionHandle, LDAP_OPT_SIZELIMIT, (void*)nNumOfSrchResults);
}


                                                                                                                  
  

ULONG CLDAPWrapper::ConfgTimeToSrch(int nTimeToSrch                  )
{
	return ldap_set_option(m_pLDAPSessionHandle, LDAP_OPT_TIMELIMIT, (void*)&nTimeToSrch);
}


                                                                                               
  
                
                                   
                                      
ULONG CLDAPWrapper::ConfgHandlingAliases(int nHandleAliases                     )
{
	return ldap_set_option(m_pLDAPSessionHandle, LDAP_OPT_DEREF, (void*)&nHandleAliases);
}


                                                                                                   
CStringList* CLDAPWrapper::FillAttributeList()
{
	int numOfEntriesInSrchResultSet=ldap_count_entries(m_pLDAPSessionHandle, m_pLDAPMsgResults);
	BerElement* pInternal;
	if (m_pLDAPSessionHandle && m_pLDAPMsgResults)
	{
		LDAPMessage* pMsg = ldap_first_entry(m_pLDAPSessionHandle, m_pLDAPMsgResults);
		for (int nKnt=1;nKnt<=numOfEntriesInSrchResultSet;++nKnt)
		{
			PCHAR pszAttrib = ldap_first_attribute(m_pLDAPSessionHandle, pMsg, &pInternal);
			while (pszAttrib)
			{
				if (!m_pResultsList.Find(pszAttrib))                                                                        
					m_pResultsList.AddTail(pszAttrib);
				ldap_memfree(pszAttrib);
				pszAttrib = ldap_next_attribute(m_pLDAPSessionHandle, pMsg, pInternal);
			}
			pMsg = ldap_next_entry(m_pLDAPSessionHandle, m_pLDAPMsgResults);
		}
	}
	return &m_pResultsList;
}


                                          
CStringList* CLDAPWrapper::GetSearchResults()
{
	int numOfEntriesInSrchResultSet=ldap_count_entries(m_pLDAPSessionHandle, m_pLDAPMsgResults);
	
	BerElement* pInternal;
	KString strAttribVals, strAttrib;
	if (m_pLDAPSessionHandle && m_pLDAPMsgResults)
	{
		PCHAR pszAttrib;
		PCHAR* pcValue;
		LDAPMessage* pMsg = ldap_first_entry(m_pLDAPSessionHandle, m_pLDAPMsgResults);
		for (int nKnt=1;nKnt<=numOfEntriesInSrchResultSet;++nKnt)
		{
			strAttribVals.Empty();

			pszAttrib = ldap_first_attribute(m_pLDAPSessionHandle, pMsg, &pInternal);
			
			while (pszAttrib)
			{

				strAttribVals = pszAttrib;
				pcValue = ldap_get_values(m_pLDAPSessionHandle, pMsg, pszAttrib);
				while (pcValue && *pcValue)
				{
					strAttribVals += ",";
					strAttribVals += *pcValue;
					++pcValue;
				}
				ldap_memfree(pszAttrib);
				m_pResultsList.AddTail(strAttribVals);
				pszAttrib = ldap_next_attribute(m_pLDAPSessionHandle, pMsg, pInternal);
			}

			              
			                                    
			                                               
			                                                                       
		
			pMsg = ldap_next_entry(m_pLDAPSessionHandle, pMsg);
		}
		if (pcValue && *pcValue)
			ldap_value_free(pcValue);
	}
	return &m_pResultsList;
}


CStringList* CLDAPWrapper::GetSearchResults(CStringList* pReqAttribList)
{
	int numOfEntriesInSrchResultSet=ldap_count_entries(m_pLDAPSessionHandle, m_pLDAPMsgResults);
	
	KString strAttribVals, strAttrib;
	if (m_pLDAPSessionHandle && m_pLDAPMsgResults)
	{
		KString strAttrib;
		PCHAR *pcValue=0, *pcValueCopy=0;
		LDAPMessage* pMsg = ldap_first_entry(m_pLDAPSessionHandle, m_pLDAPMsgResults);
		POSITION posInReqAttribList;
		for (int nKnt=1;nKnt<=numOfEntriesInSrchResultSet;++nKnt)
		{
			strAttribVals.Empty();
			posInReqAttribList = pReqAttribList->GetHeadPosition();
			
			while (posInReqAttribList)
			{
				strAttrib = pReqAttribList->GetAt(posInReqAttribList);
				int nLen = strAttrib.GetLength();
				LPTSTR buffer = strAttrib.GetBuffer(nLen);
				strAttribVals = strAttrib;
				pcValue = ldap_get_values(m_pLDAPSessionHandle, pMsg, buffer);
				pcValueCopy = pcValue;                              
				while (pcValue && *pcValue)
				{
					strAttribVals += ",";
					strAttribVals += *pcValue;
					++pcValue;
				}
				strAttrib.ReleaseBuffer();
				m_pResultsList.AddTail(strAttribVals);
				pReqAttribList->GetNext(posInReqAttribList);
			}

			              
			                                    
			                                               
			                                                                       
		
			pMsg = ldap_next_entry(m_pLDAPSessionHandle, pMsg);
		}
		if(pcValueCopy && *pcValueCopy)
			ldap_value_free(pcValueCopy);
	}
	return &m_pResultsList;
}


bool CLDAPWrapper::GetSearchResults(PCHAR pszHostName, ULONG ulPortNum, PCHAR pszEntryToSrch, PCHAR pszSrchFilter, KString strOutputAttribute, KString& strResult, PCHAR pszUserName, PCHAR pszPassword)
{
	bool  bReturnVal = OpenConnection(pszHostName, ulPortNum);
	if (!bReturnVal)
		return bReturnVal;

	bReturnVal = SimpleSyncBind(pszUserName, pszPassword);
	if (!bReturnVal)
		return bReturnVal;
	
	                                                                          
	                                                                               
	                 
	                                                       
	                                            

	bReturnVal = SyncSearch(pszEntryToSrch, pszSrchFilter);
	if (!bReturnVal)
		return bReturnVal;
	
	CStringList pstrAttribList;
	pstrAttribList.AddTail(strOutputAttribute);
	GetSearchResults(&pstrAttribList);                                                  
	
	                                                                                    
	                 
	POSITION pos = m_pResultsList.GetHeadPosition();
	while (pos)
	{
		KString strTemp = m_pResultsList.GetNext(pos);
		                                               
		                                
		strResult += strTemp.Right(strTemp.GetLength() - strTemp.Find(',')-1);
		if (pos)
			strResult += _T(",");
	}

	return true;          
}

                                                                                                               

bool CLDAPWrapper::UnBind()
{
	ULONG ulRetVal = ldap_unbind_s(m_pLDAPSessionHandle);
	m_pLDAPSessionHandle = 0;
	if (LDAP_SUCCESS == ulRetVal)
	{
		return true;
	}
	else
	{
		m_strErrMsg = ldap_err2string(ulRetVal);
		return false;
	}

}

#endif               

