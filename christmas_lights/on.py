#Just simply turn the lights on one by one and keep them that way

import RPi.GPIO as GPIO, time

str_1 = 6
str_2 = 13
str_3 = 19
str_4 = 26

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
time.sleep(1.0)

#GPIO0
GPIO.setup(str_1, GPIO.OUT)
GPIO.output(str_1, False)
time.sleep(1.0)

#GPIO1
GPIO.setup(str_2,  GPIO.OUT)
GPIO.output(str_2, False)
time.sleep(1.0)

#GPIO2
GPIO.setup(str_3,  GPIO.OUT)
GPIO.output(str_3, False)
time.sleep(1.0)

#GPIO3
GPIO.setup(str_4,  GPIO.OUT)
GPIO.output(str_4, False)
time.sleep(1.0)
