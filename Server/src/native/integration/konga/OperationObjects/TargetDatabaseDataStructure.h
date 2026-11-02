/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_TARGET_DATABASE_DATA_STRUCTURE_H
#define KONGA_OPERATION_TARGET_DATABASE_DATA_STRUCTURE_H

#include <boost/noncopyable.hpp>

#include "DatabaseDataStructure.h"
#include "TargetDataStructure.h"
#include "DatabaseTargetLocation.h"

namespace konga {

	class TargetDatabaseDataStructure : public DatabaseDataStructure, public TargetDataStructure, private boost::noncopyable {
	private:
		TargetDatabaseDataStructure(void) : m_iInsertMode(1) { }
	public:
		~TargetDatabaseDataStructure(void) { }

		DatabaseTargetLocation* getTargetLocation() const {
			return m_pDatabaseTargetLocation.get();
		}

		void doPreTransformation(const OperationEngineContext& OperationEngineContext) const {
			                 
		}

		friend class LocationCreatorDb;

	private:
		short m_iInsertMode;
		std::auto_ptr<DatabaseTargetLocation> m_pDatabaseTargetLocation;
		std::string m_stUpdateFields;
		std::string m_stUpdateKey;
	};
}

#endif                                                    

