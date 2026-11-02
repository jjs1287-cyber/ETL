/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef DATABASE_OBJECT_H
#define DATABASE_OBJECT_H

#include <vector>
#include <string>

#include "DatabaseColumn.h"

namespace konga {

class DatabaseObject {
public:
	DatabaseObject(const std::string& stObjType) : m_stObjectType(stObjType) {}
	virtual ~DatabaseObject();

	std::string GetName() const { return m_stName; }
	void SetName(const std::string& stName) { m_stName = stName; }

	std::string GetSchemaName() const { return m_stSchemaName; }
	void SetSchemaName(const std::string& stSchemaName) { m_stSchemaName = stSchemaName; }	

	std::string GetObjectType() const { return m_stObjectType; }
	void SetObjectType(const std::string& stObjectType) { m_stObjectType = stObjectType; }

	std::vector<DatabaseColumn*>& GetColumns() { return m_columns; }
	void SetColumns(const std::vector<DatabaseColumn*>& cols) { m_columns = cols; }
	void AddColumn(DatabaseColumn* pColumn) { m_columns.push_back(pColumn); }

private:
	std::string m_stName, m_stSchemaName, m_stObjectType;
	std::vector<DatabaseColumn*> m_columns;
};

}                   

#endif                     
