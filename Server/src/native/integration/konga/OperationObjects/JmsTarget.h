/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_JMS_TARGET_H
#define KONGA_OPERATION_JMS_TARGET_H

#include "FileTypeTargetLocation.h"
#include "JmsLocation.h"

namespace konga {

	class JmsTarget : public FileTypeTargetLocation, public JmsLocation {
	public:
		                                                                                
		                                                                      
		JmsTarget(int jmsMessageId);
		virtual ~JmsTarget(void);

		std::string getFileName() const {
			                                            
			                                       
			return std::string();
		}

		virtual void doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		virtual void testFileTargetConnection(bool bCopyTestFile) const;

	private:
		int m_iJmsMessageId;
	};
}
#endif                                
