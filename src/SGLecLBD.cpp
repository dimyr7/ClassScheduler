#include "SGLecLBD.hpp"
#include "SectionGroup.hpp"
#include "SectionCombo.hpp"
/*
 * Object Creation
 */
SGLecLBD::SGLecLBD(std::string id) : SectionGroup(id){
	return;
}

bool 	SGLecLBD::addSection(Section* section){
	std::string type = section->getSectionType();
	// If the section is a lecture and has the correct identifier
	if(type.compare("Lecture") == 0){
		this->_lectures.push_back(section);
		return true;
	}
	// If the section is a LBD and has the correct identifier 
	else if(type.compare("Laboratory-Discussion") == 0){
		this->_labDiscussions.push_back(section);
		return true;
	}
	// This section doesnt belong here
	return false;
}


std::vector<SectionCombo*> SGLecLBD::getCombos() const{
	std::vector<SectionCombo*> combos;
	
	// For all possilbe lectures
	for(std::vector<Section*>::const_iterator it = this->_lectures.begin(); it != this->_lectures.end(); it++){
		//Section* lecture = *it;
		Section* lecture = *it;
		// For all possible lab-dis 
		for(std::vector<Section*>::const_iterator is = this->_labDiscussions.begin(); is != this->_labDiscussions.end(); is++){
			//Section* labdis = *is;	
			Section* labdis = *is;

			// If the lectures & lab-dis overlap, then its not a valid combination
			if(Section::overlap(lecture, labdis)){
				continue;
			}
			// Otherwise create a new section combo
			SectionCombo* sectionCombo = new SectionCombo();
			sectionCombo->addSection(lecture);
			sectionCombo->addSection(labdis);
			combos.push_back(sectionCombo);
		}
		
	}
	return combos;
}

std::vector<Section*> SGLecLBD::getLecSections()const {
	return this->_lectures;
}

std::vector<Section*> SGLecLBD::getLBDSections() const{
	return this->_labDiscussions;
}


std::ostream& operator<<(std::ostream& os, const SGLecLBD& group){
	os << "=== SectioGroup Lec-LBD ===" << std::endl;
	os << "=== Lectures ===" << std::endl;
	/*
	 * Print all lecture section names
	 */
	for(std::vector<Section*>::const_iterator it = group.getLecSections().begin(); it != group.getLecSections().end(); it++){
		os << (*it)->getSectionName() << " - " << (*it)->getCRN() << std::endl;
	}

	/*
	 * Print all Lab-Dis section names
	 */
	os << "=== Lab/Discussion ===" <<std::endl;
	for(std::vector<Section*>::const_iterator it = group.getLBDSections().begin(); it != group.getLBDSections().end(); it++){
		os << (*it)->getSectionName() << " - " << (*it)->getCRN() << std::endl;
	}
	return os;
}
