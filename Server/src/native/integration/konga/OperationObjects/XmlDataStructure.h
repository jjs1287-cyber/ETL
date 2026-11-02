/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_XML_DATA_STRUCTURE_H
#define KONGA_OPERATION_XML_DATA_STRUCTURE_H

#include <string>

#include <boost/noncopyable.hpp>

#include "DataStructure.h"

namespace konga {

	class AutoDeleteFileSet;
	class SchemaManager;
	class DbConnection;

	class XmlDataStructure : public DataStructure, private boost::noncopyable {

	protected:
		XmlDataStructure(void) : m_lTransformationId(0), m_pAutoDeleteDtds(0) { }

	public:
		virtual ~XmlDataStructure(void);

		friend class LocationCreatorDb;

	protected:
		std::string getSchemaFileName() const;

		                                            
		void CopySourceSchemaFilesToTmpDir(const std::string& stTmpDir, DbConnection* pDbConn) const;

		                                            
		void CopyTargetSchemaFilesToTmpDir(const std::string& stTmpDir, DbConnection* pDbConn) const;

	private:
		void CopySchemaFilesToTmpDir(const std::string& stTmpDir, SchemaManager& schemaMgr) const;
		void CopySchemaFilesToTmpDirOld(const std::string& stTmpDir, const std::string& stSourceOrTarget) const;
		void ClearFileSet() const;

	private:
		long m_lTransformationId;
		std::string m_stSchema;
		std::string m_stRoot;

		                           
		mutable AutoDeleteFileSet* m_pAutoDeleteDtds;
	};
}
#endif                                        

