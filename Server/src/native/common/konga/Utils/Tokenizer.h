/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_TOKENIZER_H
#define KONGA_UTILS_TOKENIZER_H

namespace konga {

	                                                       
	class Tokenizer {
	public:
		Tokenizer(void) {}
		virtual ~Tokenizer(void) {}

		virtual bool NextToken() = 0;
		virtual bool StepBack() = 0;

		virtual const char* peek_next_token() = 0;
		virtual const char* token() const = 0;

		virtual void SpanTo(char c, bool useBackSlashEscape=true) = 0;
		virtual void spanTo_CSV_String() = 0;

		virtual size_t getNextPos() = 0;
		virtual void setNextPos(size_t next_position) = 0;

		virtual size_t getLineNumber() = 0;
	};
}

#endif                          
