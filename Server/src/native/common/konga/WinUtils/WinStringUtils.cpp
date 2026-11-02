/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "WinStringUtils.h"

  
                                                                 
  
long konga::AnsiToBstr(const std::string& stIn, BSTR& bsOut)
{
	if( !stIn.empty() )
	{
		size_t nWideChars = ::MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, stIn.c_str(), (int)stIn.length(), 0, 0);
		if( !nWideChars )
			return ::GetLastError();

		bsOut = ::SysAllocStringLen(0, (unsigned int)nWideChars);
        if (!bsOut) return ERROR_NOT_ENOUGH_MEMORY;
		size_t lRet = ::MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, stIn.c_str(), (int)stIn.length(), bsOut, (int)nWideChars);
		if( !lRet )
		{
			lRet = ::GetLastError();
			::SysFreeString(bsOut);
			bsOut = 0;
			return (long)lRet;
		}
	}
	else
	{
		bsOut = ::SysAllocStringLen(nullptr, 0);
        if (!bsOut) return ERROR_NOT_ENOUGH_MEMORY;
		                                
	}
	return 0;
}

  
                                                        
  
long konga::BstrToAnsi(const BSTR bsIn, char*& pcOut)
{
	if( bsIn )
	{
		long nAnsiChars = ::WideCharToMultiByte(CP_UTF8, WC_ERR_INVALID_CHARS, bsIn, -1, 0, 0, 0, 0);
		if( !nAnsiChars )
			return ::GetLastError();

		pcOut = new char[nAnsiChars];                                                       
		long lRet = ::WideCharToMultiByte(CP_UTF8, WC_ERR_INVALID_CHARS, bsIn, -1, pcOut, nAnsiChars, 0, 0);
		if( !lRet )
		{
			lRet = ::GetLastError();
			delete []pcOut;
			pcOut = 0;
			return lRet;
		}
	}
	else
	{
		pcOut = new char[1];
		pcOut[0] = 0;
	}
	return 0;
}


