#include <fstream>
#include <iostream>
#include <string>

#include <boost/property_tree/ptree.hpp>
#include <boost/foreach.hpp>
#include <boost/algorithm/string/split.hpp>

#include "Course.hpp"
#include "SectionBuilder.hpp"
#include "JSONParser.hpp"


void test1(){
	Section::SectionBuilder b;
	
	b.setSectionName("AL1");
	b.setSectionType("Lecture-Discussion");
	b.setDescription("Lecture for a class");
	b.setCRN("11111");

	b.setInstructorName("Alan Turing");

	b.setStartTime(Section::Week::Day::monday, 6, 0);
	b.setEndTime(Section::Week::Day::monday, 8, 0);

	b.setStartTime(Section::Week::Day::tuesday, 7, 0);
	b.setEndTime(Section::Week::Day::tuesday, 9, 0);

	b.setSemesterStart(1, 1, 2015);
	b.setSemesterEnd(30, 12, 2015);

	b.setSemsterYear("2015");
	b.setSemesterSeason("All Year");
	b.setSemesterName("365 All year");

	b.setLocationLat(2.4);
	b.setLocationLon(-23.6);
	b.setLocationBuilding("Siebel");
	b.setLocationRoom("1204");


	Section* section = b.buildSection();
	std::cout << (*section) << std::endl;

}

void parseTimes(Section::SectionBuilder& builder, std::string daysOfWeek, std::string start, std::string end){
	int startHour = stoi(start.substr(0, 2));
	int startMin  = stoi(start.substr(3, 5));
	bool isStartPM = (start.substr(6, 8).compare("PM") == 0);
	if(isStartPM  and startHour!= 12){
		startHour +=12;
	}
	else if(not isStartPM and startHour == 12){
		startHour = 0;
	}


	int endHour   = stoi(end.substr(0, 2));
	int endMin    = stoi(end.substr(3, 5));
	bool isEndPM= (end.substr(6, 8).compare("PM") == 0);	
	if(isEndPM and endHour != 12){
		endHour += 12;
	}
	else if(not isEndPM and endHour == 12){
		endHour = 0;
	}

	//std::cout << "Start Time: " << startHour << ":" << startMin << std::endl;
	//std::cout << "End Time: " << endHour << ":" << endMin << std::endl << std::endl;

	if(daysOfWeek.find("M")  != std::string::npos){
		builder.setStartTime(Section::Week::Day::monday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::monday, endHour, endMin);
	}
	if(daysOfWeek.find("T")  != std::string::npos){
		builder.setStartTime(Section::Week::Day::tuesday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::tuesday, endHour, endMin);
	}
	if(daysOfWeek.find("W") != std::string::npos){
		builder.setStartTime(Section::Week::Day::wednesday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::wednesday, endHour, endMin);
	}
	if(daysOfWeek.find("R") != std::string::npos){
		builder.setStartTime(Section::Week::Day::thursday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::thursday, endHour, endMin);
	}
	if(daysOfWeek.find("F") != std::string::npos){
		builder.setStartTime(Section::Week::Day::friday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::friday, endHour, endMin);
	}
}

void parseDates(Section::SectionBuilder& builder, std::string start, std::string end){
	
	int startDay = stoi(start.substr(8, 10));
	int startMonth = stoi(start.substr(5, 7));
	int startYear = stoi(start.substr(0, 4));
	builder.setSemesterStart(startDay, startMonth, startYear);

	int endDay = stoi(end.substr(8, 10));
	int endMonth = stoi(end.substr(5,7));
	int endYear = stoi(start.substr(0, 4));
	builder.setSemesterEnd(endDay, endMonth, endYear);
	
}

int main(){
	boost::property_tree::ptree *courseTree = JSONParser::openJSON("example.json");

	std::string dep = courseTree->find("Department")->second.data();
	std::string num = courseTree->find("getCourseNumber")->second.data();
	Course course(dep, num);
	

	BOOST_FOREACH(boost::property_tree::ptree::value_type &v, courseTree->get_child("Sections")){
		Section::SectionBuilder builder;
		boost::property_tree::ptree sectionTree = v.second;
		
		/*
		 * Getting sections info from JSON
		 */
		std::string sectionName = sectionTree.get<std::string>("Code");
		std::string sectionType = sectionTree.get<std::string>("Meetings..Type.Name");
		std::string crn 		= sectionTree.get<std::string>("CRN");

		builder.setSectionName(sectionName);
		builder.setSectionType(sectionType);
		builder.setDescription("A description of a course");
		builder.setCRN(crn);

		/*
		 * Get instructor info from JSON
		 */
		std::string firstNameProf = sectionTree.get<std::string>("Meetings..Instructors..FirstName");
		std::string lastNameProf  = sectionTree.get<std::string>("Meetings..Instructors..LastName");

		builder.setInstructorName(firstNameProf + " " + lastNameProf);

		/*
		 * Getting week times
		 */
		std::string daysOfWeek = sectionTree.get<std::string>("Meetings..Days");
		std::string startTime  = sectionTree.get<std::string>("Meetings..Start");
		std::string endTime    = sectionTree.get<std::string>("Meetings..End");
		parseTimes(builder, daysOfWeek, startTime, endTime);


		/*
		 * Get Semester dates
		 */
		std::string startDate = sectionTree.get<std::string>("Start");
		std::string endDate   = sectionTree.get<std::string>("End");
		parseDates(builder, startDate, endDate);
		builder.setSemsterYear("2016");
		builder.setSemesterName("Spring 1");
		builder.setSemesterSeason("Spring");
		//std::cout<< val << std::endl;	
		

		/*
		 * Location data for builder
		 */
		std::string building = sectionTree.get<std::string>("Meetings..Building");

		builder.setLocationLat(-15);
		builder.setLocationLon(15);
		builder.setLocationBuilding(building);
		builder.setLocationRoom("Lecture hall");

		Section* section = builder.buildSection();
		//course.addSection(section);
		std::cout << *section << std::endl;
	}
	
	
	return 0;
}
