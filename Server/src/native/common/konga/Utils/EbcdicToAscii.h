/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef EBCDIC2ASCII_H
#define EBCDIC2ASCII_H

#include <string>

namespace konga {
	void EbcdicToAscii(const std::string& ebcdicFileName, const std::string& asciiFileName);
}
#endif                  
