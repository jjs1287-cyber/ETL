/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef BINARY_DATA_FILE_IO_H
#define BINARY_DATA_FILE_IO_H

#include <string>
#include "BinaryData.h"

namespace konga {

                                                                                            
                                         
                      
void ReadBinaryDataFromFile(BinaryData& Data, const std::string& stFileName, bool bAppend = false);

                               
                      
void WriteBinaryDataToFile(const BinaryData& Data, const std::string& stFileName, bool bAppend = false);

}                   

#endif                         
