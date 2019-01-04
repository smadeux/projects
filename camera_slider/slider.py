import subprocess
from time import sleep
import sys
import re
import RPi.GPIO as GPIO

#Function to run a demo
def demoRun():
    #Gphoto2 commands
    prog_name = "gphoto2"
    take_pic = "--trigger-capture"

    get_config = "--get-config"
    set_config = "--set-config"
    auto_detect = "--auto-detect"

    #Configurations
    pics_left = "availableshots"
    battery_level = "batterylevel"
    shutter_speed = "shutterspeed"

    #Check if camera is connected
    while(1):
        CL_output = subprocess.check_output([prog_name, auto_detect])
        camera_info = CL_output.split('-'*58+'\n')
        if(len(camera_info[1]) > 0):
            camera_name = (camera_info[1].split(' '*2))[0]
            print("Slider connected to " + camera_name)
            connected = True
            break
        else:
            print("Camera is either turned off or not connected to slider")
            choice = raw_input("Do you wish to try \"again\" or \"continue\"? ")
            if(choice == "continue"):
                connected = False
                break
            elif(choice != "again"):
                print("Invalid input")

    #spacer
    print("...")

    #Slider constants
    steps_per_inch = 1015

    #Raspberry Pi Setup
    DIR = 12    #Direction Pin
    STEP = 16   #Step Pin
    #Microstep Pins
    MS1 = 5    		#MS1-3 are for micro stepping
    MS2 = 6
    MS3 = 13
    SPR = 400       #Steps per Rev
    SPR16 = 6400    #1/16th steps per rev


    #Set up GPIO
    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(DIR, GPIO.OUT)
    GPIO.setup(STEP, GPIO.OUT)
    GPIO.setup(MS1, GPIO.OUT)
    GPIO.setup(MS2, GPIO.OUT)
    GPIO.setup(MS3, GPIO.OUT)

    #Set microsteps
    GPIO.output(MS1, 1)
    GPIO.output(MS2, 1)
    GPIO.output(MS3, 1)

    
    #inch per second delays
    quarter_sec_per_inch = 0.000246
    half_sec_per_inch = 0.000493
    one_sec_per_inch = 0.000985
    two_sec_per_inch = 0.00197
    three_sec_per_inch = 0.00296

    input = raw_input("Hit \"Enter\" to see a demo!")
    print("Running Demo...")

    GPIO.output(DIR, 1)
    
    for x in range(int(steps_per_inch*1.5)):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(two_sec_per_inch/2.0)
        GPIO.output(STEP, GPIO.LOW)
        sleep(two_sec_per_inch/2.0)

    if(connected):
        out_put = subprocess.check_output([prog_name, take_pic])
        sleep(1.5)

    GPIO.output(DIR, 0)
        

    for x in range(steps_per_inch*1):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(one_sec_per_inch/2.0)
        GPIO.output(STEP, GPIO.LOW)
        sleep(one_sec_per_inch/2.0)

    if(connected):
        out_put = subprocess.check_output([prog_name, take_pic])
        sleep(1.5)

    GPIO.output(DIR, 1)

    for x in range(250):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(half_sec_per_inch/2.0)
        GPIO.output(STEP, GPIO.LOW)
        sleep(half_sec_per_inch/2.0)
       
    for x in range(int(steps_per_inch*6.5)):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(quarter_sec_per_inch/2.0)
        GPIO.output(STEP, GPIO.LOW)
        sleep(quarter_sec_per_inch/2.0)

    if(connected):
        out_put = subprocess.check_output([prog_name, take_pic])
        sleep(1)
    
    GPIO.output(DIR, 0)
            
    sleep(0.5)

    for x in range(int(steps_per_inch*2.5)):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(one_sec_per_inch/2.0)
        GPIO.output(STEP, GPIO.LOW)
        sleep(one_sec_per_inch/2.0)

    if(connected):
        out_put = subprocess.check_output([prog_name, take_pic])
        sleep(1)

    for x in range(int(steps_per_inch*2.5)):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(half_sec_per_inch/2.0)
        GPIO.output(STEP, GPIO.LOW)
        sleep(half_sec_per_inch/2.0)

    if(connected):
        out_put = subprocess.check_output([prog_name, take_pic])
        sleep(1)

    for x in range(steps_per_inch*2):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(half_sec_per_inch/2.0)
        GPIO.output(STEP, GPIO.LOW)
        sleep(half_sec_per_inch/2.0)

    if(connected):
        out_put = subprocess.check_output([prog_name, take_pic])
        sleep(1)

    print("...")
    print("Demo complete.")
    sleep(1)



print("\n   _____ ___     __             ____  ____  ____  ____ ")
print("  / ___// (_)___/ /__  _____   / __ \\/ __ \\/ __ \\/ __ \\")
print("  \\__ \\/ / / __  / _ \\/ ___/  / /_/ / / / / / / / / / /")
print(" ___/ / / / /_/ /  __/ /      \\__, / /_/ / /_/ / /_/ / ")
print("/____/_/_/\\__,_/\\___/_/      /____/\\____/\\____/\\____/ \n\n")
print("Welcome...\n")


#Set Up Command Line Variables
while(1):
    if(len(sys.argv) == 1):
        #promt for information
        print("Tell me a bit about your timelapse...")
        shot_interval_sec = float(input("How many seconds in between shots? "))
        lapse_length_min = float(input("How many minutes long will your timelapse be? "))
        lapse_distance_ft = float(input("How many feet do you want the camera to move? "))
        while(1):
            direction  = raw_input("Are you starting \"close\" to or \"away\" from the motor? ")
            if(direction == "close" or direction == "away"):
                break
            else:
                print("Invalid input")
        break
    elif(len(sys.argv) == 2 and sys.argv[1] == "demo"):
        demoRun()
        quit()
    elif(len(sys.argv) < 5):
        print ("Correct format: python <prog.py> <shot_interval_sec> <lapse_length_min> <lapse_distance_ft> <close to/away from motor>")
        quit()
    elif(len(sys.argv) == 5):
        shot_interval_sec = double(sys.argv[1])
        lapse_length_min = double(sys.argv[2])
        lapse_distance_ft = double(sys.argv[3])
        direction = string(sys.argv[4])
        if(direction == "close" or direction == "away"):
            break
        else:
            print("Invalid input for camera position")
            quit()
        break

#spacer
print("...")

#Gphoto2 commands
prog_name = "gphoto2"
take_pic = "--trigger-capture"
get_config = "--get-config"
set_config = "--set-config"
auto_detect = "--auto-detect"

#Configurations
pics_left = "availableshots"
battery_level = "batterylevel"
shutter_speed = "shutterspeed"

#Check if camera is connected
while(1):
    CL_output = subprocess.check_output([prog_name, auto_detect])
    camera_info = CL_output.split('-'*58+'\n')
    if(len(camera_info[1]) > 0):
        camera_name = (camera_info[1].split(' '*2))[0]
        print("Slider connected to " + camera_name)
        connected = True
        break
    else:
        print("Camera is either turned off or not connected to slider")
        choice = raw_input("Do you wish to try \"again\" or \"continue\"? ")
        if(choice == "continue"):
            connected = False
            break
        elif(choice != "again"):
            print("Invalid input")

#spacer
print("...")


#Find shutter speed
if(connected):
    CL_output = subprocess.check_output([prog_name, get_config, shutter_speed])
    results = (re.search('Current: (.*)\n', CL_output)).group(1).split('/')
    if(len(results) > 1):
        shutter_speed_sec = 1.0/int(results[1])
    else:
        shutter_speed_sec = float(results[0])
else:
    speed = raw_input("What is your camera's shutter speed in seconds (fraction or decimal)? ")
    results = speed.split('/')
    if(len(results) > 1):
        shutter_speed_sec = 1.0/int(results[1])
    else:
        shutter_speed_sec = float(results[0])


#System Limits
MAX_LENGTH_IN = 38
MIN_SHUTTER_SPEED = 0
MAX_SPEED_DISTANCE_RATIO = 0

#Slider constants
steps_per_inch = 1015

#Raspberry Pi Setup
DIR = 12    #Direction Pin
STEP = 16   #Step Pin
#Microstep Pins
MS1 = 5    
MS2 = 6
MS3 = 13
close_motor = 1
away_motor = 0 
SPR = 400       #Steps per Rev
SPR16 = 6400    #1/16th steps per rev


#Set up GPIO
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(DIR, GPIO.OUT)
GPIO.setup(STEP, GPIO.OUT)
GPIO.setup(MS1, GPIO.OUT)
GPIO.setup(MS2, GPIO.OUT)
GPIO.setup(MS3, GPIO.OUT)
if(direction == "away"):
    GPIO.output(DIR, away_motor)
elif(direction == "close"):
    GPIO.output(DIR, close_motor)

#Set microsteps
GPIO.output(MS1, 1)
GPIO.output(MS2, 1)
GPIO.output(MS3, 1)


#Available time until next picture
time_between_pics_sec = shot_interval_sec - shutter_speed_sec
#print("time_between_pics_sec: %f", time_between_pics_sec)

shot_move_buffer_sec = time_between_pics_sec * .05
#print("shot_move_buffer_sec: %f", shot_move_buffer_sec)

lapse_length_sec = float(lapse_length_min*60)
#print("lapse_length_sec: %f", lapse_length_sec)

num_intervals = int(lapse_length_sec / shot_interval_sec)
#print("num_intervals: %f", num_intervals)

#lapse_distance
lapse_distance_in = float(lapse_distance_ft*12)
#print("lapse_distance_in: %f", lapse_distance_in)

total_num_steps = int(steps_per_inch * lapse_distance_in)
#print("total_num_steps: %f", total_num_steps)

steps_per_interval = int(total_num_steps/num_intervals)
#print("steps_per_interval: %f", steps_per_interval)

move_time_sec = float(time_between_pics_sec - (shot_move_buffer_sec*2))
#print("move_time_sec: %f", move_time_sec)

sleep_between_steps = float(move_time_sec/steps_per_interval)
#print("sleep_between_steps: %f", sleep_between_steps)

#Make sure everything is good and then start
while(1):
    choice = raw_input("Type \"go\" when ready to begin or \"demo\" to exit: ")
    if(choice == "demo"):
        demoRun()
        quit()
    elif(choice != "go"):
        print("Invalid input")
    else:
        break
print("Taking timlapse...")
for pics in range(num_intervals):
    #make sure output is blank
    if(connected):
        out_put = subprocess.check_output([prog_name, take_pic])
    sleep(shutter_speed_sec)
    sleep(shot_move_buffer_sec)
    for steps in range(steps_per_interval):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(sleep_between_steps/2.0)
        GPIO.output(STEP, GPIO.LOW)
        sleep(sleep_between_steps/2.0)
    sleep(shot_move_buffer_sec)
    #Put in checks for battery level and camera memory

#We're all done here
print("...")
print("All finished! Enjoy your timelapse!")
