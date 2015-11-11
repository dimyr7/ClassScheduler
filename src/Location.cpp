#include "Location.hpp"
/*
 * ======================================================
 * Object Creation
 * ======================================================
 */

Location::Location(std::string  buildingName, std:: string room, double latitude, double longitude){
	if(not Location::validLatitude(latitude) or not Location::validLongitude(longitude)){
		this->_latitude  = 0;
		this->_longitude = 0;
	}
	else{
		this->_latitude	 = latitude;
		this->_longitude = longitude;
	}
	this->_buildingName = buildingName;
	this->_roomNumber = room;
}
Location::~Location(){
	return;
}

Location::Location(const Location& copy){
	this->_latitude = copy._latitude;
	this->_longitude = copy._longitude;
	this->_buildingName = copy._buildingName;
	this->_roomNumber = copy._roomNumber;
}

Location& Location::operator=(const Location& copy){
	this->_latitude = copy._latitude;
	this->_longitude = copy._longitude;
	this->_buildingName = copy._buildingName;
	this->_roomNumber = copy._roomNumber;
	return *this;
}
/*
 * ======================================================
 * Getters & Setters
 * ======================================================
 */

double Location::getLatitude() const{
	return this->_latitude;
}
double Location::getLongitude() const{
	return this->_longitude;
}

std::string Location::getBuildingName() const{
	return this->_buildingName;
}
std::string Location::getRoomNumber() const{
	return this->_roomNumber;
}
/*
 * ======================================================
 * Validators
 * ======================================================
 */

bool Location::validLatitude(double latitude){
	return (latitude >= -90) and (latitude <= 90);
}
bool Location::validLongitude(double longitude){
	return (longitude >= -180) and (longitude <= 180);
}

std::ostream& operator<<(std::ostream& os, const Location& location){
	os << location.getBuildingName() << " - " << location.getRoomNumber() << std::endl;
	os << "(" << location.getLatitude() << " , " << location.getLongitude() << ")" << std::endl;
	return os;
}
