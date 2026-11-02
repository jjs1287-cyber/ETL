/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATIONCHAIN_H
#define TRANSFORMATIONCHAIN_H

#include <string>
#include <vector>

#include <konga/TransformationUtils/TransformationWebServiceInfo.h>

#include "TransformationEngine.h"

namespace konga {
    class DbConnection;
	class OperationDataElementHolder;
}

struct TransformationChainItem
{
	TransformationChainItem() : TransformationId(0), NextTransformationId(0), FunctionId(0) {};
	int	TransformationId;
	int	NextTransformationId;
	int	FunctionId;
	konga::TransformationWebServiceInfo	WebServiceInfo;
};

class TransformationChain {
public:
	TransformationChain(int iOperationId) : m_iOperationId(iOperationId) {};
	~TransformationChain(void) {}

	int GetOperationId() const { return m_iOperationId; }
	bool IsValid() const { return m_stError.empty(); }
	std::string GetError() const { return m_stError; }
	size_t Length() const { return m_vChainItems.size(); }
	const TransformationChainItem& GetTransformationChainItem(int i) const { return m_vChainItems[i]; }
	int Create(int iFirstTransformationId, const konga::OperationDataElementHolder& deHolder, konga::DbConnection& tranDbConn);

	const TransformationChainItem* GetLastTransformationChainItem() const;

private:
	int	m_iOperationId;
	std::vector<TransformationChainItem> m_vChainItems;
	std::string	m_stError;
	
	void AddChainItem(TransformationChainItem item);

	void SetWebServiceJtrFileLocations();	
};

#endif                         
