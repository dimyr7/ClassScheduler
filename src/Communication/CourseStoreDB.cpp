ptinclude <Communication/CourseStoreDB.hpp>

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

CourseStoreDB::CourseStoreDB(std::string host, std::string path, std::string port){
	this->_host = host;
	this->_path = path;
	this->_port = port;
	this->_p
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

std::string* CourseStoreDB::getGZip(){
	struct addrinfo hints;
	struct addrinfo *res;

	memset(&hints, 0, sizeof(struct addrinfo));
	
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	int getAddrInfoFlag = getaddrinfo("google.com", "80", &hints, &res);
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

	std::string query = "GET " + this->_path + " HTTP/1.0\r\nHost: " + this->_host + "\r\nConnection: Keep-Alive\r\ncontent-type: application/x-gzip\r\n\r\n";

	if(sendMessege(sockfd, query) != true){
		return new std::string("");
	}

	std::string messege = this->recvMessege(sockfd, 5000);
	//std::cerr << messege << std::endl;
	if(not this->verifyStatus(messege, OK)){
		std::cerr << "Messege status failed" << std::endl;
		return new std::string("");
	}

	int lengthOfNextChunk = stoi(messege.substr(messege.find("\r\n\r\n")), 0, 16);
	std::cerr << "Length of chunk: " << lengthOfNextChunk << std::endl;
	std::vector<std::string> chunks;
	for(int i = 0 ; i < 150; i++){		
//	while((messege = this->recvMessege(sockfd, lengthOfNextChunk+2)).compare("") != 0){
		messege = this->recvMessege(sockfd, lengthOfNextChunk+2);
		std::cout <<  messege << std::endl;
	}

	freeaddrinfo(res);
	close(sockfd);
	return new std::string(messege);
}

bool CourseStoreDB::verifyStatus(std::string messege, int status){
	if(messege.substr(0, 5).compare("HTTP/") != 0){
		return false;
	}
	else if(messege.substr(5,3).compare(HTTP_V) != 0){
		return false;
	}
	else if(messege.substr(8,1).compare(" ") != 0){
	}
	else if(stoi(messege.substr(9,3)) != status){
		return false;
	}
	return true;
}
bool CourseStoreDB::sendMessege(int sockfd, std::string msg){
	size_t sentBytes = send(sockfd, (void*)msg.c_str(), strlen(msg.c_str()), 0);
	if(sentBytes != strlen(msg.c_str())){
		printf("Messege: %lu bytes\n Sent: %lu bytes\n", strlen(msg.c_str()), sentBytes);
		return false;
	}
	return true;
}

std::string CourseStoreDB::recvMessege(int sockfd, int length){ 
	std::cerr<< "*** Calling recv() ***" << std::endl;
	char buf[length];
	
	
	
	int readBytes = recv(sockfd, buf, length, MSG_WAITALL);
	if(readBytes == -1){
		std::cerr<< "Failure on recv()" << std::endl;
		std::cerr<< "Read " << readBytes <<" bytes\n" << std::endl;
		return "";
	}
	//printf("Received: %s\n", buf);
	return std::string(buf);
}
