/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_FILE_CONTENTS_H
#define KONGA_FILE_CONTENTS_H

#include <string>
#include <map>
#include "StringBuilder.h"


namespace konga {

	class FileContents {
	public:
		FileContents(const std::string& fileName);
		~FileContents();

		void init();

		size_t size();

		const std::string& getDicStr(size_t pos, size_t len);
		const std::string& getDicStr(const std::string& str);
		char at(size_t i);
		std::string getStr(size_t pos, size_t len);

		                            
		size_t findNextTokenLength(size_t begin_pos, size_t end_pos, const char* terminators);
		void fetchToken(char*token, size_t pos, size_t len);

		void open();
		void close();

		size_t findCsvEndOfField(size_t pos, char delimiter);
		size_t findNormalEndOfField(size_t pos, char qualifier, char delimiter);
		std::string readCsvData(size_t& pos, char delimiter, bool& bIsNull);
		std::string readNormalData(size_t& pos, char qualifier, char delimiter);
		const std::string& getFileName(){return fileName;}
		size_t getLineNumber(size_t pos);

	private:
		void initBuf();
		bool load(size_t i);
		bool findBuffer(size_t i);
		void appendEscapedChar(size_t& pos, konga::StringBuilder& sb);

	private:
		char*current_buf;
		long long current_offset, current_length, current_end_pos;
		long long* start_pos, *length;
		int current;
		char**buf;
		std::map<std::string, bool>dic;
		FILE*in;
		long long size0;
		const std::string fileName;
	};
}
#endif                        
