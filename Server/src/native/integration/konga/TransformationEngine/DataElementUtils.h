/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRANSFORMATION_ENGINE_DATA_ELEMENT_UTILS_H
#define KONGA_TRANSFORMATION_ENGINE_DATA_ELEMENT_UTILS_H

#include <string>

#include <konga/DataElement/DataElement.h>

class CROMData;
class CROM;

namespace konga {
	class Utf8Conv;
}

int GetNumEscapedChars(const std::string& s);
std::string TrimAndRestoreEscapedChars(const std::string&s0, const char qual);
int GetNumCharsToEscape(const std::string& s, const char qual);
bool RequireEscape(const std::string&s0, const char qual);
std::string EscapeString(const std::string& s0, const char qual);
std::string readDelimitedText(const char*buf, int&k, konga::DataElement::Type type, char str_qual, char num_qual, char delimiter, bool&bIsNull, int&err);
std::string readFixedFieldText(const char*buf, const int len, int pos_begin, int pos_len, bool& bIsNull, int&err);
void DataElementRead(konga::DataElement& de, FILE* in, char* buf, const int buf_len, int&len, int& k, char str_qual, char num_qual, char delimiter, bool isDatabase, bool bTrim, std::string*pRecord=0, konga::Utf8Conv* conv = 0);
void CromDataRead(CROMData* de, FILE* in, char* buf, const int buf_len, int&len, int& k,char str_qual, char num_qual, char delimiter);

#endif                                                    
