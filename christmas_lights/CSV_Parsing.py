#Lights flash in sequence with the first minute of
#Wizards in Winter by Trans-Siberian Orchestra

import csv
import RPi.GPIO as GPIO, time

import pygame

pygame.init()
pygame.display.set_mode((200,100))

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
GPIO.output(str_1, True)
GPIO.setup(str_2,  GPIO.OUT)
GPIO.output(str_2, False)
time.sleep(1.0)

#GPIO2
GPIO.output(str_2, True)
GPIO.setup(str_3,  GPIO.OUT)
GPIO.output(str_3, False)
time.sleep(1.0)

#GPIO3
GPIO.output(str_3, True)
GPIO.setup(str_4,  GPIO.OUT)
GPIO.output(str_4, False)
time.sleep(1.0)

GPIO.output(str_4, True)

#Open file containing light sequence
sequence = open('WiW.csv')

#Play the song
pygame.mixer.music.load("Wizards.mp3")
pygame.mixer.music.play(-1,0.0)

#Wait a little bit for the song to start
time.sleep(0.42)

#Parse the file with the light sequence
while True:
	my_line = sequence.readline()
	if not my_line: break
	my_line2 = my_line.split(",")

	if(my_line2[0] == "255"):
		GPIO.output(str_1, False)
	elif(my_line2[0] == "0"):
		GPIO.output(str_1, True)

	if(my_line2[1] == "255"):
		GPIO.output(str_2, False)
	elif(my_line2[1] == "0"):
		GPIO.output(str_2, True)

	if(my_line2[2] == "255"):
		GPIO.output(str_3, False)
	elif(my_line2[2] == "0"):
		GPIO.output(str_3, True)

	if "255" in my_line2[3]:
		GPIO.output(str_4, False)
	elif "0" in my_line2[3]:
		GPIO.output(str_4, True)

	time.sleep(0.05)

clock = pygame.time.Clock()
clock.tick(10)
while pygame.mixer.music.get_busy():
    pygame.event.poll()
    clock.tick(10)


sequence.close()
