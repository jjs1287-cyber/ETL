/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef CERT_INFO_H
#define CERT_INFO_H

#include <konga/Utils/PortDefs.h>

#ifdef WIN32
	#include <windows.h>
	#include <wininet.h>
	#define _WINSOCK2API_
#endif

#include <string>

namespace CrSecurity
{

class CertPolicy
{
public:
	CertPolicy() : m_bIgnoreCertCommonNameInvalid(false), 
		m_bIgnoreCertDateInvalid(false), m_bIgnoreRedirectToHttp(false) {}
	virtual ~CertPolicy() {}

	long PolicyAsFlags() const 
	{
		return 
			m_bIgnoreCertCommonNameInvalid ? INTERNET_FLAG_IGNORE_CERT_CN_INVALID : 0 |
			m_bIgnoreCertDateInvalid ? INTERNET_FLAG_IGNORE_CERT_DATE_INVALID : 0 |
			m_bIgnoreRedirectToHttp ? INTERNET_FLAG_IGNORE_REDIRECT_TO_HTTP : 0;
	}

	void IgnoreCertCommonNameInvalid(bool bIgnoreCertCommonNameInvalid) { m_bIgnoreCertCommonNameInvalid = bIgnoreCertCommonNameInvalid; }
	void IgnoreCertDateInvalid(bool bIgnoreCertDateInvalid) { m_bIgnoreCertDateInvalid = bIgnoreCertDateInvalid; }
	void IgnoreRedirectToHttp(bool bIgnoreRedirectToHttp) { m_bIgnoreRedirectToHttp = bIgnoreRedirectToHttp; }

	bool IgnoreCertCommonNameInvalid() const { return m_bIgnoreCertCommonNameInvalid; }
	bool IgnoreCertDateInvalid() const { return m_bIgnoreCertDateInvalid; }
	bool IgnoreRedirectToHttp() const { return m_bIgnoreRedirectToHttp; }

	CertPolicy& operator= (const CertPolicy& rhs)
	{
		if( &rhs != this )
		{
			m_bIgnoreCertCommonNameInvalid = rhs.IgnoreCertCommonNameInvalid();
			m_bIgnoreCertDateInvalid = rhs.IgnoreCertDateInvalid();
			m_bIgnoreRedirectToHttp = rhs.IgnoreRedirectToHttp();
		}
		return *this;
	}

private:
	bool m_bIgnoreCertCommonNameInvalid;
	bool m_bIgnoreCertDateInvalid;
	bool m_bIgnoreRedirectToHttp;
};

class CertInfo
{
public:
	CertInfo() : m_iHashLen(0), m_pbHash(0) {}
	CertInfo(const CertInfo& rhs) : m_iHashLen(0), m_pbHash(0) { *this = rhs; }
	virtual ~CertInfo() { ClearHash(); }

	std::string CertificateName() const { return m_stCertificateName; }
	std::string StoreName() const { return m_stStoreName; }
	std::string SerialNumber() const { return m_stSerialNumber; }
	std::string IssuerName() const { return m_stIssuerName; }
	std::string IssuerSerialNumber() const { return m_stIssuerSerialNumber; }
	CertPolicy CertificatePolicy() const { return m_CertPolicy; }
	std::string GetHash() const { return m_stHash; }
	void GetHash(char*& pbHash, int& iLen) const
	{
		pbHash = m_pbHash;
		iLen = m_iHashLen;
	}

	void CertificateName(const std::string& stCertificateName) { m_stCertificateName = stCertificateName; }
	void StoreName(const std::string& stStoreName) { m_stStoreName = stStoreName; }
	void SerialNumber(const std::string& stSerialNumber) { m_stSerialNumber = stSerialNumber; }
	void IssuerName(const std::string& stIssuerName) { m_stIssuerName = stIssuerName; }
	void IssuerSerialNumber(const std::string& stIssuerSerialNumber) { m_stIssuerSerialNumber = stIssuerSerialNumber; }
	void CertificatePolicy(const CertPolicy& certPolicy) { m_CertPolicy = certPolicy; }
	void SetHash(const std::string& stHash) { ClearHash(); m_stHash = stHash; }
	void SetHash(char const* pbHash, int iLen)
	{
		ClearHash();
		if ( pbHash == 0 || iLen <= 0 )
			return;

		m_iHashLen = iLen;
		m_pbHash = new char[m_iHashLen];
		memcpy(m_pbHash, pbHash, m_iHashLen);
	}

	CertInfo& operator= (const CertInfo& rhs)
	{
		if( &rhs != this )
		{
			m_stCertificateName = rhs.CertificateName();
			m_stStoreName = rhs.StoreName();
			m_stSerialNumber = rhs.SerialNumber();
			m_stIssuerName = rhs.IssuerName();
			m_stIssuerSerialNumber = rhs.IssuerSerialNumber();

			m_CertPolicy = rhs.CertificatePolicy();

			char* pbHash;
			int iLen;
			rhs.GetHash(pbHash, iLen);
			if ( pbHash && iLen > 0 )                                                                
				SetHash(pbHash, iLen);
			else
				ClearHash();
			m_stHash = rhs.GetHash();
		}
		return *this;
	}

protected:
	void ClearHash()
	{
		if ( m_pbHash )
			delete[] m_pbHash;
		m_pbHash = 0;
		m_iHashLen = 0;
		m_stHash.erase();
	}

private:
	int m_iHashLen;
	char* m_pbHash;

	std::string m_stCertificateName;
	std::string m_stStoreName;
	std::string m_stSerialNumber;
	std::string m_stHash;
	std::string m_stIssuerName;
	std::string m_stIssuerSerialNumber;

	CertPolicy m_CertPolicy;
};

};                        

#endif               
