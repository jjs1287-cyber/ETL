/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef CROM_DB_H
#define CROM_DB_H

#include <string>
#include <xercesc/sax/AttributeList.hpp>                                

class CROM;
class CROM_Attr;

class CROM_DB                        
{
	friend class CROM;
	friend class JtrReader;

public:
	enum MODE {unset, set_insert, set_update};

protected:
	int				m_iFKCounter;			                                       
	                                                                                         
	                                                            
	MODE m_insert_update;
	

	KString			m_SQLFilter;
	KString			m_SQLOrderBy;
	                                                        
	KString			m_Schema;
	KString			m_TableName;
	std::string		m_ManualSQL;

	CROM_DB() :
			m_iFKCounter(0),
			m_SQLFilter(""),
			m_Schema(""),
			m_TableName(""),
			m_ManualSQL(""),
			m_insert_update(unset)
	{}

	CROM_DB(CROM_DB*to_be_cloned) :
			m_iFKCounter(to_be_cloned->m_iFKCounter),
			m_SQLFilter(to_be_cloned->m_SQLFilter),
			m_Schema(to_be_cloned->m_Schema),
			m_TableName(to_be_cloned->m_TableName),
			m_ManualSQL(to_be_cloned->m_ManualSQL),
			m_insert_update(unset)
	{}
	CROM_DB(XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names);             

	                          
	                                               

	void set_FKCounter(short s){m_iFKCounter=s;}
	short get_FKCounter() const {return m_iFKCounter;}
	void set_sqlFilter(const std::string& s){m_SQLFilter=s.c_str();}
	const KString& get_sqlFilter() const {return m_SQLFilter;}
	void set_sqlOrderBy(const std::string& s){m_SQLOrderBy=s.c_str();}
	const KString& get_sqlOrderBy() const {return m_SQLOrderBy;}
	void set_dbSchema(const std::string& s){
		m_Schema=s.c_str();
	}
	const KString& get_dbSchema() const {return m_Schema;}
	void set_sqlTableName(const std::string& s){
		m_TableName=s;
	}
	const KString& get_sqlTableName(){return m_TableName;}
	void set_manualSQL(const std::string& s) {
		m_ManualSQL = s;
	}
	const std::string& get_manualSQL() const {
		return m_ManualSQL;
	}
	void set_MODE(MODE mode){
		m_insert_update=mode;
	}
	MODE get_MODE(){
		switch(m_insert_update){
			case set_insert:
				m_insert_update=unset;
				return set_insert;
			case set_update:
				m_insert_update=unset;
				return set_update;
			default:
				return unset;
		}
	}
};

#endif             
