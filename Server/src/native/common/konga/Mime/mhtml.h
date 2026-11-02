/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_MHTML_H
#define KONGA_MHTML_H

#include <string>

#define MAX_URL_SIZE 10000

namespace konga {										
	class Part;
	class HtmlTag{
	public:
		enum Type { A, Img, Error, End, Other };
	};
	
	class Mhtml
	{
	private:
		Mhtml(const Mhtml&);	                                                           
	public:
		Mhtml();
		                                                                      
		Mhtml(const std::string& stHtmlFileName);
		~Mhtml();
		void Reset();
		void SetFileName(const char* html_filename, const std::string& originalUrl = "");
		void AddPart(const char* filename, int order, const std::string& url);
		int MakeMhtml();
		int MakeFile();
		std::string GetHeaders() const { return m_Header; }
		std::string BoundaryString() const ;
		std::string FirstBoundaryString() const ;
		bool IsUrl() const { return m_isURL; }                                                          
		std::string OriginalUrl() const { return m_HtmlOriginalUrl; }
		std::string m_Server, m_Scheme, m_UrlPath;
		std::string GetMessageString() const { return m_MessageString; }
		void SetFileType(bool IsUnicode) { m_IsUnicode = IsUnicode; }
	private:
		Mhtml& operator=(const Mhtml&);	                       
		Part** m_MultiParts;
		int m_CurrentPart, m_PartNumber, m_NoOfParts, m_MaxParts, m_CurrentOrder;
		bool m_isURL;
		bool m_IsUnicode;
		std::string m_Header;
		std::string m_BaseDir;
		std::string m_HtmlOriginalUrl;
		std::string m_MessageString;
	};
	
	class Part
	{
	public:
		enum Content_Type { Html, Gif, Jpg, Mpg, Pdf, Unknown };
		Part(Mhtml* MhtmlParent, const std::string& parentUrl, const std::string& filename, int order, const std::string& BaseDir);
		~Part();
		int Process(std::string& message);
		const char* GetFilename() { return m_FileName.c_str(); }
	protected:
		Content_Type FindExt();
		int ConvertHtml(std::string& message);
		int CheckAndConcate(std::string& message, const char* Str); 
		int OpenFile(const char* open_mode= "r");
	private:
		Content_Type m_ContentType;
		std::string m_FileName, m_FullFileName;
		int m_Order;
		std::string m_Message;
		std::string m_TmpFile, m_ParentUrl, m_Url;
		Mhtml *m_MhtmlParent;
		FILE* in;
	};
	
	class HtmlParser
	{
	public:
		HtmlParser(FILE* fd);
		HtmlTag::Type Parse(bool bIncludeLinks);
		bool HasNewPart(){
			return (m_AsciiFileToAdd[0] != 0);
		}
		char* GetNewPartName();
		std::string GetParsedString(){
			return m_Message;
		}
	protected:
		char* ReadAsciiLine()
		{
			m_Pos = 0;
			return fgets(m_AsciiLine, 255, in);
		}
		char NextAsciiChar();
		void WriteStr(int n);
		void GetAsciiFileToAdd(char c);
		void WriteAsciiFileToAdd(char c, bool bWriteCid=true, bool bApplet=false);
		HtmlTag::Type ParseAsciiFile(bool bIncludeLinks);
	
	private:
		FILE *in;
		char m_AsciiFileToAdd[MAX_URL_SIZE], m_AsciiLine[1024];
		char m_FileToAdd[MAX_URL_SIZE*2];
		int m_Pos, m_EndOfUrlPos, m_NumOfNewParts;
		std::string m_Message;
	};
}                   
#endif                 

