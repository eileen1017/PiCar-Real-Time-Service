# PiCar-Real-Time-Service

Project Name: PiCar

Submission File Hierarchy:
> PiCar\
> |- FinalReport.pdf
> |- PicarProjectFinal\
>    |- app\src\main\java\com\example\picarprojectfinal\
>        |- MainActivity.java
>        |- PicarData.java
>        |- PicarDO.java
>        |- ShowTable.java
> |- RPI\
>    |- avoid.py
>    |- PicarApp.py
>    |- testing.py

Description of file hierarchy:
1. Readme.txt: this file including file hierarchy and its description, and instruction for installation and execution process.
2. FinalReport.pdf: 6-pages final report of the project
3. FinalPresentation.pdf: final demo slides
4. Result\PicarTable.txt: the downloaded table result from AWS DynamoDB which is collected during Final Demo on 23rd April.
5. PicarProjectFinal folder: the android application of the project
	a. MainActivity.java: main program of the application, consisting action triggers for avoidance, recognization and DynamoDB access.
	b. PicarData.java: data structure used when transferring data from MainActivity and ShowTable ViewController.
	c. PicarDO.java: Module file used when reading data from DynamoDB to the application
	d. ShowTable.java: Set up table view and display data in the application
6. PRI folder: should put in raspberry pi server
	a. avoid.py: python program for avoidance functionality and data transmission to AWS IoT
	b. testing.py: python program for recognization functionality and data transmission to AWS IoT
	c. PicarApp.py: python program for socket connection between raspberry pi and android app


How To Run:
------------AWS service--------------
Make sure all setup finished in AWS services following steps in Section 4.3 of FinalReport.pdf

------------Raspberry Pi--------------
1. Download files(avoid.py, PicarApp.py, testing.py) in RPI folder, and put them into Raspberry Pi server
2. Under the fold where has the files, put AWS IoT policy certificates such as device certificate, private key and root certificate authority into the project folder. 
3. Modify configureEndpoint, configureCredentials with the certificates above, publish topic in files avoid.py and testing.py.
4. Modify Host and Port variables in PicarApp.py to make connection with PicarProjectFinal Android App. 
5. Do INSTALL step 1.
6. Open PicarApp.py and run it. 

------------Android App---------------
1. Download PicarProjectFinal folder under submission file.
2. Do not modify build.gradle, direct to "cd PicarProjectFinal\app\src\main\res\raw", open "awsconfiguration.json", modify PoolID, Region in CredentialsProvider part, and Region in DynamoDBObjectMapper part same as your setup in AWS Cognito and DynamoDB.
3. Open the project with Android Studio, Build the project and Run.

Then, you can put the port and address in the android app, and connect to the raspberry pi and run certain command with button pressed.


Install:
1. Install AWS IoT SDK: In RPI terminal and under project folder, "git clone https://github.com/aws/aws-iot-device-sdk-python", open the folder "cd aws-iot-device-sdk-python/", run "python setup.py install".
2. Make sure to install Android Studio from https://developer.android.com/studio. 
