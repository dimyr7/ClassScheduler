#include <Communication/CourseStoreDB.hpp>

#include <sys/types.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netdb.h>
#include <iostream>
#include <vector>

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>


#define OK 200
#define HTTP_V "1.0"
#define MB 1048576
CourseStoreDB::CourseStoreDB(std::string host, std::string path, std::string port){
	this->_host = host;
	this->_path = path;
	this->_port = port;
}

CourseStoreDB::CourseStoreDB(const CourseStoreDB &copy){
	this->_host = copy._host;
	this->_path = copy._path;
	this->_port = copy._port;
}

CourseStoreDB::~CourseStoreDB(){
	return;
}


CourseStoreDB& CourseStoreDB::operator=(const CourseStoreDB &copy){
	this->_host = copy._host;
	this->_path = copy._path;
	this->_port = copy._port;
	return *this;
}

std::string* CourseStoreDB::getJson(){
	struct addrinfo hints;
	struct addrinfo *res;

	memset(&hints, 0, sizeof(struct addrinfo));
	
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	int getAddrInfoFlag = getaddrinfo(this->_host.c_str(), this->_port.c_str(), &hints, &res);
	if(getAddrInfoFlag != 0){
		printf("Failure on getAddrInfo()\n");
		exit(1);
	}
 
	int sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
	if(sockfd == -1){
		printf("Failure on socket()\n");
		exit(1);
	}

	int connectFlag = connect(sockfd, res->ai_addr, res->ai_addrlen);
	if(connectFlag != 0){
		printf("Failure on connect()\n%s\n", strerror(errno));
		exit(1);
	}

	std::string query = "GET " + this->_path + " HTTP/" +HTTP_V +"\r\n\r\n";

	if(sendMessage(sockfd, query) != true){
		return new std::string("");
	}

	std::string message = this->recvMessage(sockfd, 3*MB);

	if(not this->verifyStatus(message, OK)){
		std::cerr << "Message status failed" << std::endl;
		return new std::string("");
	}

	message = message.substr(message.find("\r\n\r\n")+4);

	


	freeaddrinfo(res);
	close(sockfd);
	return new std::string(message);
}

bool CourseStoreDB::verifyStatus(std::string message, int status){
	if(message.substr(0, 5).compare("HTTP/") != 0){
		return false;
	}
	else if(message.substr(5,3).compare(HTTP_V) != 0){
		return false;
	}
	else if(message.substr(8,1).compare(" ") != 0){
	}
	else if(stoi(message.substr(9,3)) != status){
		return false;
	}
	return true;
}
bool CourseStoreDB::sendMessage(int sockfd, std::string msg){
	size_t sentBytes = send(sockfd, (void*)msg.c_str(), strlen(msg.c_str()), 0);
	if(sentBytes != strlen(msg.c_str())){
		printf("Mesaege: %lu bytes\n Sent: %lu bytes\n", strlen(msg.c_str()), sentBytes);
		return false;
	}
	return true;
}

std::string CourseStoreDB::recvMessage(int sockfd, int length){ 
	//std::cerr<< "*** Calling recv() ***" << std::endl;
	char buf[length];
	int readBytes = recv(sockfd, buf, length, MSG_WAITALL);
	if(readBytes == -1){
		std::cerr<< "Failure on recv()" << std::endl;
		std::cerr<< "Read " << readBytes <<" bytes\n" << std::endl;
		return "";
	}
	return std::string(buf);
}
