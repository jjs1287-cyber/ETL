/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef CROM_LDAP_H
#define CROM_LDAP_H

#include <boost/noncopyable.hpp>
#include <xercesc/sax/AttributeList.hpp>                                

class CROM_Ldap :  boost::noncopyable
{
	friend class CROM;
	friend class JtrReader;
protected:
	CROM_Ldap(){}
	CROM_Ldap(CROM_Ldap*to_be_cloned){}
	CROM_Ldap(XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names){}             
	~CROM_Ldap(){}
};
#endif              
