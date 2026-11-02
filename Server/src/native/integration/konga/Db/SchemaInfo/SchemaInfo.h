/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef SCHEMAINFO_H
#define SCHEMAINFO_H

#include <string>
#include <list>
#include <map>
#include <vector>

#include <dtl/DTL.h>

#define USE_ODBC_FOR_SCHEMA_INFO
                                  

namespace konga {

class DatabaseObject;
class DatabaseColumn;

class SchemaInfo {
public:
	SchemaInfo(const std::string& stConnectionString) : m_stConnectionString(stConnectionString) {}
	virtual ~SchemaInfo() {}

	                                                                            
	                                                             
	                                                                               
	                                                                                                                                  
	virtual void GetObjects(std::vector<DatabaseObject*>& objs, std::map<std::string, bool> mObjTypes,
		const std::string& stTableSearchString, bool bGetColumnInformation,
		const std::string& stSchemaSearchString = "",
		bool bSqlAttrMetaDataId = false);

	                                                                                                               
	std::string GetQuoteCharacter() const { return m_stQuoteChar; };

	void GetDrivers(std::vector<std::string>& vDriverNames);

private:
	std::string m_stConnectionString;
	std::string m_stQuoteChar;

	#ifdef USE_DTL_FOR_SCHEMA_INFO
		dtl::DBConnection m_db;
	#endif
};

}                   

#endif                
