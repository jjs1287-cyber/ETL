/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_DATA_STRUCTURE_H
#define KONGA_OPERATION_DATA_STRUCTURE_H

namespace konga {

	enum DataStructureNature {
		NATURE_NONE		= 0,
		NATURE_FLAT		= 1,
		NATURE_TREE		= 2
	};

	class DataStructure {
	protected:
		DataStructure(void) { }
	public:
		virtual ~DataStructure(void) { }

		virtual DataStructureNature getNature() const {	                                                 
			return NATURE_NONE;
		}
	};
}
#endif                                    

