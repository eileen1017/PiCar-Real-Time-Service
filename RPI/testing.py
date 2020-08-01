# for car 5
from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
from datetime import date, datetime
import RPi.GPIO as GPIO # for motor
from picar import PiCar
from picamera import PiCamera
import picamera.array
import time
import cv2
import math
import sys

# car = PiCar(mock_car=False) 
# camera = PiCamera()
# stream=picamera.array.PiRGBArray(camera)

factor = 2
#desiredSpeed = 4
minDist = 20
#timeBetweenDisplay = 0.25 # 0.25/0.1
#timeBetweenSample = 0.005
timeBetweenPic = 0.2

def updateDirection(camera, stream, car, dist,myMQTTClient):
    #print ("taking picture")
    camera.capture(stream, format='bgr', use_video_port=True)
    img = stream.array
    #cv2.imwrite('img1.png', img)
    stream.truncate(0)
    hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    mask = cv2.inRange(hsv, (20,110,100), (35, 255, 255)) # yellow
    #cv2.imwrite('mask1.png', mask)
    message = ""
    Mblue = cv2.moments(mask)
    if Mblue["m00"] == 0:
        ang = 45
        message = "Recognization: yellow not found"
        print("yellow not found")
        
    else:
        cxB = int(Mblue["m10"]/Mblue["m00"]) # center x
        cyB = int(Mblue["m01"]/Mblue["m00"]) # center y
        ang = findAngle(cxB, cyB)
        # calculate duty cycle
        ang = ang - 90
        if ang > 90:
            ang = 90
        elif ang < -90:
            ang = -90
        message = "Recognization: yellow found at " + str(ang)
        print("yellow found at ", str(ang))
    cycle = angle2dutycycle(ang) / factor
    car.set_steer_servo(cycle)
    now = datetime.now()
    now_str = now.strftime('%Y-%m-%dT%H:%M:%S')
    payload = '{ "dev_id": "'+  message  +'","time": "' + str(now) + '","distance": ' +str(dist) + ' }'
    #print('{ "timestamp": "' + str(now) + '","distance": ' + str(dist) + ' }')    
    myMQTTClient.publish("[YOUR-TOPIC]t", payload, 0)
    #print("cycle", cycle)

def angle2dutycycle(angle): # angle: [-90, 90]
    # calculate duty cycle
    return -angle/9

def findAngle(cx, cy):
    y = 768-cy
    if cx < 680:# Q2
        # left part
        x = 680 - cx
        ang = math.atan2(y, x) * 180 / math.pi # angle in degree
    else:# Q1
        x = cx - 680
        ang = 90 + math.atan2(x, y) * 180 / math.pi # angle in degree
    #print("angle: ", ang)
    return ang

def runRecognization(car, camera, stream):
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

    
    car.set_motor(85)
    currTime = time.time()
    startTime = currTime
    calculationTime = currTime # and calculationTime
    lastPicTime = currTime

    # set all servos to middle
    car.set_nod_servo(2)
    car.set_swivel_servo(0)
    car.set_steer_servo(0)
    
    # read distance, calculate next reading time
    dist = car.read_distance()
    # take picture before start and find direction
    updateDirection(camera, stream, car, dist, myMQTTClient)
    
    while True:
        currTime = time.time()
        # check distance
        dist = car.read_distance()
        print(dist)
        if dist < minDist and car.read_distance() < minDist: # stop if too close
            car.set_motor(0) # stop
            break
        # check direction
        if (timeBetweenPic + lastPicTime) < currTime:
            updateDirection(camera, stream, car, dist,myMQTTClient)
            lastPicTime = currTime
    car.set_motor(0)
    camera.close()
    GPIO.cleanup()
    

def main():
    car = PiCar(mock_car=False) 
    camera = PiCamera()
    stream=picamera.array.PiRGBArray(camera)
    runRecognization(car, camera, stream)