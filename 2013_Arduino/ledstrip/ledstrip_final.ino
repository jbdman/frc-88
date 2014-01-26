#include "LPD8806.h"
#include "SPI.h"

// Number of RGB LEDs in strand (this happens to be the same for
// all the left and right strands, logo is different)
#define nLEDs 22
#define nLEDsLogo 28

// Various data pins for the strands' data and clock
//int dataPin  = 2;
//int clockPin = 3;
#define leftClockPin 8
#define leftDataPin 11
#define rightClockPin 9
#define rightDataPin 12
#define logoClockPin 10
#define logoDataPin 13


// Input pins that together make up a 4 bit number to pass in mode
#define modeInputPin1 2
#define modeInputPin2 3
#define modeInputPin3 4
#define modeInputPin4 5

// Input pins that analog signals are sent to via PWM
#define pwmInputPinLeft 6
#define pwmInputPinRight 7

// This variable stores the position along the strips for effects
// such as chase and rainbow
int ledPosition = 0;

// This is an array that when run through plays some effect on the
// logo strand.  The first index is the position in the effect, and
// the second index is the LED to address.
boolean logoEffectTest[][nLEDsLogo] = {
 //1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}};
  
boolean logoEffectNumberLeft[][nLEDsLogo] = {
 //1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0}, // 0
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 1
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0}, // 2
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0}, // 3
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0}, // 4
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0}, // 5
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}, // 6
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}, // 7
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}, // 8
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0}, // 9
  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}, // 10 (A)
  {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0}};// 11 (G)

boolean logoEffectNumberRight[][nLEDsLogo] = {
 //1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28
  {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 0
  {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 1
  {0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 2
  {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 3
  {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 4
  {0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 5
  {0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 6
  {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 7
  {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 8
  {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 9
  {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 10 (A)
  {0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};// 11 (G)

// Strip objects
LPD8806 leftStrip = LPD8806(nLEDs, leftDataPin, leftClockPin);
LPD8806 rightStrip = LPD8806(nLEDs, rightDataPin, rightClockPin);
LPD8806 logoStrip = LPD8806(nLEDsLogo, logoDataPin, logoClockPin);

void setup() {
  // Start up the LED strips
  leftStrip.begin();
  rightStrip.begin();
  logoStrip.begin();

  // Update the strip, to start they are all 'off'
  leftStrip.show();
  rightStrip.show();
  logoStrip.show();
  
  // Serial for debugging
//  Serial.begin(9600);

  // Set the input pins to be, well, inputs
  // We also set the pins to HIGH.  While in input modes, this turns on the
  // internal 20k pullup resistors.
  pinMode(modeInputPin1, INPUT);
  digitalWrite(modeInputPin1, HIGH);
  pinMode(modeInputPin2, INPUT);
  digitalWrite(modeInputPin2, HIGH);
  pinMode(modeInputPin3, INPUT);
  digitalWrite(modeInputPin3, HIGH);
  pinMode(modeInputPin4, INPUT);
  digitalWrite(modeInputPin4, HIGH);
  
  pinMode(pwmInputPinLeft, INPUT);
  pinMode(pwmInputPinRight, INPUT);
  
//  cycleLogoNums(Color(0, 127, 0), 500);
  cycleLogo88(Color(127, 0, 0), 100, true);
//  for (int i = 0; i < 14; i++) {
//    logoEffect(logoEffectTest, 14, 3, Color(127, 0, 0), i, true);
//    logoStrip.show();
//    delay(100);
//  }
//  for (int i = 0; i < 14; i++) {
//    logoEffect(logoEffectTest, 14, 3, Color(0, 127, 0), i, true); 
//    logoStrip.show();
//    delay(100);
//  }
//  for (int i = 0; i < 14; i++) {  
//    logoEffect(logoEffectTest, 14, 3, Color(0, 0, 127), i, true);
//    logoStrip.show();
//    delay(100);
//  }
}

void loop() {
  // Get the PWM inputs from the respective pins
  // a -1.0 PWM from the cRIO has a pulse width of 0.68 ms (680 us)
  // a 1.0 PWM from the cRIO has a pulse width of 2.29 ms (2290 us)
  // Because these functions hold up the program, we limit them to 
  // timeout at 25 ms (25000 us) because the signal is a 50mHz
  // just in case the input doesn't happen, we don't sit around for 3 minutes
  int pwmInputLeft = pulseIn(pwmInputPinLeft, HIGH, 25000);
  int pwmInputRight = pulseIn(pwmInputPinRight, HIGH, 25000);
  float scaledInputLeft = scaleInput(pwmInputLeft);
  float scaledInputRight = scaleInput(pwmInputRight);  
  
  // Construct the mode variable from the various input pins
  int mode = digitalRead(modeInputPin1)
           | (digitalRead(modeInputPin2) << 1)
           | (digitalRead(modeInputPin3) << 2)
           | (digitalRead(modeInputPin4) << 3);
           
  boolean logoSet = false;
  
  switch(mode) {
    case 0: // Rainbow fill
      rainbowCycle(&leftStrip);
      rainbowCycle(&rightStrip);
      break;
    case 1: // Blue alliance chase                                    
      // Left blue, right yellow
      chaseFade(&leftStrip, 2, 20, Color(0, 0, 127));
      chaseFade(&rightStrip, 2, 20, Color(0, 0, 127));
      // Chasing on the logo is weird and not implemented yet, so we just set it solid
      fill(&logoStrip, nLEDsLogo, Color(0, 0, 127));
      logoSet = true;
      break;
    case 2: // Red alliance chase
      chaseFade(&leftStrip, 2, 16, Color(127, 0, 0));
      chaseFade(&rightStrip, 2, 16, Color(127, 0, 0));
      // Chasing on the logo is weird and not implemented yet, so we just set it solid
      fill(&logoStrip, nLEDsLogo, Color(127, 0, 0));
      logoSet = true;
      break;
    case 3: // Drive dependent fill
      uint32_t color;
      
      if (scaledInputLeft < 0) {
        // If the input is negative, set the color accordingly and then make it
        // positive because we can't pass a negative number of LEDs
        scaledInputLeft *= -1;
        color = Color(127, 0, 0);
      } else {
        // Otherwise, just set it to the forward color
        color = Color(0, 127, 0);
      }
      fill(&leftStrip, (int)(scaledInputLeft * nLEDs), color);
      
      // Do the same as we did above for the right strip
      if (scaledInputRight < 0) {
        scaledInputRight *= -1;
        color = Color(127, 0, 0);
      } else {
        color = Color(0, 127, 0);
      }
      fill(&rightStrip, (int)(scaledInputRight * nLEDs), color);
      break;
    case 4: // Climb dependent fill
      // We don't do the positive/negative correction here because we only
      // expect positive values from the PWM input
      fill(&leftStrip, (int)(scaledInputLeft * nLEDs), Color(0, 127, 0));
      fill(&rightStrip, (int)(scaledInputRight * nLEDs), Color(0, 127, 0));
      break;
    case 5: // BLINK EVERYTHING
      tj(&leftStrip);
      tj(&rightStrip);
      logoEffect(logoEffectNumberRight, 11, 0, Color(0, 0, 127), 11, true);
      logoEffect(logoEffectNumberLeft, 11, 0, Color(0, 0, 127), 10, false);
      logoSet = true;
      break;
    case 6: // Flash green.
      break;
    case 7: // Boston mode
      // Left blue, right yellow
      chaseFade(&leftStrip, 2, 20, Color(0, 0, 127));
      chaseFade(&rightStrip, 2, 20, Color(127, 127, 0));
      // Chasing on the logo is weird and not implemented yet, so we just set it solid
      fill(&logoStrip, nLEDsLogo, Color(0, 0, 127));
      logoSet = true;
      break;
    case 0xF: // No input, robot must be disabled or otherwise not sending input
      // So we just make it all rainbow and stuff
      rainbowCycle(&leftStrip);
      rainbowCycle(&rightStrip);
      break;
  }
  
  // If the logo hasn't been set by one of the modes, we set it to its default
  if (!logoSet) {
    fill(&logoStrip, nLEDsLogo, Color(0, 127, 0));
  }
  
  ledPosition++;
  
  leftStrip.show();
  rightStrip.show();
  logoStrip.show();
  
  delay(15);
}

float scaleInput(int input) {
  static int LOWER_INPUT = 650;
  static int UPPER_INPUT = 2350;
  static int MAX_RANGE = UPPER_INPUT - LOWER_INPUT;
  // First, correct the input to be in our expected range, because sometimes
  // it doesn't happen... (like after a PulseIn timeout, input will be 0)
  if (input < LOWER_INPUT) {
    input = LOWER_INPUT;
  } else if (input > UPPER_INPUT) {
    input = UPPER_INPUT;
  }
  // Reduce the input to start at 0 instead of something like 650.
  double output = input - LOWER_INPUT;
  // Normalize the input to be based equally around 0.  (ie, -850 to 850)
  output -= (MAX_RANGE / 2);
  // Scale the input to be on a range of -1 to 1.
  output /= (MAX_RANGE / 2);
  return output;
}

void clearPixels(LPD8806 *strip){
  for (int i = 0; i < strip->numPixels(); i++) {
    strip->setPixelColor(i, 0);
  }
}

void logoEffect(boolean effect[][nLEDsLogo], int effectLength, int chaseLength, uint32_t color, int effectPosition, boolean resetStrip) {
  if (resetStrip) {
    clearPixels(&logoStrip);
  }
  // Cycle through each LED in the strip
  for (int i = 0; i < nLEDsLogo; i++) {
    // If, at the current position, the LED is set to true, turn it on
    if (effect[effectPosition % effectLength][i])
    {
      logoStrip.setPixelColor(i, color);
    }
    // Chase the preceding LEDs
    for (int j = 0; j < chaseLength; j++) {
      for (int i = 0; i < nLEDsLogo; i++) {
        if (effect[(effectPosition - (j + 1)) % effectLength][i]) {
          // Calculate how much we need to fade by
          float fadeFactor = 1 - ((float)j / chaseLength);
          logoStrip.setPixelColor(i, color);
        }
      }
    }
  }
}

void logoNum(int num, uint32_t color) {
  // Seperate the num into units and tens, which correspond to the left and right
  // digits in our two digit display
  int right = 0;
  int left = 0;
  // This if-else block is probably done really poorly, but it's 1 am.
  if (num <= 0) {
    // I know this probably isn't optimal, but we don't actually do anything here.
    // We leave left and right as 0.
  } else if (num <= 9) {
    right = num % 10;
  } else if (num <= 99) {
    right = num % 10;
    left = (num / 10) % 10;
  } else {
    right = 9;
    left = 9;
  }
  
  clearPixels(&logoStrip);
  
  // Cycle through each LED in the strip
  for (int i = 0; i < nLEDsLogo; i++) {
    // We can reference the number lookup tables with simply the number because they
    // are ordered such that the number at index 0 is 0, index 1 is 1, etc
    // If this LED for this number is set to true, turn it on
    if (logoEffectNumberLeft[left][i]) {
      logoStrip.setPixelColor(i, color);
    }
    if (logoEffectNumberRight[right][i]) {
      logoStrip.setPixelColor(i, color);
    }
  }
}

void cycleLogoNums(uint32_t color, int wait) {
  // cycle 0-9 on right digit (0, 1, 2, etc)
  for (int i = 0; i < 10; i++) {
    logoNum(i, color);
    logoStrip.show();
    delay(wait);
  }
  // cycle 0-9 on the left digit (0, 10, 20, etc)
  for (int i = 0; i < 10; i++) {
    logoNum(i * 10, color);
    logoStrip.show();
    delay(wait);
  }
}

void cycleLogo88(uint32_t color, int wait, boolean startZero) {
  if (startZero) {
    for (int i = 0; i < 88; i++) {
      logoNum(i, color);
      logoStrip.show();
      delay(wait);
    }
  } else {
    for (int i = 87; i >= 0; i--) {
      logoNum(i, color);
      logoStrip.show();
      delay(wait);
    }
  }
}

void chase(LPD8806 *strip, int chaseLength, uint32_t color, boolean resetStrip) {
  if (resetStrip) {
    clearPixels(strip);
  }
  // Iterate through the length of the chase
  for (int i = 0; i < chaseLength; i++) {
    // Turn on the pixels.  The LED we address is where we last left off, 
    // plus the position we are in the chase (i), and then mod to the number of pixels.
    strip->setPixelColor((i + ledPosition) % strip->numPixels(), color);
  }
}

void chaseFade(LPD8806 *strip, int chaseLength, int fadeLength, uint32_t color) {
  clearPixels(strip);
  // Note, ledPosition is the last pixel in the chase itself, and therefore
  // it is also the pixel immediately before the fade
  // iterate through all the pixels we need for the fade chase
  for (int j = 0; j < fadeLength; j++) {
    // Calculate how much we need to fade by
    float fadeFactor = 1 - ((float)j / fadeLength);
    // Set the pixel
    strip->setPixelColor((ledPosition - j) % strip->numPixels(), 
                        scaleColor(color, fadeFactor));
  }
  // The chase itself is nothing fancy, and we can reuse the same chase function.
  chase(strip, chaseLength, color, false);
}

void chaseDoubleColor(LPD8806 *strip, int chaseLength, uint32_t color1, uint32_t color2) {
  clearPixels(strip);
  uint32_t color = 0;
  for (int i = 0; i < chaseLength; i++) {
    // Alternate between the two colors
    if ((i % 2) == 0) {
      color = color1;
    }
    else {
      color = color2;
    }
    strip->setPixelColor((i + ledPosition) % strip->numPixels(), color);
  }
}

// Sets every pixel to an entirely random color
void randomColor(LPD8806 *strip) {
  uint32_t color = 0;
  
  for (int i = 0; i < strip->numPixels(); i++) {
    int r = random(128);
    int g = random(128);
    int b = random(128);
    strip->setPixelColor(i, Color(r, g, b));
  }
}

// Sets every pixel to r, g and b to each be either totally on or off.
void tj(LPD8806 *strip) {
  uint32_t color = 0;
  for (int i = 0; i < strip->numPixels(); i++) {
    int c = random(1, 8);
    // no purple
    if(c==5) { c = 4; }
    // if white, try again (i.e. low frequency of white)
    if(c==7) { c = random(1, 8); }
    int r = 64  * (c>>2 & 1);
    int g = 64 * (c>>1 & 1);
    int b = 64 * (c & 1);
    strip->setPixelColor(i, Color(r, g, b));
  }
}

// Fills the strip to a certain length of a solid color
void fill(LPD8806 *strip, int length, uint32_t color) {
  clearPixels(strip);
  // Limit the length of the fill to be the length of the strip
  if (length > strip->numPixels()) {
    length = strip->numPixels();
  }
  
  // Set each pixel in the length to be the desired color
  for (int i = 0; i < length; i++) {
    strip->setPixelColor(i, color);
  }
}

// Two fill lines, one starting from each end of the strip.
// Neither fill may go beyond halfway
void doubleFill(LPD8806 *strip, int length1, int length2, uint32_t color1, uint32_t color2) {
  clearPixels(strip);
  // Limit both lengths to be half of the strip length
  if (length1 > (strip->numPixels() / 2)) {
    length1 = strip->numPixels() / 2;
  }
  if (length2 > (strip->numPixels() / 2)) {
    length2 = strip->numPixels() / 2;
  }
  
  // We determine the LED end positions of each fill
  // For the first, it's just the length
  int endPosition1 = length1;
  // For the second, it's coming from the other end so it's the strip length - fill length
  // We then subtract 1 because strip length is 0-indexed and fill length is 1-indexed
  // or something, it works
  int endPosition2 = strip->numPixels() - length2 -1;
  
  // Set each pixel in each length to be the desired color
  for (int i = 0; i < endPosition1; i++) {
    strip->setPixelColor(i, color1);
  }
  for (int j = strip->numPixels(); j > endPosition2; j--) {
    strip->setPixelColor(j, color2);
  }
  
  // Update the strip
  strip->show();
}

// Take a color and a scaling factor (between 0 and 1) and scale the
// brightness to it, while retaining the color.
uint32_t scaleColor(uint32_t color, float scale) {
  int g = (color >> 16) & 0x7f;
  int r = (color >> 8)  & 0x7f;
  int b = color         & 0x7f;
  return Color(r * scale, g * scale, b * scale);
}

// (copied from strandtest)
// Modified to allow interruptions, I'm not sure if it's actually going to work.
// Slightly different, this one makes the rainbow wheel equally distributed 
// along the chain
void rainbowCycle(LPD8806 *strip) {
  uint16_t i, j;
  j = ledPosition % 384;
  for (i=0; i < strip->numPixels(); i++) {
    // tricky math! we use each pixel as a fraction of the full 384-color wheel
    // (thats the i / strip->numPixels() part)
    // Then add in j which makes the colors go around per pixel
    // the % 384 is to make the wheel cycle around
    strip->setPixelColor(i, Wheel( ((i * 384 / strip->numPixels()) + j) % 384) );
  } 
}

// (copied from strandtest)
// Input a value 0 to 384 to get a color value.
// The colours are a transition r - g -b - back to r
uint32_t Wheel(uint16_t WheelPos)
{
  byte r, g, b;
  switch(WheelPos / 128)
  {
    case 0:
      r = 127 - WheelPos % 128;   // Red down
      g = WheelPos % 128;         // Green up
      b = 0;                      // Blue off
      break; 
    case 1:
      g = 127 - WheelPos % 128;   // Green down
      b = WheelPos % 128;         // Blue up
      r = 0;                      // Red off
      break; 
    case 2:
      b = 127 - WheelPos % 128;   // Blue down 
      r = WheelPos % 128;         // Red up
      g = 0;                      // Green off
      break; 
  }
  return(Color(r,g,b));
}

// (copied from LPD8806)
// Convert separate R,G,B into combined 32-bit GRB color:
uint32_t Color(byte r, byte g, byte b) {
  return ((uint32_t)(g | 0x80) << 16) |
         ((uint32_t)(r | 0x80) <<  8) |
                     b | 0x80 ;
}
