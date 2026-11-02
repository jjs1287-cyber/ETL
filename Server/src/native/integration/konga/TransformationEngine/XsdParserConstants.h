/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDPARSER_CONSTANTS_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDPARSER_CONSTANTS_H

namespace XsdParserConstants {

	enum NodeType {t_element, t_attribute, t_group, t_value, t_root, t_typeRoot, t_unknown};

	enum FormAttributeValue { FORM_ATTRIBUTE_NOT_SET, FORM_ATTRIBUTE_QUALIFIED, FORM_ATTRIBUTE_UNQUALIFIED };

	enum ComplexTypeConsumer { Element, LocalExtendedType, GlobalExtendedType, LocalRestrictedType, GlobalRestrictedType };

}

#endif
