/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef WIN_AUTOCLEANUP_H
#define WIN_AUTOCLEANUP_H

                                                                        
  

#include <vector>
#include <string>

#ifdef min
#undef min
#undef max
#endif

#ifdef WIN32

	#include <shlwapi.h>
	
	#ifdef ALG_CLASS_ANY        
		#include <wincrypt.h>
    #endif
	
#endif         

#include <windows.h>
#undef min
#undef max

#include <konga/Utils/FileUtils.h>

namespace konga {

	class AutoHandle
	{
	public:
		AutoHandle(HANDLE handle) : m_handle(handle) { }
		~AutoHandle() { ::CloseHandle(m_handle); }
	private:
		HANDLE m_handle;
	};

	class AutoCriticalSection
	{
	public:
		AutoCriticalSection(CRITICAL_SECTION& section, bool bEnterSectionNow=true) :
		  m_section(section) { if(bEnterSectionNow) ::EnterCriticalSection(&m_section); }
		~AutoCriticalSection() { ::LeaveCriticalSection(&m_section); }
	private:
		CRITICAL_SECTION& m_section;
	};
	
	class AutoCriticalSectionEx
	{
	public:
		AutoCriticalSectionEx(CRITICAL_SECTION& section) : m_section(section), m_bLocked(true)
		{
			::EnterCriticalSection(&m_section);
		}
		~AutoCriticalSectionEx() {  Exit(); }
	
		void Exit()
		{
			if ( m_bLocked )
			{
				::LeaveCriticalSection(&m_section);
				m_bLocked = false;
			}
		}
	
	private:
		CRITICAL_SECTION& m_section;
		bool m_bLocked;
	};
	
	struct AutoInitializeCriticalSection
	{
		AutoInitializeCriticalSection() { ::InitializeCriticalSection(&m_section); }
		~AutoInitializeCriticalSection() { ::DeleteCriticalSection(&m_section); }
		operator CRITICAL_SECTION&() { return m_section; }
		CRITICAL_SECTION m_section;
	};
	
	class AutoRegistryKey
	{
	public:
		AutoRegistryKey(HKEY& key) : m_key(key) { }
		~AutoRegistryKey() { 
			::RegCloseKey(m_key);
		}
	private:
		HKEY& m_key;
	};

	class AutoLibrary
	{
	public:
		AutoLibrary(HMODULE& h) : m_hLib(h) { }
		~AutoLibrary() {
			::FreeLibrary( m_hLib );
		}
	private:
		HMODULE& m_hLib;
	};

	struct AutoCoInitialize
	{
		AutoCoInitialize() { ::CoInitialize(0); }
		~AutoCoInitialize() { ::CoUninitialize(); }
	};

	#ifdef ALG_CLASS_ANY
	#ifdef HAVE_WIN32_CRYPTO_HEADERS
	
	class AutoCryptoKey
	{
	public:
		AutoCryptoKey(HCRYPTKEY& key) : m_key(key) { }
		~AutoCryptoKey() { 
			::CryptDestroyKey(m_key); 
		}
	private:
		HCRYPTKEY& m_key;
	};
	
	class AutoCryptoHash
	{
	public:
		AutoCryptoHash(HCRYPTHASH& key) : m_key(key) { }
		~AutoCryptoHash() { 
			::CryptDestroyHash(m_key); 
		}
	private:
		HCRYPTHASH& m_key;
	};
	
	class AutoCryptoContext
	{
	public:
		AutoCryptoContext(HCRYPTPROV& ctx) : m_ctx(ctx) { }
		~AutoCryptoContext() {
			CryptReleaseContext(m_ctx, 0); 
		}
	private:
		HCRYPTPROV& m_ctx;
	};
	
	class AutoCRYPT_ATTR_BLOB
	{
	public:
		AutoCRYPT_ATTR_BLOB(PCRYPT_ATTR_BLOB& ctx) : m_ctx(ctx) { }
		~AutoCRYPT_ATTR_BLOB()
		{
			if (m_ctx)
				delete m_ctx;
		}
	private:
	                         
		PCRYPT_ATTR_BLOB m_ctx;
	};
	
	class AutoCryptoMessage
	{
	public:
		AutoCryptoMessage(HCRYPTMSG& msg) : m_msg(msg) { }
		~AutoCryptoMessage() {
			CryptMsgClose(m_msg); 
		}
	private:
		HCRYPTMSG& m_msg;
	};
	
	#endif                              
	
	#ifdef HAVE_WIN32_CERT_HEADERS
	class AutoCertContext
	{
	public:
		AutoCertContext(PCCERT_CONTEXT& ctx) : m_ctx(ctx) { }
		~AutoCertContext() {
			CertFreeCertificateContext(m_ctx); 
		}
		operator=(PCCERT_CONTEXT rhs) { if (m_ctx) CertFreeCertificateContext(m_ctx); m_ctx = rhs; }
	private:
	                         
		PCCERT_CONTEXT m_ctx;
	};
	
	class AutoCertStore
	{
	public:
		AutoCertStore(HCERTSTORE& store) : m_store(store) { }
		~AutoCertStore() {
			CertCloseStore(m_store, 0); 
		}
		operator=(HCERTSTORE& rhs) { if (m_store) CertCloseStore(m_store, 0); m_store = rhs; }
	private:
		HCERTSTORE& m_store;
	};
	
	#endif                           
	#endif                 

}

#endif                     

