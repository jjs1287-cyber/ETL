/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <konga/Utils/Settings.h>
#include <konga/Utils/util.h>

#include "SoapEncoding.h"

using namespace std;
using namespace konga;

                    
namespace {
	const string OPTION_NAMES[] = { "SoapEncoding_v11", "SoapEncoding_v12" };
	const string SCHEMA_SECTION("Settings/XMLSchemas");
}

SoapEncoding::SoapEncodingVersion SoapEncoding::GetDefaultVersion() {
	return SoapEncoding_Version_11;
}

bool SoapEncoding::GetSchemaLocation(SoapEncodingVersion version, string& stSchemaLocation) {
	stSchemaLocation = getAbsolutePath( GetOptionAsString(SCHEMA_SECTION,
		OPTION_NAMES[static_cast<int>(version)], "DataInterchange/xml_dtd/Schemas/SOAP_Encoding_v11.xsd"), true );
	return true;
	  
             
          
                        
                            

                                                                  
                          
               

                                                                                  
                          
               

                                
   
}

