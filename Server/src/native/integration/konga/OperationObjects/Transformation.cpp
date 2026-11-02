/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include "SourceDataStructure.h"
#include "TargetDataStructure.h"
#include "Transformation.h"

using namespace konga;

Transformation::Transformation(void) : m_lID(0), m_bIsValid(false), m_pSourceStructure(0), m_pTargetStructure(0) {}

Transformation::~Transformation(void) {}

