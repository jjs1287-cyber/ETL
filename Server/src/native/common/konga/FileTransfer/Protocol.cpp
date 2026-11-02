/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                                  

#include <konga/Utils/StringUtils.h>

#include "Protocol.h"

using namespace konga;
using std::string;

                                                 
                                    
long Protocol::PutResponseType(long type)
{
	switch(type)
	{
	case 0:
#ifndef _UNICODE
		m_Conversion = UTOC;
#endif
		m_Type = 0;
		break;

	case 1:
#ifdef _UNICODE
		m_Conversion = CTOU;
#endif
		m_Type = 1;
		break;

	default:
		return -1;
	}

	return 0;
}

void Protocol::AddError(const std::string& stError_in) const {
	                                    
	string stError(TrimString(stError_in, ' '));
	stError = TrimString(stError, '\n');
	if ( stError.empty() )
		return;

	if ( m_stLastError.find(stError) != std::string::npos )
		return;

	                                 
	if ( !m_stLastError.empty() )
		m_stLastError += "\n";

	m_stLastError += stError;
}

std::string Protocol::GetLastError() const {
	return m_stLastError;
}

                                                     
                             
   

                                                                    
                                      
   

                                                    
                                                                    
   

      

                                                                                   
 
             
                            
                                    
     
                       
                                
                                   
     

                     
                            
     
                                                   
                                                    
                                                                                  
     
         
     
                                                                                  
                      
     
                        
    
                      
               
 
      
