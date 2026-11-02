/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_ITEM_WARNING_H
#define KONGA_WS_DEPLOY_ITEM_WARNING_H

#include <string>

namespace konga {
namespace interchange_deploy {

	   
                                                              
                                                                                 
                    
    
	class ItemWarning {
	public:
		ItemWarning(int iEntityTypeId, const std::string& stGuid, const std::string& stWarning) :
		  m_iEntityTypeId(iEntityTypeId), m_stGuid(stGuid), m_stWarning(stWarning)
		  {}

		virtual ~ItemWarning(void) {}

		int GetEntityTypeId() const {
			return m_iEntityTypeId;
		}

		const std::string& GetGuid() const {
			return m_stGuid;
		}

		const std::string& GetWarning() const {
			return m_stWarning;
		}

	private:
		int m_iEntityTypeId;
		std::string m_stGuid;
		std::string m_stWarning;
	};
}
}

#endif                                  

