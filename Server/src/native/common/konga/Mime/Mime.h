/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#if !defined(AFX_MIME_H__0E6B4D9A_BE2E_11D3_9A22_000000000000__INCLUDED_)
#define AFX_MIME_H__0E6B4D9A_BE2E_11D3_9A22_000000000000__INCLUDED_

#include <string>

namespace konga {
	namespace Mime  
	{
		std::string EncodeBase64(const std::string& filename);
		std::string EncodeQuotedPrintable(const std::string& filename);
	
		std::string ContentTypeFromFileName(const std::string& filename);
	
		struct EncodingException
		{
			EncodingException(const std::string& reason) : m_reason(reason) {}
			std::string Reason() const { return m_reason; }
		protected:
			std::string m_reason;
		};	
	};
}

#endif                                                                         

