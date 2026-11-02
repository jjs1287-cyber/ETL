/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <iostream>	       
#include <sstream>	               

#include <boost/thread/mutex.hpp>
#include <boost/scoped_array.hpp>
#include <boost/noncopyable.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>	            
#include <konga/Utils/PortDefs.h>		              
#include <konga/Utils/util.h>

#include <konga/Crypto/Crypto.h>	               

#include "CurlTransferLibWrapper.h"

using namespace konga;
using std::string;
using std::vector;

                                                                 
#ifndef CURL_VERSION_SSPI
#define CURL_VERSION_SSPI 0
#endif
#ifndef CURL_VERSION_CONV
#define CURL_VERSION_CONV 0
#endif

namespace {
	boost::mutex g_global_curl_init_mutex;

	                                          
	string GetYesNo(int bitfield, int bit) {
		if ( bit == 0 )
			return string("NO");
		return string( (bitfield&bit)!=0 ? "YES" : "NO" );
	}

	                                            
	                                      
	class ETLForgeCurlInitializer {
	public:
		ETLForgeCurlInitializer(void) : m_bInitialized(false)
		{
			Init();
		}

		~ETLForgeCurlInitializer(void) {
			if ( m_bInitialized ) {
				curl_global_cleanup();
			}
		}

		string GetCurlInfo() const {
			return m_stMessage;
		}

		void Init() {
			boost::mutex::scoped_lock lock(g_global_curl_init_mutex);

			try {
				if ( m_bInitialized )
					return;

				if ( curl_global_init(CURL_GLOBAL_ALL) == CURLE_OK ) {
					m_bInitialized = true;
					m_stMessage = "Curl global init succeeded.";
				}
				else {
					m_stMessage = "Curl global init failed.";
				}

				                                          
				curl_version_info_data* curlInfo = curl_version_info( CURLVERSION_NOW );
				if ( curlInfo == 0 ) {
					m_stMessage += "\nFailed to get curl info.";
				}
				else {
					const int features = curlInfo->features;
					if ( curlInfo->version )
						m_stMessage += "\nCurl Version: " + string(curlInfo->version);
					else
						m_stMessage += "\nFailed to determinte curl version.";

					if ( curlInfo->host )
						m_stMessage += "\nBuilt for: " + string(curlInfo->host);

					if ( curlInfo->protocols ) {
						m_stMessage += "\nSupported protocols: ";
						for( int i = 0; curlInfo->protocols[i]!=0; ++i ) {
							if ( i != 0 )
								m_stMessage += ", ";
							m_stMessage += curlInfo->protocols[i];
						}
						m_stMessage += "\n";
					}
					
					const bool bSupportsSSL = (features & CURL_VERSION_SSL) != 0;
					m_stMessage += "SSL Support: " + GetYesNo(features, CURL_VERSION_SSL);
					if ( bSupportsSSL && curlInfo->ssl_version )
						m_stMessage += "\nOpenSSL Version: " + string(curlInfo->ssl_version);

					const bool bSupportsCompression = (features & CURL_VERSION_LIBZ) != 0;
					m_stMessage += "\nDeflate Support: " + GetYesNo(features, CURL_VERSION_LIBZ);

					if ( bSupportsCompression && curlInfo->libz_version )
						m_stMessage += "\nZlib Version: " + string(curlInfo->libz_version);

					m_stMessage += "\nNTLM auth Support: " + GetYesNo(features, CURL_VERSION_NTLM);
					m_stMessage += "\nKerberos auth Support: " + GetYesNo(features, CURL_VERSION_KERBEROS4);
					m_stMessage += "\nGSS Negotiate auth Support: " + GetYesNo(features, CURL_VERSION_GSSNEGOTIATE);
					m_stMessage += "\nSPNEGO auth Support: " + GetYesNo(features, CURL_VERSION_SPNEGO);
					m_stMessage += "\nSupports files larger than 2 GB: " + GetYesNo(features, CURL_VERSION_LARGEFILE);
					m_stMessage += "\nInternational Domain Names Support: " + GetYesNo(features, CURL_VERSION_IDN);
					m_stMessage += "\nSSPI Support: " + GetYesNo(features, CURL_VERSION_SSPI);
					m_stMessage += "\ncharacter conversion Support: " + GetYesNo(features, CURL_VERSION_CONV);

					if ( curlInfo->age >= CURLVERSION_SECOND && curlInfo->ares )
						m_stMessage += "\n" + string(curlInfo->ares);
					if ( curlInfo->age >= CURLVERSION_THIRD && curlInfo->libidn )
						m_stMessage += "\n" + string(curlInfo->libidn);
					                                                      
                                                                             
                                                                               

					if ( features & CURL_VERSION_DEBUG )
						m_stMessage += "\nWarning: Curl was built with extra debug capabilities built-in.";

#ifdef _DEBUG
					std::cout << "DEBUG: Reporting Curl capabilities:" << std::endl;
					std::cout << m_stMessage << std::endl << std::endl;
#endif
				}
			}
			catch(...) {
#ifndef _DEBUG
				if ( !m_stMessage.empty() )
					m_stMessage += "\n";
				m_stMessage += "Global curl initialization failed. Unknown exception occurred.";
#else
				throw;
#endif
			}
			if ( !m_bInitialized )
				std::cerr << "Curl initialization failed." << std::endl << m_stMessage << std::endl;
		}

	private:
		bool m_bInitialized;
		string m_stMessage;
	};

	ETLForgeCurlInitializer g_InitCurl;
}

                     
namespace {
	typedef vector<string> vstrings;

	                                                                                                                            
	                                      
	                                                                                                       
	struct curl_write_function_stream {
		curl_write_function_stream(string& st) : m_bIgnore(false), m_pFile(0), m_pst(&st) {}
		curl_write_function_stream() : m_bIgnore(false), m_pFile(0), m_pst(0) {}
		~curl_write_function_stream() {
			clear();
		}

		void clear() {
			m_bIgnore = false;
			if ( m_pFile != 0 ) {
				fclose(m_pFile);
				m_pFile = 0;
			}
			m_pst = 0;
		}

		bool m_bIgnore;
		FILE* m_pFile;
		string* m_pst;
	};

	                                                                                                                     
	                                                          
	size_t curl_write_function( void *ptr, size_t size, size_t nmemb, void *s) {
		if ( size*nmemb == 0 || ptr == 0 || s == 0)
			return 0;

		curl_write_function_stream* stream = static_cast<curl_write_function_stream*>(s);
		if ( stream->m_bIgnore ) {
			                                                
			return size*nmemb;
		} else if ( stream->m_pst != 0 ) {
			                        
			stream->m_pst->append((const char*)ptr, size*nmemb);
			return size*nmemb;
		}
		else if ( stream->m_pFile != 0 ) {
			                
			size_t written = fwrite(ptr, size, nmemb, stream->m_pFile);
			return written*size;
		}
		else {
			                                                                                 
			return 0;
		}
	}

	struct curl_read_function_stream {
		curl_read_function_stream(string& st) : m_pFile(0), m_pst(&st), m_pos(0), m_size(0) {}
		curl_read_function_stream() : m_pFile(0), m_pst(0), m_pos(0), m_size(0) {}
		~curl_read_function_stream() {
			clear();
		}

		void clear() {
			if ( m_pFile ) {
				fclose(m_pFile);
				m_pFile = 0;
			}
			m_pst = 0;
			m_pos = 0;
			m_size = 0;
		}

		void rewindStream() {
			if ( m_pFile ) {
				::rewind(m_pFile);
			}
			m_pos = 0;
		}

		FILE* m_pFile;
		const string* m_pst;

		                                                                                              
		size_t m_pos;
		size_t m_size;
	};

	                                                  
    size_t curl_read_function(void *ptr, size_t size, size_t nmemb, void *s) {
		if ( ptr == 0 || size*nmemb == 0 || s == 0 )
			return 0;

		curl_read_function_stream* stream = static_cast<curl_read_function_stream*>(s);
		if ( stream->m_pst != 0 ) {
			size_t nBytesToCopy = (size*nmemb > (stream->m_size-stream->m_pos)*sizeof(char)) ? stream->m_size-stream->m_pos : size*nmemb;
			if ( nBytesToCopy == 0 )
				return 0;        

			memcpy(ptr, stream->m_pst->substr(stream->m_pos, nBytesToCopy/sizeof(char)).c_str(), nBytesToCopy);
			stream->m_pos += nBytesToCopy/sizeof(char);
			return nBytesToCopy;
		}
		else if ( stream->m_pFile ) {
			size_t read = fread(ptr, size, nmemb, stream->m_pFile);
			return read*size;
		}
		else {
			                                                                                    
			return 0;
                                                                                          
		}
	}

	                                                        
	curlioerr curl_ioctl_function(CURL *handle, int cmd,void *clientp) {
		if ( cmd == CURLIOCMD_RESTARTREAD ) {
			curl_read_function_stream* s = static_cast<curl_read_function_stream*>(clientp);
			if ( clientp ) {
				s->rewindStream();
				return CURLIOE_OK;
			}
			else
				return CURLIOE_FAILRESTART;
		}
		else {
			return CURLIOE_UNKNOWNCMD;
		}
	}

	                                                                                              
	struct curl_write_header_stream {
		curl_write_header_stream() {}
		~curl_write_header_stream() {}
		void clear() {
			m_vHeaders.clear();
		}
		vstrings m_vHeaders;
	};

	                                                                
    size_t curl_write_header_function( void *ptr, size_t size, size_t nmemb, void *s) {
		if ( size*nmemb == 0 || ptr == 0 || s == 0 )
			return 0;

		curl_write_header_stream* stream = static_cast<curl_write_header_stream*>(s);
		boost::scoped_array<char> pc(new char[size*nmemb+1]);
		memcpy(pc.get(), ptr, size*nmemb);
		pc[size*nmemb] = 0;
		stream->m_vHeaders.push_back(string(pc.get()));
		return size*nmemb;
	}

	                                                           
	int curl_debug_callback_function(CURL *pCurl, curl_infotype type, char *bc, size_t size, void *pv) {
		if ( bc == 0 || size == 0 || pv == 0 ) return 0;
		FILE* debug_log = (FILE*)pv;

		string sType("CURL DEBUG ");
		switch(type) {
			case CURLINFO_TEXT:
				sType += "TEXT";
				break;
			case CURLINFO_HEADER_IN:
				sType += "HEADER_IN";
				break;
			case CURLINFO_HEADER_OUT:
				sType += "HEADER_OUT";
				break;
			case CURLINFO_DATA_IN:
				sType += "DATA_IN";
				break;
			case CURLINFO_DATA_OUT:
				sType += "DATA_OUT";
				break;
			default:
				sType += "UNKNOWN";
				break;
		};

		const string sData(bc, bc[size-1]=='\n' ? size-1 : size);
		fprintf(debug_log, "%s: %s\n", sType.c_str(), sData.c_str());
		fflush(debug_log);
		return 0;
	}

	inline void Capitalize(string& stMsg) {
		if ( !stMsg.empty() ) {
			stMsg[0] = toupper(*stMsg.begin());
		}
	}

	                                                         
	                                                             
	inline void AddPeriodAndSpace(string& stMsg) {
		if ( !stMsg.empty() ) {
			if ( *(stMsg.end()-1) == ' ' )
				return;
			if ( *(stMsg.end()-1) != '.' )
				stMsg += ". ";
			else
				stMsg += " ";
		}
	}

	                                               
	void FixCurlSpelling(string& stErr) {
		string stTmp;
		ReplaceStringWithString(stErr, stTmp, "couldn't", "could not");
		ReplaceStringWithString(stTmp, stErr, "couldnt", "could not");
	}

	inline void HandleCurlError(const CURLcode code, CURL* curl = 0, const string& stErr = "",
		const char* const pErrorBuffer = 0, vector<string>* pHeaders = 0) {
		if ( code != CURLE_OK ) {
			string stCurlError(curl_easy_strerror(code));
			if ( !stCurlError.empty() ) {
				FixCurlSpelling(stCurlError);
				Capitalize(stCurlError);
				AddPeriodAndSpace(stCurlError);
			}

			if ( pErrorBuffer != 0 ) {
				string stTmp(pErrorBuffer);
				if ( !stTmp.empty() && stCurlError.find(stTmp) == string::npos ) {
					FixCurlSpelling(stTmp);
					Capitalize(stTmp);
					AddPeriodAndSpace(stTmp);
					stCurlError += stTmp;
				}
			}

			                         
			long lErr = 0;
			string stOsError;
			if ( curl_easy_getinfo(curl, CURLINFO_OS_ERRNO, &lErr) == CURLE_OK ) {
				if ( lErr != 0 ) {
					stOsError = ErrorResolver().Resolve(lErr);
					if ( !stOsError.empty() ) {
						if ( stCurlError.find(stOsError) != string::npos )
							stOsError.clear();
						Capitalize(stOsError);
					}
				}
			}

			                                       
			long lRespCode = 0;
			if ( curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, &lRespCode) != CURLE_OK ) {
				lRespCode = 0;
			}

			string stError(stErr);
			AddPeriodAndSpace(stError);

			stError += stCurlError;

			if ( !stOsError.empty() ) {
				AddPeriodAndSpace(stError);
				stError += stOsError;
			}

			if ( lRespCode != 0 ) {
				AddPeriodAndSpace(stError);
				stError += "(Last response code: " + toString(lRespCode) + ")";
			}

			if ( pHeaders != 0 && !pHeaders->empty() ) {
				string stAllHeaders("\nThe following headers were returned:\n");
				for( vector<string>::const_iterator cit = pHeaders->begin(); cit != pHeaders->end(); ++cit ) {
					stAllHeaders += *cit;
				}
				stError += stAllHeaders;
			}

			throw CrException(stError, code);
		}
	}

	inline void HandleSystemError(long errnum, const string& stErr = "") {
		if ( errnum != 0 ) {
			throw CrException(stErr + ErrorResolver().Resolve(errnum), errnum);
		}
	}
	
	FILE* OpenLocalFileForWriting(const string& stFileName) {
#ifdef WIN32
		const int count = MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, stFileName.c_str(), -1, 0, 0);
		if (count == 0) throw CrException("Invalid UTF-8 local transfer path");
		std::vector<wchar_t> path(count);
		if (MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, stFileName.c_str(), -1, path.data(), count) != count)
			throw CrException("Cannot decode UTF-8 local transfer path");
		FILE* pFile = _wfopen(path.data(), L"wb");
#else
		FILE* pFile = fopen(stFileName.c_str(), "wb");
#endif
		if ( pFile == 0 ) {
			HandleSystemError(::GetLastError(), "Failed to open the file \"" + stFileName + "\" for writing. ");
			return 0;
		}
		return pFile;
	}

	FILE* OpenLocalFileForReading(const string& stFileName) {
#ifdef WIN32
		const int count = MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, stFileName.c_str(), -1, 0, 0);
		if (count == 0) throw CrException("Invalid UTF-8 local transfer path");
		std::vector<wchar_t> path(count);
		if (MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, stFileName.c_str(), -1, path.data(), count) != count)
			throw CrException("Cannot decode UTF-8 local transfer path");
		FILE* pFile = _wfopen(path.data(), L"rb");
#else
		FILE* pFile = fopen(stFileName.c_str(), "rb");
#endif
		if ( pFile == 0 ) {
			HandleSystemError(GetLastError(), "Failed to open the file \"" + stFileName + "\" for reading. ");
			return 0;
		}
		return pFile;
	}

	inline bool isFtpUrl(const char* chUrl) {
		return ( strncasecmp(chUrl, "ftp://", 6) == 0 ||
				 strncasecmp(chUrl, "ftps://", 7) == 0 );
	}

	inline bool isSftpUrl(const char* chUrl) {
		return ( strncasecmp(chUrl, "sftp://", 7) == 0 );
	}

	inline bool isFtpUrl(const string& stUrl) {
		return isFtpUrl(stUrl.c_str());
	}

	inline bool isHttpUrl(const char* chUrl) {
		return ( strncasecmp(chUrl, "http://", 7) == 0 ||
				 strncasecmp(chUrl, "https://", 8) == 0 );
	}

}                      

                                                                       
                                                                         
                                                                                 
class konga::CurlTransferLibWrapper::CurlPersistedData : noncopyable {
public:
	CurlPersistedData() : m_pcParsedUrl(0), m_pcUserAgent(0),
		m_pcErrorBuffer(0),
		m_form(0), m_last(0),
		m_ftp_quote(0), m_ftp_postquote(0),
		m_http_request_headers(0) {
	};

	~CurlPersistedData() {
		clear();
	}

	void clear() {
		if ( m_pcParsedUrl != 0 ) {
			delete[] m_pcParsedUrl;
			m_pcParsedUrl = 0;
		}
		if ( m_pcUserAgent != 0 ) {
			 delete[] m_pcUserAgent;
			 m_pcUserAgent = 0;
		}
		if ( m_pcErrorBuffer != 0 ) {
			delete[] m_pcErrorBuffer;
			m_pcErrorBuffer = 0;
		}
		m_stResult.clear();
		m_write_stream.clear();
		m_header_stream.clear();

		clear_slists();
		clear_form_data();

		for( vector<char*>::iterator it = m_vpc.begin(); it != m_vpc.end(); ++it ) {
			delete[] *it;
		}
		m_vpc.clear();
	}

	void clear_slists() {
		if ( m_ftp_quote != 0 ) {
			curl_slist_free_all(m_ftp_quote);
			m_ftp_quote = 0;
		}
		if ( m_ftp_postquote != 0 ) {
			curl_slist_free_all(m_ftp_postquote);
			m_ftp_postquote = 0;
		}
		if ( m_http_request_headers != 0 ) {
			curl_slist_free_all(m_http_request_headers);
			m_http_request_headers = 0;
		}
	}

	void clear_form_data() {
		if ( m_form ) {
			curl_formfree(m_form);
		}
	}

	struct curl_httppost* addFileUploadPart(
		const string& stName_in, const string& stFileName,
		const string& stFilePath, const string& stContentType)
	{
		if ( stFilePath.empty() )
			throw CrException("Failed to add file upload part. An empty file path was specified.");

		const string stName( stName_in.empty() ? "file" : stName_in);
		char* pFileName = konga::strsave(stFileName.empty() ? "file" : stFileName.c_str());
		m_vpc.push_back(pFileName);
		char* pFilePath = konga::strsave(stFilePath.c_str());
		m_vpc.push_back(pFilePath);
		char* pContentType = konga::strsave(stContentType.empty() ? "application/octet-stream" : stContentType.c_str());
		m_vpc.push_back(pContentType);
		CURLFORMcode code = curl_formadd(&m_form, &m_last,
			CURLFORM_COPYNAME, stName.c_str(),
			CURLFORM_FILE, pFilePath,                               
			CURLFORM_FILENAME, pFileName,
			CURLFORM_CONTENTTYPE, pContentType,
			CURLFORM_END);
		if( code != CURL_FORMADD_OK ) {
			                               
			throw CrException("curl_formadd returned error: " + toString(code));
		}
		return m_form;
	}

	                                                                
	char* init_error_buffer() {
		if ( m_pcErrorBuffer != 0 ) {
			delete[] m_pcErrorBuffer;
			m_pcErrorBuffer = 0;
		}
		m_pcErrorBuffer = new char[CURL_ERROR_SIZE];
		m_pcErrorBuffer[0] = 0;
		return m_pcErrorBuffer;
	}

	const char* get_error_buffer() const {
		return m_pcErrorBuffer;
	}

	void addCharArrayToDelete(char* const pCharArrToDelete) {
		m_vpc.push_back(pCharArrToDelete);
	}

	char* setUrl(const string& stUrl) {
		if ( m_pcParsedUrl ) {
			delete[] m_pcParsedUrl;
			m_pcParsedUrl = 0;
		}
		if ( stUrl.empty() )
			throw CrException("Failed to set URL. No URL was specified.");

		return (m_pcParsedUrl = konga::strsave(stUrl.c_str()));
	}

	string getUrl() const {
		return m_pcParsedUrl == 0 ? "" : m_pcParsedUrl;
	}

	char* setUserAgent(const string& stUserAgent) {
		if ( m_pcUserAgent ) {
			delete m_pcUserAgent;
			m_pcUserAgent = 0;
		}
		return ( m_pcUserAgent = konga::strsave(stUserAgent.c_str()) );
	}

private:
	char* m_pcParsedUrl;
	char* m_pcUserAgent;
	char* m_pcErrorBuffer;

	struct curl_httppost* m_form;
	struct curl_httppost* m_last;

	vector<char*> m_vpc;                                                        

public:

	string m_stResult;
	curl_write_function_stream m_write_stream;
	curl_read_function_stream m_read_stream;
	curl_write_header_stream m_header_stream;

	struct curl_slist* m_ftp_quote;
	struct curl_slist* m_ftp_postquote;
	struct curl_slist* m_http_request_headers;

private:
};

CurlTransferLibWrapper::CurlTransferLibWrapper(void)
	: m_handle(0), m_pCurlData(new CurlPersistedData),
	m_bPost(false), m_bUseRfc1867(false), m_bUseHttpChunkedTransferEncoding(false), m_bEnableExpect100Continue(false),
	m_DebugFile(0),
	m_pstLocalResponseString(0), m_pstLocalRequestString(0)
{
	g_InitCurl.Init();
	try {
		reset();
	}
	catch(...) {
		                                                                                             
		try {
			Cleanup();
		}
		catch(...) {
			                                   
		}

		throw;
	}
}

CurlTransferLibWrapper::~CurlTransferLibWrapper(void) {
	Cleanup();
}

void CurlTransferLibWrapper::Cleanup() {
	if ( m_handle != 0 ) {
		curl_easy_cleanup(m_handle);
		m_handle = 0;
	}

	if ( m_pCurlData != 0 ) {
		delete m_pCurlData;
		m_pCurlData = 0;
	}
}

void CurlTransferLibWrapper::reset() {
	if ( m_handle != 0 ) {
		curl_easy_cleanup(m_handle);
		m_handle = 0;
		if ( m_pCurlData != 0 )
			m_pCurlData->clear();
	}

	m_pstLocalResponseString = 0;
	m_stLocalResponseFileName.clear();
	m_pstLocalRequestString = 0;
	m_stLocalRequestFileName.clear();
	m_vResponseHeaders.clear();

	m_handle = curl_easy_init();
	if ( m_handle == 0 ) {
		throw CrException("Failed to initialize curl handle. curl_easy_init() returned null." \
			" Unknown error. Available curl info:\n"
			+ g_InitCurl.GetCurlInfo());
	}

	                                                            
	curl_easy_setopt(m_handle, CURLOPT_NOSIGNAL, 1);
}

void CurlTransferLibWrapper::checkHandle() {
	if ( m_handle == 0 ) {
		reset();
	}
}

void CurlTransferLibWrapper::initDebug() {
#ifdef _DEBUG
                            
                                                     
    
#endif
	if ( m_DebugFile == 0 || m_handle == 0) return;

	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_VERBOSE, 1) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_DEBUGFUNCTION, curl_debug_callback_function) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_DEBUGDATA, (void*)m_DebugFile) );
}

void CurlTransferLibWrapper::setUrl(const string &stUrl) {
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_URL, m_pCurlData->setUrl(stUrl)) );
}

void CurlTransferLibWrapper::setUserAndPassword(const string& stUser, const string& stPassword) {
	if ( stUser.empty() )
		throw CrException("An empty user-name was specified.");

	const string& stCurlUserPass = stUser + ':' + stPassword;
	char* pc = konga::strsave(stCurlUserPass.c_str());
	m_pCurlData->addCharArrayToDelete(pc);
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_USERPWD, pc), m_handle, "Failed to set user name and password." );

	if( isHttp() ) {
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_HTTPAUTH, CURLAUTH_ANY), m_handle, "Failed to set allowed http authentication methods." );
	}
}

                           

void CurlTransferLibWrapper::setProxySettings(const string& sHostName, long lPort, const string& sUserName, const string& sPassword) {
	if ( sHostName.empty() )
		return;

#ifdef USE_ENV_FOR_PROXY
	                                           
	string auth;
	if ( !sUserName.empty() ) {
		auth = sUserName;
		if ( !sPassword.empty() ) {
			auth += (":" + sPassword);
		}
		auth += "@";
	}

	string host(sHostName);
	if ( lPort > 0 ) {
		host += (":" + toString(lPort));
	}

	string tmp = konga::getEnvironmentVariableValue("all_proxy");

	const string all_proxy("http://" + auth + host);
	konga::setEnvironmentVariableValue("all_proxy", all_proxy);

	char buf[1024];
	::GetEnvironmentVariable("all_proxy", buf, 1024);

	tmp = konga::getEnvironmentVariableValue("all_proxy");
#else
	string curlopt_proxy(sHostName);
	if ( lPort > 0 )
		curlopt_proxy += (":" + toString(lPort));

	char* pc = konga::strsave(curlopt_proxy.c_str());
	m_pCurlData->addCharArrayToDelete(pc);
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_PROXY, pc), m_handle, "Failed to set proxy host and port." );

	if ( sUserName.empty() )
		return;

	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_PROXYAUTH, CURLAUTH_ANY), m_handle, "Failed to set allowed proxy authentication methods." );

	string curlopt_proxyuserpwd(sUserName);
	if ( !sPassword.empty() )
		curlopt_proxyuserpwd += (":" + sPassword);

	pc = konga::strsave(curlopt_proxyuserpwd.c_str());
	m_pCurlData->addCharArrayToDelete(pc);
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_PROXYUSERPWD, pc), m_handle, "Failed to set proxy username and password." );
#endif
}

void CurlTransferLibWrapper::setVerifyPeerCert(bool bVerify) {
	long peer = bVerify ? 1 : 0;
	long host = bVerify ? 2 : 0;
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_SSL_VERIFYPEER, peer) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_SSL_VERIFYHOST, host) );
}

void CurlTransferLibWrapper::setResponseFile(const string& stLocalResultFile) {
	m_stLocalResponseFileName = stLocalResultFile;
	m_pstLocalResponseString = 0;
}

                                                                                             
void CurlTransferLibWrapper::setResponseString(string& pstLocalResultString) {
	m_stLocalResponseFileName.clear();
	m_pstLocalResponseString = &pstLocalResultString;
	m_pstLocalResponseString->clear();
}

void CurlTransferLibWrapper::setRequestFile(const string& stLocalRequestFile) {
	m_stLocalRequestFileName = stLocalRequestFile;
	m_pstLocalRequestString = 0;
}

void CurlTransferLibWrapper::setRequestString(const string& stLocalRequestString) {
	m_stLocalRequestFileName.clear();
	m_pstLocalRequestString = &stLocalRequestString;
}

void CurlTransferLibWrapper::setInputCookieFileName(const std::string& stFileName) {
	if ( stFileName.empty() ) {
		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_COOKIEFILE, ""), m_handle, "Failed to enable cookie support in curl.");
	}
	else {
		char* pFileName = konga::strsave(stFileName.c_str());
		m_pCurlData->addCharArrayToDelete(pFileName);
		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_COOKIEFILE, pFileName), m_handle, "Failed to set cookie file.");
	}
}

void CurlTransferLibWrapper::setOutputCookieFileName(const std::string& stFileName) {
	if ( stFileName.empty() )
		return;

	char* pFileName = konga::strsave(stFileName.c_str());
	m_pCurlData->addCharArrayToDelete(pFileName);
	HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_COOKIEJAR, pFileName), m_handle, "Failed to set cookiejar file.");
}

void CurlTransferLibWrapper::setUserAgent(const string& stUserAgent) {
	if ( !stUserAgent.empty() ) {
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_USERAGENT, m_pCurlData->setUserAgent(stUserAgent)) );
	}
}

void CurlTransferLibWrapper::followRedirects(int max_redir) {
	if ( max_redir == 0 ) {
		                                                              
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_FOLLOWLOCATION, 0) );
	}
	else {
		              
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_FOLLOWLOCATION, 1) );

		                                                                                          
		                                                          
                                                                                          
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_POST301, 1) );

		                                                                               
		                                                                   
		if ( max_redir > 0 ) {
			HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_MAXREDIRS, max_redir) );
		}
		else {
			HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_MAXREDIRS, -1) );
		}
	}
}

void CurlTransferLibWrapper::setFtpAscii() {
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_TRANSFERTEXT, 1) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_CRLF, 1) );
}

void CurlTransferLibWrapper::setFtpBinary() {
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_TRANSFERTEXT, 0) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_CRLF, 0) );
}

void CurlTransferLibWrapper::setFtpRenameTo(const string& stRenameFrom, const string& stRenameTo) {
	m_stFtpRNFR = stRenameFrom;
	m_stFtpRNTO = stRenameTo;
}

namespace {
	const char pcPort[] = "-";
}

void CurlTransferLibWrapper::setFtpPassiveMode(bool bPassiveMode) {
	if ( !isFtpUrl(m_pCurlData->getUrl().c_str()) )
		return;

	if ( bPassiveMode )
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_FTPPORT, 0) );
	else
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_FTPPORT, pcPort) );
}

void CurlTransferLibWrapper::setFtpSslNone() {
	setFtpSslLevel(CURLUSESSL_NONE);
}

void CurlTransferLibWrapper::setFtpSslTry() {
	setFtpSslLevel(CURLUSESSL_TRY);
}

void CurlTransferLibWrapper::setFtpSslControl() {
	setFtpSslLevel(CURLUSESSL_CONTROL);
}

void CurlTransferLibWrapper::setFtpSslAll() {
	setFtpSslLevel(CURLUSESSL_ALL);
}

void CurlTransferLibWrapper::preferAUTH_TLS(bool preferAUTH_TLS) {
	if ( preferAUTH_TLS ) {
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_FTPSSLAUTH, CURLFTPAUTH_TLS) );
	}
	else {
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_FTPSSLAUTH, CURLFTPAUTH_SSL) );
	}
}

void CurlTransferLibWrapper::configureSftp(const string& keyFile,const string& publicFile,const string& passphrase,const string& fingerprint) {
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_SSH_HOST_PUBLIC_KEY_SHA256,(fingerprint+"=").c_str()));
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_SSH_AUTH_TYPES,keyFile.empty()?CURLSSH_AUTH_PASSWORD:CURLSSH_AUTH_PUBLICKEY));
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_SSH_PRIVATE_KEYFILE,keyFile.empty()?"":keyFile.c_str()));
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_SSH_PUBLIC_KEYFILE,publicFile.empty()?"":publicFile.c_str()));
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_KEYPASSWD,passphrase.c_str()));
}

void CurlTransferLibWrapper::setSshPublicKeyFileName(const string& stPubKeyFile) {
	if ( !stPubKeyFile.empty() && FileExists(stPubKeyFile) ) {
		char* pch = strsave(stPubKeyFile.c_str());
		m_pCurlData->addCharArrayToDelete(pch);
		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_SSH_PUBLIC_KEYFILE, pch),
			m_handle, "Failed to set the public keyfile.");
	}
}

void CurlTransferLibWrapper::setSshPrivateKeyFileName(const string& stPrivateKeyFile,
													  const string& stPassphrase)
{
	if ( !stPrivateKeyFile.empty() && FileExists(stPrivateKeyFile) ) {
		char* pch = strsave(stPrivateKeyFile.c_str());
		m_pCurlData->addCharArrayToDelete(pch);
		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_SSH_PRIVATE_KEYFILE, pch),
			m_handle, "Failed to set the private keyfile.");

		if ( !stPassphrase.empty() ) {
			char* pch = strsave(stPassphrase.c_str());
			m_pCurlData->addCharArrayToDelete(pch);
			HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_KEYPASSWD, pch),
				m_handle, "Failed to set the passphrase for the private keyfile.");
		}

		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_SSH_AUTH_TYPES, CURLSSH_AUTH_PUBLICKEY),
			m_handle, "Failed to set the CURLOPT_SSH_AUTH_TYPES=CURLSSH_AUTH_PUBLICKEY.");
	}
}

                                                 
void CurlTransferLibWrapper::setFtpSslLevel(long level) {
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_USE_SSL, level) );
}

void CurlTransferLibWrapper::SetupHeaderStream() {
	                                            
	m_pCurlData->m_header_stream.clear();
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_HEADERFUNCTION, curl_write_header_function) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_WRITEHEADER, &m_pCurlData->m_header_stream) );
}

void CurlTransferLibWrapper::SetupWriteStream(bool bIgnore) {
	                                                      
	m_pCurlData->m_write_stream.clear();
	if ( bIgnore ) {
		m_pCurlData->m_write_stream.m_bIgnore = bIgnore;
	} else if ( m_stLocalResponseFileName.empty() ) {
		                        
		if ( m_pstLocalResponseString != 0 ) {
			m_pCurlData->m_write_stream.m_pst = m_pstLocalResponseString;

		}
		else {
			                                                                                     
			m_pCurlData->m_stResult.clear();
			m_pCurlData->m_write_stream.m_pst = &m_pCurlData->m_stResult;
		}
	}
	else {
		                                   
		m_pCurlData->m_write_stream.m_pFile = OpenLocalFileForWriting(m_stLocalResponseFileName);
	}

	                                                                   
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_WRITEFUNCTION, curl_write_function) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_WRITEDATA, &m_pCurlData->m_write_stream) );
}

void CurlTransferLibWrapper::SetupReadStream() {
	if ( m_bUseRfc1867 )
		return;	                                            

	m_pCurlData->m_read_stream.clear();
	if ( m_stLocalRequestFileName.empty() ) {
		                            
		if ( m_pstLocalRequestString == 0 )
			throw CrException("Usage Error: No local source was specified. A file name or a string must be provided.");
		m_pCurlData->m_read_stream.m_pst = m_pstLocalRequestString;
		m_pCurlData->m_read_stream.m_size = m_pstLocalRequestString->length();
	}
	else {
		                                     
		m_pCurlData->m_read_stream.m_pFile = OpenLocalFileForReading(m_stLocalRequestFileName);
	}

	          
	                
	long lDataSize = 0;
	if ( m_pCurlData->m_read_stream.m_pFile != 0 ) {
		long lErr = 0;
		lDataSize = static_cast<long>(konga::GetFileSize(m_stLocalRequestFileName, lErr));
	}
	else {
		lDataSize = m_pCurlData->m_read_stream.m_size;
	}

	if ( isHttp() ) {
		if ( m_bUseHttpChunkedTransferEncoding ) {
			                                                                           
			addHttpRequestHeader("Transfer-Encoding: chunked");
		}
		else {
			if ( m_bPost ) {
				HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_POSTFIELDSIZE, lDataSize) );
                                                                                                                                  
			}
			else {
				HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_INFILESIZE, lDataSize) );
			}
		}
	}
	else {
		                                                    
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_INFILESIZE, lDataSize) );
	}

	                                                                    
	HandleCurlError(
		curl_easy_setopt(m_handle, CURLOPT_READFUNCTION, curl_read_function)
		);
	HandleCurlError(
		curl_easy_setopt(m_handle, CURLOPT_READDATA, &(m_pCurlData->m_read_stream))
		);
	HandleCurlError(
		curl_easy_setopt(m_handle, CURLOPT_IOCTLFUNCTION, curl_ioctl_function)
		);
	HandleCurlError(
		curl_easy_setopt(m_handle, CURLOPT_IOCTLDATA, &(m_pCurlData->m_read_stream))
		);
}

void CurlTransferLibWrapper::ftpsWorkAround() {
	                                  
	if ( strncasecmp(m_pCurlData->getUrl().c_str(), "ftps://", 7) == 0 ) {
		m_pCurlData->m_ftp_quote = curl_slist_append(m_pCurlData->m_ftp_quote, "PROT P");
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_QUOTE, m_pCurlData->m_ftp_quote) );
	}
}

CURLcode CurlTransferLibWrapper::CurlEasyPerformWrapper() {
	                             
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_ERRORBUFFER, m_pCurlData->init_error_buffer()) );

	initDebug();
	ftpsWorkAround();

	const CURLcode cc = curl_easy_perform(m_handle);

	                                                    
	m_vResponseHeaders = m_pCurlData->m_header_stream.m_vHeaders;
	m_pCurlData->m_header_stream.clear();

	                                
	m_pCurlData->m_read_stream.clear();
	m_pCurlData->m_write_stream.clear();

	                                     
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_POSTQUOTE, 0) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_QUOTE, 0) );
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_HTTPHEADER, 0) );
	m_pCurlData->clear_slists();
	return cc;
}

void CurlTransferLibWrapper::setHeadersIfNeeded() {
	if ( m_pCurlData->m_http_request_headers != 0 ) {
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_HTTPHEADER, m_pCurlData->m_http_request_headers) );
	}
}

void CurlTransferLibWrapper::overrideHttpVerbIfRequested() {
	if ( !m_stOverrideHttpVerb.empty() ) {
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_CUSTOMREQUEST, m_stOverrideHttpVerb.c_str()),
			m_handle, "Set verb to " + m_stOverrideHttpVerb);
	}
}

void CurlTransferLibWrapper::Get() {
	SetupWriteStream();
	SetupHeaderStream();

	setHeadersIfNeeded();
	overrideHttpVerbIfRequested();

	const CURLcode cc = CurlEasyPerformWrapper();

	HandleCurlError(cc, m_handle, "Get failed.",  m_pCurlData->get_error_buffer(), &m_vResponseHeaders);
}

void CurlTransferLibWrapper::Put() {
	bool bCURLOPT_UPLOAD = false;
	if ( m_bUseRfc1867 ) {
		if ( !m_stLocalRequestFileName.empty() ) {
			struct curl_httppost* pForm =
				m_pCurlData->addFileUploadPart(m_stFormName, m_stFormFileName, m_stLocalRequestFileName, m_stFormContentType);
			HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_HTTPPOST, pForm) );
		}
		else
			throw CrException("Cannot use RFC 1867 upload without specifying a file name.");
	}
	else if ( m_bPost ) {
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_POST, 1) );
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_POSTFIELDS, 0) );	                                                         
	}
	else {
		                                                                
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_UPLOAD, 1) );
		bCURLOPT_UPLOAD = true;
	}
	
	overrideHttpVerbIfRequested();

	if ( isHttp() ) {
		                                                                                                                 
		if ( !m_bEnableExpect100Continue ) addHttpRequestHeader("Expect:");
	}

	if ( isFtp() && !m_stFtpRNFR.empty() && !m_stFtpRNTO.empty() ) {
		string stCmd( "RNFR " + m_stFtpRNFR );
		m_pCurlData->m_ftp_postquote = curl_slist_append(m_pCurlData->m_ftp_postquote, stCmd.c_str());

		stCmd = "RNTO " + m_stFtpRNTO;
		m_pCurlData->m_ftp_postquote = curl_slist_append(m_pCurlData->m_ftp_postquote, stCmd.c_str());
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_POSTQUOTE, m_pCurlData->m_ftp_postquote) );
	}

	SetupWriteStream();
	SetupReadStream();
	SetupHeaderStream();

	setHeadersIfNeeded();

	const CURLcode cc = CurlEasyPerformWrapper();

	if ( bCURLOPT_UPLOAD ) {
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_UPLOAD, 0) );
	}

	HandleCurlError(cc ,m_handle, "Failed to put data to the url " + string(m_pCurlData->getUrl()), m_pCurlData->get_error_buffer(), &m_vResponseHeaders);
}

void CurlTransferLibWrapper::setTimeOut(long lTimeOut_sec) {
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_TIMEOUT, lTimeOut_sec) );
}

void CurlTransferLibWrapper::addHttpRequestHeader(const std::string& stRequestHeader) {
	m_pCurlData->m_http_request_headers = curl_slist_append(m_pCurlData->m_http_request_headers, stRequestHeader.c_str());
}

void CurlTransferLibWrapper::isHttpPost(bool bIsPost) {
	m_bPost = bIsPost;
}

void CurlTransferLibWrapper::rfc1867FileUpload(const string& stName,
											   const string& stFileName,
											   const string& stContentType)
{
	m_bUseRfc1867 = true;
	m_stFormName = stName;
	m_stFormFileName = stFileName;
	m_stFormContentType = stContentType;
}

void CurlTransferLibWrapper::setNoBody(bool bNoBody) {
	HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_NOBODY, bNoBody ? 1 : 0), 0, "Failed to set option CURLOPT_NOBODY.");
}

void CurlTransferLibWrapper::setExplicitHttpVerb(const std::string& stVerb) {
	m_stOverrideHttpVerb = stVerb;
}

void CurlTransferLibWrapper::requestAllSupportedEncodings() {
	char* pc = konga::strsave("");
	m_pCurlData->addCharArrayToDelete(pc);
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_ENCODING, pc), m_handle, "Failed to set the Accept-Encoding header to accept all supported encodings." );
}

void CurlTransferLibWrapper::requestSpecificEncoding(const std::string& encoding) {
	char* pc = konga::strsave(encoding.c_str());
	m_pCurlData->addCharArrayToDelete(pc);
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_ENCODING, pc), m_handle, "Failed to set the Accept-Encoding header to accept the \"" + encoding + "\" encoding." );
}

void CurlTransferLibWrapper::enableHttpChunkedTransferEncoding(bool bEnable) {
	m_bUseHttpChunkedTransferEncoding = bEnable;
}

void CurlTransferLibWrapper::enableExpect100Continue(bool bEnable) {
	m_bEnableExpect100Continue = bEnable;
}

void CurlTransferLibWrapper::overrideContentType(const std::string& contentType) {
	this->addHttpRequestHeader("Content-Type: " + contentType);
}

vstrings CurlTransferLibWrapper::getResponseHeaders() const {
	return m_vResponseHeaders;
}

string CurlTransferLibWrapper::getFtpDirectoryListing(bool bNlstOnly) {
	                                                                                                   
	m_pCurlData->m_stResult.clear();
	setResponseString(m_pCurlData->m_stResult);
	SetupWriteStream();
	SetupHeaderStream();

	if ( bNlstOnly ) {
		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_DIRLISTONLY, 1), m_handle, "Set directory list only option (FTP NLST)");
	}

	const CURLcode cc = CurlEasyPerformWrapper();
	HandleCurlError(cc , m_handle, "Failed to get ftp directory list for url " + m_pCurlData->getUrl() + ".", m_pCurlData->get_error_buffer(), &m_vResponseHeaders);
	if ( bNlstOnly ) {
		                                                                          
		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_DIRLISTONLY, 0), m_handle, "Reset directory list only option (FTP NLST)");
	}
	return m_pCurlData->m_stResult;
}

namespace {
	                                                             
	                                   
	void split_url(const string& url, string& server_part, string& path_part) {

		                                
		string::size_type ipos = url.find("://");
		if ( ipos == string::npos )
			throw CrException("The URL '" + url + "' is not valid.");
		if ( ipos+3 >= url.size() )
			throw CrException("The URL '" + url + "' is not valid.");

		ipos = url.find("/", ipos + 3);
		if (ipos == string::npos) {
			server_part = url;
			path_part.clear();
			return;
		}

		server_part = url.substr(0, ipos);
		path_part = url.substr(ipos);
		if ( *(path_part.end()-1) == '/' )
			path_part.erase(path_part.end()-1);
	}

	  
                                  
                           
                                     
                     
    
   
  
   
}

void CurlTransferLibWrapper::ftpRename(const string& stOldFileName_in, const string& stNewFileName_in) {

	const string stOldFileName( TrimString(TrimString(stOldFileName_in), '/') );
	const string stNewFileName( TrimString(TrimString(stNewFileName_in), '/') );

	m_pCurlData->clear_slists();
	if ( isFtp() ) {
		string stCmd( "RNFR " + stOldFileName );
		m_pCurlData->m_ftp_postquote = curl_slist_append(m_pCurlData->m_ftp_postquote, stCmd.c_str());

		stCmd = "RNTO " + stNewFileName;
		m_pCurlData->m_ftp_postquote = curl_slist_append(m_pCurlData->m_ftp_postquote, stCmd.c_str());
	}
	else if ( isSftp() ) {
		                                                                                     
		                                              
		string url(m_pCurlData->getUrl()), server, path;
		split_url(url, server, path);

		string stCmd( "rename \"" + path + "/" + stOldFileName + "\" \""
			+ path + "/" + stNewFileName + "\"");
		m_pCurlData->m_ftp_postquote = curl_slist_append(m_pCurlData->m_ftp_postquote, stCmd.c_str());
	}
	else {
		throw CrException("Rename has not been implemented for the protocol of this URL: "
			+ m_pCurlData->getUrl());
	}
	HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_POSTQUOTE, m_pCurlData->m_ftp_postquote) );

	                                                  
	m_pCurlData->m_stResult.clear();
	SetupWriteStream(true);
	SetupHeaderStream();

	                                                                               
	                                                                                      
	HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_DIRLISTONLY, 1), m_handle);

	const CURLcode cc = CurlEasyPerformWrapper();
	HandleCurlError(cc, m_handle, "Ftp rename failed.", m_pCurlData->get_error_buffer(), &m_vResponseHeaders);

	                                                 
	HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_DIRLISTONLY, 0), m_handle);
}

void CurlTransferLibWrapper::Delete(const std::string& stFileName) {

	const bool bIsFtp = isFtp();
	if ( bIsFtp || isSftp() ) {
		m_pCurlData->clear_slists();

		string stCmd;
		if ( bIsFtp ) {
			stCmd = "DELE " + stFileName;
		}
		else {
			                          
			string url(m_pCurlData->getUrl()), server, path;
			split_url(url, server, path);
			stCmd = "rm \"" + path + "/" + stFileName + "\"";
		}
		m_pCurlData->m_ftp_postquote = curl_slist_append(m_pCurlData->m_ftp_postquote, stCmd.c_str());
		HandleCurlError( curl_easy_setopt(m_handle, CURLOPT_POSTQUOTE, m_pCurlData->m_ftp_postquote) );

		                                                  
		m_pCurlData->m_stResult.clear();
		SetupWriteStream(true);
		SetupHeaderStream();

		                                                                               
		                                                                                      
		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_DIRLISTONLY, 1), m_handle);

		const CURLcode cc = CurlEasyPerformWrapper();
		HandleCurlError(cc, m_handle, "Delete failed.", m_pCurlData->get_error_buffer(), &m_vResponseHeaders);

		                                                 
		HandleCurlError(curl_easy_setopt(m_handle, CURLOPT_DIRLISTONLY, 0), m_handle);
	}
	else {
		throw CrException("Delete has not been implemented for the protocol of the URL '" + m_pCurlData->getUrl() + "'.");
	}
}

bool CurlTransferLibWrapper::isFtp() {
	return isFtpUrl(m_pCurlData->getUrl().c_str());
}

bool CurlTransferLibWrapper::isSftp() {
	return isSftpUrl(m_pCurlData->getUrl().c_str());
}

bool CurlTransferLibWrapper::isHttp() {
	return isHttpUrl(m_pCurlData->getUrl().c_str());
}

void CurlTransferLibWrapper::setDebugFile(FILE* f) {
	m_DebugFile = f;
}
