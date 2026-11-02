/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_SCHEMA_VALIDATOR_H
#define KONGA_XML_SCHEMA_VALIDATOR_H

#include <string>

namespace konga {
	namespace xml {
		class XmlSchemaValidator {
		public:
			XmlSchemaValidator(void);
			~XmlSchemaValidator(void);

			                                                 
			                                                                       
			                  
			                                                                                                  
			bool ValidateXmlSchema(const std::string& stSchemaFilePath, std::string& stError) const;

			                                                                   
			                                                                             
			                                                  
			                                                       
			                                                                             
			bool ValidateXmlInstance(const std::string& stXmlFilePath, const std::string& stSchema,
				const std::string& stTargetNamespace, std::string& stError) const;
		};
	}
}
#endif                                

