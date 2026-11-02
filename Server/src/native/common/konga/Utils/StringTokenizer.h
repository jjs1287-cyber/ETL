/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef KONGA_STRING_TOKENIZER_H
#define KONGA_STRING_TOKENIZER_H

#include <string>

#include "Tokenizer.h"

namespace konga {

	class StringTokenizer : public Tokenizer {
public:
	StringTokenizer(const char*str, const char*terminators);
	StringTokenizer(const char*str, int len, const char*terminators);
	~StringTokenizer();

	bool NextToken();
	bool StepBack();

	const char*peek_next_token();
	const char*getStringAtCurrentPosition();

	void Reset(){m_k=0;}

	const char*token() const { return m_token; }
	const char*getString() const {return m_str;}

	void SpanTo(char c, bool useBackSlashEscape=true);
	void spanTo_CSV_String();
	size_t getNextPos(){return m_k;}
	void setNextPos(size_t next_position){m_k=next_position;}
	size_t getLineNumber(){return 0;}

private:
	const char* m_str,*m_terminators;
	int m_k, m_length;                                               
	                                                                             
	                                                                                       
	char*m_token;
	int m_nmax;
	char m_c[2];
	std::string m_peek_next_token;
};

}                   

#endif                            

