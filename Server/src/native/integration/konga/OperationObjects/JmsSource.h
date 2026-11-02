/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_JMS_SOURCE_H
#define KONGA_OPERATION_JMS_SOURCE_H

#include <string>

#include "JmsLocation.h"
#include "FileTypeSourceLocation.h"

namespace konga {

	class JmsSource : public FileTypeSourceLocation, public JmsLocation {
	public:
		JmsSource(const std::string& sLocalJmsPayloadPath);
		virtual ~JmsSource(void);

		virtual void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {
			                 
		}

		virtual void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
			                                                           
		}

		virtual std::vector<std::string> getFileList() const;

	protected:
		virtual std::string getFileFilter() const {
			                  
			return std::string("");
		}

		                                                                 
		std::string getLocalPayloadPath() const;

		void doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

	private:
		std::string m_sLocalJmsPayloadPath;
	};
}
#endif                                
