/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_SOURCE_DATABASE_DATA_STRUCTURE_H
#define KONGA_OPERATION_SOURCE_DATABASE_DATA_STRUCTURE_H

#include <boost/noncopyable.hpp>

#include "SourceDataStructure.h"
#include "DatabaseDataStructure.h"
#include "DatabaseSourceLocation.h"

namespace konga {

	class SourceDatabaseDataStructure : public DatabaseDataStructure, public SourceDataStructure, private boost::noncopyable {
	private:
		SourceDatabaseDataStructure(void) { }
	public:
		virtual ~SourceDatabaseDataStructure(void) {}

		DatabaseSourceLocation* getSourceLocation() const {
			return m_pDatabaseSourceLocation.get();
		}

		void doPreTransformation(const OperationEngineContext& context) const {
			                 
		}

		friend class LocationCreatorDb;

	private:
		std::auto_ptr<DatabaseSourceLocation> m_pDatabaseSourceLocation;
		std::string m_stWhere;
		std::string m_stDiffKeys;
	};
}

#endif                                                    

