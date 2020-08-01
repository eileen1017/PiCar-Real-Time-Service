import RPi.GPIO as GPIO
from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
from datetime import date, datetime
from picar import PiCar
from picamera import PiCamera
import picamera.array
import time
import cv2
import numpy
import math
import uuid 


# parameters
minDist = 20
    
def runAvoidance():
     
    # set up AWS IoT certificate based connection
    myMQTTClient = AWSIoTMQTTClient("123afhlss411")
    myMQTTClient.configureEndpoint("[YOUR-AWS-IOT-ENDPOINT]", 8883)
    myMQTTClient.configureCredentials("AmazonRootCA1.pem", 
                      "[YOUR]-private.pem.key", 
                      "[YOUR]-certificate.pem.crt")
    myMQTTClient.configureOfflinePublishQueueing(-1)  # Infinite offline Publish queueing
    myMQTTClient.configureDrainingFrequency(2)  # Draining: 2 Hz
    myMQTTClient.configureConnectDisconnectTimeout(10)  # 10 sec
    myMQTTClient.configureMQTTOperationTimeout(5)  # 5 sec
    #connect and publish
    myMQTTClient.connect()
    myMQTTClient.publish("[YOUR-TOPIC]", "connected", 0)

    # setup car
    car = PiCar(mock_car=False)
    print(car)
    camera = PiCamera()
    stream=picamera.array.PiRGBArray(camera)
    # car initial set up
    # set all servos to middle
    car.set_nod_servo(2)
    car.set_swivel_servo(0)
    car.set_steer_servo(0)
    car.set_motor(85)

    dist = car.read_distance()
    #loop and publish sensor reading
    while 1:
        now = datetime.now()
        now_str = now.strftime('%Y-%m-%dT%H:%M:%S')
        randomSeed = "Avoidance"
        payload = '{ "dev_id": "'+  randomSeed  +'","time": "' + str(now) + '","distance": ' +str(dist) + ' }'
        print('{ "timestamp": "' + str(now) + '","distance": ' + str(dist) + ' }')    
        myMQTTClient.publish("picar_project/test", payload, 0)
        if dist < minDist and car.read_distance() < minDist: # stop if too close
            myMQTTClient.publish("[YOUR-TOPIC]", "car stop", 0)
            car.set_motor(0) # stop
            break
        else: # update time for next distance reading
            time.sleep(0.1)
            dist = car.read_distance()
    camera.close()
    GPIO.cleanup()
        
def main():
    runAvoidance()
