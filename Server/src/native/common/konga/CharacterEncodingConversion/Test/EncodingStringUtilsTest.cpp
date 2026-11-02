#include "EncodingStringUtilsTest.h"
#include <sstream>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <fstream>
                     
namespace konga {
EncodingStringUtilsTest::EncodingStringUtilsTest(void)
{
}

EncodingStringUtilsTest::~EncodingStringUtilsTest(void)
{
}


bool EncodingStringUtilsTest::Test_Utf8CharacterSize(const std::string& utf8string,const std::size_t originalsize)
{
	size_t calculatedsize=EncodingStringUtils::Utf8CharacterSize(utf8string);
	if (originalsize!=calculatedsize)
	{		
			std::stringstream error;
			error<<"File="<<__FILE__<<"Line="<<__LINE__<<std::endl;
			error<<"Unicode convert function or length calculation is wrong! Please fix it"<<std::endl;
			throw konga::CrException(error.str());
			return false;
	}
	return true;
}

  
                    
                                                                                      
 
                                                                        
                                                                  
                                          
                        
                        
  
                                      
                                                      
  
             
 
  

  
                    
                                                                                 
 
                                                                         
                                       
                                                                         
                                                                                 
                                                                          
                                                                         
                                    
                                                                                               
                                          
                  
 
  

}