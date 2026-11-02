/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBRELATIONS_TABLEKEYINFO_H
#define KONGA_DBRELATIONS_TABLEKEYINFO_H

#include <vector>

#include "KeyInfo.h"

namespace konga {

	class TableKeyInfo {
	public:
		TableKeyInfo(const std::vector<KeyInfo>& vKeyInfo)
			: m_vKeyInfo(vKeyInfo) {}
		~TableKeyInfo(void) {}

		const KeyInfo* GetPrimaryKey() const {
			std::vector<KeyInfo>::const_iterator cit = m_vKeyInfo.begin();
			for(; cit != m_vKeyInfo.end(); ++cit ) {
				if ( cit->IsPrimary() )
					return &(*cit);
			}
			return 0;
		}

		const std::vector<KeyInfo>& GetKeyInfo() const {
			return m_vKeyInfo;
		}

	private:
		std::vector<KeyInfo> m_vKeyInfo;
	};
}

#endif                                    

