#include "Location.hpp"
/*
 * ======================================================
 * Object Creation
 * ======================================================
 */

Section::Location::Location(double latitude, double longitude){
	if(not Location::validLatitude(latitude) or not Location::validLongitude(longitude)){
		this->_latitude  = 0;
		this->_longitude = 0;
	}
	else{
		this->_latitude	 = latitude;
		this->_longitude = longitude;
	}
	this->_buildingName = "";
	this->_roomNumber = "";
}
Section::Location::~Location(){
	return;
}

Section::Location::Location(const Location& copy){
	this->_latitude = copy._latitude;
	this->_longitude = copy._longitude;
	this->_buildingName = copy._buildingName;
	this->_roomNumber = copy._roomNumber;
}

Section::Location& Section::Location::operator=(const Location& copy){
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
 
bool Section::Location::setCoord(double latitude, double longitude){
	if(not validLatitude(latitude) or not validLongitude(longitude)){
		return false;
	}
	this->_latitude	 = latitude;
	this->_longitude = longitude;
	return true;
}
bool Section::Location::setLatitude(double latitude){
	if(not validLatitude(latitude)){
		return false;
	}
	this->_latitude  = latitude;
	return true;
}
bool Section::Location::setLongitude(double longitude){
	if(not validLongitude(longitude)){
		return false;
	}
	this->_longitude  = longitude;
	return true;
}

void Section::Location::setBuildingName(std::string building){
	this->_buildingName = building;
}
void Section::Location::setRoomNumber(std::string roomNumber){
	this->_roomNumber = roomNumber;
}

double Section::Location::getLatitude() const{
	return this->_latitude;
}
double Section::Location::getLongitude() const{
	return this->_longitude;
}

std::string Section::Location::getBuildingName() const{
	return this->_buildingName;
}
std::string Section::Location::getRoomNumber() const{
	return this->_roomNumber;
}
/*
 * ======================================================
 * Validators
 * ======================================================
 */

bool Section::Location::validLatitude(double latitude){
	return (latitude >= -90) and (latitude <= 90);
}
bool Section::Location::validLongitude(double longitude){
	return (longitude >= -180) and (longitude <= 180);
}

