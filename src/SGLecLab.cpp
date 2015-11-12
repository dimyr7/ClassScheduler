#include "SGLecLab.hpp"
SGLecLab::SGLecLab(std::string id) : Course::SectionGroup(id){
	return;
}

std::vector<Section*> SGLecLab::getLecSections() const{
	return this->_lectures;
}

std::vector<Section*> SGLecLab::getLabSections() const{
	return this->_labs;
}

bool SGLecLab::addSection(Section* section){
	// Is this a lecture
	if(section->getSectionType().compare("Lecture") == 0){
		this->_lectures.push_back(section);
		return true;
	}
	// Is this a lab section
	else if(section->getSectionType().compare("Laboratory") == 0){
		this->_labs.push_back(section);
		return true;
	}
	// this section doesn't belong here
	return false;
}


std::vector<SectionCombo*> SGLecLab::getCombos() const{
	std::vector<SectionCombo*> combos;	
	// For all possible sections
	for(std::vector<Section*>::const_iterator it = this->_lectures.begin(); it != this->_lectures.end(); it++){
		Section* lecture = *it;
		// and all possible labs
		for(std::vector<Section*>::const_iterator is = this->_labs.begin(); is != this->_labs.end(); is++){
			Section* lab = *is;
			if(Section::overlap(lecture, lab)){
				continue;
			}
			SectionCombo* sectionCombo = new SectionCombo();
			sectionCombo->addSection(lecture);
			sectionCombo->addSection(lab);
			combos.push_back(sectionCombo);
		}
	}
	return combos;
}

std::ostream& operator<<(std::ostream& os, const SGLecLab& group){
	os << "=== SectioGroup Lec-Lab ===" << std::endl;
	os << "=== Lectures ===" << std::endl;
	/*
	 * Print all lecture section names
	 */
	for(std::vector<Section*>::const_iterator it = group.getLecSections().begin(); it != group.getLecSections().end(); it++){
		os << (*it)->getSectionName() << " - "<< (*it)->getCRN() << std::endl;
	}

	/*
	 * Print all Lab section names
	 */
	os << "=== Lab ===" <<std::endl;
	for(std::vector<Section*>::const_iterator it = group.getLabSections().begin(); it != group.getLabSections().end(); it++){
		os << (*it)->getSectionName() << " - " << (*it)->getCRN() << std::endl;
	}
	return os;
}
