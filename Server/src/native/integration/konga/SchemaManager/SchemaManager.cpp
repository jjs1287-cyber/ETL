/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <map>
#include <cassert>
#include <algorithm>

#include <konga/Utils/util.h>		                  
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FilesTraverser.h>
#include <konga/Utils/DBConnectionInfo.h>

#ifdef WIN32
#include <windows.h>
#endif
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/TranDb/SourceDtdView.h>
#include <konga/Db/TranDb/TargetDtdView.h>
#include <konga/Db/TranDb/XmlDtdChildrenView.h>
#include <konga/Db/TranDb/WsdlTab.h>

#include <konga/Db/TranDb/TRANSFORMATIONSTAB_V.h>
#include <konga/DbRelations/TransformationTypeIdConstants.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include "SchemaManager.h"

using namespace konga;
using namespace std;
using TranDb::XmlDtdChildrenView;
using TranDb::SourceDtdView;
using TranDb::TargetDtdView;


SchemaManager::SchemaManager(void) : DbConnectionHolder("") {
	this->SetInternalDbName("TranDb");
}

vector<string> konga::SchemaManager::getSourceDtds(int tranId)
{
	SmartDbPtr<SourceDtdView> view( SourceDtdView::MakeInstance(&GetDbConnection()) );
	return getDtds<SourceDtdView>(tranId, *view);
}

vector<string> konga::SchemaManager::getTargetDtds(int tranId)
{
	SmartDbPtr<TargetDtdView> view( TargetDtdView::MakeInstance(&GetDbConnection()) );
	return getDtds<TargetDtdView>(tranId, *view);
}

template<typename T>
std::vector<std::string> konga::SchemaManager::getDtds(int tranId, T& view) {
	try {
		m_vSchemaPaths.clear();
		m_vXmlId.clear();
		view.Select("WHERE Transformation_ID = " + konga::toString(tranId));

		SmartDbPtr<XmlDtdChildrenView>
			pXmlDtdChildrenView( XmlDtdChildrenView::MakeInstance(&GetDbConnection()) );

		while(view.Next()){
			if(view.IsXML_PathNull() || view.IsXML_IDNull())continue;
			const int iXmlId = view.GetXML_ID();
			addXsd(iXmlId, view.GetXML_Path());
			loadDependentXsd(iXmlId, *pXmlDtdChildrenView);
		}
		return m_vSchemaPaths;
	}
	catch( DbException& e )
	{
		throw CrException(std::string("getSourceDtd() failed, ")+e.GetErrorString());
	}
}

void konga::SchemaManager::loadDependentXsd(int iXmlId, TranDb::XmlDtdChildrenView& xmlDtdChildrenView) {
	xmlDtdChildrenView.Select("WHERE XML_ID = "+konga::toString(iXmlId));

	                                                          
	                                                                         
	map<int,string>mp;
	while(xmlDtdChildrenView.Next()){
		if(xmlDtdChildrenView.IsChild_IDNull())continue;
		const int child_id=xmlDtdChildrenView.GetChild_ID();
		if(alreadyProcessed(child_id))continue;
		if(!xmlDtdChildrenView.IsXML_PathNull())
			mp[child_id]=xmlDtdChildrenView.GetXML_Path();
	}

	for(map<int,string>::iterator it=mp.begin();it!=mp.end();it++){
		const int child_id=it->first;
		addXsd(child_id, it->second);
		loadDependentXsd(child_id, xmlDtdChildrenView);
	}
}

bool konga::SchemaManager::alreadyProcessed(int iXmlId)
{
	return std::find(m_vXmlId.begin(), m_vXmlId.end(), iXmlId) != m_vXmlId.end();
}

void konga::SchemaManager::addXsd(int iXmlId, const std::string& sXsdFile) {
	if ( !alreadyProcessed(iXmlId) ) {
		m_vXmlId.push_back(iXmlId);
		m_vSchemaPaths.push_back(sXsdFile);
	}
}

namespace {
	std::string getPath(const std::string& sDir0)
	{
		string::size_type k1=sDir0.find_last_of('/'), k2=sDir0.find_last_of('\\');
		if(k1==string::npos)k1=0;
		if(k2==string::npos)k2=0;
		const string::size_type k=k1>k2?k1:k2;
		return sDir0.substr(0,k+1);
	}
}

void konga::SchemaManager::copyToDir(AutoDeleteFileSet& set,const std::string& sDir_)
{
	if(m_vSchemaPaths.empty())return;
	std::string sDir=sDir_;
	AppendPathSeparator(sDir);
	std::vector<std::string>::iterator it=m_vSchemaPaths.begin();
	std::string sDir0=getPath(*it);
	int len=sDir0.size();
	while(it!=m_vSchemaPaths.end()){
		std::string sRelative=*it;
		if(strncmp(sDir0.c_str(),sRelative.c_str(),len)!=0)
			throw CrException("the children dtd path is not compatible with the parent's.");
		                                              
		                 
		std::string sTo=sDir+sRelative.substr(len);
		std::string sFrom=konga::getAbsolutePath(sRelative);
		bool success=kCopyFile(sFrom, sTo);
		if(success)set.AddFile(sTo);
		it++;
	}
}

void SchemaManager::copyRequiredSourceSchemasToDir(const std::string& sTmpDir,
									int tranId, int structureType)
{
	copyRequiredSchemasToDir(sTmpDir, tranId, structureType, true);
}

void SchemaManager::copyRequiredTargetSchemasToDir(const std::string& sTmpDir,
									int tranId, int structureType)
{
	copyRequiredSchemasToDir(sTmpDir, tranId, structureType, false);
}

string SchemaManager::copyRequiredFilesForWsdl(long lWsdlId,
											   const string& sTmpDir,
											   vector<long>& vAlreadyHandledWsdlIds)
{
	                                             
	if ( std::find(vAlreadyHandledWsdlIds.begin(), vAlreadyHandledWsdlIds.end(), lWsdlId) != vAlreadyHandledWsdlIds.end() )
		return "";

	if ( !IsDirectory(sTmpDir) )
		throw CrException("Internal Error: Failed to copy WSDL file, the requested temporary directory does not exist: "
		+ sTmpDir);

	                     
	SmartDbPtr<TranDb::WsdlTab> pWsdlTab( TranDb::WsdlTab::MakeInstance(&GetDbConnection()) );
	const string& sWsdlId = toString(lWsdlId);
	pWsdlTab->Select("WHERE Wsdl_Id = " + sWsdlId);
	if ( !pWsdlTab->Next() ) {
		throw CrException("Internal Error: There is no WSDL file with id = " + sWsdlId);
	}

	const string stAbsoluteWsdlPath( konga::getAbsolutePath(pWsdlTab->GetWsdlPath()) );
	if ( !FileExists(stAbsoluteWsdlPath) ) {
		throw CrException("Internal Error: The requested WSDL file (with id="
		+ sWsdlId + ") does not exist at the path where it should be ('"
		+ stAbsoluteWsdlPath + "'). Try to upload the file again.");
	}
	pWsdlTab.CloseAndDelete();

	kCopyFile(stAbsoluteWsdlPath, sTmpDir + GetLeafPath(stAbsoluteWsdlPath), true);
	vAlreadyHandledWsdlIds.push_back(lWsdlId);

	                                            
	SmartDbPtr<DynamicDbRecordSet> rsDependencies( DynamicDbRecordSet::MakeInstance(&GetDbConnection()) );
	rsDependencies->SetSqlStatement("SELECT Child_Wsdl_Id " \
		"FROM WsdlDependenciesTab WHERE Parent_Wsdl_Id = " + sWsdlId);
	rsDependencies->Open();
	while( rsDependencies->Next() ) {
		const long lChildWsdlId = atol(rsDependencies->GetFieldValue(0).c_str());
		if (lChildWsdlId < 1 )
			continue;
		copyRequiredFilesForWsdl(lChildWsdlId, sTmpDir, vAlreadyHandledWsdlIds);
	}
	return stAbsoluteWsdlPath;
}

string SchemaManager::copyRequiredFilesForWsdl(long lWsdlId, const std::string& sTmpDir_in) {
	vector<long> vAlreadyHandledWsdlIds;
	string sTmpDir(sTmpDir_in);
	AppendPathSeparator(sTmpDir);
	return copyRequiredFilesForWsdl(lWsdlId, sTmpDir, vAlreadyHandledWsdlIds);
}

namespace {
	void CopyFilesToTmpDir(const std::string& sTmpDir,
		const vector<string> files)
	{
		for( vector<string>::const_iterator cit = files.begin();
			cit != files.end(); ++cit)
		{
			kCopyFile(*cit, sTmpDir, true);
		}
	}

	class WsXsdFileAction : public FileAction {
	public:
		WsXsdFileAction(void) {}
		~WsXsdFileAction(void) {}

		void doAction(const std::string& stFilePath) {
			m_fileCollection.push_back(stFilePath);
		}

		vector<string> getFileCollection() const {
			return m_fileCollection;
		}

	private:
		vector<string> m_fileCollection;
	};

	class WxdFileMatcher : public Matcher {
	public:
		WxdFileMatcher(void) {}
		~WxdFileMatcher(void) {}

		bool isMatch(const std::string& sFileName) const {
			return ( sFileName.size() > 4 &&
				EqualsIgnoreCase(sFileName.substr(sFileName.size()-4, 4), ".xsd"));
		}
	};

	vector<string> GetSchemasForWebService(int tranId, bool isSource,
		DbConnection* pConn) {
		using namespace TranDb;
		SmartDbPtr<TRANSFORMATIONSTAB_V> pTranTab(TRANSFORMATIONSTAB_V::MakeInstance(pConn));
		pTranTab->Select("WHERE Transformation_ID = " + toString(tranId));
		if ( !pTranTab->Next() )
			throw CrException("There is no transformation defined with TransformationId = " + toString(tranId));

		const string sMainSchema(isSource?
			(pTranTab->IsSource_XMLNull() ? "" : pTranTab->GetSource_XML()) :
			(pTranTab->IsTarget_XMLNull() ? "" : pTranTab->GetTarget_XML()));

		if ( !FileExists(sMainSchema) )
			throw CrException("Internal Error: The main schema for a web service call does not exist. " \
			"This could mean that the file has been deleted.\n" \
			"The file is: \"" + sMainSchema + "\".");

		WsXsdFileAction action;
		WxdFileMatcher matcher;
		FilesTraverser traverser(action, matcher);
		return action.getFileCollection();
	}
}

void SchemaManager::copyRequiredSchemasToDir(const std::string& sTmpDir,
	int tranId, int structureType, bool isSource)
{
	switch(structureType) {
		case XmlTypeId:
			CopyFilesToTmpDir(sTmpDir, isSource? getSourceDtds(tranId) : getTargetDtds(tranId));
			break;
		case WebServiceInputTypeId:
		case WebServiceOutputTypeId:
			CopyFilesToTmpDir(sTmpDir, GetSchemasForWebService(tranId, isSource, &GetDbConnection()));
			break;
		default:
			break;
	}
}

