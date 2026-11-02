/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XSD_CONSTANTS
#define KONGA_XSD_CONSTANTS

#include <string>

                                                                                        
                   
namespace XsdConstants {
	const std::string SCHEMA_DEFINITION_NAMESPACE_1999 = "http://www.w3.org/1999/XMLSchema";
	const std::string SCHEMA_DEFINITION_NAMESPACE_2001 = "http://www.w3.org/2001/XMLSchema";
	const std::string SCHEMA_INSTANCE_NAMESPACE_1999 = "http://www.w3.org/1999/XMLSchema-instance";
	const std::string SCHEMA_INSTANCE_NAMESPACE_2001 = "http://www.w3.org/2001/XMLSchema-instance";

	const std::string ANY_NAMESPACE = "##any";
	const std::string OTHER_NAMESPACE = "##other";
	const std::string LOCAL_NAMESPACE = "##local";
	const std::string TARGET_NAMESPACE = "##targetNamespace";

	const std::string PROCESS_CONTENTS = "processContents";
	const std::string PROCESS_CONTENTS_LAX = "lax";
	const std::string PROCESS_CONTENTS_SKIP = "skip";
	const std::string PROCESS_CONTENTS_STRICT = "strict";


	const std::string SCHEMA = "schema";

	const std::string INCLUDE = "include";
	const std::string IMPORT = "import";

	const std::string XMLNS = "xmlns";

	const std::string ELEMENT_FORM_DEFAULT = "elementFormDefault";
	const std::string ATTRIBUTE_FORM_DEFAULT = "attributeFormDefault";
	const std::string FORM = "form";
	const std::string FORM_QUALIFIED = "qualified";
	const std::string FORM_UNQUALIFIED = "unqualified";

	const std::string ELEMENT = "element";
	const std::string ATTRIBUTE = "attribute";
	const std::string SEQUENCE = "sequence";
	const std::string ALL = "all";
	const std::string ANY = "any";
	const std::string ANY_ATTRIBUTE= "anyAttribute";
	const std::string CHOICE = "choice";
	const std::string COMPLEX_TYPE = "complexType";
	const std::string COMPLEX_CONTENT = "complexContent";
	const std::string SIMPLE_TYPE = "simpleType";
	const std::string EXTENSION = "extension";
	const std::string RESTRICTION = "restriction";
	const std::string GROUP = "group";
	const std::string ATTRIBUTE_GROUP = "attributeGroup";
	const std::string ANNOTATION = "annotation";
	const std::string ENUMERATION = "enumeration";

	const std::string NAME_ATTRIBUTE = "name";
	const std::string BASE_ATTRIBUTE = "base";
	const std::string REF_ATTRIBUTE = "ref";
	const std::string TYPE_ATTRIBUTE = "type";
	const std::string ABSTRACT_ATTRIBUTE = "abstract";
	const std::string SCHEMALOCATION_ATTRIBUTE = "schemaLocation";
	const std::string VALUE_ATTRIBUTE = "value";
	const std::string DEFAULT_ATTRIBUTE = "default";
	const std::string FIXED_ATTRIBUTE = "fixed";
	const std::string USE_ATTRIBUTE = "use";
	const std::string REQUIRED_ATTRIBUTE = "required";
	const std::string PROHIBITED_ATTRIBUTE = "prohibited";
	const std::string SUBSTITUTIONGROUP_ATTRIBUTE = "substitutionGroup";

	const std::string ANY_TYPE = "anyType";
	const std::string TYPE_ARRAY = "array";

	const std::string MIN_OCCURS = "minOccurs";
	const std::string MAX_OCCURS = "maxOccurs";
	const std::string UNBOUNDED = "unbounded";

	const std::string NILLABLE = "nillable";
	const std::string NIL = "nil";
	const std::string TYPE = "type";

	const std::string SUBSTITUTION_GROUP = "substitutionGroup";

	const std::string VALUE_TRUE = "true";
	const std::string VALUE_FALSE = "false";

	enum SchemaVersion { SchemaVersion_Unknown, SchemaVersion_1999, SchemaVersion_2001 };

	enum XsdNodeType { UnknownNode, ElementNode, AttributeNode, SequenceNode, AllNode, AnyNode, AnyAttributeNode, 
					   ChoiceNode,	ComplexTypeNode, ComplexContentNode, SimpleTypeNode, ExtensionNode, RestrictionNode,
					   GroupNode, AttributeGroupNode, AnnotationNode, EnumerationNode };

	                                                                                       
	enum AnyElementNamespace { Any, Local, Other, List };

	                                                                                             
	enum ProcessContentsValue { Lax, Skip, Strict };

}                          
                      

#endif                       

