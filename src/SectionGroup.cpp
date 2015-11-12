#include "SectionGroup.hpp"

Course::SectionGroup::SectionGroup(std::string id){
	this->_id = id;
}


Course::SectionGroup::SectionGroup(const SectionGroup& copy){
	this->_id = copy._id;
}

Course::SectionGroup& Course::SectionGroup::operator=(const SectionGroup& copy){
	this->_id = copy._id;
	return *this;
}

bool Course::SectionGroup::validID(const std::string id){
	return (this->_id.compare(id) == 0);
}

Course::SectionGroup::~SectionGroup(){
	// None of the Sections are deleted 
	return;
}
