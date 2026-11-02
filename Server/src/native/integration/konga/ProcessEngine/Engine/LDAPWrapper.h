/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                       
  
                                                                      

#ifndef LDAP_WRAPPER_H
#define LDAP_WRAPPER_H

#ifdef DS_USE_LDAP

#include <winldap.h>

class CLDAPWrapper  
{
private:
	
	LDAP* m_pLDAPSessionHandle;
	LDAPMessage* m_pLDAPMsgResults;                                                                                       

public:
	KString m_strErrMsg;                    
	CStringList m_pResultsList;                                                         
	
	CLDAPWrapper();
	virtual ~CLDAPWrapper();

	bool InitConnection(PCHAR pchHostName, ULONG ulPortNum=LDAP_PORT);
	bool ConnectOnly(LDAP* pLDAPSessionHandle, int nTimeOutSeconds);
	bool OpenConnection(PCHAR pcHostName, ULONG ulPortNum=LDAP_PORT);
	bool ValidSessionHandle();
	bool SimpleSyncBind(PCHAR pcUserName=0, PCHAR psPasswd=0);
	bool SyncSearch(PCHAR pcEntryToSrch, PCHAR pcSrchFilter, ULONG bAttrsOnly=0, 
							   PCHAR pcArrayOfAttr[]=0, ULONG ulScopeOfSrch=LDAP_SCOPE_SUBTREE);
	bool GetSearchResults(PCHAR pszHostName, ULONG ulPortNum, PCHAR pszEntryToSrch, 
		PCHAR pcSrchFilter, KString strOutputAttribute, KString& strResult, PCHAR pszUserName=0, PCHAR pszPassword=0);
	KString GetLDAPError() {return m_strErrMsg;}

	void FreeSrchResults();
	ULONG ConfgNumOfSrchResults(int* nNumOfSrchResults);
	ULONG ConfgTimeToSrch(int nTimeToSrch=LDAP_NO_LIMIT);
	ULONG ConfgHandlingAliases(int nHandleAliases=LDAP_DEREF_ALWAYS);
	CStringList* GetSearchResults();
	CStringList* GetSearchResults(CStringList* pReqAttribList);
	CStringList* FillAttributeList();
	bool UnBind();
};

#endif               

#endif                  

