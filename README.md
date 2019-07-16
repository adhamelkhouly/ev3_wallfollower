# ev3_wallfollower
EV3 Wall-Following Robot

### Table of Contents

1. [Project Description](#Project)
2. [File Description](#files)
3. [Licensing, Authors, and Acknowledgements](#licensing)

## Project Description<a name="Project"></a>

This project contains a few files and algorithms to be loaded on an EV3 kit and
result in a wall-following robot. 

Parts needed:
- 1 Ultrasonic Sensor
- 2 Motors

## File Descriptions <a name="files"></a>
- /src/
  - bangbang_controller.py: file containing code for a Bang Bang controller. This a simple threshold correction controller
  - main.java : file containing main code to be run on the EV3 kit
  - pc_controller.java :  file containing code for a better version controller. This controling method allows for less sudden movements
  - printer.java : this code controls the EV3 screen output
  - ultrasonic_controller.java : an interface for the Ultrasonic Sensor
  - ultrasonic_poller.java : a thread for constantly polling distance from walls

## Licensing, Authors, Acknowledgements<a name="licensing"></a>
This was done as part of Design Principles and Method at McGill University.
