/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_INTERCHANGE_DATA_IMPL_H
#define KONGA_WS_CHECKOUT_INTERCHANGE_DATA_IMPL_H

#include <string>
#include <vector>


#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include "../InterchangeDataHolder.hpp"
#include "../InterchangeDataTable.hpp"
#include "../CInterchangeProjects.hpp"
#include "../DownloadDataHolder.hpp"
#include "../RequiredFiles.hpp"

#include "CheckoutSpecialHandling.h"

namespace konga {
	class DbConnection;
	class DbRelations;
	class GuidToIdMapper;
	class CachedGuidToIdMapper;
	class InterchangeProject;

namespace interchange_checkout {

	class SelectSqlGenerator;

	class NullCheckoutSpecialHandling : public CheckoutSpecialHandling {
	public:
		NullCheckoutSpecialHandling(void) {}
		virtual ~NullCheckoutSpecialHandling(void) {}

		virtual void HandleColumn(vColumnValuesT& vColumnValues) {
			                
		}
	};

	class CheckoutInterchangeDataImpl : WsImplBaseAuthenticator {
	public:
		CheckoutInterchangeDataImpl(const char* user, const char* password);
		~CheckoutInterchangeDataImpl(void);

		checkout_interchange_data::InterchangeDataHolder* CheckoutInterchangeData(const char* const projectId);

		DownloadDataHolder* DownloadIntegrationProject(const char* const projectId);

		checkout_interchange_data::RequiredFiles* DownloadRequiredFiles(int iEntityTypeId, const char* const entityGuid);

		checkout_interchange_data::InterchangeDataHolder* GetProjectTree();

		checkout_interchange_data::CInterchangeProjects* GetInterchangeProjects();

	protected:
		typedef std::vector<CheckoutSpecialHandling*> vshT;
		                                                        
		checkout_interchange_data::InterchangeDataTable* GetInterchangeDataTable(const std::string& stName,
			SelectSqlGenerator& sqlGenerator,
			vshT& vSpecialHandling);

		checkout_interchange_data::InterchangeDataTable* GetInterchangeDataTable(const std::string& stName,
			SelectSqlGenerator& sqlGenerator,
			CheckoutSpecialHandling& specialHandling)
		{
			vshT v(1);
			v[0] = &specialHandling;
			return GetInterchangeDataTable(stName, sqlGenerator, v);
		}

		checkout_interchange_data::InterchangeDataTable* GetInterchangeDataTable(const std::string& stName,
			SelectSqlGenerator& sqlGenerator)
		{
			NullCheckoutSpecialHandling s;
			return GetInterchangeDataTable(stName, sqlGenerator, s);
		}

		void CleanUp();

	private:
		DbConnection* m_pDbConnection;
		DbRelations* m_pTranDbRelations;
		GuidToIdMapper* m_pNonCachedGuidToIdMapper;
		GuidToIdMapper* m_pGuidToIdMapper;
		InterchangeProject* m_pInterchangeProject;
	};

}                        
}         

#endif                                             

