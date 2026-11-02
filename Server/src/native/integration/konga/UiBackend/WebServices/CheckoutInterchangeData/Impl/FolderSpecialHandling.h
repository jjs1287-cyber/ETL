/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_FOLDER_ITEM_SPECIAL_HANDLING_H
#define KONGA_WS_CHECKOUT_FOLDER_ITEM_SPECIAL_HANDLING_H

#include "CheckoutSpecialHandling.h"

namespace konga {
	class GuidToIdMapper;

namespace interchange_checkout {

	                                                   
	class FolderSpecialHandling : public CheckoutSpecialHandling {
	public:
		FolderSpecialHandling(GuidToIdMapper& guidToIdMapper)
			: m_guidToIdMapper(guidToIdMapper), m_bIsItem(false),
			m_bHasInterchangeProject(true) {}

		virtual ~FolderSpecialHandling(void) {}

		virtual void HandleColumn(vColumnValuesT& vColumnValues);

		void SetIsItem(bool bIsItem) { m_bIsItem = bIsItem; }

	private:
		GuidToIdMapper& m_guidToIdMapper;
		bool m_bIsItem;
		bool m_bHasInterchangeProject;
	};

}
}

#endif                                                    

