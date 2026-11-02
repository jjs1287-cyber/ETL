/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef CROM_EXTRA_ATTRIBUTE_H
#define CROM_EXTRA_ATTRIBUTE_H

#include <string>

class ExtraAttribute
{
	friend class CROM_Attr;
	friend class CROM_Xml;

protected:
	bool m_NSQualify;
	std::string m_Namespace;
	std::string m_SchemaType;
	std::string m_SchemaTypeNamespace;


protected:
	ExtraAttribute() : m_NSQualify(false)
	{}

	ExtraAttribute* clone(){
		ExtraAttribute* e=new ExtraAttribute();
		e->m_NSQualify=m_NSQualify;
		e->m_Namespace=m_Namespace;
		e->m_SchemaType=m_SchemaType;
		e->m_SchemaTypeNamespace=m_SchemaTypeNamespace;
		return e;
	}
};


#endif                          
