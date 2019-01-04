import RPi.GPIO as GPIO, time

#The lights start out at a slower pace and speed up until they're going
#as fast as they can go

GPIO.setmode(GPIO.BOARD)
time.sleep(1.0)

#GPIO0
GPIO.setup(11, GPIO.OUT)
GPIO.output(11, True)
time.sleep(1.0)

#GPIO1
GPIO.output(11, False)
GPIO.setup(12,  GPIO.OUT)
GPIO.output(12, True)
time.sleep(1.0)

#GPIO2
GPIO.output(12, False)
GPIO.setup(13,  GPIO.OUT)
GPIO.output(13, True)
time.sleep(1.0)

#GPIO3
GPIO.output(13, False)
GPIO.setup(15,  GPIO.OUT)
GPIO.output(15, True)
time.sleep(1.0)

GPIO.output(11, True)
GPIO.output(12, True)
GPIO.output(13, True)

sleep = 0.5
num_times = 0
while (num_times < 4):
        if num_times == 0:
                count = 2
        elif num_times == 1:
                count = 4
        elif num_times == 2:
                count = 4
        elif num_times == 3:
                count = 4

        while (count > 0):
                GPIO.output(11, False)
                time.sleep(sleep)

                GPIO.output(11, True)
                GPIO.output(12, False)
                time.sleep(sleep)

                GPIO.output(12, True)
                GPIO.output(13, False)
                time.sleep(sleep)

                GPIO.output(13, True)
                GPIO.output(15, False)
                time.sleep(sleep)

                GPIO.output(15, True)
                count = count - 1

        num_times = num_times + 1
        sleep = sleep/2

sleep = sleep*2
        
while True:
        GPIO.output(11, False)
        time.sleep(sleep)

        GPIO.output(11, True)
        GPIO.output(12, False)
        time.sleep(sleep)

        GPIO.output(12, True)
        GPIO.output(13, False)
        time.sleep(sleep)

        GPIO.output(13, True)
        GPIO.output(15, False)
        time.sleep(sleep)

        GPIO.output(15, True)
