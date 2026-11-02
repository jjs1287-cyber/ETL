/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef CRMIME_H
#define CRMIME_H

#include <string>

#include <boost/noncopyable.hpp>

namespace konga {

	class Mhtml;

	class CrMime : boost::noncopyable {
	public:
		CrMime();
		~CrMime();
		void SetHtmlFileNames(const std::string* pstFileName, const std::string* pstOriginalUrl, const bool* pbIsUnicode, int N);
		void SetFileNames(const std::string* pstFileName, int N);
		void SetAsciiStrings(const std::string* pstAsciiStrings, int N);

		int ProcessMessage();
		std::string GetBody() const { return m_stMessage; }
		std::string GetMimeHeaders() const{ return m_stHeader; }
		void Reset();
		std::string BoundaryString() const;

	private:
		int m_NoOfAsciiStrings, m_NoOfFiles, m_NoOfMhtml;
		std::string* m_pstFileName;
		std::string *m_pstAsciiString;
		Mhtml *m_pMhtml;
		std::string m_stMessage, m_stHeader;
	};
}
#endif            

