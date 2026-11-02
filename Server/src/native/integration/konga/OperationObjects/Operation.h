/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_H
#define KONGA_OPERATION_H

#include <memory>
#include <string>

#include <boost/noncopyable.hpp>

#include <konga/Utils/KeyValues.h>

#include <konga/OperationPipeline/ActivityType.h>

#include "OperationType.h"

namespace konga {

	class SourceDataLocation;
	class TargetDataLocation;
	class Transformation;
	class ProcessedFiles;
	class OperationEngineContext;
	class OperationDataElementHolder;

	  
                                                               
                                                                           
   
                                                     
                                                                                           
    
	class Operation : boost::noncopyable {
	protected:
		Operation(void);
	public:
		virtual ~Operation(void);

		long getId() const;
		std::string getName() const;
		OperationType getOperationType() const;

		bool isDebugOn() const;

		bool ignoreHistory() const;
		bool isDiff() const;
		bool doLogging() const;

		long getSuccessOperationId() const;
		long getFailureOperationId() const;

		long getSuccessMessageId() const;
		long getFailureMessageId() const;

		long getSourceId() const;
		long getTargetId() const;

		virtual ActivityType getActivityType() const { return ActivityType_Transformation; }

		                                                                   
		bool shouldWriteHistoryLog() const;

		                                                          
		                                                      
		         
		                         
		                 
		int getOperationLogLevel() const;

		                                                                                           
		const SourceDataLocation* getSource() const;
		const TargetDataLocation* getTarget() const;

		const Transformation* getTransformation() const;

		                                                 
		virtual void doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& deHolder) = 0;

		struct OperatonDataStruct {
			OperatonDataStruct(void) : m_lOperationID(0), m_lSuccessOperationID(0), m_lFailureOperationID(0),
				m_lSourceId(0), m_lTargetId(0), m_lTransformationId(0),
				m_OperationType(OPERATION_NOTDEFINED), m_bIgnoreHistory(false), m_bDiff(false) {}
			~OperatonDataStruct(void) {}
		
			long m_lOperationID;
			long m_lSuccessOperationID;
			long m_lFailureOperationID;
			long m_lSuccessMessageID;
			long m_lFailureMessageID;
			long m_lSourceId;
			long m_lTargetId;
			long m_lTransformationId;
			std::string m_stOperationName;
			OperationType m_OperationType;
			bool m_bIgnoreHistory;
			bool m_bDiff;
			bool m_bTrace;
		};

		friend class OperationCreatorDb;	                                

	private:
		OperatonDataStruct m_OperationData;
		KeyValues m_extendedProperties;

		std::auto_ptr<SourceDataLocation> m_pSource;
		std::auto_ptr<TargetDataLocation> m_pTarget;
		std::auto_ptr<Transformation> m_pTransformation;
	};
}
#endif                     

