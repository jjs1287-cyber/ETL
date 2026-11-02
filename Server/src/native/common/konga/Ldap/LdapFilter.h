/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef LDAPFILTER_H
#define LDAPFILTER_H

#include <string>
#include <vector>

#include <boost/noncopyable.hpp>

#ifdef WIN32
	#include <windows.h>
	#include <winldap.h>
#else
	                                                 
	#define LDAP_DEPRECATED 1
	#include <ldap.h>
#endif

namespace konga {

   
                                                                                             
                                                                            
                                                               
                                                         
   
class LdapClassFilter {
public:
	enum SearchScope {
		LDAP_SEARCH_SCOPE_BASE=LDAP_SCOPE_BASE,
		LDAP_SEARCH_SCOPE_ONE_LEVEL=LDAP_SCOPE_ONELEVEL,
		LDAP_SEARCH_SCOPE_SUBTREE=LDAP_SCOPE_SUBTREE
	};

	static const SearchScope DEFAULT_LDAP_SEARCH_SCOPE;;

	LdapClassFilter::LdapClassFilter(const std::string& class_name, const std::string& path, const std::string& filter, SearchScope scope = DEFAULT_LDAP_SEARCH_SCOPE);

	~LdapClassFilter(void) {}

	std::string getClassName() const;

	void setClassName(const std::string& sClassName);

	std::string getPath() const;

	void setPath(const std::string& sPath);

	std::string getFilter() const;

	void setFilter(const std::string& filter);

	SearchScope getScope() const;

	bool operator==(const LdapClassFilter& filter) const;

private:
	SearchScope m_scope;
	std::string m_class_name;
	std::string m_path;
	std::string m_filter;
};

                                             
class LdapFilterCollection : boost::noncopyable {
public:
	LdapFilterCollection(void);

	~LdapFilterCollection(void);

	void add(LdapClassFilter*p);

	LdapClassFilter* getFirstLdapClassFiler() const;

	LdapClassFilter* getNextLdapClassFilter() const;

	int size() const;


private:
	std::vector<LdapClassFilter*> filters;
	mutable std::vector<LdapClassFilter*>::const_iterator it;
};

}

#endif                
