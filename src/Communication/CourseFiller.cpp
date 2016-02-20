#include "Communication/CourseFiller.hpp"
#include <iostream>
#include <string>

bool CourseFiller::fill(CourseStore* store){
	CourseStoreDB db("104.236.4.226", "/lookup/CS", "7819");
	std::string* jsonString = db.getJson();

	rapidjson::Document doc;
	doc.Parse(jsonString->c_str());
	for (rapidjson::SizeType i = 0; i < doc.Size(); i++){
	    Course* course = CourseFiller::buildCourse(doc[i]);
		std::string name = course->getDepartment() + course->getCourseNumber();
		course->getCombos();
		store->insert(name, course);
	}
	return true;
}

Course* CourseFiller::buildCourse(rapidjson::Value& courseJson){
	std::string department = courseJson["department"].GetString();
	std::string courseNumber = std::to_string(courseJson["courseNumber"].GetInt());
	std::string courseName = courseJson["name"].GetString();
	Course* course = new Course(department, courseNumber, courseName);
	
	for(rapidjson::SizeType i = 0; i < courseJson["sections"].Size(); i++){
		Section* section = CourseFiller::buildSection(courseJson["sections"][i]);	
		course->addSection(section);
	}
	return course;
}

Section* CourseFiller::buildSection(rapidjson::Value& sectionJson){
	Section::SectionBuilder sectBuild;
	
	sectBuild.setCRN(std::to_string(sectionJson["crn"].GetInt()));

	std::string startSemester = sectionJson["start"].GetString();
	int yearS = stoi(startSemester.substr(0,4));
	int monthS = stoi(startSemester.substr(5, 7));
    int dayS = stoi(startSemester.substr(8, 10));
	sectBuild.setSemesterStart(dayS, monthS, yearS);

	std::string endSemester = sectionJson["end"].GetString();
    int yearE = stoi(endSemester.substr(0, 4));
    int monthE = stoi(endSemester.substr(5, 7));
    int dayE = stoi(endSemester.substr(8, 10));
    sectBuild.setSemesterEnd(dayE, monthE, yearE);
	for (auto i = 0; i < (int)sectionJson["meetings"].Size(); i++) {
		const rapidjson::Value &meeting = sectionJson["meetings"][i];

		std::string start(meeting["start"].GetString());    
		std::string end(meeting["end"].GetString());    
				const char *days = meeting["days"].GetString();

		std::vector<int> startTime = convertTime(start);
		std::vector<int> endTime = convertTime(end);


		for (int i = 0; i < (int)strlen(days); i++) {
			if (days[i] == 'M') {
				sectBuild.setStartTime(Week::Day::monday, startTime[0], startTime[1]);
				sectBuild.setEndTime(Week::Day::monday, endTime[0], endTime[1]);
			} else if (days[i] == 'T') {
				sectBuild.setStartTime(Week::Day::tuesday, startTime[0], startTime[1]);
				sectBuild.setEndTime(Week::Day::tuesday, endTime[0], endTime[1]);
			} else if (days[i] == 'W') {
				sectBuild.setStartTime(Week::Day::wednesday, startTime[0], startTime[1]);
				sectBuild.setEndTime(Week::Day::wednesday, endTime[0], endTime[1]); 
			} else if (days[i] == 'R') {
				sectBuild.setStartTime(Week::Day::thursday, startTime[0], startTime[1]);
				sectBuild.setEndTime(Week::Day::thursday, endTime[0], endTime[1]); 
			} else if (days[i] == 'F') {
				sectBuild.setStartTime(Week::Day::friday, startTime[0], startTime[1]);
				sectBuild.setEndTime(Week::Day::friday, endTime[0], endTime[1]); 
			}
		}
		if(meeting.HasMember("building")){
			sectBuild.setLocationBuilding(meeting["building"].GetString());
		}
		sectBuild.setSectionName(sectionJson["code"].GetString());
		sectBuild.setSectionType(meeting["type"]["name"].GetString());
		// Information we don't have yet in the json files
		sectBuild.setLocationLat(40.113803);
		sectBuild.setLocationLon(-88.224904);
		sectBuild.setLocationRoom("3340");
		if(meeting.HasMember("instrustor")){
			for (auto j = 0; j < (int)meeting["instructors"].Size(); j++) {
				std::string name = std::string(meeting["instructors"][j]["last"].GetString()) + ", "
					+ std::string(meeting["instructors"][j]["first"].GetString());
				sectBuild.setInstructorName(name);
			}
		}
	}
	return sectBuild.buildSection();
}

/*
 *  Converts a string containing a time into two integers,
 *  one for the hour and one for the minutes.  Time is changed
 *  from 12 hour clock to 24 hour clock.
 *  e.g. "3:50 PM" -> [15, 50].
 */
std::vector<int> CourseFiller::convertTime(std::string time) {
	if(time.compare("ARRANGED") == 0 or time.compare("")== 0){
		std::vector<int> nullTime;
		nullTime.push_back(0);
		nullTime.push_back(0);
		return nullTime;
	}
    std::vector<int> time_24;
    time_24.push_back(stoi(time.substr(0, 2)));
    time_24.push_back(stoi(time.substr(3, 5)));
    if (time[6] == 'A' && time_24[0] == 12) {
        time_24[0] = 0;
    } else if (time[6] == 'P' && time_24[0] != 12) {
        time_24[0] += 12;
    }
    return time_24;
}
