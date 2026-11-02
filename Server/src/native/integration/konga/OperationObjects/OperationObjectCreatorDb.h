/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_OBJECT_CREATOR_DB_H
#define KONGA_OPERATION_OBJECT_CREATOR_DB_H

namespace konga {

	class EmailMessage;
	class DbConnection;
	class OperationDataElementHolder;

	class OperationObjectCreatorDb {
	public:
		OperationObjectCreatorDb(void) : m_pDEHolder(0) {}
		~OperationObjectCreatorDb(void) {}

		                                                          
		  

		EmailMessage* getEmailMessage(long lMessageId, DbConnection& dbConnection, OperationDataElementHolder& deHolder, bool bUseSystemEmailSettingsAsDefault);

	private:
		std::string replace_de(const std::string& s);
		OperationDataElementHolder* m_pDEHolder;
	};
}

#endif                                       

