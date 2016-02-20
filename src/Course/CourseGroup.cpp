#include "Course/CourseGroup.hpp"
CourseGroup::CourseGroup(size_t numCourses){
	this->_numCourses = numCourses;
	this->_courses = std::valarray<Course*>(numCourses);
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
	std::valarray<size_t> index = std::valarray<size_t>(this->_numCourses);
	std::vector<Schedule*> schedules;
	for(auto it = std::begin(index); it != std::end(index); it++){
		*it = 0;
	}
	do{
		std::vector<SectionCombo*> combinations;
		for(size_t i = 0; i < index.size(); i++){
			combinations.push_back(this->_courses[i]->getCombos()[index[i]]);
		}
		bool overlap = CourseGroup::overlap(combinations);
		if(not overlap){
			Schedule* newSchedule = new Schedule(combinations);
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

