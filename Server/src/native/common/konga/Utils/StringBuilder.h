/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_STRING_BUFFER_H
#define KONGA_STRING_BUFFER_H

#include <string>
#include "KString.h"

namespace konga {
	class StringBuilder {
	public:
		StringBuilder(size_t length=0);
		~StringBuilder();
		StringBuilder& append(char c);
		StringBuilder& append(const std::string& s);
		StringBuilder& append(const char*s);
		StringBuilder& append(const KString& s);
		StringBuilder& append(int i);
		StringBuilder& append(long l);
		StringBuilder& append(long long ll);
		StringBuilder& append(double ff);
		StringBuilder& append(float f);
		StringBuilder& append(size_t k);
		std::string toString();
		size_t size();
		char* getCurrentPointer();
		void write(FILE*);
		void reset();
	private:
		void expand(size_t add);
		char*buf;
		size_t length;
		size_t pos;
	};
}
#endif                         

