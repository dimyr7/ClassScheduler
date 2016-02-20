#include "Course/Schedule.hpp"

Schedule::Schedule(){
	return;
}

Schedule::~Schedule(){
	// Don't delete the SectionCombos
	return;
}
Schedule::Schedule(std::vector<SectionCombo*> combos){
	this->_combos = combos;	
}

Schedule::Schedule(const Schedule& copy){
	this->_combos = copy._combos;
}

Schedule& Schedule::operator=(const Schedule& copy){
	this->_combos = copy._combos;
	return *this;
}

void Schedule::addCombo(SectionCombo* combo){
	this->_combos.push_back(combo);
}
std::vector<SectionCombo*> Schedule::getCombos() const{
	return this->_combos;
}
