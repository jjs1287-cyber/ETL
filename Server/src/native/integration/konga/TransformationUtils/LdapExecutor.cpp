/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <memory>					                

#include <konga/Utils/Settings.h>		                   
#include <konga/Utils/StringUtils.h>	             
#include <konga/Utils/CrException.h>

#include <konga/Ldap/konga_ldap.h>
#include <konga/Ldap/LdapTreeNode.h>

#include "LdapExecutor.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

 LdapExecutor::LdapExecutor(void) : m_ldap(0), m_ldap_mod(0),
	 m_bEnableAutoUpdateMode(GetOptionAsBoolean("TransformationEngine", "LdapEnableAutoUpdateMode", false))
 {
 }

 LdapExecutor::~LdapExecutor(void) {
	 CloseLdapConnections();
 }

bool LdapExecutor::LdapConnect(const string&host, const string&user, const string&password, int iSecure, long lPort)
{
	                     
	if(m_ldap_mod!=0){
		delete m_ldap_mod;
	}
	m_ldap_mod=new konga::LdapMod();

	const string key(host+","+user+","+password+","+konga::toString(lPort));
	map<string,konga::Ldap*>::const_iterator it=m_vLdap.find(key);
	if(it==m_vLdap.end()){
		std::auto_ptr<konga::Ldap> pLdap(new konga::Ldap());
		const bool bUseReferrals = GetOptionAsInt("LDAP", "UseReferrals", 0) != 0;
		pLdap->Connect(host,user,password,iSecure,lPort,bUseReferrals);
		m_ldap=m_vLdap[key]=pLdap.release();
		return true;
	} else {
		m_ldap=it->second;
		return false;
	}
}

string LdapExecutor::LdapSearchWithResultAsXml(const string& path, const string& filter, const vector<string>&vAttr,
										const LdapSearchParameters& params, FILE* out, bool bReturnNullIfNoResults)
{
	CheckForCurrentConnection();
	std::auto_ptr<LdapTreeNode> pRoot(m_ldap->Search(path, filter, vAttr, params));
	if(out)fprintf(out,"$$$$$$ LdapSearch(\"%s\",\"%s\", %d)\n",path.c_str(), filter.c_str(), params.iDetail);
	string sRes;
	if( pRoot.get() ) {
		if( bReturnNullIfNoResults && pRoot->size() == 0 ) return "";
		if( params.iDetail > 0 )
			return LdapTreeNodeToXmlString( *pRoot.get() );
		else
			return LdapTreeNodeToSimpleString( *pRoot.get() );
	}
	return sRes;
}

                                                                                 
konga::LdapTreeNode* LdapExecutor::LdapSearch(const string& path, const string& filter, const vector<string>&vAttr, const LdapSearchParameters& params, int nChunk)
{
	CheckForCurrentConnection();
	return m_ldap->Search(path,filter,vAttr,params, nChunk);
}

konga::LdapTreeNode* LdapExecutor::NextSearchChunk(int nChunk)
{
	return m_ldap->NextSearchChunk(nChunk);
}


bool LdapExecutor::LdapAdd(const string& type, const string& value, FILE* out)
{
	if(m_ldap_mod==0) m_ldap_mod = new konga::LdapMod();
	m_ldap_mod->AddAttribute(type,value);
	if(out)fprintf(out,"$$$$$$ LdapAdd(\"%s\",\"%s\")\n",type.c_str(), value.c_str());
	return true;
}

namespace {
	string GetMultipleValuesForDebugLog(const vector<string>& values) {
		string str("[");
		for(vector<string>::const_iterator cit = values.begin(); cit != values.end(); ++cit) {
			if(str.size()>1) str+=",";
			str += *cit;
		}
		str += "]";
		return str;
	}
}

bool LdapExecutor::LdapAdd(const std::string& type, const std::vector<std::string>& values, FILE* out) {
	if(m_ldap_mod==0) m_ldap_mod = new konga::LdapMod();
	m_ldap_mod->AddAttribute(type, values);
	if(out)fprintf(out,"$$$$$$ LdapAdd(\"%s\",%s)\n",type.c_str(), GetMultipleValuesForDebugLog(values).c_str());
	return true;
}

bool LdapExecutor::LdapRemove(const string& type, const string& value, FILE* out)
{
	if(m_ldap_mod==0) m_ldap_mod = new konga::LdapMod();
	m_ldap_mod->DeleteAttribute(type,value);
	if(out)fprintf(out,"$$$$$$ LdapRemove(\"%s\",\"%s\")\n",type.c_str(), value.c_str());
	return true;
}

bool LdapExecutor::LdapRename(const string& sDn, const string& sNewRdn, const string& sNewParent, bool bDeleteOldRdn, FILE* out)
{
	CheckForCurrentConnection();
	m_ldap->Rename(sDn, sNewRdn, sNewParent, bDeleteOldRdn);
	if(out)fprintf(out,"$$$$$$ LdapRename(\"%s\",\"%s\",\"%s\",\"%s\")\n", sDn.c_str(), sNewRdn.c_str(), sNewParent.c_str(), bDeleteOldRdn?"true":"false");
	return true;
}

bool LdapExecutor::LdapDeleteEntry(const string& dn, FILE* out)
{
	CheckForCurrentConnection();
	m_ldap->DeleteEntry(dn);
	if(out)fprintf(out,"$$$$$$ LdapDeleteEntry(\"%s\")\n", dn.c_str());
	return true;
}

bool LdapExecutor::LdapReplace(const std::string& type, const std::vector<std::string>& values, FILE* out) {
	if(m_ldap_mod==0) m_ldap_mod = new konga::LdapMod();
	m_ldap_mod->ReplaceAttribute(type,values);
	if(out)fprintf(out,"$$$$$$ LdapReplace(\"%s\",%s)\n",type.c_str(), GetMultipleValuesForDebugLog(values).c_str());
	return true;
}

bool LdapExecutor::LdapDelete(const std::string& type, const std::vector<std::string>& values, FILE* out) {
	if(m_ldap_mod==0) m_ldap_mod = new konga::LdapMod();
	m_ldap_mod->DeleteAttribute(type,values);
	if(out)fprintf(out,"$$$$$$ LdapDelete(\"%s\",%s)\n",type.c_str(), GetMultipleValuesForDebugLog(values).c_str());
	return true;
}

bool LdapExecutor::LdapReplace(const string& type, const string& value, FILE* out)
{
	if(m_ldap_mod==0) m_ldap_mod = new konga::LdapMod();
	m_ldap_mod->ReplaceAttribute(type,value);
	if(out)fprintf(out,"$$$$$$ LdapReplace(\"%s\",\"%s\")\n",type.c_str(), value.c_str());
	return true;
}

namespace {

	                                                                     
	template<typename T>
	class AutoDeleteAndNullPtr {
	public:
		AutoDeleteAndNullPtr(T*& p) : m_p(p) {}
		~AutoDeleteAndNullPtr(void) {
			if ( m_p ) {
				delete m_p;
				m_p = 0;
			}
		}
	private:
		T*& m_p;
	};
}

bool LdapExecutor::LdapExecute(const string& path, FILE* out)
{
	if(out)fprintf(out,"$$$$$$ LdapExecute(\"%s\")\n",path.c_str());
	if(m_ldap_mod==0) return false;

	CheckForCurrentConnection();

	                                                               
	AutoDeleteAndNullPtr<konga::LdapMod> adopt_m_ldap_mod(m_ldap_mod);
	m_ldap->Exec(*m_ldap_mod, path, m_bEnableAutoUpdateMode);
	return true;
}

void LdapExecutor::CheckForCurrentConnection() const {
	if ( m_ldap == 0 )
		throw CrException("Failed to perform LDAP operation. There is no current LDAP connection. Please call LdapConnect first.");
}

void LdapExecutor::CloseLdapConnections() {
	if(m_ldap_mod!=0) {
		delete m_ldap_mod;
		m_ldap_mod=0;
	}

	for(map<string,konga::Ldap*>::iterator it=m_vLdap.begin();it!=m_vLdap.end();it++){
		it->second->Disconnect();
		delete it->second;
		it->second = 0;
	}
	m_vLdap.clear();
	m_ldap = 0;
}
