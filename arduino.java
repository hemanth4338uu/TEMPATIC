#include <OneWire.h>
#include <DallasTemperature.h>

OneWire oneWire(2); // Replace with your sensor pin
DallasTemperature sensors(&oneWire);

const int relayPin = 7; // Replace with your relay pin
const float targetTemperature = 25.0; // Set your desired temperature here

void setup() {
  sensors.begin();
  pinMode(relayPin, OUTPUT);
}

void loop() {
  sensors.requestTemperatures();
  float currentTemperature = sensors.getTempCByIndex(0);

  if (currentTemperature < targetTemperature) {
    digitalWrite(relayPin, HIGH); // Turn on heater or cooler
  } else {
    digitalWrite(relayPin, LOW); // Turn off heater or cooler
  }

  delay(1000); // Delay for reading temperature periodically
}
