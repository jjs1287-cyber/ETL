/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef CROM_ATTR_H
#define CROM_ATTR_H

#include <string>
#include <boost/noncopyable.hpp>
#include <xercesc/sax/AttributeList.hpp>                                

#include <konga/Utils/DateUtils.h>
#include <konga/Utils/KString.h>

#include "ExtraAttribute.h"

class CROM_Attr : boost::noncopyable                                
{
	friend class CROM;
	friend class JtrReader;

protected:
	short			m_fieldPos;
	short			m_fieldLength;
	int				m_DbColType;
	int				m_DbColSize;
	int				m_DbDecimalDigits;
	KString			m_Value,
					m_ValueType,
					m_DefaultValue,
					m_Format,
					m_Domain;                                                              
	KString			m_LinkToPrimaryKey;
	CROM*			m_PkLink;

	                                                                        
	                                                         
	bool			m_unmap;

private:
	ExtraAttribute* m_extra;

	CROM_Attr(const char*value):
		m_fieldPos(0), 
		m_fieldLength(0),
		m_DbColType(0),
		m_DbColSize(0),
		m_DbDecimalDigits(0),
		m_Value(value),
		m_ValueType(""),
		m_DefaultValue(""),
		m_Format(""),
		m_Domain(""),
		m_LinkToPrimaryKey(""),
		m_PkLink(0),
		m_extra(0),
		m_unmap(false)
	{
	}

	CROM_Attr(CROM_Attr*to_be_cloned, bool isXml) :
		m_fieldPos(to_be_cloned->m_fieldPos),
		m_fieldLength(to_be_cloned->m_fieldLength),
		m_DbColType(to_be_cloned->m_DbColType),
		m_DbColSize(to_be_cloned->m_DbColSize),
		m_DbDecimalDigits(to_be_cloned->m_DbDecimalDigits),
		m_Value(to_be_cloned->m_Value),
		m_ValueType(to_be_cloned->m_ValueType),
		m_DefaultValue(to_be_cloned->m_DefaultValue),
		m_Format(to_be_cloned->m_Format),
		m_Domain(to_be_cloned->m_Domain),
		m_LinkToPrimaryKey(to_be_cloned->m_LinkToPrimaryKey),
		m_PkLink(0),
		m_extra(0),
		m_unmap(false)
	{
		if(to_be_cloned->m_extra)m_extra=to_be_cloned->m_extra->clone();
		if(isXml)patch();
	}

	~CROM_Attr(void) {
		if ( m_extra ) {
			delete m_extra;
			m_extra = 0;
		}
	}

private:

	CROM_Attr(XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names, bool isXml);             

	void set_fieldPos(short fieldPos){m_fieldPos=fieldPos;}
	void set_fieldLength(short fieldLength){m_fieldLength=fieldLength;}
	void set_DbColType(int DbColType){m_DbColType=DbColType;}
	void set_DbColSize(int DbColSize){m_DbColSize=DbColSize;}
	void set_DbDecimalDigits(int DbDecimalDigits){m_DbDecimalDigits=DbDecimalDigits;}
	void set_Value(const std::string& Value){m_Value=Value;}
	void set_ValueType(const std::string& ValueType){m_ValueType=ValueType;}
	void set_DefaultValue(const std::string& DefaultValue){m_DefaultValue=DefaultValue;}
	void set_Format(const std::string& Format){m_Format=Format;}
	void set_Domain(const std::string& Domain){m_Domain=Domain;}
	void set_LinkToPrimaryKey(const std::string& LinkToPrimaryKey){m_LinkToPrimaryKey=LinkToPrimaryKey;}
	void set_NSQualify(bool b){
		if(!b&&m_extra==0)return;
		if(m_extra==0)m_extra=new ExtraAttribute();
		m_extra->m_NSQualify=b;
	}
	void set_Namespace(const std::string& s){
		if(s.empty()&&m_extra==0)return;
		if(m_extra==0)m_extra=new ExtraAttribute();
		m_extra->m_Namespace=s;
	}
	void set_SchemaType(const std::string& s){
		if(s.empty()&&m_extra==0)return;
		if(m_extra==0)m_extra=new ExtraAttribute();
		m_extra->m_SchemaType=s;
	}
	void set_SchemaTypeNamespace(const std::string& s){
		if(s.empty()&&m_extra==0)return;
		if(m_extra==0)m_extra=new ExtraAttribute();
		m_extra->m_SchemaTypeNamespace=s;
	}

	short get_fieldPos() const {return m_fieldPos;}
	short get_fieldLength() const {return m_fieldLength;}
	int get_DbColSize() const {return  m_DbColSize;}
	int get_DbDecimalDigits() const {return  m_DbDecimalDigits;}
	const KString& get_Value() const {return  m_Value;}
	const KString& get_ValueType() const {return  m_ValueType;}
	const KString& get_DefaultValue() const {return  m_DefaultValue;}
	const KString& get_Format() const {return  m_Format;}
	const KString& get_Domain() const {return  m_Domain;}
	const KString& get_LinkToPrimaryKey() const {return  m_LinkToPrimaryKey;}

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

protected:
	void patch()                                                 
	{
		if(m_Format.IsEmpty()&&(
			m_ValueType==konga::CrDateTime::XML_DATE_FORMAT ||
			m_ValueType==konga::CrDateTime::XML_TIME_FORMAT ||
			m_ValueType==konga::CrDateTime::XML_DATE_TIME_FORMAT))m_Format=m_ValueType;
	}
	void set_unmapped(bool flag=true){m_unmap=flag;}
	bool isUnmapped(){
		if(m_unmap){
			m_unmap=false;
			return true;
		}
		else return false;
	}
};

#endif               
