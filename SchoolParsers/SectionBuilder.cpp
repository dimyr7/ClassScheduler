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

	return true;
}
