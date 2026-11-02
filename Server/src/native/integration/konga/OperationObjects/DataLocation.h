/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_DATALOCATION_H
#define KONGA_OPERATION_DATALOCATION_H

#include <string>

#include <konga/Utils/KeyValues.h>

#include <konga/DbRelations/SourceTypeIdConstants.h>

namespace konga {
	class ProcessedFiles;
	class OperationEngine;
	class OperationDataElementHolder;

	  
                                                            
    
	class DataLocation {
	protected:
		DataLocation(void);
	public:
		virtual ~DataLocation(void) {}

		long getId() const;
        const KeyValues& compressionProperties() const { return m_extendedProperties; }
        std::string compressionPassword() const;
		std::string getName() const;

		std::string getDecryptedPassword() const;

		                                               
		                                                                                                    
		                                                                                          
		struct CommonDataStruct {
			CommonDataStruct() : m_id(0), m_type(SourceTypeUndefined), m_lDocumentId(0), m_lTimeOut(-1), m_lTransferType(-1),
				m_lEncryptChoice(0), m_lPort(-1), m_iPassiveMode(0), m_lSecured(0), m_lReferralMode(0), m_iHttpVerb(0) {}
			                                        
			long m_id;
			std::string m_stName;
			int m_type;
			long m_lDocumentId;
			std::string m_stLocator;
		
			std::string m_stPath;
			std::string m_stFileName;
			long m_lTimeOut;
			std::string m_stLogin;
			std::string m_stPassword;	                                                 
		
			std::string m_stDSN;
			long m_lTransferType;
			std::string m_stEncryptText;
			long m_lEncryptChoice;
			std::string m_stDriver;
		
			long m_lPort;
			int m_iPassiveMode;
			std::string m_stDBName;
			std::string m_stConnectString;
			long m_lSecured;	                                                 
								                                                     

			long m_lReferralMode;	                          
									                                                                                                                 
									                                                                                                                             

			int m_iHttpVerb;
		
			std::string m_stHttpUrl;
			std::string m_stCertificateName;
			std::string m_stOptionalConnectString;
			std::string m_stODBC_UNC_UserID;
			std::string m_stODBC_UNC_Password;
		};

		friend class LocationCreatorDb;
		friend class OperationObjectCreatorKeyValues;

	protected:
		std::string getEncryptedPassword() const;

		std::string replace_de(const std::string& str) const;

		static int getPercentDone(int iFile, int iFileMax);

	protected:
		OperationDataElementHolder* m_pDataElementHolder;
		CommonDataStruct m_CommonData;
		KeyValues m_extendedProperties;
	};
}

#endif                                  

