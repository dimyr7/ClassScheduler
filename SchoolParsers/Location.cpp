#include "Location.hpp"
namespace CourseInfo{
Location::Location(double latitude, double longitude){
	if(not validLatitude(latitude) or not validLongitude(longitude)){
		this->_latitude  = latitude;
		this->_longitude = longitude;
	}
	else{
		this->_latitude	 = latitude;
		this->_longitude = longitude;
	}
}
Location::~Location(){
	return;
}

bool Location::setCoord(double latitude, double longitude){
	if(not validLatitude(latitude) or not validLongitude(longitude)){
		return false;
	}
	this->_latitude	 = latitude;
	this->_longitude = longitude;
	return true;
}
bool Location::setLatitude(double latitude){
	if(not validLatitude(latitude)){
		return false;
	}
	this->_latitude  = latitude;
	return true;
}
bool Location::setLongitude(double longitude){
	if(not validLongitude(longitude)){
		return false;
	}
	this->_longitude  = longitude;
	return true;
}

void Location::setBuildingName(string building){
	this->_buildingName = building;
}
void Location::setRoomNumber(string roomNumber){
	this->_roomNumber = roomNumber;
}

double Location::getLatitude(){
	return this->_latitude;
}
double Location::getLongitude(){
	return this->_longitude;
}

string Location::getBuildingName(){
	return this->_buildingName;
}
string Location::getRoomNumber(){
	return this->_roomNumber;
}

bool Location::validLatitude(double latitude){
	return (latitude >= -90) and (latitude <= 90);
}
bool Location::validLongitude(double longitude){
	return (longitude >= -180) and (longitude <= 180);
}
}
