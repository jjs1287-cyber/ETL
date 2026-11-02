/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __DETRANSFORM_H_
#define __DETRANSFORM_H_

#include <string>

#include <konga/Utils/StringTokenizer.h>
#include <konga/DataElement/DataElement.h>

class CErrorLog;
class TreeMapper;
class KString;
class CTransform;

class DETransform {

public:
	DETransform(TreeMapper* pTreeMapper, long lScriptId, CTransform* pTrans);
	~DETransform();

public:
	std::string testExpression(const std::string& stNameValuePairs, const std::string& stExpression, std::string& stWarnings);

	                                                                                                 
	konga::DataElement* runScript(const std::string& stNameValuePairs, const std::string& stExpression, std::string& stWarnings);

	std::string getLastError() const;

private:
	std::string defineInputDEs(const std::string& stNameValuePairs, int numValueToParse);

private:
	CErrorLog* getErrorLog() const;

private:
	TreeMapper* m_pTreeMapper;
	CTransform* m_pTransform, *m_pTranform_created;
	FILE* m_debug_log;
	std::map<std::string,std::string> nameTypeMap, nameValueMap;
};

#endif                   
