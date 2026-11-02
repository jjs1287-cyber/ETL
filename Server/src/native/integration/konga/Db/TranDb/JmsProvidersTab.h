#ifndef KONGA_DB_TRANDB_JmsProvidersTab_H
#define KONGA_DB_TRANDB_JmsProvidersTab_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class JmsProvidersTabRow
{
public:
	JmsProvidersTabRow() : 
			field_ProviderId(0),
			IsProviderIdNull(true),
			IsProviderIdPrimaryKey(true),
			IsProviderIdDirty(false),
			IsProviderNameNull(true),
			IsProviderNamePrimaryKey(false),
			IsProviderNameDirty(false),
			IsClasspathNull(true),
			IsClasspathPrimaryKey(false),
			IsClasspathDirty(false),
			IsProviderDescriptionNull(true),
			IsProviderDescriptionPrimaryKey(false),
			IsProviderDescriptionDirty(false)
	{
	}

	void Copy(const JmsProvidersTabRow& r)
	{
		field_ProviderId = r.field_ProviderId;
		IsProviderIdNull = r.IsProviderIdNull;
		IsProviderIdDirty = r.IsProviderIdDirty;

		field_ProviderName = r.field_ProviderName;
		IsProviderNameNull = r.IsProviderNameNull;
		IsProviderNameDirty = r.IsProviderNameDirty;

		field_Classpath = r.field_Classpath;
		IsClasspathNull = r.IsClasspathNull;
		IsClasspathDirty = r.IsClasspathDirty;

		field_ProviderDescription = r.field_ProviderDescription;
		IsProviderDescriptionNull = r.IsProviderDescriptionNull;
		IsProviderDescriptionDirty = r.IsProviderDescriptionDirty;

	}

	void CopyPKs(const JmsProvidersTabRow& r)
	{
		field_ProviderId = r.field_ProviderId;
		IsProviderIdNull = r.IsProviderIdNull;
		IsProviderIdDirty = r.IsProviderIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProviderIdDirty = b;
		IsProviderNameDirty = b;
		IsClasspathDirty = b;
		IsProviderDescriptionDirty = b;
	}

	bool IsDirty() const
	{
		return IsProviderIdDirty ||
			IsProviderNameDirty ||
			IsClasspathDirty ||
			IsProviderDescriptionDirty;
	}

	int field_ProviderId;
	bool IsProviderIdNull;
	bool IsProviderIdDirty;

	std::string field_ProviderName;
	bool IsProviderNameNull;
	bool IsProviderNameDirty;

	std::string field_Classpath;
	bool IsClasspathNull;
	bool IsClasspathDirty;

	std::string field_ProviderDescription;
	bool IsProviderDescriptionNull;
	bool IsProviderDescriptionDirty;


private:
	bool IsProviderIdPrimaryKey;
	bool IsProviderNamePrimaryKey;
	bool IsClasspathPrimaryKey;
	bool IsProviderDescriptionPrimaryKey;

};


class JmsProvidersTab : public konga::DbRecordSet {
public:
	JmsProvidersTab() {}

	static JmsProvidersTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "JmsProvidersTab";
	}

	void DoFieldExchange(const JmsProvidersTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetProviderId() {
		if( m_row.IsProviderIdNull )
			throw konga::DbNullException("ProviderId");
		return m_row.field_ProviderId;
	}
	void SetProviderId(int i) {
		m_row.field_ProviderId = i;
		m_row.IsProviderIdNull = false;
		m_row.IsProviderIdDirty = true;
	}
	bool IsProviderIdNull() const {
		return m_row.IsProviderIdNull;
	}
	void SetProviderIdToNull() {
		m_row.IsProviderIdNull = true;
		m_row.IsProviderIdDirty = true;
	}

	std::string GetProviderName() {
		if( m_row.IsProviderNameNull )
			throw konga::DbNullException("ProviderName");
		return m_row.field_ProviderName;
	}
	void SetProviderName(const std::string& st) {
		m_row.field_ProviderName = st;
		m_row.IsProviderNameNull = false;
		m_row.IsProviderNameDirty = true;
	}
	bool IsProviderNameNull() const {
		return m_row.IsProviderNameNull;
	}
	void SetProviderNameToNull() {
		m_row.field_ProviderName = "";
		m_row.IsProviderNameNull = true;
		m_row.IsProviderNameDirty = true;
	}

	std::string GetClasspath() {
		if( m_row.IsClasspathNull )
			throw konga::DbNullException("Classpath");
		return m_row.field_Classpath;
	}
	void SetClasspath(const std::string& st) {
		m_row.field_Classpath = st;
		m_row.IsClasspathNull = false;
		m_row.IsClasspathDirty = true;
	}
	bool IsClasspathNull() const {
		return m_row.IsClasspathNull;
	}
	void SetClasspathToNull() {
		m_row.field_Classpath = "";
		m_row.IsClasspathNull = true;
		m_row.IsClasspathDirty = true;
	}

	std::string GetProviderDescription() {
		if( m_row.IsProviderDescriptionNull )
			throw konga::DbNullException("ProviderDescription");
		return m_row.field_ProviderDescription;
	}
	void SetProviderDescription(const std::string& st) {
		m_row.field_ProviderDescription = st;
		m_row.IsProviderDescriptionNull = false;
		m_row.IsProviderDescriptionDirty = true;
	}
	bool IsProviderDescriptionNull() const {
		return m_row.IsProviderDescriptionNull;
	}
	void SetProviderDescriptionToNull() {
		m_row.field_ProviderDescription = "";
		m_row.IsProviderDescriptionNull = true;
		m_row.IsProviderDescriptionDirty = true;
	}

protected:
	JmsProvidersTabRow m_row;

private:
	bool IsProviderIdDirty() const {
		return m_row.IsProviderIdDirty;
	};
	bool IsProviderNameDirty() const {
		return m_row.IsProviderNameDirty;
	};
	bool IsClasspathDirty() const {
		return m_row.IsClasspathDirty;
	};
	bool IsProviderDescriptionDirty() const {
		return m_row.IsProviderDescriptionDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
