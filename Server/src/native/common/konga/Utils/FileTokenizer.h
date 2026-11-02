/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef KONGA_FILE_TOKENIZER_H
#define KONGA_FILE_TOKENIZER_H

#include "Tokenizer.h"
#include "FileContents.h"

#include <string>

namespace konga {

	class FileTokenizer : public Tokenizer {
	public:
		FileTokenizer(FileContents* fc, const char*terminators);
		FileTokenizer(FileContents* fc, size_t begin_pos, size_t end_pos, const char*terminators);
		~FileTokenizer();

		bool NextToken();
		bool StepBack();

		size_t getNextPos(){return m_k;}
		void setNextPos(size_t next_position){m_k=next_position;}

		const char*peek_next_token();
		const char*token() const {
			return m_token;
		}

		                                               
		void SpanTo(char c, bool useBackSlashEscape=true);
		void spanTo_CSV_String();
		size_t getLineNumber(){return fc->getLineNumber(m_k);}

	private:
		void init();

	private:
		FileContents* fc;
		const char *m_terminators;
		size_t m_k, m_end_pos;                                               
		                                                                             
		                                                                                       
		size_t m_nmax;
		char*m_token;
		char m_span_terminator[2];
		std::string m_peek_next_token;
	};

}                   
#endif                          

