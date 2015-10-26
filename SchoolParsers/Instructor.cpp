#include "Instructor.hpp"
namespace CourseInfo{
	/*
	 * ======================================================
	 * Object Creation
	 * ======================================================
	 */
	Instructor::Instructor(string name){
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

	string Instructor::getName()const{
		return this->_name;
	}

	void Instructor::setName(string name){
		this->_name = name;
	}
}
