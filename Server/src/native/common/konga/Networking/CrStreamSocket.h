/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_STREAM_SOCKET_H
#define KONGA_STREAM_SOCKET_H

#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>		                        

namespace konga {
namespace networking {

class RawSocket;

class CrSocketException : public CrException
{
public:
	CrSocketException(const std::string& stError = "", long lErr = 0x80004005L) : CrException(stError, lErr) {}
	~CrSocketException() {}
};

class CrStreamSocket
{
public:
	CrStreamSocket();
	CrStreamSocket(RawSocket* pSock);                                        
	virtual ~CrStreamSocket();

	                                
	static long InitSocketAPI();
	static long DisableSocketAPI();
	static bool IsAPIInitialized();

	void HandleError(long lErr, const std::string& sErr);

	                                                                                            
	                            
	void Connect(const std::string& stAddress, unsigned long lPort);

	void Close();
	bool IsConnected() const { return m_bConnected; }
	void VerifyConnected() const {
		if( !IsConnected() )
			throw CrSocketException("Not connected.", m_lLastError = -1);
	}

	                                                                                             
	void ReadString(std::string& stData);

	                                   
	void WriteString(const std::string& stData);

	                                                         
	                                                             
	template<typename T>
	long Read(T& dataHolder, int iTimeOutInMillis = 10000) {
		VerifyConnected();
		m_iReadTimeOutMillis = iTimeOutInMillis;
		long len = -1;

		static const unsigned long READ_BUF_SIZE = 4096;
		char buf[READ_BUF_SIZE + 1];
		if( IsReceivedDataAvailable(false) ) {
			while( IsReceivedDataAvailable() ) {
				len = RECV(buf, READ_BUF_SIZE, 0);
				if( len == -1 ) HandleError(::GetLastError(), "Read error: ");
				if( !len ) break;                     
				buf[ len ] = 0;
				dataHolder.append(buf, len);
			}
		}
		return len;
	}

	                                                                                  
	template<typename T>
	void Write(const T* pData, unsigned long nDataItems) {
		VerifyConnected();

		size_t lTotLen = nDataItems * sizeof(T), lWritten = 0;
		long lRet;
		while( lWritten < lTotLen ) {
			lRet = SEND(reinterpret_cast<const char*>(&pData[lWritten]), lTotLen-lWritten, 0);
			if( lRet == -1 ) HandleError(::GetLastError(), "Send error: ");
			lWritten += lRet;
		}
	}

	                       
	                                    
	                                                        
	                                                                                          
	bool IsReceivedDataAvailable(bool bPoll = true, bool bBlock = false);

	long GetLastErrorNumber() const { return m_lLastError; }
	std::string GetLastError();

protected:
	void init();

	void SetConnected(bool bConnected) { m_bConnected = bConnected; }
	void SetLastErrorString(std::string s) { m_stLastError = s; }
	void SetLastErrorNumber(long l) { m_lLastError = l; }

	long RECV(char* buf, const unsigned long len, const int flags);
	long SEND(const char* buf, const unsigned long len, const int flags);

	RawSocket* GetSocket() { return m_pSock; }
	void SetSocket(RawSocket* ps) { m_pSock = ps; }

private:
	bool m_bConnected;
	unsigned long m_lPort;
	mutable long m_lLastError;
	int m_iReadTimeOutMillis;                                                         
	RawSocket* m_pSock;

	std::string m_stHostName, m_stIpAddress;
	std::string m_stLastError;
};

}                        
}                   

#endif                         

