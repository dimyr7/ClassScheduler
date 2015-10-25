#include "Instructor.hpp"
namespace CourseInfo{
	Instructor::Instructor(string name){
		this->_name = name;
	}
	Instructor::~Instructor(){
		return;
	}
	string Instructor::getName(){
		return this->_name;
	}

	void Instructor::setName(string name){
		this->_name = name;
	}
}
