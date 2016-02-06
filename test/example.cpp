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
	Section::SectionBuilder b;
	Section* section = b.buildSection();
	BOOST_REQUIRE(section == NULL);
}
BOOST_AUTO_TEST_SUITE_END()


/*
 * Semester Tests
 */
BOOST_AUTO_TEST_SUITE( Semester_Tests )
BOOST_AUTO_TEST_CASE( start_end_switch ){
	Section::SectionBuilder b;
	b.setCRN("11111");
	// set start date Jan 12, 2015
	b.setSemesterStart(12, 1, 2015);

	// set end date Jan 1, 2015
	b.setSemesterEnd(1, 1, 2015);

	Section* section = b.buildSection();
	BOOST_CHECK_MESSAGE( section == NULL, "Start Date is not Before end date.");
}
BOOST_AUTO_TEST_CASE( correct_semester ){
	Section::SectionBuilder b;
	b.setCRN("11111");
	b.setSemesterStart(1, 2, 2015);
	b.setSemesterEnd( 3, 4, 2015);

	Section* section = b.buildSection();
	BOOST_REQUIRE(section != NULL);

	Semester* semester = section->getSemester();
	BOOST_CHECK(semester != NULL);

	int sDay = semester->getStartDay();
	int sMonth = semester->getStartMonth();
	int sYear = semester->getStartYear();

	int eDay = semester->getEndDay();
	int eMonth = semester->getEndMonth();
	int eYear = semester->getEndYear();

	BOOST_CHECK(sDay == 1);
	BOOST_CHECK(sMonth == 2);
	BOOST_CHECK(sYear == 2015);

	BOOST_CHECK(eDay == 3);
	BOOST_CHECK(eMonth == 4);
	BOOST_CHECK(eYear == 2015);

}
BOOST_AUTO_TEST_CASE( actual_dates ){
	Section::SectionBuilder b;	
	b.setCRN("11111");
	b.setSemesterStart( 10000, 10000, 2015);
	b.setSemesterEnd( 10000, 2, 2015);

	Section* section = b.buildSection();

	BOOST_REQUIRE(section == NULL);
}
BOOST_AUTO_TEST_SUITE_END()


/*
 * Week Tests
 */
BOOST_AUTO_TEST_SUITE( Week_Tests )
BOOST_AUTO_TEST_CASE( times_switch ){
	Section::SectionBuilder b;
	b.setCRN("12121");
	b.setStartTime(Week::monday, 2, 2);
	b.setEndTime(Week::monday, 1, 1);

	Section* section = b.buildSection();

	BOOST_REQUIRE(section == NULL);
}
BOOST_AUTO_TEST_CASE( correct_times ){
	Section::SectionBuilder b;
	b.setCRN("124");
	b.setStartTime(Week::monday, 1, 2);
	b.setEndTime(Week::monday, 3, 4);

	b.setSemesterStart(1, 2, 2015);
	b.setSemesterEnd(3, 4, 2015);


	Section* section = b.buildSection();
	BOOST_REQUIRE( section != NULL );

	Week* week = section->getWeek();
	BOOST_REQUIRE( week != NULL );

	Time* start = week->getTimes(Week::monday, true);
	Time* end   = week->getTimes(Week::monday, false);

	int startH = start->getHour();
	int startM = start->getMinute();

	int endH = end->getHour();
	int endM = end->getMinute();

	BOOST_CHECK(startH == 1);
	BOOST_CHECK(startM == 2);
	BOOST_CHECK(endH == 3);
	BOOST_CHECK(endM == 4);
}
BOOST_AUTO_TEST_SUITE_END()

/*
 * Instrutor Tests
 */
BOOST_AUTO_TEST_SUITE( Instructor_Tests )
BOOST_AUTO_TEST_CASE( correct_name ){
	Section::SectionBuilder b;
	b.setCRN("123");
	b.setInstructorName(" blah ");
	b.setSemesterStart(1, 2, 2015);
	b.setSemesterEnd  (3, 4, 2015);

	Section* section = b.buildSection();
	BOOST_REQUIRE(section != NULL);

	Instructor* inst = section->getInstructor();
	BOOST_REQUIRE( inst != NULL);

	std::string name = inst->getName();
	BOOST_REQUIRE( name.compare(" blah ") == 0);
}
BOOST_AUTO_TEST_SUITE_END()


/*
 * Location Tests
 */
BOOST_AUTO_TEST_SUITE( Location_Tests )
BOOST_AUTO_TEST_CASE( correct_location ){
	Section::SectionBuilder b;
	b.setCRN("123");
	b.setSemesterStart(1, 2, 2015);
	b.setSemesterEnd(3, 4, 2015);

	b.setLocationLat(4);
	b.setLocationLon(3);
	b.setLocationRoom("Room 100");
	b.setLocationBuilding("Siebel");

	Section* section = b.buildSection();
	BOOST_REQUIRE( section != NULL);

	Location* loc = section->getBuilding();
	BOOST_REQUIRE( loc != NULL);

	double lat = loc->getLatitude();
	double lon = loc->getLongitude();
	std::string building = loc->getBuildingName();
	std::string room = loc->getRoomNumber();

	BOOST_CHECK(lat == 4);
	BOOST_CHECK(lon == 3);
	BOOST_CHECK(building.compare("Siebel") == 0);
	BOOST_CHECK(room.compare("Room 100") == 0);
}

BOOST_AUTO_TEST_CASE( out_of_bounds_coords ){
	Section::SectionBuilder b;
	b.setCRN("123");
	b.setSemesterStart(1, 2, 2015);
	b.setSemesterEnd(3, 4, 2015);

	b.setLocationLat(91);
	b.setLocationLon(181);

	Section* section = b.buildSection();
	BOOST_REQUIRE(section == NULL);
}
BOOST_AUTO_TEST_SUITE_END()
