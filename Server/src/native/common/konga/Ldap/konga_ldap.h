/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_LDAP_H
#define KONGA_LDAP_H

#ifdef WIN32
	#include <windows.h>
	#include <winldap.h>
#else
	#include <ldap.h>
	typedef char *PCHAR;	                                       
#endif

#include <string>
#include <vector>

#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include "LdapFilter.h"		                               

namespace konga {

class LdapTreeNode;

class LdapMod {
public:
	typedef std::vector<LDAPMod*>::size_type size_type;
	typedef std::vector<LDAPMod*>::const_iterator const_iterator;
	typedef std::vector<LDAPMod*>::iterator iterator;

public:
	LdapMod(const std::string& sCharacterEncoding = "UTF-8");
	~LdapMod();

	void AddAttribute(const std::string& sType, const std::vector<std::string>& values);
	void AddAttribute(const std::string& sType, const std::string& sValue);
	void DeleteAttribute(const std::string& sType, const std::vector<std::string>& values);
	void DeleteAttribute(const std::string& sType, const std::string& sValue);
	void ReplaceAttribute(const std::string& sType, const std::vector<std::string>& values);
	void ReplaceAttribute(const std::string& sType, const std::string& sValue);

	void ModifyAttribute(unsigned long nModOp, const std::string& sType, const std::vector<std::string>& values);

	size_type size() const { return m_mods.size(); }
	const_iterator begin() const { return m_mods.begin(); }
	const_iterator end() const { return m_mods.end(); }
	iterator begin() { return m_mods.begin(); }
	iterator end() { return m_mods.end(); }

private:
	const std::string m_sCharacterEncoding;
	konga::Utf8Conv* m_conv;
	std::vector<LDAPMod*> m_mods;
};

class LdapMessage {
public:
	LdapMessage(LDAPMessage *pMsg = 0) : m_pMsg(pMsg) {}
	virtual ~LdapMessage();
	LDAPMessage* Release();
	void Attach(LDAPMessage *pMsg);
private:
	LDAPMessage *m_pMsg;
};

class AutoBerElement {
public:
	AutoBerElement(BerElement *pBer = 0) : m_pBer(pBer) {}
	virtual ~AutoBerElement();
private:
	BerElement *m_pBer;
};

class AutoLdapMemory {
public:
	AutoLdapMemory(PCHAR pMem = 0) : m_pMem(pMem) {}
	virtual ~AutoLdapMemory();
private:
	PCHAR m_pMem;
};

struct LdapSearchParameters {

	LdapSearchParameters() : includeDnInSearchResult(false), usePagedSearch(false), iDetail(0),
		iMaxNumRecords(1000), scope(LdapClassFilter::DEFAULT_LDAP_SEARCH_SCOPE) {}

	LdapSearchParameters(LdapClassFilter::SearchScope scope_) : includeDnInSearchResult(false), 
		usePagedSearch(false), iDetail(0), iMaxNumRecords(1000), scope(scope_) {}

	bool includeDnInSearchResult, usePagedSearch;
	int iDetail;
	long iMaxNumRecords;	                   
	LdapClassFilter::SearchScope scope;
};

class Ldap {
public:
	Ldap(const std::string& sCharacterEncoding = "UTF-8");
	virtual ~Ldap();

	virtual void Connect(const std::string& sHost, const std::string& sUserDN, const std::string& sPassword,
		int Secure = 0, long nPort = -1, bool bUseReferrals = false);
	virtual void Disconnect();

	std::string GetLastLdapError(unsigned long lRetErr = LDAP_SUCCESS);

	void Rename(const std::string& sDn, const std::string& sNewRdn, const std::string& sNewParent, bool bDeleteOldRdn);

	                                                                                           
	                              
	void Exec(LdapMod& ldapMod, const std::string& sDistinguishedName, bool bEnableAutoUpdateMode = false);
	void DeleteEntry(const std::string& sDistinguishedName);

	LdapTreeNode* Search(const std::string& sDistinguishedName, const std::string& sFilter, 
						 const std::vector<std::string>& vAttributes,
						 const LdapSearchParameters& params, int iChunkSize = 0);
	LdapTreeNode* NextSearchChunk(int iChunkSize = 250);

	std::string GetPrettyConnectionString() const;

protected:
	LDAPMessage* InitializeSearch(const std::string& sDistinguishedName, const std::string& sFilter, 
						 const std::vector<std::string>& vAttributes, const LdapSearchParameters& params);
	unsigned long CountSearchResults(LDAPMessage* pSearchResult);
	LdapTreeNode* GetSearchResults(LDAPMessage* pSearchResult, const LdapSearchParameters& params, int iChunkSize);
	void LoopOnePage(LDAPMessage* pSearchResult, LdapTreeNode *pResult, const LdapSearchParameters& params, int iChunkSize);

private:
	std::string m_sHost, m_sFilter, m_sDistinguishedName;
	long m_nPort;
	LDAP* m_pLdap;

	LDAPMessage* m_pSearchResult;                                                 
	LDAPMessage* m_pLastReturnedEntry;                                                 
	LdapSearchParameters m_SearchParams;                                                 

	const std::string m_sCharacterEncoding;
	konga::Utf8Conv*  m_conv;

	#ifdef WIN32
	LDAPSearch* m_pLdapSearchBlock;                                          
	#endif
};

std::string LdapTreeNodeToSimpleString(const LdapTreeNode& node);
std::string LdapTreeNodeToXmlString(const LdapTreeNode& node);

}                   

#endif                
