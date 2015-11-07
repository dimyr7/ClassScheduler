#ifndef LOCATION_H
#define LOCATION_H

#include <string>
#include "Section.hpp"

class Section::Location{
	friend class Section;
	public: 
		/*
		* getters
		*/
		double  getLatitude() const;
		double 	getLongitude() const;

		std::string 	getBuildingName() const;
		std::string  getRoomNumber() const;
		
		friend std::ostream& operator<<(std::ostream& os, const Location& location);
	private:
		/*
		* if the lat or long is not valid, it is set to (0, 0)
		*/
		Location(double latitude = 0, double longitude = 0);
		/*
		* Location destructor
		*/
		~Location();
		/*
		* Copy constructor
		*/
		Location(const Location&  copy);
		/*
		* Assignment operator
		*/
		Location& operator=(const Location& copy);


		/*
		* setters
		* Returns true on success
		* Return false otherwise and doesnt  change anything
		*/
		bool setCoord(double latitude, double longitude);
		bool setLatitude(double latitude);
		bool setLongitude(double longitude);

		void setBuildingName(std::string building);	
		void setRoomNumber(std::string roomNumber);



		/*
		* validators
		*/
		static bool validLatitude(double latitude);
		static bool validLongitude(double longitude);

		/*
		* data members
		*/
		double _latitude;
		double _longitude;
		std::string _buildingName;
		std::string _roomNumber;

};
#endif
