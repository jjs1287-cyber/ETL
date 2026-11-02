/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_SCHEMA_INFO_DATABASE_COLUMN_H
#define ETLFORGE_SCHEMA_INFO_DATABASE_COLUMN_H

#include <string>

namespace konga {

class DatabaseColumn {
public:
	DatabaseColumn() : m_bIsNullable(false), m_bIsPrimaryKey(false), m_SqlType(-1), m_CharacterSize(0), m_DecimalDigits(0) {}
	virtual ~DatabaseColumn() {}

	void SetName(const std::string& stName) { m_stName = stName; }
	std::string GetName() const { return m_stName; }

	void SetType(const std::string& stType) { m_stType = stType; }
	std::string GetType() const { return m_stType; }

	void SetSqlType(long sqlType) { m_SqlType = sqlType; }
	long GetSqlType() const { return m_SqlType; }

	void SetCharacterSize(long size) { m_CharacterSize = size; }
	long GetCharacterSize() const { return m_CharacterSize; }

	void SetDecimalDigits(long digits) { m_DecimalDigits = digits; }
	long GetDecimalDigits() const { return m_DecimalDigits; }

	void SetDefaultValue(const std::string& stDefaultValue) { m_stDefaultValue = stDefaultValue; }
	std::string GetDefaultValue() const { return m_stDefaultValue; }

	void SetNullable(bool bNullable) { m_bIsNullable = bNullable; }
	bool IsNullable() const { return m_bIsNullable; }

	void SetPrimaryKey(bool bPrimaryKey) { m_bIsPrimaryKey = bPrimaryKey; }
	bool IsPrimaryKey() const { return m_bIsPrimaryKey; }

private:
	bool m_bIsNullable;
	bool m_bIsPrimaryKey;
	long m_SqlType;
	long m_CharacterSize;
	long m_DecimalDigits;
	std::string m_stName;
	std::string m_stType;
	std::string m_stDefaultValue;
};

}                   

#endif                                          
