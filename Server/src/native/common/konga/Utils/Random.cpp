/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <math.h>	        
#include <string.h>	         

#include <boost/random.hpp>
#include <boost/date_time/posix_time/posix_time.hpp>                                                 

#include "Random.h"

using namespace konga;
using std::string;

namespace {
	boost::mt19937 g_rng( boost::posix_time::microsec_clock::local_time().time_of_day().ticks() );
	boost::uniform_int<> g_uniform_int_distr(1, konga::getRandMax());
	boost::variate_generator< boost::mt19937, boost::uniform_int<> > g_uniform_int_generator(g_rng, g_uniform_int_distr);
}

int konga::getRandMax() {
	return RAND_MAX;
}

int konga::getRandomNumber() {
	return g_uniform_int_generator();
}

namespace {
	inline int getRandomNumberImpl(int imin, int imax) {
		if ( imin > imax )
			return 0;
		else if ( imin == imax )
			return imin;

		double d = static_cast<double>(konga::getRandomNumber())/getRandMax();
		d *= (imax-imin+1);
		int r = imin + static_cast<int>(floor(d));
		if ( r > imax ) r = imax;
		if ( r < imin ) r = imin;
		return r;
	}
}

int konga::getRandomNumber(int min, int max) {
	return getRandomNumberImpl(min, max);
}

string konga::getRandomString(int minLength, int maxLength, const char* input) {
	if ( minLength > maxLength || input == 0 || *input == 0 )
		return "";

	const int length = getRandomNumberImpl(minLength, maxLength);
	string retVal;
	const size_t maxIndex = strlen(input) - 1;
	for( int i = 0; i < length; ++i ) {
		retVal += input[getRandomNumberImpl(0, maxIndex)];
	}
	return retVal;
}
