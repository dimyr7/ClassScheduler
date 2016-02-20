#include "Course/CourseStore.hpp"

CourseStore::CourseStore(){
	return;	
}

CourseStore::CourseStore(const CourseStore &copy){
	this->_courses = copy._courses;
}

CourseStore::~CourseStore(){
	return;
}

CourseStore& CourseStore::operator=(const CourseStore &copy){
	this->_courses = copy._courses;
	return *this;
}

bool CourseStore::insert(std::string courseName, Course* courseData){
	if(this->_courses.find(courseName) != this->_courses.end()){
		return false;	
	}
	this->_courses[courseName] = courseData;
	return true;
}


Course* CourseStore::get(std::string courseName){
	if(this->_courses.find(courseName) == this->_courses.end()){
		return NULL;
	}
	return this->_courses.at(courseName);
}
