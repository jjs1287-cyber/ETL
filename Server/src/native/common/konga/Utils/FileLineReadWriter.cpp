/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <stdio.h>
#include <string.h>

#include <konga/Utils/FileUtils.h>		                                 
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>		                        

#include "FileLineReadWriter.h"

using namespace konga;
using std::string;

FileLineReadWriter::FileLineReadWriter(const string& stTmpDir) : m_stTmpDir(stTmpDir) {
	if ( !m_stTmpDir.empty() ) {
		AppendPathSeparator(m_stTmpDir);
		if ( !IsDirectory(m_stTmpDir) )
			throw CrException("The specified temporary directory is not a directory: " + stTmpDir);
	}
}

FileLineReadWriter::~FileLineReadWriter(void) {
}

namespace {
	inline void WriteLineToFile(const char* s, FILE*& f) {
		if ( s ) {
			size_t sizeToWrite = strlen(s);
			if ( sizeToWrite > 0 ) {
				if ( fwrite(s, sizeToWrite, 1, f) < 1 ) {
					const int iErr = ferror(f);
					if ( iErr == 0 )
						throw CrException("Unexpected error. All the data was now written but no error reported (ferror returned 0)");
					else {
						throw CrException("Failed to write to file"
							". Reason: " + ErrorResolver().Resolve(iErr));
					}
				}
			}
		}
	}
}

int FileLineReadWriter::Read(const char* filename) {
	if ( filename == 0 )
		throw CrException("Failed to read from file, filename is null.");

	FILE* fin = fopen(filename, "rb");
	if( fin == 0 ) {
		const long lErr = ::GetLastError();
		throw CrException("Failed to open file '" + string(filename) +
			"'. Reason: " + ErrorResolver().Resolve(lErr));
	}
	AutoFileHandle adopt_fin(fin);
	rewind( fin );

	string stTmpFile;
	if ( m_stTmpDir.empty() )
		stTmpFile = konga::getTmpFileName();
	else
		stTmpFile = konga::getTmpFileName("", m_stTmpDir);

	FILE* fout = fopen(stTmpFile.c_str(), "wb+");
	if( fout == 0 ) {
		const long lErr = ::GetLastError();
		throw CrException("Failed to open temporary file '" + stTmpFile +
			"'. Reason: " + ErrorResolver().Resolve(lErr));
	}
	AutoDeleteFileSet auto_delete_tmp_file;
	auto_delete_tmp_file.AddFile(stTmpFile);
	AutoFileHandle adopt_fout(fout);                                                               

	const int MAX_LINE_LENGTH = 10000;
	char line[MAX_LINE_LENGTH];

	try {
		while( !feof( fin ) ) {
			if( !fgets(line, MAX_LINE_LENGTH, fin) )
				break;
			WriteLineToFile(ChangeLine(line), fout);
		}

		                                                                   
		const int iErr = ferror(fin);
		if ( iErr ) {
			throw CrException("An error occurred while reading the file '" + string(filename)
				+ "'. Reason: " + ErrorResolver().Resolve(iErr));
		}

		                               
		WriteLineToFile( ChangeLine(0), fout);
	}
	catch ( const AbortRead& abort) {
		return abort.GetReason();
	}

	                                 
	adopt_fin.Close();
	adopt_fout.Close();

	konga::kMoveFile(stTmpFile, filename, true);	                                             
	auto_delete_tmp_file.SetAutoDelete(false);		                                                                        
	return 0;
}

