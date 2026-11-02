/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include "LdapFilter.h"

using namespace konga;
using std::string;

const LdapClassFilter::SearchScope LdapClassFilter::DEFAULT_LDAP_SEARCH_SCOPE = LdapClassFilter::LDAP_SEARCH_SCOPE_ONE_LEVEL;

LdapClassFilter::LdapClassFilter(const std::string& class_name, const std::string& path, const std::string& filter, SearchScope scope) :
	m_scope(scope), m_class_name(class_name), m_path(path), m_filter("")
{
	setFilter(filter);
}

string LdapClassFilter::getClassName() const {
	return m_class_name;
}

void LdapClassFilter::setClassName(const std::string& sClassName) {
	m_class_name = sClassName;
}

string LdapClassFilter::getPath() const {
	return m_path;
}

void LdapClassFilter::setPath(const std::string& sPath) {
	m_path = sPath;
}

string LdapClassFilter::getFilter() const {
	return m_filter;
}

void LdapClassFilter::setFilter(const std::string& filter) {
	if ( filter.empty() ) {
		if ( !m_class_name.empty() ) {
			m_filter = "(objectClass=" + m_class_name + ")";
		}
		else {
			m_filter.clear();
		}
	}
	else {
		m_filter=filter;
	}
}

LdapClassFilter::SearchScope LdapClassFilter::getScope() const {
	return m_scope;
}

bool LdapClassFilter::operator==(const LdapClassFilter& filter) const
{
	return
		m_class_name==filter.m_class_name &&
		m_path==filter.m_path &&
		m_filter==filter.m_filter &&
		m_scope==filter.m_scope;
}

LdapFilterCollection::LdapFilterCollection(){it=filters.end();}

LdapFilterCollection::~LdapFilterCollection(){
	for(LdapClassFilter* lf=getFirstLdapClassFiler();lf;lf=getNextLdapClassFilter()){
		if(lf)delete lf;
	}
}

void LdapFilterCollection::add(LdapClassFilter*p){
	filters.push_back(p);
}

LdapClassFilter* LdapFilterCollection::getFirstLdapClassFiler() const {
	it=filters.begin();
	if(it==filters.end())return 0;
	return *it;
}
LdapClassFilter* LdapFilterCollection::getNextLdapClassFilter() const {
	if(it==filters.end())return 0;
	if((++it)==filters.end())return 0;
	return *it;
}

int LdapFilterCollection::size() const {
	return filters.size();
}

