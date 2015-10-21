#include "Course.hpp"
Course::Course(){
	this->_department = "";
	this->_courseNumber = "";
	//TODO How to initialize vector
	//this->_sections (1, NULL);
}

Course::Course(string department,string  courseNumber){
	this->_department = department;
	if(!Course::validateDepartment(department)){
		this->_department = "";
	}

	this->_courseNumber = courseNumber;
	if(!Course::validateCourseNumber(courseNumber)){
		this->_courseNumber = "";
	}

	//this->_sections();
}

string Course::getDepartment(){
	return _department;
}

string Course::getCourseNumber(){
	return _courseNumber;
}

vector<Section *> Course::getSections(){
	return _sections;
}


bool Course::setDepartment(string department){
	if(Course::validateDepartment(department)){
		this->_department = department;	
		return true;
	}
	return false;
}

bool Course::setCourseNumber(string courseNumber){
	if(Course::validateCourseNumber(courseNumber)){
		this->_courseNumber = courseNumber;
		return true;
	}
	return false;
}


void Course::addSection(Section* section){
	this->_sections.push_back(section);
}

bool Course::validateDepartment(string department){
	// TODO
	return true;
}

bool Course::validateCourseNumber(string courseNumber){
	// TODO
	return true;
}



