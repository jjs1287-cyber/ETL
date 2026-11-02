/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_CSVUTILS_H
#define ETLFORGE_CSVUTILS_H

#include <string>

class KString;

namespace konga {

int get_length(char*buf);

   
                                                                                                        
                                                             
                                                                                 
    
int getCSVFieldText(const char*sBuffer,int iStart, std::string& text, char cDelimiter);

void CSV_escape(std::string& s);
void CSV_restore(std::string& s);

                                                                           
void qualifyCsvString(std::string& s, char delimiter, bool bForceQuote=false);
void qualifyCsvStringK(KString& s, char delimiter, bool bForceQuote=false);	                                                                                               

void qualifyCsvString(std::string& s, const char* delimiters, bool bForceQuote=false);

   
                                                   
                               
                                                             
                                                                                                                          
                                   
   
                                                                        
    
std::string readCsvData(FILE*in, char*buf, const int buf_len, int& len, int& pos, bool& bIsNall, const unsigned char cDelimiter, const char* nullValue, std::string*pRecord=0);

void writeCsvData(FILE*out,const char*data, char delimiter, bool qualified=false);

bool fileHasNullChars(const std::string& fileName);
bool removeNullCharsFromFile(const std::string& fileName);	
}

#endif                       
