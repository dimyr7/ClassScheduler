#include "SectionBuilder.hpp"
#include "Section.hpp"
Section::SectionBuilder::SectionBuilder(){
	// Section Info
	this->_sectionName = "";
	this->_sectionType = "";
	this->_description = "";
	this->_crn = "";
	
	// Week 
	for(int i = 0; i < Week::DAYSINWEEK; i++){
		for(int j = 0; j < Week::DAYSINWEEK; j++){
			this->_weekTimes[i][j][0] = -1;
			this->_weekTimes[i][j][1] = -1;
		}
	}
	
	
	// Semester
	for(int i = 0; i < 3 ; i++){
		this->_semesterStartDate[i] = -1;
		this->_semesterEndDate[i] = -1;
	}

	this->_semesterYear = "";
	this->_semesterSeason = "";
	this->_semesterName = "";


	// Location
	this->_locationLat = 0;
	this->_locationLon = 0;
	this->_locationBuildingName = "";
	this->_locationRoomNumber = "";
}

Section* Section::SectionBuilder::buildSection(){
	if(not Section::SectionBuilder::validateInput()){
		return NULL;
	}

	// Creating Week Time
	Week* week = new Week();
	for(int i = 0; i < 7; i++){
		int* startArr = this->_weekTimes[i][0];
		int* endArr = this->_weekTimes[i][1];


		// Skip any times that are not set
		if(startArr[0] == -1 or startArr[1] == -1 or endArr[0] == -1 or endArr[1] == -1){
			continue;
		}
		Time* startTime = new Time(startArr[0], startArr[1]);
		Time* endTime = new Time(endArr[0], endArr[1]);
		
		week->setDay((Week::Day)i, startTime, endTime);
	}
	/*
	 * Creating Instructor
	 */
    std::vector<Instructor*> instructor;
    for (auto instruct : this->_instructorName) {
        instructor.push_back(new Instructor(instruct));
    }

	
	// Creating a Location	
	Location* building = new Location(this->_locationBuildingName, this->_locationRoomNumber, this->_locationLat, this->_locationLon);
		

	// Creating a semester
	Semester* semester = new Semester(this->_semesterYear, this->_semesterSeason, this->_semesterName);
	semester->setDates(this->_semesterStartDate, this->_semesterEndDate);


	
	// Creating a Section 
	Section* sec = new Section();
	sec->setDescription(this->_description);
	sec->setSectionName(this->_sectionName);
	sec->setSectionType(this->_sectionType);
	sec->setCRN(this->_crn);

	for (auto instruct : instructor) {
        sec->addInstructor(instruct);
    }
    
    sec->setDaysOfWeek(week);
	sec->setSemester(semester);
	sec->setLocation(building);

	return sec;
}

bool Section::SectionBuilder::validateInput(){
	/*
	 * Validating week time
	 */
	for(int i = 0; i < Week::DAYSINWEEK; i++){
		int* startArr = this->_weekTimes[i][0];
		int* endArr   = this->_weekTimes[i][1];

		if(startArr[0] == -1 or startArr[1] == -1 or endArr[0] == -1 or endArr[1] == -1){
			continue;
		}

		Time* startTime = new Time(startArr[0], startArr[1]);
		Time* endTime = new Time(endArr[0], endArr[1]);
		if(not Time::before(startTime, endTime)){
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
	if(not Location::validLatitude(this->_locationLat) or not Location::validLongitude(this->_locationLon)){
		return false;
	}
	
	/*
	 * Validating Semester
	 */
	if(not Semester::before(this->_semesterStartDate, this->_semesterEndDate)){
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
void Section::SectionBuilder::setSectionName(std::string name){
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
	this->_instructorName.push_back(instructor);
}

/*
 * Setters for Time objects for Week
 */
void Section::SectionBuilder::setStartTime(Week::Day day, int hour, int minute){
	this->_weekTimes[day][0][0] = hour;
	this->_weekTimes[day][0][1] = minute;
}

void Section::SectionBuilder::setEndTime(Week::Day day, int hour, int minute){
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

