/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                                                
  
                                                                      

#ifdef WIN32
	                       
	#include <atldbcli.h>
	#include <comdef.h>
	#include <windows.h>
#endif         

#include "StringUtils.h"

#include "ErrorResolver.h"

std::string ErrorResolver::Resolve(unsigned long err, bool bForceDBError          )
{
	m_Error = "";
	m_hr = err;

#ifdef WIN32
	_com_error ce(err);
	if(	(err>=0x80040e00 && err<=0x80040eff) ||                             
		(err>=0x40e00 && err<=0x40eff) || bForceDBError ) 
	{
		m_hr = myHandleResult(err, L"", 0, m_Error);
		m_isOledbErr = true;
	}
	else if( ce.WCode() )
	{
		m_Error = ce.ErrorMessage();
		m_isComErr = true;
	}
#if defined(_WININET_)
	else if( err == ERROR_INTERNET_EXTENDED_ERROR )
	{
		unsigned long uerr, len=10000;
		char lastResponse[10000];
		if( ::InternetGetLastResponseInfo(&uerr,lastResponse,&len) )
		{
			if( len )
				m_Error = lastResponse;
			else
			{
				unsigned long lerr = ::GetLastError();
				m_Error = "No response text available. Error number is ";
				char buf[100];
				m_Error += itoa(uerr,buf,10);
				m_Error += " and last system error is ";
				std::string theError;
				GetLastErrorText(theError);
				m_Error += theError;
			}
		}
		else
		{
			if( len )
			{
				char *lastResp = new char[len];
				AutoArrayDelete<char> adopt_lastResp(lastResp);
				if( ::InternetGetLastResponseInfo(&uerr,lastResp,&len) )
				{
					if( len )
						m_Error = lastResp;
					else
					{
						unsigned long lerr = ::GetLastError();
						m_Error = "No response text available. Error number is ";
						char buf[100];
						m_Error += itoa(uerr,buf,10);
						m_Error += " and last system error is ";
						std::string theError;
						GetLastErrorText(theError);
						m_Error += theError;
					}
				}
			}
			else
			{
				char buf[1000];
				sprintf(buf,"InternetGetLastResponseInfo failed with error number %d",::GetLastError());
				m_Error = buf;
			}
		}
		m_isInetErr = true;
	}
#endif                      
	else
	{
		GetLastErrorText(m_Error);
		m_isWinErr = true;
	}
#else         
	const char* pc = strerror(err);
	m_Error = pc ? pc : "";
#endif

	                                                              
	std::string::size_type ipos = m_Error.find_last_not_of(" \t\r\n");
	if ( ipos != std::string::npos && ipos != m_Error.size()-1 )
		m_Error.erase(ipos+1);
	
	                                                     
	if ( m_Error.size() > 2 && *(m_Error.end()-1)=='.' && *(m_Error.end()-2)=='.' )
		m_Error.erase(m_Error.end()-1);
	return m_Error;
}

#ifdef WIN32

HRESULT myHandleResult(HRESULT hrReturned, LPCWSTR pwszFile, ULONG ulLine, std::string& strErr);

std::string ErrorResolver::GetLastErrorText(unsigned long lErr      , const std::string& stModule       ) const
{
	std::string stError;
	GetLastErrorText(stError, lErr, stModule);
	return stError;
}

void ErrorResolver::GetLastErrorText(std::string& theError, unsigned long lErr      , const std::string& stModule       ) const
{
	unsigned long err = lErr?lErr:m_hr;
	LPTSTR lpMsgBuf;
	int len = FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM,
		NULL, err, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR)&lpMsgBuf, 1, NULL);
	if( len )
	{
		lpMsgBuf[ len ] = 0;
		theError = lpMsgBuf;
		LocalFree(lpMsgBuf);
	}
	else
	{
		HMODULE hDll = ::LoadLibrary(stModule.empty()?"wininet.dll":stModule.c_str());
		int len = FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_HMODULE,
			hDll, err, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR)&lpMsgBuf, 1, NULL);
		if( len )
		{
			lpMsgBuf[ len ] = 0;
			theError = lpMsgBuf;
			LocalFree( lpMsgBuf );
		}
		else
		{
			HMODULE hDll2 = ::LoadLibrary("winhttp5.dll");
			len = FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_HMODULE,
				hDll2, err, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR)&lpMsgBuf, 1, NULL);
			if( len )
			{
				lpMsgBuf[ len ] = 0;
				theError = lpMsgBuf;
				LocalFree( lpMsgBuf );
			}
			else
			{
				char buf[4096];
				sprintf(buf,"FormatMessage() failed with error number %d when trying to get "
					" the error string for error number %u ( = 0x%x).", ::GetLastError(), err, err);
				theError = buf;
			}
			if( hDll2 ) ::FreeLibrary(hDll2);
		}
		if( hDll ) ::FreeLibrary(hDll);
	}
	SetLastError(0);
}


HRESULT myHandleResult(HRESULT hrReturned, LPCWSTR pwszFile, ULONG ulLine, std::string& strErr);

HRESULT myDisplayErrorRecord(HRESULT hrReturned, ULONG iRecord,
							 IErrorRecords* pIErrorRecords, LPCWSTR
							 pwszFile, ULONG ulLine, std::string& strErr);
HRESULT myDisplayErrorInfo(HRESULT hrReturned, IErrorInfo* pIErrorInfo, LPCWSTR pwszFile, ULONG ulLine, std::string& strErr);

HRESULT myGetSqlErrorInfo(ULONG iRecord, IErrorRecords* pIErrorRecords, BSTR* pBstr, LONG* plNativeError, std::string& strErr);

                              
#define CHECK_HR(hr)	  \
	if(FAILED(hr))		  \
		goto CLEANUP

                                                         
#define XCHECK_HR(hr)												\
{																	\
	if( g_dwFlags & DISPLAY_METHODCALLS )							\
		_ftprintf(stderr, LONGSTRING(#hr) "\n");						\
	if(FAILED(myHandleResult(hr, LONGSTRING(__FILE__), __LINE__)))	\
		goto CLEANUP; 												\
}

                                                                        
                 
  
                                                                      
                                                                   
                                                                      
                                                                       
                                                                   
                                               
  
                                                                        
HRESULT myHandleResult
	(
	HRESULT			 hrReturned,
	LPCWSTR			 pwszFile,
	ULONG			 ulLine, 
	std::string&         strErr
	)
{
	HRESULT			 hr;
	IErrorInfo * 	 pIErrorInfo	  = NULL;
	IErrorRecords *	 pIErrorRecords   = NULL;
	ULONG			 cRecords;
	ULONG			 iErr;

	                                                              
	                                                                  
	if( FAILED(hrReturned) )
	{
		                                                        
		                                                    
		                                                         
		hr = GetErrorInfo(0, &pIErrorInfo);

		                                                                  
		if( SUCCEEDED(hr) && pIErrorInfo )
		{
			                                                        
			                                                             
			                                                       
			hr = pIErrorInfo->QueryInterface(IID_IErrorRecords, (void**)&pIErrorRecords);
			if( SUCCEEDED(hr) )
			{
				                                                  
				CHECK_HR(hr = pIErrorRecords->GetRecordCount(&cRecords));
				
				                                             
				                                              
				for( iErr = 0; iErr < cRecords; iErr++ )
				{
					myDisplayErrorRecord(hrReturned, iErr, pIErrorRecords, pwszFile, ulLine, strErr);
				}
			}
			                                                   
			                                               
			else
			{
				myDisplayErrorInfo(hrReturned, pIErrorInfo, pwszFile, ulLine, strErr);
			}
		}
		                                                                      
		else
		{
                                                        
                                                            

		USES_CONVERSION;
		char buf[1024];
		_stprintf(buf, "No Error Info posted; HResult: 0x%08x", hrReturned);
		strErr += buf;
	}
}

CLEANUP:
	if( pIErrorInfo )
		pIErrorInfo->Release();
	if( pIErrorRecords )
		pIErrorRecords->Release();
	return hrReturned;
}


                                                                        
                       
  
                                                                  
                                                                  
  
                                                                        
HRESULT myDisplayErrorRecord
	(
	HRESULT			 hrReturned,
	ULONG			 iRecord,
	IErrorRecords *	 pIErrorRecords,
	LPCWSTR			 pwszFile,
	ULONG			 ulLine, std::string& strErr
	)
{
	HRESULT			 hr;
	IErrorInfo * 	 pIErrorInfo	   = NULL;
	BSTR 			 bstrDescription   = NULL;
	BSTR 			 bstrSource 	   = NULL;
	BSTR 			 bstrSQLInfo	   = NULL;

	static LCID		 lcid			   = GetUserDefaultLCID();

	LONG 			 lNativeError	   = 0;
	ERRORINFO		 ErrorInfo;

	                                                              
	CHECK_HR(hr = pIErrorRecords->GetErrorInfo(iRecord, lcid, &pIErrorInfo));

	                                     
	CHECK_HR(hr = pIErrorInfo->GetDescription(&bstrDescription));

	                                
	CHECK_HR(hr = pIErrorInfo->GetSource(&bstrSource));

	                                                   
	CHECK_HR(hr = pIErrorRecords->GetBasicErrorInfo(iRecord, &ErrorInfo));

	                                                                                                       

	                                                                    
	myGetSqlErrorInfo(iRecord, pIErrorRecords, &bstrSQLInfo, &lNativeError, strErr);

	char buf[1024];
	                                             
	if( bstrSQLInfo )
	{
		USES_CONVERSION;
		_stprintf(buf, "ErrorRecord:  HResult: 0x%08x, Description: %s, SQLErrorInfo: %s, Source: %s", 
			ErrorInfo.hrError, (LPCSTR)_bstr_t(bstrDescription),
			(LPCSTR)_bstr_t(bstrSQLInfo), (LPCSTR)_bstr_t(bstrSource));
	}
	else
	{
		USES_CONVERSION;
		_stprintf(buf, "ErrorRecord: HResult: 0x%08x, Description: %s, Source: %s", ErrorInfo.hrError,
			(LPCSTR)_bstr_t(bstrDescription), (LPCSTR)_bstr_t(bstrSource));
	}
	strErr += buf;

CLEANUP:
	if( pIErrorInfo )
		pIErrorInfo->Release();
	SysFreeString(bstrDescription);
	SysFreeString(bstrSource);
	SysFreeString(bstrSQLInfo);
	return hr;
}


                                                                        
                     
  
                                                                     
                                                    
  
                                                                        
HRESULT myDisplayErrorInfo
	(
	HRESULT		  hrReturned, 
	IErrorInfo *   pIErrorInfo, 
	LPCWSTR		  pwszFile, 
	ULONG		  ulLine, std::string& strErr
	)
{
	HRESULT		  hr;
	BSTR 		  bstrDescription	= NULL;
	BSTR 		  bstrSource		= NULL;

	                                    
	CHECK_HR(hr = pIErrorInfo->GetDescription(&bstrDescription));

	                                                                
	CHECK_HR(hr = pIErrorInfo->GetSource(&bstrSource));

	{
	                                  
	USES_CONVERSION;
	char buf[1024];
	_stprintf(buf, "ErrorInfo:  HResult: 0x%08x, Description: %s, Source: %s", 
		hrReturned, (LPCSTR)_bstr_t(bstrDescription), (LPCSTR)_bstr_t(bstrSource));
	strErr += buf;
	}

CLEANUP:
	SysFreeString(bstrDescription);
	SysFreeString(bstrSource);
	return hr;
}


                                                                        
                    
  
                                                                
                                               
  
                                                                        
HRESULT myGetSqlErrorInfo
	(
	ULONG			 iRecord,
	IErrorRecords *	 pIErrorRecords,
	BSTR *			 pBstr,
	LONG *			 plNativeError, std::string& strErr
	)
{
	HRESULT			 hr;
	ISQLErrorInfo *	 pISQLErrorInfo   = NULL;
	LONG 			 lNativeError	  = 0;

	                                                            
	                                                               
	                                                   
	CHECK_HR(hr = pIErrorRecords->GetCustomErrorObject(
			iRecord,							             
			IID_ISQLErrorInfo,					          
			(IUnknown**)&pISQLErrorInfo 		                     
			));

	                                                          
	                                                     
	if( pISQLErrorInfo )
		hr = pISQLErrorInfo->GetSQLInfo(pBstr, &lNativeError);

CLEANUP:
	if( plNativeError )
		*plNativeError = lNativeError;
	if( pISQLErrorInfo )
		pISQLErrorInfo->Release();
	return hr;
}

#endif        
