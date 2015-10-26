#include "Section.hpp"
namespace CourseInfo{
	/*
	 * ======================================================
	 * Object Creation
	 * ======================================================
	 */

	Section::Section(){
		this->_sectionName = "";
		this->_sectionType = "";
		this->_description = "";

		this->_instructor = NULL;
		this->_daysOfWeek = NULL;
		this->_dates = NULL;
		this->_bulding = NULL;
	}
	
	Section::~Section(){
		delete this->_instructor;
		delete this->_daysOfWeek;
		delete this->_dates;
		delete this->_bulding;
	}

	Section::Section(const Section& copy){
		this->_sectionName = copy._sectionName;
		this->_sectionType = copy._sectionType;
		this->_description = copy._description;

		*(this->_instructor) = *(copy._instructor);
		*(this->_daysOfWeek) = *(copy._daysOfWeek);
		*(this->_dates) 	 = *(copy._dates);
		*(this->_bulding)	 = *(copy._bulding);
	}

	Section& Section::operator=(const Section& copy){
		this->_sectionName = copy._sectionName;
		this->_sectionType = copy._sectionType;
		this->_description = copy._description;

		*(this->_instructor) = *(copy._instructor);
		*(this->_daysOfWeek) = *(copy._daysOfWeek);
		*(this->_dates) 	 = *(copy._dates);
		*(this->_bulding)	 = *(copy._bulding);

		return *this;

	}

	/*
	 * ======================================================
	 * Setters
	 * ======================================================
	 */


	void Section::setSectionName(string sectionName){
		this->_sectionName = sectionName;
	}

	void Section::setSectionType(string sectionType){
		this->_sectionType = sectionType;
	}
	
	void Section::setDescription(string description){
		this->_description = description;
	}

	void Section::setInstructor(Instructor* instructor){
		this->_instructor = instructor;
	}
	
	void Section::setDaysOfWeek(Week* daysOfWeek){
		this->_daysOfWeek = daysOfWeek;
	}
	
	void Section::setSemester(Semester* semester){
		this->_dates = semester;
	}

	void Section::setLocation(Location* building){
		this->_bulding = building;
	}

	/*
	 * ======================================================
	 * Getters
	 * ======================================================
	 */


	string Section::getSectionName() const{
		return this->_sectionName;
	}

	string Section::getSectionType() const{
		return this->_sectionType;
	}

	string Section::getDescription() const{
		return this->_description;
	}

	Instructor* Section::getInstructor() const{
		return this->_instructor;
	}

	Week* Section::getWeek() const{
		return this->_daysOfWeek;
	}

	Semester* Section::getSemester() const{
		return this->_dates;
	}

	Location* Section::getBuilding() const{
		return this->_bulding;
	}
}
