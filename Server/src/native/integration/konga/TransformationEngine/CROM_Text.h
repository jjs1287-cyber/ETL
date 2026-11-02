/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef CROM_TEXT_H
#define CROM_TEXT_H

#include <string>
#include <xercesc/sax/AttributeList.hpp>                                

#include "CROM_Attr.h"

class CROM_Text                  
{
	friend class CROM;
	friend class JtrReader;

protected:
	char			m_strQual;
	char			m_numQual;
	char			m_delimiter;
	short			m_identPos;
	short			m_identLength;
	CROM_Text() :
			           
			m_strQual(0), 
			m_numQual(0), 
			m_delimiter(0),
			m_identPos(0),
			m_identLength(0)
	{}

	CROM_Text(CROM_Text*to_be_cloned) :
			m_strQual(to_be_cloned->m_strQual), 
			m_numQual(to_be_cloned->m_numQual), 
			m_delimiter(to_be_cloned->m_delimiter),
			m_identPos(to_be_cloned->m_identPos),
			m_identLength(to_be_cloned->m_identLength)
	{}

	CROM_Text(XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names);             
	~CROM_Text() {}

	void set_strQual(char c){m_strQual=c;}
	char get_strQual() const {return m_strQual;}
	void set_numQual(char c){m_numQual=c;}
	char get_numQual() const {return m_numQual;}
	void set_delimiter(char c){m_delimiter=c;}
	char get_delimiter() const {return m_delimiter;}
	void set_identPos(short s){m_identPos=s;}
	short get_identPos() const{return m_identPos;}
	void set_identLength(short s){m_identLength=s;}
	short get_identLength() const {return m_identLength;}
};
#endif               
