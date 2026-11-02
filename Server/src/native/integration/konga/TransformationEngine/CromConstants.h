/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef CROMConstants_h
#define CROMConstants_h

                                                                       
                                                                         
                                                                         

                
                           
                           
                                                                                                                  
                                                                                                                  
                                                                                                          
                                                                                                    
                                                                    
                                                                                            

#define CROM_t_Unknown					0
#define CROM_t_Element					1
#define CROM_t_Value					2
#define CROM_t_Attribute				4
#define CROM_t_Collection				8
#define CROM_t_OneOf					0x10
#define CROM_t_Optional					0x20	
#define CROM_t_AtLeastOne				0x40
#define CROM_t_OracleSeqNo				0x80
#define CROM_t_SimpleCromMatch			0x100
#define CROM_t_ComplexCromMatch			0x200
#define CROM_t_PrimaryKey				0x100
#define CROM_t_ForeignKey				0x200
#define CROM_t_UseDbFcn					0x400
#define CROM_t_Precondition				0x800
#define CROM_t_Mapped					0x1000
#define CROM_t_SourceUpdateColumn		0x2000
#define CROM_t_CouldBeRoot				0x2001
#define CROM_t_Null						0x4000
#define CROM_t_Root						0x8000
#define CROM_t_LoopNode					0x10000
#define CROM_t_Added					0x20000
#define CROM_t_OneToOne					0x40000
#define CROM_t_UpdateKey				0x40000
#define CROM_t_OuterJoin				0x80000
#define CROM_t_Array					0x100000
#define CROM_t_AllGroup					0x200000
#define CROM_t_AnyElement				0x400000
#define CROM_t_Hidden					0x800000
#define CROM_t_HasDefault				0x1000000
#define CROM_t_AnonymousGroup			0x2000000
#define CROM_t_DerivedTypeHolder		0x4000000
#define CROM_t_TypeDefinition			0x8000000
#define CROM_t_SubstitutionGroupHead	0x10000000
#define CROM_t_Flat						0x20000000
#define CROM_t_ReadOnly                 0x40000000
#define CROM_t_FixedValue				0x80000000

#define CROM_t_AnyThing			0xFFF
namespace {
 const int CROM_t_NonLeaf = CROM_t_Element|CROM_t_AllGroup|CROM_t_OneOf|CROM_t_AnyElement|CROM_t_AnonymousGroup|CROM_t_TypeDefinition;
 const int CROM_t_Loop = CROM_t_Collection|CROM_t_AtLeastOne|CROM_t_OuterJoin|CROM_t_LoopNode;
 const int CROM_t_AttValuePrecondition = CROM_t_Attribute|CROM_t_Value|CROM_t_Precondition;
}

#define CROM_t_LdapAttributeRemove	        CROM_t_PrimaryKey
#define CROM_t_LdapAttributeReplaceAll		CROM_t_ForeignKey


#define CROM_t_AttV			6

                    
#define CROM_t_KindOfStringDefined		0x80
#define CROM_t_KindOfString				0x40
#define CROM_t_PatternMatching_2		0x100

#define CROM_t_BatchNode				0x200
#define CROM_t_BatchParent				0x400
#define CROM_t_BatchChild				0x800
#define CROM_t_NotBatchNode				0
#define CROM_t_DefineHasSubElement		0x1000
#define CROM_t_HasSubElement			0x2000
#define CROM_t_NormalizationFixGenerator 0x4000
namespace {
 const int CROM_t_BatchBits = CROM_t_BatchNode|CROM_t_BatchParent|CROM_t_BatchChild;
}

#define LoopChar '.'
#define NonLoopChar '$'
#define ConditionChar '@'
#define LINKING_NODE_BEGIN "[Link "
#define LINKING_NODE_END "]"

#endif
