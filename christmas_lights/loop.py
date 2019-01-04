#Turn the lights on and off in a loop

import RPi.GPIO as GPIO, time

GPIO.setmode(GPIO.BOARD)
time.sleep(1.0)

#GPIO0
GPIO.setup(11, GPIO.OUT)
GPIO.output(11, True)
time.sleep(2.0)

#GPIO1
GPIO.output(11, False)
GPIO.setup(12,  GPIO.OUT)
GPIO.output(12, True)
time.sleep(2.0)

#GPIO2
GPIO.output(12, False)
GPIO.setup(13,  GPIO.OUT)
GPIO.output(13, True)
time.sleep(2.0)

#GPIO3
GPIO.output(13, False)
GPIO.setup(15,  GPIO.OUT)
GPIO.output(15, True)
time.sleep(2.0)

GPIO.output(15, False)

while (True):
        count1 = 10
	while (count1 > 0):
		GPIO.output(11, False)
		time.sleep(2.0)

		GPIO.output(11, True)
		GPIO.output(12, False)
		time.sleep(2.0)

		GPIO.output(12, True)
		GPIO.output(13, False)
		time.sleep(2.0)

		GPIO.output(13, True)
		GPIO.output(15, False)
		time.sleep(2.0)

		GPIO.output(15, True)
		count1 = count1 - 1

        count2 = 10
	while (count2 > 0):
		GPIO.output(12, False)
		GPIO.output(15, False)
		GPIO.output(11, True)
		GPIO.output(13, True)
		time.sleep(2.0)
		GPIO.output(11, False)
		GPIO.output(13, False)
		GPIO.output(12, True)
		GPIO.output(15, True)
		time.sleep(2.0)
		count2 = count2 - 1
