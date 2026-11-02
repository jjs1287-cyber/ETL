/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <stdio.h>		                    

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/util.h>	                  
#include <konga/Utils/FileUtils.h>	            
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CrException.h>

#ifndef WIN32
#include <konga/Utils/PortDefs.h>	                                          
#endif

#include "XercesXmlUtils.h"

using namespace konga;
using std::string;

XERCES_CPP_NAMESPACE_USE

XercesNodeTraverser::XercesNodeTraverser(DOMNode* pStartNode) : m_pCurrentNode(pStartNode) {
}

XERCES_CPP_NAMESPACE::DOMNode* XercesNodeTraverser::GetNextNode() {
	                           
	DOMNode* pFirstChild = m_pCurrentNode->getFirstChild();
	if ( pFirstChild != 0 ) {
		m_parent_stack.push(m_pCurrentNode);
		return (m_pCurrentNode = pFirstChild);
	}

	                                               
	DOMNode* pNextSibling = m_pCurrentNode->getNextSibling();
	if ( pNextSibling != 0 ) {
		return (m_pCurrentNode = pNextSibling);
	}

	                                                                 
	while( !m_parent_stack.empty() ) {
		pNextSibling = m_parent_stack.top()->getNextSibling();
		m_parent_stack.pop();
		if ( pNextSibling != 0 ) {
			return (m_pCurrentNode = pNextSibling);
		}
	}

	                
	return 0;
}

DOMNode* konga::FindSameNode(DOMNode* pStartNode, const DOMNode* pContextNode) {
	if ( pStartNode->isSameNode(pContextNode) )
		return pStartNode;
	
	XercesNodeTraverser traverser(pStartNode);
	DOMNode* pNode;
	while( (pNode=traverser.GetNextNode()) ) {
		if ( pNode->isSameNode(pContextNode) )
			return pNode;
	}

	                 
	return 0;
}

namespace {
	const unsigned char ef = 0xEF;
	const unsigned char bb = 0xBB;
	const unsigned char bf = 0xBF;
}

void konga::XercesBOMWorkaround(const string& stFileName) {
	FILE* fin = fopen(stFileName.c_str(), "rb");
	if ( fin == 0 )
		return;	                         

	AutoFileHandle adopt_fin(fin);

	const size_t size = sizeof(unsigned char);
	unsigned char bom[3];
	size_t bytesRead = fread(bom, size, 3, fin);
	if ( bytesRead != 3 )
		return;	                                                                            

	if ( bom[0] != ef || bom[1] != bb || bom[2] != bf )
		return;	                  

	                                            
	const string stTmpFileName(stFileName + GetGUIDString());
	FILE* fout = fopen(stTmpFileName.c_str(), "wb");
	if ( fout == 0 ) {
		const long lErr = GetLastError();
		throw CrException("Failed to open temporary file while removing BOM: '" + stTmpFileName
			+ "'. Reason: " + ErrorResolver(lErr).Resolve(lErr));
	}

	                                                                               
	AutoDeleteFileSet auto_delete_tmp_file;
	auto_delete_tmp_file.AddFile(stTmpFileName);

	AutoFileHandle adopt_fout(fout);

	                                                                              
	unsigned char buf = 0;
	while ( fread(&buf, size, 1, fin) == 1 ) {
		if ( fwrite(&buf, size, 1, fout) != 1 ) {
			const long lErr = GetLastError();
			throw CrException("Failed to write to temporary file while removing BOM: '" + stTmpFileName
				+ "'. Reason: " + ErrorResolver(lErr).Resolve(lErr));
		}
	}

	if ( ferror(fin) != 0 ) {
		                                       
		const long lErr = GetLastError();
		throw CrException("Failed to read from file '" + stFileName
			+ "' while removing BOM. Reason: " + ErrorResolver(lErr).Resolve(lErr));
	}

	                              
	adopt_fout.Close();
	adopt_fin.Close();
	konga::kMoveFile(stTmpFileName, stFileName, true);
}
