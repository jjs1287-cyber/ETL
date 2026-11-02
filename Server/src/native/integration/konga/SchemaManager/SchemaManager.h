/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef SCHEMA_MANAGER_H
#define SCHEMA_MANAGER_H

#include <vector>
#include <string>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

namespace TranDb {
	class XmlDtdChildrenView;
}

namespace konga {

class DbConnection;
class AutoDeleteFileSet;

class SchemaManager : public DbConnectionHolder
{
	public:
		SchemaManager(void);
		explicit SchemaManager(DbConnection* pDbConnection) :
			DbConnectionHolder(pDbConnection) {}

		~SchemaManager(void) {};

		                                                                  
		                                             
		std::vector<std::string> getSourceDtds(int tranId);
		std::vector<std::string> getTargetDtds(int tranId);

		                                                              
		void copyToDir(AutoDeleteFileSet& set,const std::string& sDir);

		                                                                               
		void copyRequiredSourceSchemasToDir(const std::string& sTmpDir,
			int tranId, int structureType);
		void copyRequiredTargetSchemasToDir(const std::string& sTmpDir,
			int tranId, int structureType);

		                                                                               
		                                            
		std::string copyRequiredFilesForWsdl(long lWsdlId, const std::string& sTmpDir);

	private:
		std::string copyRequiredFilesForWsdl(long lWsdlId, const std::string& sTmpDir,
			std::vector<long>& vAlreadyHandledWsdlIds);

		void loadDependentXsd(int iXmlId, TranDb::XmlDtdChildrenView& xmlDtdChildrenView);
		bool alreadyProcessed(int iXmlId);
		void addXsd(int iXmlId, const std::string& sXsdFile);

		                                                                                              
		template<typename T>
		std::vector<std::string> getDtds(int tranId, T& DtdView);

		void copyRequiredSchemasToDir(const std::string& sTmpDir,
			int tranId, int structureType, bool isSource);

	private:
		std::vector<std::string> m_vSchemaPaths;
		std::vector<int> m_vXmlId;
	};
}

#endif                            

