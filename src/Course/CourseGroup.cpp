#include "Course/CourseGroup.hpp"
#include "Course/Course.hpp"
#include <valarray>
#include <string>

CourseGroup::CourseGroup(size_t numCourses){
	this->_numCourses = numCourses;
	for(size_t i = 0; i < numCourses; i++){
		this->_courses[i] = NULL;
	}
}

CourseGroup::CourseGroup(const CourseGroup& copy){
	this->_numCourses = copy._numCourses;
	for(size_t i = 0; i < this->_numCourses; i++){
		this->_courses[i] = new Course(*(copy._courses[i]));
	}
}

CourseGroup& CourseGroup::operator=(const CourseGroup& copy){
	this->_numCourses = copy._numCourses;
	for(size_t i = 0; i < this->_numCourses; i++){
		this->_courses[i] = new Course(*(copy._courses[i]));
	}
	return *this;
}


CourseGroup::~CourseGroup(){
	std::cout << "Deleting CourseGroup" << std::endl;
}

bool CourseGroup::addCourse(Course* course){
	for(size_t i = 0; i < this->_numCourses; i++){
		if(this->_courses[i] == NULL){
			this->_courses[i] = course;
			return true;
		}
		else if(this->_courses[i] == course){
			return true;
		}
	}
	return false;
}

std::vector<Schedule*> CourseGroup::genSchedules(){
	// The the value at index[i] will represent the index of the sectioncombo of course i
	std::valarray<size_t> index = std::valarray<size_t>(this->_numCourses);

	// Possible schedules
	std::vector<Schedule*> schedules;

	// Reset all index locations
	for(auto it = std::begin(index); it != std::end(index); it++){
		*it = 0;
	}
	do{
		std::vector<SectionCombo*> propCombo;
		// Grab the sectionscombos for each course
		for(size_t i = 0; i < index.size(); i++){
			size_t indexOfCourseCombo = index[i];
			Course* currCourse = this->_courses[i];
			std::vector<SectionCombo*> currCombos = currCourse->getCombos();
			propCombo.push_back(currCombos[indexOfCourseCombo]);
		}
		// Check if the sectioncombos overlap
		bool overlap = CourseGroup::overlap(propCombo);
		if(not overlap){
			Schedule* newSchedule = new Schedule(propCombo);
			schedules.push_back(newSchedule);
		}

	}
	while(this->nextIteration(index));
	return schedules;
}

bool CourseGroup::nextIteration(std::valarray<size_t> &index){
	for(size_t i = 0; i < index.size(); i++){
		size_t indexVal = index[i];
		if(indexVal+1 == this->_courses[i]->getCombos().size()){
			index[i] = 0;
		}
		else{
			index[i]++;
			return true;
		}
	}
	return false;
}

bool CourseGroup::overlap(std::vector<SectionCombo*> potCombo){
	for(size_t i = 0; i < potCombo.size()-1; i++){
		for(size_t j = 1; j < potCombo.size(); j++){
			SectionCombo* comboA = potCombo[i];
			SectionCombo* comboB = potCombo[j];
			bool overlap = SectionCombo::overlap(comboA, comboB);
			if(overlap){
				return true;
			}
		}
	}
	return false;
}

