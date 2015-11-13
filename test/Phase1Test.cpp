#define BOOST_TEST_MODULE SectionBuilder
#define BOOST_TEST_DYN_LINK
#include "SectionBuilder.hpp"

#include <boost/test/unit_test.hpp>
#include <boost/test/unit_test_suite.hpp>

/*
 * General Tests
 */
BOOST_AUTO_TEST_SUITE( General_Tests )
BOOST_AUTO_TEST_CASE( NULL_Test ){
	BOOST_REQUIRE(true);
}
BOOST_AUTO_TEST_SUITE_END()


/*
 * Semester Tests
 */
BOOST_AUTO_TEST_SUITE( Semester_Tests )
BOOST_AUTO_TEST_CASE( start_end_switch ){
	BOOST_REQUIRE(true);
}
BOOST_AUTO_TEST_CASE( correct_semester ){
	BOOST_REQUIRE(true);
}
BOOST_AUTO_TEST_CASE( actual_dates ){
	BOOST_REQUIRE(true);
}
BOOST_AUTO_TEST_SUITE_END()


/*
 * Week Tests
 */
BOOST_AUTO_TEST_SUITE( Week_Tests )
BOOST_AUTO_TEST_CASE( times_switch ){
	BOOST_REQUIRE(true);
}
BOOST_AUTO_TEST_CASE( correct_times ){
	BOOST_REQUIRE(true);
}
BOOST_AUTO_TEST_SUITE_END()

/*
 * Instrutor Tests
 */
BOOST_AUTO_TEST_SUITE( Instructor_Tests )
BOOST_AUTO_TEST_CASE( correct_name ){
	BOOST_REQUIRE(true);
}
BOOST_AUTO_TEST_SUITE_END()


/*
 * Location Tests
	 */
BOOST_AUTO_TEST_SUITE( Location_Tests )
BOOST_AUTO_TEST_CASE( correct_location ){
	BOOST_REQUIRE(true);
}

BOOST_AUTO_TEST_CASE( out_of_bounds_coords ){
	BOOST_REQUIRE(true);
}
BOOST_AUTO_TEST_SUITE_END()
