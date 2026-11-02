/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_TEXT_DATA_STRUCTURE_H
#define KONGA_OPERATION_TEXT_DATA_STRUCTURE_H

#include <string>

#include "DataStructure.h"

namespace konga {

	class TextDataStructure : public DataStructure {

	protected:
		TextDataStructure(void) { }
	public:
		virtual ~TextDataStructure(void) { }

		friend class LocationCreatorDb;

	private:
		std::string m_stTextDoc;
		std::string m_stStructureFile;
	};
}
#endif                                         

