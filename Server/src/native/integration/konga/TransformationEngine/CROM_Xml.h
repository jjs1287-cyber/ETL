/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef CROM_XML_H
#define CROM_XML_H

#include <boost/noncopyable.hpp>
#include <xercesc/sax/AttributeList.hpp>                                

#include <konga/Utils/MapUtils.h>

#include "CROM_Attr.h"

class ExtraAttribute;

class CROM_Xml : boost::noncopyable
{
	friend class CROM;
	friend class JtrReader;
private:
	bool			m_isAbstract;
	int				m_iMinUse;
	int				m_iMaxUse;
	KString			m_sArrayType;
	std::string		m_sAnyElementNamespace;

	ExtraAttribute* m_extra;
	CROM*			m_pComplexTypeDefinition;
	std::map<std::string, std::string> m_attributeMap;
	std::string m_prefix;

protected:
	typedef std::map<std::string, bool> strBoolMapT;
	strBoolMapT	m_TypeExtensionContainer;
	strBoolMapT	m_SubstitutionGroupContainer;

	CROM_Xml() :
			m_isAbstract(false),
			m_iMinUse(1), 
			m_iMaxUse(1),
			m_sArrayType(""), 
			m_extra(0),
			m_pComplexTypeDefinition(0)

	{}

	CROM_Xml(CROM_Xml* to_be_cloned) :
			m_isAbstract(to_be_cloned->m_isAbstract), 
			m_iMinUse(to_be_cloned->m_iMinUse), 
			m_iMaxUse(to_be_cloned->m_iMaxUse),
			m_sArrayType(to_be_cloned->m_sArrayType), 
			m_sAnyElementNamespace(to_be_cloned->m_sAnyElementNamespace),
			m_extra(0),
			m_pComplexTypeDefinition(to_be_cloned->m_pComplexTypeDefinition),
			m_TypeExtensionContainer(to_be_cloned->m_TypeExtensionContainer),
			m_SubstitutionGroupContainer(to_be_cloned->m_SubstitutionGroupContainer)
	{
		if(to_be_cloned->m_extra)m_extra=to_be_cloned->m_extra->clone();
	}

	                          
	                                                                
	CROM_Xml(XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names);             

	~CROM_Xml(void) {
		if ( m_extra ) {
			delete m_extra;
			m_extra = 0;
		}
	}
	
	const std::map<std::string, std::string>& get_AttributeMap(){
		return m_attributeMap;
	}
	void addAttribute(const std::string& name, const std::string& value){
		m_attributeMap[name]=value;
	}

public:
	void addTypeExtension(const std::string& typeName){
		konga::AddOrUpdate<strBoolMapT, std::string, bool>(m_TypeExtensionContainer, typeName, true);
	}
	
	void addSubstitutionGroup(const std::string& typeName){
		konga::AddOrUpdate<strBoolMapT, std::string, bool>(m_SubstitutionGroupContainer, typeName, true);
	}

	void set_Abstract(bool b){m_isAbstract=b;}
	void set_MinUse(int i){m_iMinUse=i;}
	void set_MaxUse(int i){m_iMaxUse=i;}
	void set_ArrayType(const std::string& s){m_sArrayType=s.c_str();}
	void set_AnyElementNamespace(const std::string& s){m_sAnyElementNamespace=s;}
	void set_NSQualify(bool b){
		if(b==false&&m_extra==0)return;
		if(b&&m_extra==0)m_extra=new ExtraAttribute();
		m_extra->m_NSQualify=b;
	}
	void set_Namespace(const std::string& s){
		if(s.length()==0&&m_extra==0)return;
		if(m_extra==0)m_extra=new ExtraAttribute();
		m_extra->m_Namespace=s;
	}
	void set_SchemaType(const std::string& s){
		if(s.length()==0&&m_extra==0)return;
		if(m_extra==0)m_extra=new ExtraAttribute();
		m_extra->m_SchemaType=s;
	}

	void set_SchemaTypeNamespace(const std::string& s){
		if(s.length()==0&&m_extra==0)return;
		if(&m_extra==0)m_extra=new ExtraAttribute();
		m_extra->m_SchemaTypeNamespace=s;
	}

	bool IsAbstract() const {return m_isAbstract;}
	int get_MinUse() const {return m_iMinUse;}
	int get_MaxUse() const {return m_iMaxUse;}
	const KString& get_ArrayType() const {return m_sArrayType;}
	const std::string& get_AnyElementNamespace() const {return m_sAnyElementNamespace;}
	bool get_NSQualify() const {
		if(m_extra==0)return false;
		return m_extra->m_NSQualify;
	}
	std::string get_Namespace() const {
		if(m_extra==0)return std::string();
		return m_extra->m_Namespace;
	}
	std::string get_SchemaType() const {
		if(m_extra==0)return std::string();
		return m_extra->m_SchemaType;
	}
	std::string get_SchemaTypeNamespace() const {
		if(m_extra==0)return std::string();
		return m_extra->m_SchemaTypeNamespace;
	}
	const std::string& getPrefix() const {return m_prefix;}
	void setPrefix(const std::string& prefix){m_prefix=prefix;}
};
#endif              
