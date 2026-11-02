/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <zlib.h>

#include <sstream>		               
#include <fstream>
#include <cassert>

#include <konga/Utils/AutoPtrCleanup.h>		                  
#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>			              
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/ErrorResolver.h>

#ifndef WIN32
#include <konga/Utils/PortDefs.h>			                                           
#endif

#include <konga/Crypto/Crypto.h>			               

#include "ZipUtils.h"

using namespace konga;
using namespace std;

namespace {

	                                                                
	                                                                                   
	                                                      
	unsigned char* TryDeflate(const unsigned char* pchInflated, unsigned long lenInflated,
		unsigned long targetBufSize, unsigned long& lenDeflatedRef)
	{
		                                                              
		AutoArrayDelete<unsigned char> pch( new unsigned char[targetBufSize] );
		if ( pch.Get() == 0 ) {
			std::stringstream ssErr;
			ssErr << "Not enough memory to allocate " << targetBufSize << " bytes to hold deflated data.";
			throw CrException(ssErr.str());
		}

		unsigned long lenDeflated = targetBufSize;
		int zRetVal = compress(pch.Get(), &lenDeflated, pchInflated, lenInflated);
		if ( zRetVal == Z_OK ) {
			                                                
			lenDeflatedRef = lenDeflated;
			return pch.Release();	                                   
		}
		else if ( zRetVal == Z_BUF_ERROR ) {
			                                                                      
			                                                 
			pch.Reset(0);
			return TryDeflate(pchInflated, lenInflated, static_cast<unsigned long>(1.5*targetBufSize), lenDeflatedRef);
		}
		else if ( zRetVal == Z_MEM_ERROR ) {
			std::stringstream ssErr;
			ssErr << "Not enough memory available for deflating buffer of size "
				<< lenInflated << ".";
			throw CrException(ssErr.str());
		}
		else {
			std::stringstream ssErr;
			ssErr << "Undocumented error (" << zRetVal << ") occurred when calling zlib method compress.";
			throw CrException(ssErr.str());
		}
	}

	unsigned char* TryInflate(const unsigned char* pchDeflated, unsigned long lenDeflated,
		unsigned long targetBufSize, unsigned long& lenInflatedRef)
	{
		                                                                 
		AutoArrayDelete<unsigned char> pch( new unsigned char[targetBufSize] );
		if ( pch.Get() == 0 ) {
			std::stringstream ssErr;
			ssErr << "Not enough memory to allocate " << targetBufSize << " bytes to hold inflated data.";
			throw CrException(ssErr.str());
		}

		unsigned long lenInflated = targetBufSize;
		int zRetVal = uncompress(pch.Get(), &lenInflated, pchDeflated, lenDeflated);
		if ( zRetVal == Z_OK ) {
			                                                
			lenInflatedRef = lenInflated;
			return pch.Release();	                                   
		}
		else if ( zRetVal == Z_BUF_ERROR ) {
			                                                                                
			                                        
			pch.Reset(0);
			return TryInflate(pchDeflated, lenDeflated, static_cast<unsigned long>(2.0*targetBufSize), lenInflatedRef);
		}
		else if ( zRetVal == Z_MEM_ERROR ) {
			std::stringstream ssErr;
			ssErr << "Not enough memory available for inflating buffer of size "
				<< lenDeflated << ".";
			throw CrException(ssErr.str());
		}
		else if ( zRetVal == Z_DATA_ERROR ) {
			throw CrException("Inflation error: The input data is corrupted or incomplete.");
		}
		else {
			std::stringstream ssErr;
			ssErr << "Undocumented error (" << zRetVal << ") occurred when calling zlib method uncompress.";
			throw CrException(ssErr.str());
		}
	}
}

unsigned char* ZipUtils::Deflate(const unsigned char* pchInflated, unsigned long lenInflated,
								 unsigned long& lenDeflated)
{
	                                                                     
	unsigned long initialBufSize = compressBound(lenInflated);
	return TryDeflate(pchInflated, lenInflated, initialBufSize, lenDeflated);
}


unsigned char* ZipUtils::Inflate(const unsigned char* pchDeflated, unsigned long lenDeflated,
								 unsigned long& lenInflated)
{
	                                      
	                                                                           
	unsigned long initialSize = (lenInflated > 0 ) ? lenInflated : 2*lenDeflated;
	return TryInflate(pchDeflated, lenDeflated, initialSize, lenInflated);
}

string ZipUtils::DecodeUnzipAndWriteToFile(const std::string& stFileName, const char* pchZippedBase64EncodedString, bool bCalculateHash) {
	assert(pchZippedBase64EncodedString);
	if ( !pchZippedBase64EncodedString )
		return "";

	                      
	int lenZipped = 0;
	AutoArrayDelete<unsigned char> compressed( DecodeBase64(pchZippedBase64EncodedString, lenZipped) );
	if ( compressed.Get() == 0 )
		return "";

	        
	unsigned long lenDeflated = lenZipped, lenInflated = 0;
	AutoArrayDelete<unsigned char> inflated( ZipUtils::Inflate(compressed.Get(), lenDeflated, lenInflated) );
	if ( inflated.Get() == 0 )
		return "";

	                                                           
	compressed.Reset(0);

	                                                        
	std::ofstream f;
	try {
		f.open(stFileName.c_str(), ofstream::out | ofstream::trunc | ofstream::binary);
		f.write(reinterpret_cast<char*>(inflated.Get()), lenInflated);
		f.close();
	}
	catch( const std::exception& ex ) {
		CrException("Failed to write to file \"" + stFileName + "\". " + ex.what());
	}

	return bCalculateHash ? md5( reinterpret_cast<char*>(inflated.Get()), lenInflated ) : string();
}

string ZipUtils::ZipAndBase64Encode(const unsigned char* pchInflated, unsigned long lenInflated) {
	unsigned long lenDeflated = 0;
	AutoArrayDelete<unsigned char> pchDeflated( Deflate(pchInflated, lenInflated, lenDeflated) );
	return EncodeBase64(pchDeflated.Get(), lenDeflated);
}

string ZipUtils::ZipAndBase64Encode(const string& stData) {
	const unsigned long lenInflated = static_cast<unsigned long>(stData.size());
	const unsigned char* pchInflated = reinterpret_cast<const unsigned char*>(stData.c_str());
	return ZipAndBase64Encode(pchInflated, lenInflated);
}

                                                                                           
string ZipUtils::ZipAndBase64EncodeFile(const std::string& stFileName, char*& pcZippedBase64EncodedString, bool bCalculateHash) {

	long lBytesRead = 0;
	string sErr;

	AutoArrayDelete<char> pcFileContents( read_file(stFileName, lBytesRead, sErr) );
	if( !sErr.empty() )
		throw CrException("Failed to zip the file '" + stFileName + "'. Reason: " + sErr);

	string sMd5( bCalculateHash ? md5(pcFileContents.Get(), lBytesRead) : "");

	                          
	unsigned long lenInflated = lBytesRead, lenDeflated = 0;
	AutoArrayDelete<unsigned char> deflated( ZipUtils::Deflate(reinterpret_cast<unsigned char*>(pcFileContents.Get()), lenInflated, lenDeflated) );
	if ( deflated.Get() == 0 )
		return "";

	                                                         
	pcFileContents.Reset(0);

	                                     
	string sBase64Encoded( EncodeBase64(deflated.Get(), lenDeflated) );

	                                                         
	deflated.Reset(0);

	pcZippedBase64EncodedString = strnewdup(sBase64Encoded.c_str());
	return sMd5;
}

namespace {
	class AutoGZFileHandle : boost::noncopyable
	{
	public:
		AutoGZFileHandle(gzFile& h) : m_FILE(h) { }
		~AutoGZFileHandle(void) {
			Close();
		}

		void Close() {
			if ( m_FILE ) {
				gzclose(m_FILE);
				m_FILE = 0;
			}
		}

	private:
		gzFile& m_FILE;
	};
}

void konga::ZipUtils::GzipFile(const std::string& stFileName) {
	FILE* f = fopen(stFileName.c_str(), "rb");
	if ( f == 0 ) {
		const long lErr = GetLastError();
		throw CrException("Failed to open source file for compression '"
			+ stFileName + "'. Reason: " + ErrorResolver(lErr).Resolve(lErr));
	}
	AutoFileHandle adopt_f(f);

	string stGZFileName( stFileName + ".gz" );
	
	gzFile gf = gzopen(stGZFileName.c_str(), "wb");
	if ( gf == 0 ) {
		const long lErr = GetLastError();
		throw CrException("Failed to open target file to compress to '"
			+ stGZFileName + "'. Reason: " + ErrorResolver(0).Resolve(lErr));
	}
	AutoGZFileHandle adopt_gf(gf);

	const size_t BUF_SIZE = 1024;
	char buf[BUF_SIZE];
	size_t bytesRead;
	while( (bytesRead = fread(buf, 1, BUF_SIZE, f)) != 0 ) {
		if ( gzwrite(gf, buf, bytesRead) == 0 ) {
		const long lErr = GetLastError();
		throw CrException("Failed to compress buffer. Reason: " + ErrorResolver(0).Resolve(lErr));
		}
	}
}

