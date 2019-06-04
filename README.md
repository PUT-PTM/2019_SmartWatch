# 2019_SmartWatch


## Overwiew:
  ePaper display 1,54' supported by Android application. Communication rely on Bluetooth module 2.0.
  Project 
 
## Description:
  Microcontroler communicates with Android app by Bluetooth. Functions included:  
    - displaying incoming SMS in real time,  
    - displaying current time,  
    - displying who send SMS.  

# SmartWatch:

## Hardware:
- STM32F4,
- ePaper display 1,54",
- Bluettoth module 2.0, HC-06.

## Setup:

### Pin connections:

  - Screen:  
      CS >> PB6  
      RST >> PA9  
      BUSY >> PA8  
      DC >> PC7  
      DIN >> PA7  
      CLK >> PA5  
      GND >> GND  
      3.3V >> Vcc  

  - Bluetooth  
      RxD >> PB10  
      TxD >> PB11  
      
### How to start:
  Download repository, folder Smart_Watch install on your Android device.  
  In folder Display there's a project in C that should be Build and Compiled in Eclipse Java Oxygen environment, then pushed on STM.
  
### How it works:
  Phone app sends communicates with flags in every message. Program on STM reads them and displays on specific place on the screen.

  
# Application

## Software
-Android Studio

## What I used?

I used Bluetooth connection by the sockets to connect to Bluetooth Module HC-06 and BroadcastReceiver to receive SMS. Time is reading off and sending in new Thread.

## Setup 

### How to usee:
  In the beggining you must give permissions to receive SMS. Then you must just click on button "Start". It will start connection with 
  microcontroller STM-32. Time is sended in next minute. You can also send your own data by typing text into EditText and click button "wyslij". 

      



  
