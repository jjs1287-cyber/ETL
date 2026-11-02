/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_INTERCHANGE_DATA_STRUCTS_H
#define KONGA_WS_DEPLOY_INTERCHANGE_DATA_STRUCTS_H

#include <string>
#include <vector>

#include <konga/Utils/NullableString.h>

#include "../InterchangeDataTable.hpp"

                                                                         
                                                  

namespace konga {
namespace interchange_deploy {


	class ClientField : public NullableString {
	public:
		                                                                           
		explicit ClientField(const char* pch) : NullableString(pch), m_bSkip(false) {}

		virtual ~ClientField(void) {}

		ClientField& operator=(const std::string& s) {
			NullableString::operator=(s);
			return *this;
		}

		ClientField& operator=(const char* pch) {
			NullableString::operator=(pch);
			return *this;
		}

		bool skip() const {
			return m_bSkip;
		}

		void skip(bool b) {
			m_bSkip = b;
		}
	
	private:
		bool m_bSkip;
	};

	typedef std::vector<ClientField> vFieldsT;

	struct InterchangeDataRowStruct {

	public:
		vFieldsT m_vFields;

	};

	typedef std::vector<InterchangeDataRowStruct> vInterchangeDataRowStructT;

	struct InterchangeDataTableStruct {

	public:
		                                         
		std::vector<std::string> m_vColumnNames;

		                                            
		vInterchangeDataRowStructT m_vRows;
	};

	void FillInterchangeDataTableStruct(const InterchangeDataTable& interchangeDataTable,
									InterchangeDataTableStruct& interchangeDataStruct);

}                   
}                                 

#endif                                              

