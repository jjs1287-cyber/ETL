/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <stdio.h>

#include <boost/scoped_array.hpp>

#include <konga/Utils/util.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>

#include "Encoder.h"
#include "CrMime.h"
#include "mhtml.h"

using namespace boost;
using namespace konga;
using std::string;

char* mime_boundary_string = "=_----------mime_boundary_string_asdew";

CrMime::CrMime(): m_NoOfAsciiStrings(0), m_NoOfFiles(0),
	m_NoOfMhtml(0), m_pstFileName(0), m_pstAsciiString(0), m_pMhtml(0) { }

CrMime::~CrMime()
{
	Reset();           
}

std::string CrMime::BoundaryString() const
{
	return mime_boundary_string;
}

void CrMime::SetHtmlFileNames(const std::string* pstFileName, const std::string* pstOriginalUrl, const bool* pbIsUnicode, int N)
{
	if ( m_pMhtml != 0 ) {
		delete[] m_pMhtml;
		m_pMhtml = 0;
	}

	m_pMhtml = new Mhtml[N];
	for(int i = 0; i < N; i++)
	{
		m_pMhtml[i].SetFileName(pstFileName[i].c_str(), pstOriginalUrl[i]);
		m_pMhtml[i].SetFileType(pbIsUnicode[i]);
	}
	m_NoOfMhtml = N;
}

void CrMime::SetFileNames(const std::string* pstFileName, int N)
{
	if ( m_pstFileName != 0 ) {
		delete[] m_pstFileName;
		m_pstFileName = 0;
	}
	m_pstFileName = new string[N];
	for(int i = 0; i < N; i++)
	{
		m_pstFileName[i] = pstFileName[i];
	}
	m_NoOfFiles = N;
}

void CrMime::SetAsciiStrings(const std::string* pstStr, int N)
{
	if ( m_pstAsciiString != 0 ) {
		delete[] m_pstAsciiString;
		m_pstAsciiString = 0;
	}
	m_pstAsciiString = new std::string[N];
	for(int i = 0; i < N; i++)
	{
		m_pstAsciiString[i] = pstStr[i];
	}
	m_NoOfAsciiStrings = N;
}
int CrMime::ProcessMessage(){

	for(int i = 0; i < m_NoOfMhtml; i++){
		m_pMhtml[i].MakeMhtml();
		m_stMessage += m_pMhtml[i].GetMessageString();
		m_stMessage += "--";
		m_stMessage += mime_boundary_string;
		m_stMessage += "--\r\n";
	}
	for(int i = 0; i < m_NoOfFiles; i++){
		std::string stErr;
		long nRead;
		scoped_array<char> pc( read_file(m_pstFileName[i].c_str(), nRead, stErr) );
		if ( !stErr.empty() ) {
			throw CrException("Failed to process the message. " + stErr);
		}

		Encoder e(&pc[0], nRead);
		e.CBase64();
		m_stMessage += e.GetEncodedString();
		m_stMessage += "--";
		m_stMessage += mime_boundary_string;
		m_stMessage += "--\r\n";
	}
	for(int i = 0; i < m_NoOfAsciiStrings; i++){
		Encoder e(m_pstAsciiString[i]);
		e.CBase64();
		m_stMessage += e.GetEncodedString();
		m_stMessage += "--";
		m_stMessage += mime_boundary_string;
		m_stMessage += "--\r\n";
	}
	
	m_stHeader = "MIME-Version: 1.0\r\nContent-Type: multipart/mixed; boundary=\"";
	m_stHeader += mime_boundary_string;
	m_stHeader += "\"\r\n";
	
	return 0;
}

void CrMime::Reset(){
	m_NoOfAsciiStrings = 0;
	m_NoOfFiles = 0;
	m_NoOfMhtml = 0;
	if(m_pstFileName) {
		 delete[] m_pstFileName;
		 m_pstFileName = 0;
	}
	if(m_pstAsciiString) {
		delete[] m_pstAsciiString;
		m_pstAsciiString = 0;
	}
	m_stMessage = "";
	m_stHeader = "";
	if(m_pMhtml) {
		 delete[] m_pMhtml;
		 m_pMhtml = 0;
	}
}
