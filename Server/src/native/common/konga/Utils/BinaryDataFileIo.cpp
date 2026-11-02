/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <boost/scoped_array.hpp>

#ifndef WIN32
	#include <konga/Utils/PortDefs.h>	                   
#else
#endif

#include "BinaryDataFileIo.h"

#include "ErrorResolver.h"
#include "AutoFileCleanup.h"
#include "CrException.h"


void konga::ReadBinaryDataFromFile(BinaryData& Data, const std::string& stFileName, bool bAppend           )
{
	FILE* file;
	file = ::fopen(stFileName.c_str(), "rb");
	if ( !file )
	{
		long lErr = ::GetLastError();
		std::string stLog = "Failed to open the file \"" + stFileName + "\" for reading. Reason:\n" +
			ErrorResolver().Resolve(lErr);
		throw CrException(lErr, stLog);
	}
	AutoFileHandle adopt_file(file);

	const int FILEBUF_SIZE = 524288;
	boost::scoped_array<char> adopt_buf(new char[FILEBUF_SIZE]);
	char* buf = adopt_buf.get();

	int iRead = 0;
	if ( !bAppend )
		Data.Size(0);
	while(true)
	{
		iRead = fread(buf, 1, FILEBUF_SIZE, file);
		if ( iRead != FILEBUF_SIZE )
		{
			if ( feof(file) )
			{
				Data.append(buf, iRead);
				break;
			}
			else
			{
				long lErr = ::GetLastError();
				std::string stLog = "Failed to read binary data from the opended file \"" + stFileName + "\". Reason:\n" + 
					ErrorResolver().Resolve(lErr);
				throw CrException(lErr, stLog);
			}
		}
		else
			Data.append(buf, FILEBUF_SIZE);
	}
}

void konga::WriteBinaryDataToFile(const BinaryData& Data, const std::string& stFileName, bool bAppend           )
{
	FILE* file = 0;
	file = fopen(stFileName.c_str(), bAppend?"ab+":"wb+");
	if ( !file )
	{
		long lErr = ::GetLastError();
		std::string stLog = "Failed to open the file \"" + stFileName + "\" for writing. Reason:\n" + 
			ErrorResolver().Resolve(lErr);
		throw CrException(lErr, stLog);
	}
	AutoFileHandle adopt_file(file);

	if ( !Data.empty() )
	{
		if ( !fwrite(Data.GetPointerToData(), Data.Size(), 1, file) )
		{
			long lErr = ::GetLastError();
			std::string stLog = "Failed to write data to the opened file \"" + stFileName + "\". Reason:\n" +
				ErrorResolver().Resolve(lErr);
			throw CrException(lErr, stLog);
		}
	}
}
