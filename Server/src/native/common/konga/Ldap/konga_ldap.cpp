/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stack>
#include <string>

#ifdef WIN32
	#include <windows.h>
	#include <winldap.h>
	#include <winber.h>
	#include <rpcdce.h>
#else
	                                                 
	#define LDAP_DEPRECATED 1
	#include <ldap.h>
	#include <konga/Utils/PortDefs.h>		                                           
#endif

#include <boost/scoped_array.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/StringUtils.h>	             

#include <konga/Crypto/Crypto.h>		                 

#include "LdapTreeNode.h"		                     
#include "konga_ldap.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	template<typename T>
	class AutoArrayOfArrays {
	public:
		AutoArrayOfArrays(T** pp = 0, unsigned long nSize = 0) : m_pp(pp), m_nSize(nSize) {}
		virtual ~AutoArrayOfArrays() {
			if( m_pp ) {
				for(unsigned long n=0; n<m_nSize; ++n)
					delete[] m_pp[n];
				delete[] m_pp;
			}
		}
	private:
		T** m_pp;
		unsigned long m_nSize;
	};

	char* newLdapDecentStr(const string& utf8, konga::Utf8Conv* conv){
		const string ret( conv != 0 ? conv->to(utf8) : utf8 );

		char* s=new char[ret.size() + 1];
		strcpy(s, ret.c_str());
		return s;
	}
}

LdapMod::LdapMod(const string& sCharacterEncoding) :
	m_sCharacterEncoding(sCharacterEncoding), m_conv(sCharacterEncoding.empty() ? 0 : konga::Utf8Conv::createEncoderFor(sCharacterEncoding))
{
}

LdapMod::~LdapMod() {
	delete m_conv;
	vector<LDAPMod*>::iterator itMod = m_mods.begin();
	for(; itMod != m_mods.end(); ++itMod) {
		if( (*itMod)->mod_type ) delete[] (*itMod)->mod_type;
		if( (*itMod)->mod_vals.modv_strvals ) {
			for(int i=0; (*itMod)->mod_vals.modv_strvals[i]; ++i) {
				delete[] (*itMod)->mod_vals.modv_strvals[i];
			}
		}
		delete *itMod;
	}
}

void LdapMod::AddAttribute(const string& sType, const vector<string>& values) {
	ModifyAttribute(LDAP_MOD_ADD, sType, values);
}

void LdapMod::AddAttribute(const string& sType, const string& sValue) {
	vector<string> v(1);
	v.push_back(sValue);
	ModifyAttribute(LDAP_MOD_ADD, sType, v);
}

void LdapMod::DeleteAttribute(const string& sType, const vector<string>& values) {
	ModifyAttribute(LDAP_MOD_DELETE, sType, values);
}

void LdapMod::DeleteAttribute(const string& sType, const string& sValue) {
	vector<string> v(1);
	v.at(0) = sValue;
	ModifyAttribute(LDAP_MOD_DELETE, sType, v);
}

void LdapMod::ReplaceAttribute(const string& sType, const vector<string>& values) {
	ModifyAttribute(LDAP_MOD_REPLACE, sType, values);
}

void LdapMod::ReplaceAttribute(const string& sType, const string& sValue) {
	vector<string> v(1);
	v.at(0) = sValue;
	ModifyAttribute(sValue.empty() ? LDAP_MOD_DELETE : LDAP_MOD_REPLACE, sType, v);
}

void LdapMod::ModifyAttribute(unsigned long nModOp, const string& sType, const vector<string>& values) {
	LDAPMod* p = new LDAPMod;
	p->mod_op = nModOp;
	p->mod_type = newLdapDecentStr(sType, m_conv);
	p->mod_vals.modv_strvals = new char*[values.size() + 1];
	vector<string>::const_iterator citValue = values.begin();
	int i=0;
	for(; citValue != values.end(); ++citValue, ++i) {
		                                                                                                       
		if( citValue->empty() )
			--i;
		else
			p->mod_vals.modv_strvals[i] = newLdapDecentStr(*citValue, m_conv);
	}
	p->mod_vals.modv_strvals[i] = 0;
	if(i==0)
		delete p;
	else
		m_mods.push_back(p);
}

LdapMessage::~LdapMessage() {
	if( m_pMsg )
		ldap_msgfree(m_pMsg);
}

LDAPMessage* LdapMessage::Release() {
	LDAPMessage *pTmpMsg = m_pMsg;
	m_pMsg = 0;
	return pTmpMsg;
}

void LdapMessage::Attach(LDAPMessage *pMsg) {
	m_pMsg = pMsg;
}

AutoBerElement::~AutoBerElement() {
	if( m_pBer )
		ber_free(m_pBer, 0);
}

AutoLdapMemory::~AutoLdapMemory() {
	if( m_pMem )
		ldap_memfree(m_pMem);
}

Ldap::Ldap(const string& sCharacterEncoding) :m_nPort(0), m_pLdap(0), m_pSearchResult(0), m_pLastReturnedEntry(0),
	m_sCharacterEncoding(sCharacterEncoding), m_conv(sCharacterEncoding.empty() ? 0 : konga::Utf8Conv::createEncoderFor(sCharacterEncoding))
	#ifdef WIN32
	, m_pLdapSearchBlock(0)
	#endif
{
}

Ldap::~Ldap() {
	delete m_conv;
	if( m_pLdap )
		Disconnect();
}

void Ldap::Disconnect() {
	if( m_pLdap ) {
		#ifdef WIN32
		if( m_pLdapSearchBlock ) {
			ldap_search_abandon_page(m_pLdap, m_pLdapSearchBlock);
			m_pLdapSearchBlock = 0;
		}
		#endif
		ldap_unbind(m_pLdap);
		m_pLdap = 0;
	}
}

#ifdef WIN32
	                                                                                                 
	#define OUR_LDAP_SSL_PORT LDAP_SSL_PORT;

	#define OUR_LDAP_OPT_SSL LDAP_OPT_SSL;
#else
	#define OUR_LDAP_SSL_PORT LDAPS_PORT;
	                  
	#define OUR_LDAP_OPT_SSL LDAP_OPT_X_TLS;
#endif

                                                             
string Ldap::GetLastLdapError(unsigned long lRetErr                     ) {
	string sErr;
	#ifdef WIN32
		unsigned long lErr = LdapGetLastError();
	#else
		unsigned long lErr = 0;
	#endif

	if( lErr != LDAP_SUCCESS ) {
		sErr = ldap_err2string(lErr);

		if( m_pLdap ) {
			char* pcErr=0;                                    
			unsigned long lResult = ldap_get_option(m_pLdap, LDAP_OPT_ERROR_STRING, &pcErr);
			if( lResult == LDAP_SUCCESS && pcErr ) {
				if( sErr != pcErr )
					sErr += "\n" + string(pcErr);
			}
		}
	}

	if( lRetErr != LDAP_SUCCESS && lRetErr != lErr ) {
		if( !sErr.empty() ) {
			sErr += "\nAdditional error: ";
		}
		sErr += string(ldap_err2string(lRetErr));
	}

	if( sErr.empty() )
		sErr = "No additional error information available.";

	return sErr;
}

#ifdef WIN32
static bool VerifyCert(LDAP* ld, PCCERT_CONTEXT pServerCert) {
	return true;
}
#endif

void Ldap::Connect(const string& sHost, const string& sUserName, const string& sPassword,
				   int iSecure          , long nPort           , bool bUseReferrals              ) {
	if( m_pLdap )
		Disconnect();

	if( sHost.empty() )
		throw CrException("Must specify LDAP server.");

	if( sUserName.empty() )
		throw CrException("Must specify a user name.");

	if( nPort <= 0 )
		nPort = LDAP_PORT;

	m_sHost = sHost;	
	m_nPort = nPort;

	                                                                                                              
	if( iSecure && m_nPort == LDAP_PORT )
		m_nPort = OUR_LDAP_SSL_PORT;

	boost::scoped_array<char> pcHost( newLdapDecentStr(sHost, m_conv) );
	#ifdef WIN32
		m_pLdap = ldap_sslinit(pcHost.get(), m_nPort, iSecure);	                                     
	#else
		                                                                                    
		m_pLdap = ldap_init(pcHost.get(), m_nPort);
	#endif

	if( !m_pLdap ) {
		const bool bStandardPort = sHost.find(':') == string::npos;
		string sErr = "Failed to open LDAP connection to " + sHost + ". Port = " + toString(m_nPort);
		if ( bStandardPort ) {
			sErr += " (standard port)";
		}
		sErr += string(". Using a ") + (iSecure?"":"non-") + string("secure connection. Error: ") +
		#ifdef WIN32
			GetLastLdapError();
		#else
			string( ldap_err2string(GetLastError()) );	                                                  
		#endif
		throw CrException(sErr);
	}

	unsigned long nVersion = LDAP_VERSION3;
    unsigned long nLdapRes = ldap_set_option(m_pLdap, LDAP_OPT_PROTOCOL_VERSION, (void*) &nVersion);
	                                
		                  

	unsigned long nReferrals = 0;
    nLdapRes = ldap_set_option(m_pLdap, LDAP_OPT_REFERRALS, (void*) &nReferrals);
	                                
		                  

#ifdef WIN32
	nLdapRes = ldap_set_option(m_pLdap, LDAP_OPT_SERVER_CERTIFICATE, &VerifyCert);
#endif

	if( iSecure == 2 ) {
		             
		                                                                   
		#ifdef WIN32
		                                                                                                 
		nLdapRes = ldap_set_option(m_pLdap,LDAP_OPT_SSL,LDAP_OPT_ON);
		if( nLdapRes != LDAP_SUCCESS ) {
			bool bStandardPort = sHost.find(':') == string::npos;
			string sErr = "Failed to enable SSL on " + sHost + ". Port = " + toString(m_nPort) +
				". Error: " + 
					GetLastLdapError(nLdapRes) + "\nldap_set_option() returned " + toString(nLdapRes);
			throw CrException(sErr);
		}
		#else
			throw CrException("Ldap ssl support has not been implemented on non-windows platforms	.");
		#endif
	}

	          
	#ifdef WIN32
	nLdapRes = ldap_connect(m_pLdap, 0);
	if( nLdapRes != LDAP_SUCCESS ) {
		bool bStandardPort = sHost.find(':') == string::npos;
		string sErr = "Failed to connect to " + sHost + ". Port = " + toString(m_nPort) +
			string(". Using a ") + (iSecure?"":"non-") + string("secure connection. Error: ") + 
				GetLastLdapError(nLdapRes);
		throw CrException(sErr);
	}
	#endif

                                                                                
                             
                                 
                                                      
                                          
                                        
                                              
                                            
                                           
                                         
                                              
                                                                            
	

                                                                                                                     
	nLdapRes = ldap_simple_bind_s(m_pLdap, (const PCHAR)sUserName.c_str(), (const PCHAR)sPassword.c_str());
	if( nLdapRes != LDAP_SUCCESS ) {
		string sErr = "Failed to login as " + sUserName + " to " + sHost + ". Port = " + toString(m_nPort) +
			string(". Using a ") + (iSecure?"":"non-") + string("secure connection. Error: ") + 
				GetLastLdapError(nLdapRes);
		throw CrException(sErr);
	}
}

void Ldap::Rename(const string& sDn, const string& sNewRdn, const string& sNewParent, bool bDeleteOldRdn) {
	if( !m_pLdap )
		throw CrException("Must be connected to a directory server before modifying an RDN.");

	boost::scoped_array<char> pcDistinguishedName( newLdapDecentStr(sDn, m_conv) );
	boost::scoped_array<char> pcNewRdn( newLdapDecentStr(sNewRdn, m_conv) );
	boost::scoped_array<char> pcNewParent( newLdapDecentStr(sNewParent, m_conv) );

	unsigned long nLdapRes = ldap_rename_s(m_pLdap, pcDistinguishedName.get(), pcNewRdn.get(), 
		sNewParent.empty()?0:pcNewParent.get(), bDeleteOldRdn, 0, 0);
	if( nLdapRes != LDAP_SUCCESS ) {
		string sErr = "Failed to modify RDN using, DN = '" +
			sDn + "', new RDN = '" + sNewRdn + "', new parent = '" + sNewParent + "'. " + 
			GetPrettyConnectionString() + "\n\nError: " + GetLastLdapError(nLdapRes);
		throw CrException(sErr);
	}
}

string Ldap::GetPrettyConnectionString() const {
	return "Server: " + m_sHost + ", Port: " + toString(m_nPort); 
}

string LDAPModtoString(const LDAPMod& mod) {
	string sRet;
	switch(mod.mod_op) {
		case LDAP_MOD_ADD:
			sRet += "Add ";
			break;
		case LDAP_MOD_REPLACE:
			sRet += "Replace ";
			break;
		case LDAP_MOD_DELETE:
			sRet += "Delete ";
			break;
		default:
			sRet += "Unsupported modification ";
	}
	sRet += "of type = '" + string(mod.mod_type) + "', value" + string(mod.mod_vals.modv_strvals[1]?"s":"") + " = ";
	for(int i=0;mod.mod_vals.modv_strvals[i];++i) {
		sRet += "'" + string(mod.mod_vals.modv_strvals[i]) + (mod.mod_vals.modv_strvals[i+1]?"', ":"'");
	}
	return sRet;
}

void Ldap::Exec(LdapMod& ldapMod, const string& sDistinguishedName, bool bEnableAutoUpdateMode              ) {
	if( !m_pLdap )
		throw CrException("Must be connected to a directory server before executing LDAP queries.");
	if( !ldapMod.size() )
		return;                       

	bool bRemoveRequested = false, bForceAdd = false;
	LDAPMod** arLdapMods = new LDAPMod*[ ldapMod.size() + 1 ];
	boost::scoped_array<LDAPMod*> adopt_arLdapMods(arLdapMods);
	LdapMod::iterator itMod = ldapMod.begin();
	string sMods;
	for(int i=0; itMod != ldapMod.end(); ++itMod, ++i) {
		arLdapMods[i] = *itMod;

		                                                                                     
		if( arLdapMods[i]->mod_type && stricmp(arLdapMods[i]->mod_type, "objectClass") == 0 )
			bForceAdd = true;

		sMods += "\n " + LDAPModtoString(*arLdapMods[i]);
		if( (*itMod)->mod_op == LDAP_MOD_DELETE )
			bRemoveRequested = true;
	}
	arLdapMods[ldapMod.size()] = 0;

	boost::scoped_array<char> pcDistinguishedName( newLdapDecentStr(sDistinguishedName, m_conv) );

	                                                                                  
	                                                                                       
	                                      
	bool bAdded = false;
	if( bForceAdd || (bEnableAutoUpdateMode && !bRemoveRequested) ) {
		vector<string> vAttributes;                                
		LdapSearchParameters params;
		params.scope = LdapClassFilter::LDAP_SEARCH_SCOPE_BASE;
		LDAPMessage* pSearchResult = InitializeSearch(sDistinguishedName, "(objectClass=*)", vAttributes, params);
		LdapMessage ldapResult(pSearchResult);

		unsigned long nEntries = CountSearchResults(pSearchResult);
		if( !nEntries )  {
			unsigned long nLdapRes = ldap_add_s(m_pLdap, pcDistinguishedName.get(), arLdapMods);
			if( nLdapRes != LDAP_SUCCESS ) {
				string sErr = "Failed to apply " + toString(ldapMod.size()) + " modifications to '" +
					sDistinguishedName + "'. " + GetPrettyConnectionString() + 
					"\n\nError: " + GetLastLdapError(nLdapRes) + 
					"\n\nThe modification(s) tried was/were:\n" + sMods;
				throw CrException(sErr);
			}
			bAdded = true;
		}
	}

	if( !bAdded ) {
		unsigned long nLdapRes = ldap_modify_s(m_pLdap, pcDistinguishedName.get(), arLdapMods);
		if( nLdapRes != LDAP_SUCCESS ) {
			string sErr = "Failed to apply " + toString(ldapMod.size()) + " modifications to '" +
					sDistinguishedName + "'. " + GetPrettyConnectionString() + 
					"\n\nError: " + GetLastLdapError(nLdapRes) + 
					"\n\nThe modification(s) tried was/were:\n" + sMods;
			throw CrException(sErr);
		}
	}
}

void Ldap::DeleteEntry(const string& sDistinguishedName) {
	if( !m_pLdap )
		throw CrException("Must be connected to a directory server before deleting directory entries.");
	if( sDistinguishedName.empty() )
		return;                       

	unsigned long nLdapRes = ldap_delete_s(m_pLdap, (PCHAR)sDistinguishedName.c_str());
	if( nLdapRes != LDAP_SUCCESS && nLdapRes != LDAP_NO_SUCH_OBJECT ) {
		string sErr = "Failed to delete the entry '" + 
			sDistinguishedName + "'. Error: " + GetLastLdapError(nLdapRes);
		throw CrException(sErr);
	}
}

namespace {
	string GetScopeString(int iScope) {
		string s;
		switch(iScope) {
			case LDAP_SCOPE_BASE:
				s = "base"; break;
			case LDAP_SCOPE_ONELEVEL:
				s = "one level"; break;
			case LDAP_SCOPE_SUBTREE:
				s = "sub tree"; break;
			default:
				s = "unknown";
		}
		return s + "(" + toString(iScope) + ")";
	}

	string PrettyLdapSearchParamString(const LdapSearchParameters& params) {
		return "The parameters used in the search were: \n" +
			string("\tDetail: ") + toString(params.iDetail) + "\n" +
			string("\tMaximum number of records: ") + toString(params.iMaxNumRecords) + "\n" +
			string("\tInclude DN in results: ") + toString(params.includeDnInSearchResult) + "\n" +
			string("\tScope: ") + GetScopeString(params.scope) + "\n" +
			string("\tUse paged search: ") + toString(params.usePagedSearch);
	}
}

LDAPMessage* Ldap::InitializeSearch(const string& sDistinguishedName, const string& sFilter, 
									const vector<string>& vAttributes, const LdapSearchParameters& params) {

	m_pSearchResult = 0;
	m_pLastReturnedEntry = 0;

	                                                  
	if( params.iMaxNumRecords > 0 ) {
		unsigned long nLdapRes = ldap_set_option(m_pLdap, LDAP_OPT_SIZELIMIT, (void*) &params.iMaxNumRecords);
		if( nLdapRes != LDAP_SUCCESS )
			throw CrException("Failed to limit the number of search resluts to " + toString(params.iMaxNumRecords) + ".");
	}

	switch(params.scope) {
		case LDAP_SCOPE_BASE:
		case LDAP_SCOPE_ONELEVEL:
		case LDAP_SCOPE_SUBTREE:
			break;
		default:
			throw CrException("The scope of an LDAP search has to be 0 (LDAP_SCOPE_BASE), 1 (LDAP_SCOPE_ONELEVEL), "
				"or 2 (LDAP_SCOPE_SUBTREE). " + toString(params.scope) + " is not a valid value for scope.");
	}

	boost::scoped_array<char> pcFilter( newLdapDecentStr(sFilter, m_conv) );
	boost::scoped_array<char> pcDistinguishedName( newLdapDecentStr(sDistinguishedName, m_conv) );
	char** arAttributes = new char*[ vAttributes.size() + 1];
	for(unsigned long n = 0; n<vAttributes.size(); ++n)
		arAttributes[n] = newLdapDecentStr(vAttributes[n], m_conv );
	arAttributes[vAttributes.size()] = 0;
	AutoArrayOfArrays<char> adopt_arAttributes(arAttributes, vAttributes.size() + 1);
	unsigned long nAttrsOnly = 0;

	                 
	m_sFilter = sFilter;
	m_sDistinguishedName = sDistinguishedName;
	LdapMessage ldapResult;
	if( params.usePagedSearch ) {
		#ifdef WIN32

		unsigned long lPageSize = 500;

		if( m_pLdapSearchBlock ) {
			ldap_search_abandon_page(m_pLdap, m_pLdapSearchBlock);
			m_pLdapSearchBlock = 0;
		}

		m_pLdapSearchBlock = ldap_search_init_page(m_pLdap, pcDistinguishedName.get(), params.scope, 
						pcFilter.get(), arAttributes, nAttrsOnly, 0, 0, lPageSize, 9999999, 0);

		if( m_pLdapSearchBlock == 0 ) {
			string sErr = "Failed to search with the filter '" + sFilter + "' under the distinguished name '" +
				sDistinguishedName + "'. " + PrettyLdapSearchParamString(params) + " Error: " + GetLastLdapError(0);
			throw CrException(sErr);
		}

		LDAPMessage* pSearchResult;
		unsigned long lTotalCount = 0;                                                                                                
		unsigned long nLdapRes = ldap_get_next_page_s(m_pLdap, m_pLdapSearchBlock, 0, lPageSize, &lTotalCount, &pSearchResult);
		ldapResult.Attach(pSearchResult);                                                                                                      
		if( nLdapRes != LDAP_SUCCESS && nLdapRes != LDAP_NO_SUCH_OBJECT && nLdapRes != LDAP_NO_SUCH_ATTRIBUTE ) {
			string sErr = "Failed to search with the filter '" + sFilter + "' under the distinguished name '" +
				sDistinguishedName + "'. " + PrettyLdapSearchParamString(params) + " Error: " + GetLastLdapError(nLdapRes);
			throw CrException(sErr);
		}
		#else
		throw CrException("Paged LDAP search is not supported on this platform.");
		#endif
	} else {
		LDAPMessage* pSearchResult;
		unsigned long nLdapRes = ldap_search_s(m_pLdap, pcDistinguishedName.get(), params.scope, pcFilter.get(), 
											arAttributes, nAttrsOnly, &pSearchResult);
		ldapResult.Attach(pSearchResult);                                                                                                      
		if( nLdapRes != LDAP_SUCCESS && nLdapRes != LDAP_NO_SUCH_OBJECT && nLdapRes != LDAP_NO_SUCH_ATTRIBUTE ) {
			string sErr( GetLastLdapError(nLdapRes) );
			sErr += "\nFailed to search with the filter '" + sFilter + "' under the distinguished name '" +
				sDistinguishedName + "'.\n" + PrettyLdapSearchParamString(params);
			throw CrException(sErr);
		}
	}
	return ldapResult.Release();
}

unsigned long Ldap::CountSearchResults(LDAPMessage* pSearchResult) {
	unsigned long nEntries = ldap_count_entries(m_pLdap, pSearchResult);
	if( nEntries == (unsigned long)-1 ) {
		int err = ldap_result2error(m_pLdap, pSearchResult, 0);
		const string sErr("Failed to get the number of entries after LDAP search completed successfully. Error: " + 
			string(ldap_err2string(err)) );
		throw CrException(sErr);
	} else if( nEntries == 0 )
		return 0;
	return nEntries;
}

namespace {

	bool IsBinary(berval* pBerval) {
		if( !pBerval->bv_len ) return false;
		unsigned char c = *pBerval->bv_val;
		for(unsigned long i=0; i<pBerval->bv_len; ++i, c = pBerval->bv_val[i]) {
			if( !c || (c<32 && c!=9 && c!=10 && c!=13) )
				return true;
		}
		return false;
	}

	string EncodeBerval(berval* pBerval, bool& isBase64) {
		isBase64 = false;
		if( !pBerval ) return "";
		if( IsBinary(pBerval) ) {
			isBase64 = true;
			return EncodeBase64(reinterpret_cast<unsigned char*>(pBerval->bv_val), pBerval->bv_len);
		}
		return pBerval->bv_val;
	}
}

void Ldap::LoopOnePage(LDAPMessage* pSearchResult, LdapTreeNode *pResult, const LdapSearchParameters& params, int iChunkSize) {

	LDAPMessage* pEntry;

	if( m_pLastReturnedEntry ) {
		                                                                                                     
		pEntry = ldap_next_entry(m_pLdap, m_pLastReturnedEntry);
	} else {
		                                                                                                                             
		pEntry = ldap_first_entry(m_pLdap, pSearchResult);
	}

	BerElement* pBer = 0;
    PCHAR pAttribute = 0;
    PCHAR* ppValue = 0;
    PCHAR pDn = 0;
	berval** ppBerval = 0;
	bool isBase64 = false;
	int i=0;
	while( pEntry ) {
		if( params.includeDnInSearchResult )
			pDn = ldap_get_dn(m_pLdap, pEntry);
		LdapTreeNode *pTreeEntry = new LdapTreeNode("Entry", "", false, pDn?pDn:"");
		pResult->AddNode(pTreeEntry);
		pAttribute = ldap_first_attribute(m_pLdap, pEntry, &pBer);

		while( pAttribute ) {
			ppBerval = ldap_get_values_len(m_pLdap, pEntry, pAttribute);
			if( ppBerval ) {
				unsigned long nValues = ldap_count_values_len(ppBerval);
				for(unsigned long n = 0; n<nValues; ++n) {
					string sName(pAttribute);
					string sData(EncodeBerval( ppBerval[n], isBase64));
					if ( m_conv ) {
						sName = m_conv->from(sName);
						if ( !isBase64 ) {
							sData = m_conv->from(sData);
						}
					}
					pTreeEntry->AddNode(new LdapTreeNode(sName, sData, isBase64));
				}

				if( ppBerval )  
					ldap_value_free_len(ppBerval);
				ppBerval = 0;
			}

			ldap_memfree(pAttribute);

			pAttribute = ldap_next_attribute(m_pLdap, pEntry, pBer);
		}

		if( pDn )
			ldap_memfree(pDn);

		if( pBer )                                                          
			ber_free(pBer,0);
		pBer = 0;

		if( iChunkSize > 0 && i++ >= iChunkSize )
			break;                  

		pEntry = ldap_next_entry(m_pLdap, pEntry);
	}

	if( ppValue )
		ldap_value_free(ppValue);

	m_pLastReturnedEntry = pEntry;
	if( !pEntry )
		m_pSearchResult = 0;
}

LdapTreeNode* Ldap::GetSearchResults(LDAPMessage* pSearchResult, const LdapSearchParameters& params, int iChunkSize          ) {

	LdapTreeNode *pResult = new LdapTreeNode("DirectoryResults");

	bool bMoreResults = false;
	do {
		LoopOnePage(pSearchResult, pResult, params, iChunkSize);

		if( params.usePagedSearch ) {
			#ifdef WIN32
			unsigned long lTotalCount = 0;                                                                                                
			unsigned long lPageSize = 500;
			unsigned long nLdapRes = ldap_get_next_page_s(m_pLdap, m_pLdapSearchBlock, 0, lPageSize, &lTotalCount, &pSearchResult);
			bMoreResults = nLdapRes != LDAP_NO_RESULTS_RETURNED && nLdapRes != LDAP_NO_SUCH_OBJECT && nLdapRes != LDAP_NO_SUCH_ATTRIBUTE;
			if( nLdapRes != LDAP_SUCCESS && nLdapRes != LDAP_NO_RESULTS_RETURNED ) {
				string sErr = "Failed to search with the filter '" + m_sFilter + "' under the distinguished name '" +
					m_sDistinguishedName + "' . Error: " + GetLastLdapError(nLdapRes);
				throw CrException(sErr);
			}
			#else
			throw CrException("Paged LDAP search is not supported on this platform.");
			#endif
		}
	} while(bMoreResults);

	return pResult;
}

LdapTreeNode* Ldap::NextSearchChunk(int iChunkSize            ) {
	assert(m_pSearchResult && "m_pSearchResult != 0 ");
	return GetSearchResults(m_pSearchResult, m_SearchParams, iChunkSize);
}

LdapTreeNode* Ldap::Search(const string& sDistinguishedName, const string& sFilter,
						 const vector<string>& vAttributes, const LdapSearchParameters& params, int iChunkSize          ) {
	if( !m_pLdap )
		throw CrException("Must be connected to a directory server before executing LDAP queries.");

	if( iChunkSize > 0 )
		m_SearchParams = params;                                            
	LDAPMessage* pSearchResult = InitializeSearch(sDistinguishedName, sFilter, vAttributes, params);
	return GetSearchResults(pSearchResult, params, iChunkSize);
}

string konga::LdapTreeNodeToSimpleString(const LdapTreeNode& node) {
	LdapTreeNode::const_iterator citNode = node.begin();
	if( citNode == node.end() )
		return node.GetData();                                     
	else
		return LdapTreeNodeToSimpleString( **citNode );
}

namespace {

string LdapTreeNodeToXmlStringRecurse(const LdapTreeNode& node, const string& sIndent = "\t") {
	LdapTreeNode::const_iterator citNode = node.begin();
	string sTag, s;
	for(; citNode != node.end(); ++citNode) {
		sTag = (*citNode)->GetName().empty() ? "Entry" : (*citNode)->GetName();
		s += '\n' + sIndent + "<" + sTag + 
								((*citNode)->IsDataBase64Encoded()?" Base64Encoded=\"true\"":"") +
								((*citNode)->GetDistinguishedName().empty()? "" : " DN=\"" + (*citNode)->GetDistinguishedName() + "\"") +
							  ">";
		if( (*citNode)->size() == 0 )             
			s += xml_text((*citNode)->GetData().c_str(), true);
		else 
			s += LdapTreeNodeToXmlStringRecurse(**citNode, sIndent + '\t') + '\n' + sIndent;
		s += "</" + sTag + ">";
	}
	return s;
}

}                       

string konga::LdapTreeNodeToXmlString(const LdapTreeNode& node) {
	return "<DirectoryResults>" + LdapTreeNodeToXmlStringRecurse(node) + "\n</DirectoryResults>";
}

