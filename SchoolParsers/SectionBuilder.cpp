#include "SectionBuilder.hpp"
#include "Section.hpp"
#include "Week.hpp"
#include "Instructor.hpp"
#include "Location.hpp"
#include "Semester.hpp"
#include "Time.hpp"
Section::SectionBuilder::SectionBuilder(){
	/*
  	 * Section Info
	 */
	this->_sectionName = "";
	this->_sectionType = "";
	this->_description = "";
	this->_crn = "";
	/*
	 * Instructor
 	 */
	this->_instructorName = "";	

	/*
	 * Week
	 */
	for(auto i : this->_weekTimes){
		for(auto j : i){
			for(auto k : j){
				k = -1;
			}
		}
	}
	
	/*
	 * Semester
	 */
	for(auto i : this->_semesterStartDate){
		i = -1;
	}

	for(auto i : this->_semesterEndDate){
		i = -1;
	}
	this->_semesterYear = "";
	this->_semesterSeason = "";
	this->_semesterName = "";


	/*
	 * Location
	 */
	this->_locationLat = 0;
	this->_locationLon = 0;
	this->_locationBuildingName = "";
	this->_locationRoomNumber = "";
}

Section* Section::SectionBuilder::buildSection(){
	if(not Section::SectionBuilder::validateInput()){
		return NULL;
	}
	/*
	 * Creating Week Time
	 */
	Section::Week* week = new Section::Week();
	for(int i = 0; i < 7; i++){
		std::array<int, 2> startArr = this->_weekTimes[i][0];
		std::array<int, 2> endArr = this->_weekTimes[i][1];
		if(startArr[0] == -1 or startArr[1] == -1 or endArr[0] == -1 or endArr[1] == -1){
			continue;
		}
		Section::Week::Time* startTime = new Section::Week::Time(startArr[0], startArr[1]);
		Section::Week::Time* endTime = new Section::Week::Time(endArr[0], endArr[1]);
		
		week->setDay((Section::Week::Day)i, startTime, endTime);
	}
	/*
	 * Creating Instructor
	 */
	Section::Instructor* instructor = new Section::Instructor(this->_instructorName);
	

	/*
	 * Creating Location
	 */

	
	Section::Location* building = new Section::Location(this->_locationLat, this->_locationLon);
	building->setRoomNumber(this->_locationRoomNumber);
	building->setBuildingName(this->_locationBuildingName);
		
	/*
	 * Creating Semseter
	 */

	Section::Semester* semester = new Section::Semester(this->_semesterYear, this->_semesterSeason, this->_semesterName);


	/*
	 * Creating Section
	 */
	Section* sec = new Section();
	sec->setDescription(this->_description);
	sec->setSectionName(this->_sectionName);
	sec->setSectionType(this->_sectionType);
	sec->setCRN(this->_crn);

	sec->setInstructor(instructor);
	sec->setDaysOfWeek(week);
	sec->setSemester(semester);
	sec->setLocation(building);

	return sec;
}

bool Section::SectionBuilder::validateInput(){
	/*
	 * Validating week time
	 */
	for(auto i : this->_weekTimes){
		std::array<int, 2> startArr = i[0];
		std::array<int, 2> endArr = i[1];

		Section::Week::Time* startTime = new Section::Week::Time(startArr[0], startArr[1]);
		Section::Week::Time* endTime = new Section::Week::Time(endArr[0], endArr[1]);
		if(Section::Week::Time::before(startTime, endTime)){
			delete startTime;
			delete endTime;
			return false;
		}	
		delete startTime;
		delete endTime;

	}

	/*
	 * Validating Location
	 */
	if(not Section::Location::validLatitude(this->_locationLat) or not Section::Location::validLongitude(this->_locationLon)){
		return false;
	}
	
	/*
	 * Validating Semester
	 */
	if(not Section::Semester::before(this->_semesterStartDate, this->_semesterEndDate)){
		return false;
	}
	if(this->_crn.std::string::compare("") == 0){
		return false;
	}
	return true;
}


/*
 * Setters for Section object
 */
void Section::SectionBuilder::setSectioName(std::string name){
	this->_sectionName = name;
}
void Section::SectionBuilder::setSectionType(std::string type){
	this->_sectionType = type;
}

void Section::SectionBuilder::setDescription(std::string description){
	this->_description = description;
}

void Section::SectionBuilder::setCRN(std::string crn){
	this->_crn = crn;
}

/*
 * Setters for Instructor object
 */
void Section::SectionBuilder::setInstructorName(std::string instructor){
	this->_instructorName = instructor;
}

/*
 * Setters for Time objects for Week
 */
void Section::SectionBuilder::setStartTime(Section::Week::Day day, int hour, int minute){
	this->_weekTimes[day][0][0] = hour;
	this->_weekTimes[day][0][1] = minute;
}

void Section::SectionBuilder::setEndTime(Section::Week::Day day, int hour, int minute){
	this->_weekTimes[day][1][0] = hour;
	this->_weekTimes[day][1][1] = minute;
}


void Section::SectionBuilder::setSemesterStart(int day, int month, int year){
	this->_semesterStartDate[0] = day;
	this->_semesterStartDate[1] = month;
	this->_semesterStartDate[2] = year;
}

void Section::SectionBuilder::setSemesterEnd(int day, int month, int year){
	this->_semesterEndDate[0] = day;
	this->_semesterEndDate[1] = month;
	this->_semesterEndDate[2] = year;
}

void Section::SectionBuilder:: setSemsterYear(std::string year){
	this->_semesterYear = year;
}

void Section::SectionBuilder::setSemesterSeason(std::string season){
	this->_semesterSeason = season;
}

void Section::SectionBuilder::setSemesterName(std::string name){
	this->_semesterName = name;
}

/*
 * Setters for Location Object
 */

void Section::SectionBuilder::setLocationLat(double lat){
	this->_locationLat = lat;
}

void Section::SectionBuilder::setLocationLon(double lon){
	this->_locationLon = lon;
}

void Section::SectionBuilder::setLocationBuilding(std::string name){
	this->_locationBuildingName = name;
}

void Section::SectionBuilder::setLocationRoom(std::string room){
	this->_locationRoomNumber = room;
}



