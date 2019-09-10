/*
 * Author: Supportive Wearable Inertial Sensing Hand (SWISH) Team
 * Affiliation: University of Kentucky
 * Date: X/X/2019
 * Description: Serve as the intermediate data acquisition and processing unit for kinematic data coming from flex (variable resistor sensors) and IMU(s)
 *
 * 
 * ___DEVELOPMENT NOTES and ACKNOWLEDGEMENT___
 * 
 * Original code to interface with MPU9250 from: https://github.com/suleymaneskil/waveshare_10_DOF_IMU
 *
 *
 *
 *
 *
 */

#include <MPU9250.h>
#include <Wire.h>
//SFE_BMP180 pressure;
double baseline;
int analogPinZero = A0;

// For testing flex resistor (analog) values
int analogValZero = 0;

void setup()
{
    Serial.begin(9600); // Initialize the Serial monitor interaction
    Wire.begin();  // I2C initiates communication
    write(0x6B, 0); //Power management registers default: 0
    write(0x6A, 0);  // I2C master 0x20 if you want it to be off, on
    write(0x37, 0x02); //Bypass mode on
    writeMag(0x0A, 0x12); // magnetic sensor regulator for continuous operation
}

void loop()
{
  
    int xh = readMag(0x04); // x read your direction, high byte
    int xl = readMag(0x03); // x read your direction, low byte
    int yh = readMag(0x06);
    int yl = readMag(0x05);
    int zh = readMag(0x08);
    int zl = readMag(0x07);
              readMag(0x09); //Mag module to make another measurement 
    int x = (xh << 8) | (xl & 0xff);
    int y = (yh << 8) | (yl & 0xff);
    int z = (zh << 8) | (zl & 0xff);

    // Analog voltage read
    analogValZero = analogRead(analogPinZero); // Read the input pin (A0)
    Serial.println(analogValZero); // Print the value out to serial
    
    // Serial port outputs
    if (x != -1) {
      
    }
    delay(500);
}
byte readMag(int reg)
{
    Wire.beginTransmission(0x0C);
    Wire.write(reg);
    Wire.endTransmission(false);
    Wire.requestFrom(0x0C, 1, false); // requested data
    byte val = Wire.read();
    Wire.endTransmission(true);
    return val;
}

void writeMag(int reg, int data)
{
    Wire.beginTransmission(0x0C);
    Wire.write(reg);
    Wire.write(data);
    Wire.endTransmission(true);
}
byte read(int reg)
{
    Wire.beginTransmission(0x68); // 0x68 sensor adresine veri transferi baslar
    Wire.write(reg);
    Wire.endTransmission(false);
    Wire.requestFrom(0x68, 1, false); // talep edilen data verisi
    byte val = Wire.read();
    Wire.endTransmission(true);
    return val;
}

void write(int reg, int data)
{
    Wire.beginTransmission(0x68); // starts transferring data to 0x68 sensor 
    Wire.write(reg);
    Wire.write(data);
    Wire.endTransmission(true);
}

void IMU_print(int xVal, int yVal, int zVal)
{
    Serial.print("X,Y,Z");
    Serial.println("");
    Serial.print(x);
    Serial.print(",");
    Serial.print(y);
    Serial.print(",");
    Serial.println(z);
    Serial.println("");
    Serial.println(""); 
}
