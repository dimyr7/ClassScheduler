#include "Course/Instructor.hpp"
#include <iostream>
/*
 * ======================================================
 * Object Creation
 * ======================================================
 */
Instructor::Instructor(std::string name){
	this->_name = name;
}
Instructor::~Instructor(){
	return;
}
Instructor::Instructor(const Instructor& copy){
	this->_name = copy._name;
}
Instructor& Instructor::operator=(const Instructor& copy){
	this->_name = copy._name;
	return *this;
}
/*
 * ======================================================
 * Getters and Setters
 * ======================================================
 */

std::string Instructor::getName() const{
	return this->_name;
}

std::ostream& operator<<(std::ostream& os, const Instructor& instructor){
		os << instructor.getName() << std::endl;
		return os;
}
