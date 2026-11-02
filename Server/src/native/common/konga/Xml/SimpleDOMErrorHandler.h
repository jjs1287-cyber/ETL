/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef EW_SIMPLE_DOMERROR_HANDLER_H_CONG
#define EW_SIMPLE_DOMERROR_HANDLER_H_CONG

#include <string>

#include <xercesc/dom/DOMErrorHandler.hpp>

namespace konga {
    namespace xml {
    	class SimpleDOMErrorHandler :
    		public XERCES_CPP_NAMESPACE::DOMErrorHandler
    	{
    	public:
    		SimpleDOMErrorHandler(void);
    		~SimpleDOMErrorHandler(void);
    
    		bool handleError(const XERCES_CPP_NAMESPACE::DOMError& domError);
    		void resetErrors();
    		bool sawErrors() const;
    		bool sawWarnings() const;
    
    		                                                                                    
    		void treatWarningsAsErrors(bool bWerror);
    
    		                                                                                                         
    		void ignoreErrors(bool bIgnoreErrors);
    
    		std::string getErrors() const;
    		std::string getWarnings() const;
    
    	private :
    		                                              
    		SimpleDOMErrorHandler(const XERCES_CPP_NAMESPACE::DOMErrorHandler&);
    		void operator=(const XERCES_CPP_NAMESPACE::DOMErrorHandler&);
    
    		bool m_bWerror;
    		bool m_bIgnoreErrors;
    		bool m_bSawWarnings;
    		bool m_bSawErrors;
    		std::string m_stErrors;
    		std::string m_stWarnings;
    	};
    }
}
#endif                                     

