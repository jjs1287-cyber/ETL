/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_ITEM_WARNING_HOLDER_H
#define KONGA_WS_DEPLOY_ITEM_WARNING_HOLDER_H

#include <vector>

#include "ItemWarning.h"

namespace konga {
namespace interchange_deploy {

	   
                                                                                  
                                                  
    
	class ItemWarningHolder {
	public:
		ItemWarningHolder(void) {}
		virtual ~ItemWarningHolder(void) {}

		bool HasWarnings() const {
			return !m_vItemWarnings.empty();
		}

		const std::vector<ItemWarning>& GetWarningsRef() const {
			return m_vItemWarnings;
		}

		void AddWarning(int iEntityType, const std::string& stGuid, const std::string& stWarning) const {
			AddWarning(ItemWarning(iEntityType, stGuid, stWarning));
		}

		void AddWarnings(const ItemWarningHolder& itemWarningHolder) const {
			std::vector<ItemWarning>::const_iterator cit = itemWarningHolder.m_vItemWarnings.begin();
			for(; cit != itemWarningHolder.m_vItemWarnings.end(); ++cit ) {
				AddWarning(*cit);
			}
		}

	protected:
		void ClearWarnings() const {
			m_vItemWarnings.clear();
		}

		void AddWarning(const ItemWarning& itemWarning) const {
			m_vItemWarnings.push_back(itemWarning);
		}

	private:
		                                                                                   
		                                  
		mutable std::vector<ItemWarning> m_vItemWarnings;
	};
}
}

#endif                                         

