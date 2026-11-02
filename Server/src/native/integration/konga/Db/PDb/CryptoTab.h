/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_CRYPTOTAB_H
#define KONGA_DB_CRPDB_CRYPTOTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace PDb
{

class CryptoTabRow
{
public:
	CryptoTabRow() : 
			field_CryptoInfoId(0),
			IsCryptoInfoIdNull(true),
			IsCryptoInfoIdPrimaryKey(true),
			IsCryptoInfoIdDirty(false),
			IsCryptoMethodNull(true),
			IsCryptoMethodPrimaryKey(false),
			IsCryptoMethodDirty(false),
			field_EncryptionAlgoritm(0),
			IsEncryptionAlgoritmNull(true),
			IsEncryptionAlgoritmPrimaryKey(false),
			IsEncryptionAlgoritmDirty(false),
			field_SigningAlgoritm(0),
			IsSigningAlgoritmNull(true),
			IsSigningAlgoritmPrimaryKey(false),
			IsSigningAlgoritmDirty(false),
			IsCertificateIdNull(true),
			IsCertificateIdPrimaryKey(false),
			IsCertificateIdDirty(false),
			IsPasswordNull(true),
			IsPasswordPrimaryKey(false),
			IsPasswordDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false)
	{
		field_InsertTime.year = 0;
		field_InsertTime.month = 0;
		field_InsertTime.day = 0;
		field_InsertTime.hour = 0;
		field_InsertTime.minute = 0;
		field_InsertTime.second = 0;
		field_InsertTime.fraction = 0;
		field_ModTime.year = 0;
		field_ModTime.month = 0;
		field_ModTime.day = 0;
		field_ModTime.hour = 0;
		field_ModTime.minute = 0;
		field_ModTime.second = 0;
		field_ModTime.fraction = 0;
	}

	void Copy(const CryptoTabRow& r)
	{
		field_CryptoInfoId = r.field_CryptoInfoId;
		IsCryptoInfoIdNull = r.IsCryptoInfoIdNull;
		IsCryptoInfoIdDirty = r.IsCryptoInfoIdDirty;

		field_CryptoMethod = r.field_CryptoMethod;
		IsCryptoMethodNull = r.IsCryptoMethodNull;
		IsCryptoMethodDirty = r.IsCryptoMethodDirty;

		field_EncryptionAlgoritm = r.field_EncryptionAlgoritm;
		IsEncryptionAlgoritmNull = r.IsEncryptionAlgoritmNull;
		IsEncryptionAlgoritmDirty = r.IsEncryptionAlgoritmDirty;

		field_SigningAlgoritm = r.field_SigningAlgoritm;
		IsSigningAlgoritmNull = r.IsSigningAlgoritmNull;
		IsSigningAlgoritmDirty = r.IsSigningAlgoritmDirty;

		field_CertificateId = r.field_CertificateId;
		IsCertificateIdNull = r.IsCertificateIdNull;
		IsCertificateIdDirty = r.IsCertificateIdDirty;

		field_Password = r.field_Password;
		IsPasswordNull = r.IsPasswordNull;
		IsPasswordDirty = r.IsPasswordDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const CryptoTabRow& r)
	{
		field_CryptoInfoId = r.field_CryptoInfoId;
		IsCryptoInfoIdNull = r.IsCryptoInfoIdNull;
		IsCryptoInfoIdDirty = r.IsCryptoInfoIdDirty;

	}

	void SetDirty(bool b)
	{
		IsCryptoInfoIdDirty = b;
		IsCryptoMethodDirty = b;
		IsEncryptionAlgoritmDirty = b;
		IsSigningAlgoritmDirty = b;
		IsCertificateIdDirty = b;
		IsPasswordDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsCryptoInfoIdDirty ||
			IsCryptoMethodDirty ||
			IsEncryptionAlgoritmDirty ||
			IsSigningAlgoritmDirty ||
			IsCertificateIdDirty ||
			IsPasswordDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_CryptoInfoId;
	bool IsCryptoInfoIdNull;
	bool IsCryptoInfoIdDirty;

	std::string field_CryptoMethod;
	bool IsCryptoMethodNull;
	bool IsCryptoMethodDirty;

	int field_EncryptionAlgoritm;
	bool IsEncryptionAlgoritmNull;
	bool IsEncryptionAlgoritmDirty;

	int field_SigningAlgoritm;
	bool IsSigningAlgoritmNull;
	bool IsSigningAlgoritmDirty;

	std::string field_CertificateId;
	bool IsCertificateIdNull;
	bool IsCertificateIdDirty;

	std::string field_Password;
	bool IsPasswordNull;
	bool IsPasswordDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsCryptoInfoIdPrimaryKey;
	bool IsCryptoMethodPrimaryKey;
	bool IsEncryptionAlgoritmPrimaryKey;
	bool IsSigningAlgoritmPrimaryKey;
	bool IsCertificateIdPrimaryKey;
	bool IsPasswordPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class CryptoTab : public konga::DbRecordSet {
public:
	CryptoTab() {}

	static CryptoTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "CryptoTab";
	}

	void DoFieldExchange(const CryptoTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetCryptoInfoId() {
		if( m_row.IsCryptoInfoIdNull )
			throw konga::DbNullException("CryptoInfoId");
		return m_row.field_CryptoInfoId;
	}
	void SetCryptoInfoId(int i) {
		m_row.field_CryptoInfoId = i;
		m_row.IsCryptoInfoIdNull = false;
		m_row.IsCryptoInfoIdDirty = true;
	}
	bool IsCryptoInfoIdNull() const {
		return m_row.IsCryptoInfoIdNull;
	}
	void SetCryptoInfoIdToNull() {
		m_row.IsCryptoInfoIdNull = true;
		m_row.IsCryptoInfoIdDirty = true;
	}

	std::string GetCryptoMethod() {
		if( m_row.IsCryptoMethodNull )
			throw konga::DbNullException("CryptoMethod");
		return m_row.field_CryptoMethod;
	}
	void SetCryptoMethod(const std::string& st) {
		m_row.field_CryptoMethod = st;
		m_row.IsCryptoMethodNull = false;
		m_row.IsCryptoMethodDirty = true;
	}
	bool IsCryptoMethodNull() const {
		return m_row.IsCryptoMethodNull;
	}
	void SetCryptoMethodToNull() {
		m_row.field_CryptoMethod = "";
		m_row.IsCryptoMethodNull = true;
		m_row.IsCryptoMethodDirty = true;
	}

	int GetEncryptionAlgoritm() {
		if( m_row.IsEncryptionAlgoritmNull )
			throw konga::DbNullException("EncryptionAlgoritm");
		return m_row.field_EncryptionAlgoritm;
	}
	void SetEncryptionAlgoritm(int i) {
		m_row.field_EncryptionAlgoritm = i;
		m_row.IsEncryptionAlgoritmNull = false;
		m_row.IsEncryptionAlgoritmDirty = true;
	}
	bool IsEncryptionAlgoritmNull() const {
		return m_row.IsEncryptionAlgoritmNull;
	}
	void SetEncryptionAlgoritmToNull() {
		m_row.IsEncryptionAlgoritmNull = true;
		m_row.IsEncryptionAlgoritmDirty = true;
	}

	int GetSigningAlgoritm() {
		if( m_row.IsSigningAlgoritmNull )
			throw konga::DbNullException("SigningAlgoritm");
		return m_row.field_SigningAlgoritm;
	}
	void SetSigningAlgoritm(int i) {
		m_row.field_SigningAlgoritm = i;
		m_row.IsSigningAlgoritmNull = false;
		m_row.IsSigningAlgoritmDirty = true;
	}
	bool IsSigningAlgoritmNull() const {
		return m_row.IsSigningAlgoritmNull;
	}
	void SetSigningAlgoritmToNull() {
		m_row.IsSigningAlgoritmNull = true;
		m_row.IsSigningAlgoritmDirty = true;
	}

	std::string GetCertificateId() {
		if( m_row.IsCertificateIdNull )
			throw konga::DbNullException("CertificateId");
		return m_row.field_CertificateId;
	}
	void SetCertificateId(const std::string& st) {
		m_row.field_CertificateId = st;
		m_row.IsCertificateIdNull = false;
		m_row.IsCertificateIdDirty = true;
	}
	bool IsCertificateIdNull() const {
		return m_row.IsCertificateIdNull;
	}
	void SetCertificateIdToNull() {
		m_row.field_CertificateId = "";
		m_row.IsCertificateIdNull = true;
		m_row.IsCertificateIdDirty = true;
	}

	std::string GetPassword() {
		if( m_row.IsPasswordNull )
			throw konga::DbNullException("Password");
		return m_row.field_Password;
	}
	void SetPassword(const std::string& st) {
		m_row.field_Password = st;
		m_row.IsPasswordNull = false;
		m_row.IsPasswordDirty = true;
	}
	bool IsPasswordNull() const {
		return m_row.IsPasswordNull;
	}
	void SetPasswordToNull() {
		m_row.field_Password = "";
		m_row.IsPasswordNull = true;
		m_row.IsPasswordDirty = true;
	}

	TIMESTAMP_STRUCT GetInsertTime() {
		if( m_row.IsInsertTimeNull )
			throw konga::DbNullException("InsertTime");
		return m_row.field_InsertTime;
	}
	void SetInsertTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_InsertTime = ts;
		m_row.IsInsertTimeNull = false;
		m_row.IsInsertTimeDirty = true;
	}
	bool IsInsertTimeNull() const {
		return m_row.IsInsertTimeNull;
	}
	void SetInsertTimeToNull() {
		m_row.IsInsertTimeNull = true;
		m_row.IsInsertTimeDirty = true;
	}

	TIMESTAMP_STRUCT GetModTime() {
		if( m_row.IsModTimeNull )
			throw konga::DbNullException("ModTime");
		return m_row.field_ModTime;
	}
	void SetModTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_ModTime = ts;
		m_row.IsModTimeNull = false;
		m_row.IsModTimeDirty = true;
	}
	bool IsModTimeNull() const {
		return m_row.IsModTimeNull;
	}
	void SetModTimeToNull() {
		m_row.IsModTimeNull = true;
		m_row.IsModTimeDirty = true;
	}

protected:
	CryptoTabRow m_row;

private:
	bool IsCryptoInfoIdDirty() const {
		return m_row.IsCryptoInfoIdDirty;
	};
	bool IsCryptoMethodDirty() const {
		return m_row.IsCryptoMethodDirty;
	};
	bool IsEncryptionAlgoritmDirty() const {
		return m_row.IsEncryptionAlgoritmDirty;
	};
	bool IsSigningAlgoritmDirty() const {
		return m_row.IsSigningAlgoritmDirty;
	};
	bool IsCertificateIdDirty() const {
		return m_row.IsCertificateIdDirty;
	};
	bool IsPasswordDirty() const {
		return m_row.IsPasswordDirty;
	};
	bool IsInsertTimeDirty() const {
		return m_row.IsInsertTimeDirty;
	};
	bool IsModTimeDirty() const {
		return m_row.IsModTimeDirty;
	};


	std::string m_stUpdateFilter;

};

}                      


#endif
