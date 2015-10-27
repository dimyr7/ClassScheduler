#include "Instructor.hpp"
/*
 * ======================================================
 * Object Creation
 * ======================================================
 */
Section::Instructor::Instructor(string name){
	this->_name = name;
}
Section::Instructor::~Instructor(){
	return;
}
Section::Instructor::Instructor(const Instructor& copy){
	this->_name = copy._name;
}
Section::Instructor& Section::Instructor::operator=(const Instructor& copy){
	this->_name = copy._name;
	return *this;
}
/*
 * ======================================================
 * Getters and Setters
 * ======================================================
 */

string Section::Instructor::getName()const{
	return this->_name;
}

void Section::Instructor::setName(string name){
	this->_name = name;
}
