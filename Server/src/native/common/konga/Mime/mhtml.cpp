/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <string>
#include <string.h>	           

#include <konga/Utils/CrException.h>	          
#include <konga/Utils/StringUtils.h>	          
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/PortDefs.h>		             
#include <konga/Utils/Url.h>
#include <konga/Utils/FileUtils.h>		              
#include <konga/FileTransfer/FileRetriever.h>

#include "Encoder.h"
#include "mhtml.h"

bool double_boundary=false;
char* boundary_string = "=_----boundary_mime";
char* boundary_string0 = "0=_----------boundary_mime";

using namespace std;
using namespace konga;

namespace {
	const int order_max = 2;

}

Mhtml::Mhtml(const string& stHtmlFilename): m_MultiParts(0),
	m_CurrentPart(0), m_PartNumber(0), m_NoOfParts(0), m_MaxParts(0), m_CurrentOrder(0),
	m_isURL(false), m_IsUnicode(false)
{
	AddPart(stHtmlFilename.c_str(), m_CurrentOrder, "");
}

Mhtml::Mhtml() : m_MultiParts(0), m_CurrentPart(0), m_PartNumber(0), m_NoOfParts(0), m_MaxParts(0),
	m_CurrentOrder(0), m_isURL(false), m_IsUnicode(false)
{
}

Mhtml::~Mhtml()
{
	for(int i=0; i<m_NoOfParts; ++i) delete m_MultiParts[i];
	delete[] m_MultiParts;
}

void Mhtml::Reset()
{
	for(int i=0; i<m_NoOfParts; ++i) delete m_MultiParts[i];
	delete[] m_MultiParts;
	m_CurrentPart=0, m_NoOfParts=0, m_MaxParts=0, m_MultiParts=0, m_CurrentOrder=0;
	m_MessageString = "";
	m_isURL=false;
}

void Mhtml::SetFileName(const char* html_filename, const std::string& stOriginalUrl        )
{
	m_HtmlOriginalUrl = stOriginalUrl;
 	AddPart(html_filename,m_CurrentOrder,stOriginalUrl);
}

int Mhtml::MakeMhtml()
{
	if(double_boundary)
	{
		m_MessageString = "--";
		m_MessageString += boundary_string0;
		m_MessageString += "\r\n";
	}
	m_MessageString += "Content-Type: multipart/related; boundary=\"";
	m_MessageString += boundary_string;
	m_MessageString += "\"\r\n\r\n";		


	if(IsUrl())
	{
		try {
			Url url;
			url.Parse(OriginalUrl().c_str());
			m_Scheme = url.getProtocol();
			m_Server = url.getHost();
			m_UrlPath = url.getPath();
		}
		catch ( const CrException&) {
			                         
			return -1;
		}
	}
	
	m_PartNumber = 0;
	while( m_CurrentPart < m_NoOfParts )
	{
		int i;
		const char *filename = m_MultiParts[m_CurrentPart]->GetFilename();
		for(i=m_CurrentPart-1; i>=0; i--)
		{
			if( strcmp(filename,m_MultiParts[i]->GetFilename()) == 0 )
				break;
		}
		if( i<0 && m_MultiParts[m_CurrentPart]->Process(m_MessageString) == 0 )
		{
			m_PartNumber++;
		}
		m_CurrentPart++;
	}

	m_MessageString += "--";
	m_MessageString += boundary_string;
	m_MessageString += "--\r\n";
	if(double_boundary){
		m_MessageString += "--";
		m_MessageString += boundary_string0;
		m_MessageString += "--\r\n";
	}
	
	m_Header = "MIME-Version: 1.0\r\nContent-Type: multipart/mixed; boundary=\"";
	m_Header += double_boundary ? boundary_string0 : boundary_string;
	m_Header += "\"\r\n";

	return 0;
}

int Mhtml::MakeFile()
{
	m_MessageString += "--";
	m_MessageString += boundary_string;
	m_MessageString += "--\r\n";
	if(double_boundary){
		m_MessageString += "--";
		m_MessageString += boundary_string0;
		m_MessageString += "--\r\n";
	}

	return 0;
}

std::string Mhtml::FirstBoundaryString() const
{
	std::string res;           
	res += double_boundary ? boundary_string0 : boundary_string;
	return res;
}

std::string Mhtml::BoundaryString() const
{
	std::string res = "--";
	res += boundary_string;
	res += "--\r\n";
	if( double_boundary )
	{
		res += "--";
		res += boundary_string0;
		res += "--\r\n";
	}
	return res;
}

void Mhtml::AddPart(const char* filename, int order, const string& url)
{
	if( order == 0 )                                 
	{
		string fn = filename;
		size_t lastWack = fn.find_last_of('/');
		size_t lastBackWack = fn.find_last_of('\\');
		size_t last = lastWack > lastBackWack ? lastWack : lastBackWack;
		if( last != string::npos )
		{
			m_BaseDir = fn.substr(0,last);                                            
		}
		else                                                         
			m_BaseDir = ".";
	}

	if(m_NoOfParts >= m_MaxParts)
	{
		m_MaxParts += 10;
		Part** saved = m_MultiParts;
		m_MultiParts = new Part*[m_MaxParts];
		for(int i=0;i<m_NoOfParts;i++) m_MultiParts[i] = saved[i];
		if( saved ) delete[] saved;
	}
	m_MultiParts[ m_NoOfParts++ ] = new Part(this, url, filename, order, m_BaseDir);
}

const char* type_str[] = { "text/html; charset=us-ascii",
	"image/gif", "image/jpeg", "image/mpeg", "image/pdf", "unknown", "text/plain; charset=utf-7" };

Part::Part(Mhtml* MhtmlParent, const string& parentUrl, const string& filename, int order, const string& BaseDir)
{
	if( !filename.empty() && filename.find(':') != std::string::npos )
		m_FullFileName = filename;
	else if( filename.length() && filename[0] == '/' || filename[0] == '\\' )
		m_FullFileName = BaseDir + filename;
	else
		m_FullFileName = BaseDir + "/" + filename;
	m_FileName = filename;
		
	m_TmpFile = "";
	m_MhtmlParent = MhtmlParent;
	m_ParentUrl = parentUrl;
	m_Order = order;
	m_Message = "";
}

Part::~Part()
{
	if(m_TmpFile.length()) 
		kDeleteFile(m_TmpFile.c_str());
}

int Part::Process(std::string& message)
{
	int res;
	m_Message += message;
	m_ContentType = FindExt();
	if(m_ContentType == Html)
	{
		res = ConvertHtml(m_Message);
		message = m_Message;
		return res;
	}
	else 
	{
		if( m_Order > order_max )
			return 0;

		int err=OpenFile("rb");
		if( err ) 
			return err;

		m_Message = message;
		m_Message += "--";
		m_Message += boundary_string;
		m_Message += "\r\n";
		m_Message += "Content-Type: ";
		m_Message += type_str[(int)m_ContentType];		
		m_Message += "; name=\"";
		CheckAndConcate(m_Message, m_FileName.c_str());
		m_Message += "\"\r\n";
		
		if( m_Order > 0 ){
			m_Message += "Content-ID: <";
			CheckAndConcate(m_Message, m_FileName.c_str());
			m_Message += ">\r\n";
		}
		
		m_Message += "Content-Transfer-Encoding: base64\r\n";
		
		if( m_Order > 0 ) {
			if( m_FileName.length() && ( strnicmp(m_FileName.c_str(),"http://",7) == 0 ||
				strnicmp(m_FileName.c_str(),"https://",8) == 0) )
			{
				m_Message += "Content-Location: ";
				CheckAndConcate(m_Message, m_FileName.c_str());
				m_Message += "\r\n";
			}
			else
			{
				m_Message += "Content-Disposition: inline; filename=\"";
				CheckAndConcate(m_Message, m_FileName.c_str());
				m_Message += "\"\r\n";
			}
		}

		m_Message += "\r\n";
		char *buff;
		fseek(in,0,SEEK_END);
		long file_length=ftell(in);
		buff = new char[file_length/sizeof(char) + 3];
		fseek(in, 0, SEEK_SET);
		size_t k = fread(buff, sizeof(char), file_length/sizeof(char), in);
		fclose(in);
		buff[k/sizeof(char)]=0;
		Encoder e(buff, static_cast<int>(k));
		res = e.CBase64();
		delete[] buff;
		m_Message += e.GetEncodedString();

		message = m_Message;
		return res;
	}
	return 1;
}


Part::Content_Type Part::FindExt()
{
	const string& stExt = konga::GetFileExtension(m_FileName);
	if(stExt.empty()) return Html;                                       
	if(stricmp(stExt.c_str(), "htm")==0)return Html;
	else if(stricmp(stExt.c_str(), "html")==0)return Html;
	else if(stricmp(stExt.c_str(), "asp")==0)return Html;                                                                         
	else if(stricmp(stExt.c_str(), "gif")==0)return Gif;
	else if(stricmp(stExt.c_str(), "jpg")==0)return Jpg;
	else if(stricmp(stExt.c_str(), "mpg")==0)return Mpg;
	else if(stricmp(stExt.c_str(), "pdf")==0)return Pdf;
	else return Unknown;
}


  
                                                              
  
                
                 
                   
                   
                        
  
                      
                       
                        
                          
  
int Part::OpenFile(const char*open_mode        )
{
	bool isUrl = m_FullFileName.find( "://") != string::npos;
	if( m_Order && (isUrl || m_ParentUrl.length()) )
	{
		string stUrl;
		if( !isUrl )
		{
			char urlPath[20000];

			if( m_ParentUrl.length()>2 && m_ParentUrl[0] == '\\' && m_ParentUrl[1] == '\\' )
			{
				                                                                      
				sprintf(urlPath, "%s", m_ParentUrl.c_str());
			}
			else
			{
				try {
					Url url;
					url.Parse(m_ParentUrl);
					stUrl = url.getProtocol();
					stUrl += "://";
					stUrl += url.getHost();
				}
				catch( const CrException& ex ) {
					std::string stErr("Failed to parse the url " + m_ParentUrl + ".\n");
					stErr += ex.GetErrorString();
					throw CrException(stErr);
				}
			}

			if( m_FileName[0] == '/' || m_FileName[0] == '\\' )                                              
				stUrl += '/';
			else	                                   
			{
				char *apc = strrchr(urlPath,'/');
				char *bpc = strrchr(urlPath,'\\');
				char *pc = apc>bpc?apc:bpc;
				if( pc || (pc = strrchr(urlPath,'\\')) )
				{
					*pc = 0;
					stUrl += urlPath;
				}
				if( m_FileName[0] != '/' && m_FileName[0] != '\\' )                                              
					stUrl += '/';
			}
			stUrl += m_FileName;
			m_Url = stUrl;
		}
		else
			stUrl = m_FileName;

		string prefix = "cr_";
		m_TmpFile = konga::getTmpFileName(prefix);
		FileRetriever fr;
		if( fr.GetAsFile(stUrl, m_TmpFile) != 0 )
		{
			kDeleteFile(m_TmpFile.c_str());
			return 1;
		}
		in = fopen(m_TmpFile.c_str(),open_mode);
	}
	else
	{
		in = fopen(m_FullFileName.c_str(),open_mode);
		m_Url = m_ParentUrl;
	}

	if( !in ) {
		printf("%s", ErrorResolver().Resolve(::GetLastError()).c_str());
		return 1;
	}
	
	return 0;
}

int Part::CheckAndConcate(std::string& message, const char* Str){
	message += Str;
	return 0;
}

int Part::ConvertHtml(std::string& message)
{
	m_Message = message;
	int err = OpenFile();
	if( err ) return err;
	
	if( m_Order > order_max ) return 0;
	m_Message += "--";
	m_Message += boundary_string;
	m_Message += "\r\n";
	m_Message += "Content-Type: ";
	m_Message += "text/html";
	m_Message += "\r\nContent-Transfer-Encoding: 7bit\r\n";
	
	if( m_Order > 0 )
	{
		m_Message += "Content-ID: <";
		CheckAndConcate(m_Message, m_FileName.c_str());
		m_Message += ">\r\n";
		m_Message += "Content-Disposition: inline; filename=\"";
		CheckAndConcate(m_Message, m_FileName.c_str());
		m_Message += "\"\r\n\r\n";
	}

	m_Message += "\r\n";
	
	if( m_Order==0 && m_Url.length() )
	{
		if( m_Url.find("://") != string::npos ){
			m_Message += "<BASE HREF=\"";
			CheckAndConcate(m_Message, m_Url.c_str());
			m_Message += "\">\r\n";
		}
		else{
			m_Message += "<BASE HREF=\"file://";
			CheckAndConcate(m_Message, m_Url.c_str());
			m_Message += "\">\r\n";
		}
	}

	                                                                                                   
	bool bIncludeLinks = m_Url.find("://") == string::npos || (strnicmp(m_Url.c_str(), "file://", 7) == 0);
	HtmlParser p(in);
	while( p.Parse(bIncludeLinks) != HtmlTag::End )
	{
		if(p.HasNewPart()){
			m_MhtmlParent->AddPart(p.GetNewPartName(), m_Order + 1, m_Url);
		}
	}
	
	m_Message += p.GetParsedString();
	m_Message += "\r\n";
	fclose(in);
	message = m_Message;
	return 0;
}

HtmlParser::HtmlParser(FILE* fd) : in(fd), m_Pos(-1)
{
	memset(m_AsciiLine, 0, 1024);
}

                                               
void HtmlParser::GetAsciiFileToAdd(char c)
{
	int k=0,kk=0,kpos=m_Pos;
	if(c=='\"'||c=='\'')
	{
		while( (c=m_AsciiLine[kpos+1+kk]) != '\"' && c != '\'' && k < MAX_URL_SIZE )
		{
			if( c == '\0' )
			{
				if( ReadAsciiLine() == 0 )
				{
					m_AsciiFileToAdd[k++] = c;                       
					break;
				}
				else
				{
					kk=0;
					kpos=-1;
					continue;
				}
			}

			m_AsciiFileToAdd[k++] = c;
			++kk;
		}
	}
	else 
	{
		while( (c=m_AsciiLine[kpos+1+kk]) != '>' && c != ' ' && k < MAX_URL_SIZE )
		{
			if( c == '\0' )
			{
				if( ReadAsciiLine() == 0 )
				{
					m_AsciiFileToAdd[k++] = c;                       
					break;
				}
				else
				{
					kk=0;
					kpos=-1;
					continue;
				}
			}

			m_AsciiFileToAdd[k++] = c;
			++kk;
		}
	}
	if(kpos < 0)
		m_EndOfUrlPos = kk;
	else 
		m_EndOfUrlPos = kk + 1 + kpos;

	if( k >= MAX_URL_SIZE )
		k = MAX_URL_SIZE - 1;                                  

	m_AsciiFileToAdd[k] = 0;
}

                                                        
                            

void HtmlParser::WriteAsciiFileToAdd(char c, bool bWriteCid         , bool bApplet          )
{
	int k=0;
	if(c=='\"'||c=='\'')
	{
		if(bWriteCid)
		{
			  
			                                                                           
			  
			if( strnicmp(&m_AsciiLine[m_Pos],"http://",7) == 0 ||
				strnicmp(&m_AsciiLine[m_Pos+1],"http://",7) == 0 )
				m_AsciiFileToAdd[k] = 0;
			else
				m_Message += "cid:";
		}

		if( bApplet )
		{
			c = NextAsciiChar(); 
			while( c != '\"' && c != '\'' )
			{
				m_AsciiFileToAdd[k++] = c;
				if( (c=m_AsciiLine[m_Pos+1]) == '\"' || c == '\'' )
				{
					m_AsciiFileToAdd[k] = 0;
					size_t len = strlen(m_AsciiFileToAdd);
					if( len<=6 || strnicmp(&m_AsciiFileToAdd[len-6], ".class", len) != 0 )
					{
						strcat(m_AsciiFileToAdd, ".class");
						k+=6;
						m_Message += ".class";
					}
				}
				c = NextAsciiChar();
			}
		}
		else
		{
			if( m_AsciiFileToAdd[0] )	  
			{
				m_Message += m_AsciiFileToAdd;
				m_Pos = m_EndOfUrlPos-1;                                                                         
				NextAsciiChar();                                              
			}
			else
				while((c=NextAsciiChar())!='\"' && c != '\'' ) m_AsciiFileToAdd[k++] = c;
		}
	}
	else
	{
		if(bWriteCid){
			m_Message += "cid:";
		}
		if( bApplet )
		{
			c = NextAsciiChar(); 
			while( c != '>' && c != ' ' )
			{
				m_AsciiFileToAdd[k++] = c;
				if( (c=m_AsciiLine[m_Pos+1]) == '>' || c == ' ' )
				{
					m_AsciiFileToAdd[k] = 0;
					size_t len = strlen(m_AsciiFileToAdd);
					if( len<=6 || strnicmp(&m_AsciiFileToAdd[len-6], ".class", len) != 0 )
					{
						strcat(m_AsciiFileToAdd, ".class");
						k+=6;
						m_Message += ".class";
					}
				}
				c = NextAsciiChar();
			}
		}
		else
		{
			if( m_AsciiFileToAdd[0] )
			{
				m_Message += m_AsciiFileToAdd;
				m_Pos = m_EndOfUrlPos;
			}
			else
				while( (c=NextAsciiChar()) != '>' && c != ' ' ) m_AsciiFileToAdd[k++] = c;
		}
	}
	m_AsciiFileToAdd[k] = 0;
}

                                                        
                            

char* HtmlParser::GetNewPartName(){
	strcpy(m_FileToAdd, m_AsciiFileToAdd);
	return m_AsciiFileToAdd;
}

HtmlTag::Type HtmlParser::Parse(bool bIncludeLinks)
{
	return ParseAsciiFile(bIncludeLinks);
}

HtmlTag::Type HtmlParser::ParseAsciiFile(bool bIncludeLinks)
{
	char c;
	m_AsciiFileToAdd[0] = 0;
	while( (c = NextAsciiChar()) !=0 )
	{
		if(c=='<')
		{
			                                
			c = m_AsciiLine[m_Pos]; 
			while(c && (c == ' ' || c == '\n' || c == '\t'))
				c = NextAsciiChar(); 

			if( (c=NextAsciiChar()) == '/' ) continue;                      
			if( c== '\0' ) break;
			int nSkip; bool bAnchor=false, bApplet=false, bFrame=false;
			if( strnicmp(&m_AsciiLine[m_Pos], "a ", 2) == 0 )
			{ 
				if( !bIncludeLinks ) continue;
				bAnchor=true;
				nSkip = 2;
			}
			else if( strnicmp(&m_AsciiLine[m_Pos], "frame ", 6) == 0 )
			{
				if( !bIncludeLinks ) continue;
				nSkip = 6;
				bFrame = true;
			}
			else if( strnicmp(&m_AsciiLine[m_Pos], "applet ", 7) == 0 ) { bApplet=true; nSkip = 7; }
			else if( strnicmp(&m_AsciiLine[m_Pos], "body ", 5) == 0 ) nSkip = 5;
			else if( strnicmp(&m_AsciiLine[m_Pos], "IMG ", 4) == 0 ) nSkip = 4;			
			else continue;
			WriteStr(nSkip);
			                                
			c = m_AsciiLine[m_Pos]; 
			while(c && (c == ' ' || c == '\n' || c == '\t'))
				c = NextAsciiChar(); 


			if( strnicmp(&m_AsciiLine[m_Pos], "HREF=", 5) == 0 ) nSkip = 5;
			else if( strnicmp(&m_AsciiLine[m_Pos], "SRC=", 4) == 0 ) nSkip = 4;
			else if( strnicmp(&m_AsciiLine[m_Pos], "code=", 5) == 0 ) nSkip = 5;
			else if( strnicmp(&m_AsciiLine[m_Pos], "background=", 11) == 0 ) nSkip = 11;
			else continue;
			WriteStr(nSkip);
			                                
			c = m_AsciiLine[m_Pos]; 
			while(c && (c == ' ' || c == '\n' || c == '\t'))
				c = NextAsciiChar(); 


			c=m_AsciiLine[m_Pos+1];
			bool bQuote = (c=='\''||c=='"');
			if( bQuote ) c=NextAsciiChar();

			if( m_AsciiLine[m_Pos+bQuote] == '#' ) continue;                              
			if( strnicmp(&m_AsciiLine[m_Pos+bQuote], "mailto:", 7) == 0 ) continue;                     

			bool IsAsp=false;
			m_EndOfUrlPos = 0;
			if( bAnchor )
			{
				GetAsciiFileToAdd(c);
				                                                      
				int kk=konga::findk(m_AsciiFileToAdd, '?');
				char qMark = m_AsciiFileToAdd[kk];
				m_AsciiFileToAdd[kk]=0;
				IsAsp = kk>4 && (stricmp(".asp",&m_AsciiFileToAdd[kk-4])==0);
				m_AsciiFileToAdd[kk]=qMark;
			}
			
			                                                                                
			WriteAsciiFileToAdd(c, !bAnchor, bApplet);                                                     

			           
			if(bAnchor)
				m_AsciiFileToAdd[0]=0;                 
			return bAnchor ? HtmlTag::A : (bFrame ? HtmlTag::Other : HtmlTag::Img);
		}
	}
	return HtmlTag::End;
}

char HtmlParser::NextAsciiChar()
{
    if( m_AsciiLine[++m_Pos] == 0 )
	{
        if( ReadAsciiLine() == 0 )
			return 0;
    }

    char c = m_AsciiLine[m_Pos];
	if( (c<32 && c != '\n') || (c == 32 && m_AsciiLine[m_Pos+1] == '\n')) {
                 
                       
	}
	else{
		char buf[2] = {c, 0};
		m_Message += buf;
	}
	return c;
}


void HtmlParser::WriteStr(int n)
{
	for(int i=1;i<n;i++)
	{
		if( m_AsciiLine[m_Pos+i] ){
			char buf[2] = {0, 0};
			sprintf(buf, "%c", m_AsciiLine[m_Pos+i]);
			m_Message += buf;
		}
		else
			break;
	}
	m_Pos += n-1;
}


