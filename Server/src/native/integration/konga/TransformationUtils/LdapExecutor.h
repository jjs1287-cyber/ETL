/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef TRANSFORMATION_UTILS_LDAP_EXECUTOR_H
#define TRANSFORMATION_UTILS_LDAP_EXECUTOR_H

#include <map>
#include <string>
#include <vector>

#include <boost/noncopyable.hpp>

namespace konga {

	class Ldap;
	class LdapMod;
	class LdapTreeNode;

	struct LdapSearchParameters;

	                                               
	                                                                                       
	class LdapExecutor : boost::noncopyable {
	public:
		LdapExecutor(void);
		~LdapExecutor(void);

		                                                                         
		                                                        
		                                                                 
		                                            
		bool LdapConnect(const std::string&host, const std::string&user, const std::string&password, int iSecure, long iPort = -1);

		std::string LdapSearchWithResultAsXml(const std::string& path, const std::string& filter, 
			const std::vector<std::string>&vAttr, const konga::LdapSearchParameters& params, FILE* out=0, bool bReturnNullIfNoResults = false);

		konga::LdapTreeNode* LdapSearch(const std::string& path, const std::string& filter,
			const std::vector<std::string>&vAttr, const konga::LdapSearchParameters& params, int nChunkSize=0);
		konga::LdapTreeNode* NextSearchChunk(int nChunkSize=250);

		bool LdapRename(const std::string& sDn, const std::string& sNewRdn, 
							const std::string& sNewParent, bool bDeleteOldRdn, FILE* out=0);
		bool LdapAdd(const std::string& type, const std::string& value, FILE* out=0);
		bool LdapAdd(const std::string& type, const std::vector<std::string>& values, FILE* out=0);

		bool LdapRemove(const std::string& type, const std::string& value, FILE* out=0);
		bool LdapReplace(const std::string& type, const std::string& value, FILE* out=0);
		bool LdapReplace(const std::string& type, const std::vector<std::string>& values, FILE* out=0);
		bool LdapDelete(const std::string& type, const std::vector<std::string>& values, FILE* out=0);
		bool LdapDeleteEntry(const std::string& dn, FILE* out=0);
		bool LdapExecute(const std::string& path, FILE* out=0);

		void CloseLdapConnections();

	private:
		                                                         
		void CheckForCurrentConnection() const;

	private:
		konga::Ldap* m_ldap;
		konga::LdapMod* m_ldap_mod;
		bool m_bEnableAutoUpdateMode;
		std::map<std::string,konga::Ldap*> m_vLdap;
	};
}

#endif                                        

