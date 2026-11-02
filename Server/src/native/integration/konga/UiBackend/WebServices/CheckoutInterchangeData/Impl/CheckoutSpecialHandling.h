/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_SPECIAL_HANDLING_H
#define KONGA_WS_CHECKOUT_SPECIAL_HANDLING_H

#include <vector>
#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/NullableString.h>

namespace konga {
namespace interchange_checkout {

	                                                                                  
	typedef std::vector<konga::NullableString> vColumnValuesT;

	                                                                           
                                                                                
                                                      
    
	class CheckoutSpecialHandling {

	public:
		static NullableString& GetColumnValueRef(const std::string& stColumnName,
			const std::vector<std::string>& vColumnNames, vColumnValuesT& vColumnValues);

	public:
		CheckoutSpecialHandling(void) : m_pvColumnNames(0) {}
		virtual ~CheckoutSpecialHandling(void) {}

		                                                        
                                                         
     
		virtual void SetColumnNames(const std::vector<std::string>* pvColumnNames) {
			m_pvColumnNames = pvColumnNames;
		}

		   
                                                                                
                                                                                        
                  
     
		NullableString& GetColumnValueRef(const std::string& stColumnName, vColumnValuesT& vColumnValues);

		                                        
		                                                          
		virtual void HandleColumn(vColumnValuesT& vColumnValues) = 0;

	protected:
		const std::vector<std::string>& GetColumnNames() const {
			if ( m_pvColumnNames == 0 )
				throw CrException("InternalError: SetColumnNames() has not been called!");
			return *m_pvColumnNames;
		}

	private:
		const std::vector<std::string>* m_pvColumnNames;
	};
}
}

#endif                                        

