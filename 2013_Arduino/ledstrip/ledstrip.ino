#include "LPD8806.h"
#include "SPI.h"

// Number of RGB LEDs in strand:
int nLEDs = 64;

// Chose 2 pins for output; can be any valid output pins:
int dataPin  = 2;
int clockPin = 3;
int pwmInput1 = 4;
int pwmInput2 = 5;

int df_length1 = 0;
int df_length2 = 0;

LPD8806 strip = LPD8806(nLEDs, dataPin, clockPin);

void setup() {
  // Start up the LED strip
  strip.begin();

  // Update the strip, to start they are all 'off'
  strip.show();
  
  // Serial for debugging
  Serial.begin(9600);
  
  pinMode(pwmInput1, INPUT);
  pinMode(pwmInput2, INPUT);
}

void loop() {
  df_length1 = pulseIn(pwmInput1);
  df_length2 = pulseIn(pwnInput2);
  
  serial.print(df_length1);
  serial.print(df_length2);
  //chaseFade(4, 24 , 30, strip.Color(127, 0, 0));
//  clearPixels();
     rainbowCycle(0);
//  chase(6, 50, strip.Color(0, 0, 127));
//  chaseDouble(6, 50, strip.Color(0, 127, 0), strip.Color(127, 0, 0));
//  fill(8, strip.Color(127, 0, 0));
//  doubleFill(6, 4, strip.Color(0, 127, 0), strip.Color(0, 0, 127));
//  for (int i = 0; i <= 16; i++) {
//    clearPixels();
//    doubleFill(i, i, strip.Color(0, 127, 0), strip.Color(0, 0, 127));
//    delay(50);
//  }
//  for (int i = 0; i <= 32; i++) {
//    clearPixels();
//    fill(i, strip.Color(0, 127, 0));
//    delay(50);
//  }
//  for (int i = 0; i < 10; i++) {
//    tj(50);
//  }
//  clearPixels();
}

void clearPixels(){
  for (int i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, 0);
  }
}

void chase(int chaseLength, int wait, uint32_t color) {
  for (int i = 0; i < strip.numPixels(); i++) {
    // i is the last pixel in the chase, and we turn it off this cycle
    // we have to turn off p_old, which is the end of the chase
    // and turn on p_new, which is the beginning of the chase
    int p_old = i % strip.numPixels();
    int p_new = (i + chaseLength) % strip.numPixels();
    strip.setPixelColor(p_old, 0);
    strip.setPixelColor(p_new, color);
    strip.show();
    delay(wait);
  }
}

void chaseFade(int chaseLength, int fadeLength, int wait, uint32_t color) {
  int pixel;
  for (int i = 0; i < strip.numPixels(); i++) {
    // i is the last pixel in the chase
    // we need to fade off the old pixels according to fadelength
    for (int j = 0; j < fadeLength; j++) {
      float fadeFactor = 1 - ((float)j / fadeLength);
      pixel = (i - j) % strip.numPixels();
      strip.setPixelColor(pixel, scaleColor(color, fadeFactor));
    }
    
    // Clear the last pixel
    pixel = (i - fadeLength) % strip.numPixels();
    strip.setPixelColor(pixel, 0);
    
    // Set the new first pixel in the chase to be maximum color
    pixel = (i + chaseLength) % strip.numPixels();
    strip.setPixelColor(pixel, color);
    strip.show();
    delay(wait);
  }
}

// Not working yet
void chaseBounce(int chaseLength, int wait, uint32_t color) {
  for (int i = 0; i < (strip.numPixels() * 2); i++) {
    int p_old = i;
    int p_new = i + chaseLength;
    strip.setPixelColor(p_old, 0);
    strip.setPixelColor(p_new, color);
    strip.show();
    delay(wait);
  }
}

void chaseDoubleColor(int chaseLength, int wait, uint32_t color1, uint32_t color2) {
  uint32_t color = 0;
  for (int i = 0; i < strip.numPixels(); i++) {
    // Alternate between the two colors
    if ((i % 2) == 0) {
      color = color1;
    }
    else {
      color = color2;
    }
    // we have to turn off p_old, which is the end of the chase
    // and turn on p_new, which is the beginning of the chase
    int p_old = i % strip.numPixels();
    int p_new = (i + chaseLength) % strip.numPixels();
    strip.setPixelColor(p_old, 0);
    strip.setPixelColor(p_new, color);
    strip.show();
    delay(wait);
  }
}

void randomColor(int wait) {
  uint32_t color = 0;
  
  for (int i = 0; i < strip.numPixels(); i++) {
    int r = random(128);
    int g = random(128);
    int b = random(128);
    strip.setPixelColor(i, strip.Color(r, g, b));
    // get random color {red, yellow, orange, blue, white}
  }
  
  strip.show();
  delay(wait);
}

void tj(int wait) {
  uint32_t color = 0;
  
  for (int i = 0; i < strip.numPixels(); i++) {
    int c = random(1, 8);
    if(c==5) { c = 4; }
    int r = 64  * (c>>2 & 1);
    int g = 64 * (c>>1 & 1);
    int b = 64 * (c & 1);
    strip.setPixelColor(i, strip.Color(r, g, b));
    // get random color {red, yellow, orange, blue, white}
  }
  
  strip.show();
  delay(wait);
}

void fill(int length, uint32_t color) {
  // Limit the length of the fill to be the length of the strip
  if (length > strip.numPixels()) {
    length = strip.numPixels();
  }
  
  // Set each pixel in the length to be the desired color
  for (int i = 0; i < length; i++) {
    strip.setPixelColor(i, color);
  }
  
  // Update the strip
  strip.show();
}

// Two fill lines, one starting from each end of the strip.
// Neither fill may go beyond halfway
void doubleFill(int length1, int length2, uint32_t color1, uint32_t color2) {
  // Limit both lengths to be half of the strip length
  if (length1 > (strip.numPixels() / 2)) {
    length1 = strip.numPixels() / 2;
  }
  if (length2 > (strip.numPixels() / 2)) {
    length2 = strip.numPixels() / 2;
  }
  
  // We determine the LED end positions of each fill
  // For the first, it's just the length
  int endPosition1 = length1;
  // For the second, it's coming from the other end so it's the strip length - fill length
  // We then subtract 1 because strip length is 0-indexed and fill length is 1-indexed
  // or something, it works
  int endPosition2 = strip.numPixels() - length2 -1;
  
  // Set each pixel in each length to be the desired color
  for (int i = 0; i < endPosition1; i++) {
    strip.setPixelColor(i, color1);
  }
  for (int j = strip.numPixels(); j > endPosition2; j--) {
    strip.setPixelColor(j, color2);
  }
  
  // Update the strip
  strip.show();
}

uint32_t scaleColor(uint32_t color, float scale) {
  int g = (color >> 16) & 0x7f;
  int r = (color >> 8)  & 0x7f;
  int b = color         & 0x7f;
  return strip.Color(r * scale, g * scale, b * scale);
}

// (copied from strandtest)
// Slightly different, this one makes the rainbow wheel equally distributed 
// along the chain
void rainbowCycle(uint8_t wait) {
  uint16_t i, j;
  
  for (j=0; j < 384 * 5; j++) {     // 5 cycles of all 384 colors in the wheel
    for (i=0; i < strip.numPixels(); i++) {
      // tricky math! we use each pixel as a fraction of the full 384-color wheel
      // (thats the i / strip.numPixels() part)
      // Then add in j which makes the colors go around per pixel
      // the % 384 is to make the wheel cycle around
      strip.setPixelColor(i, Wheel( ((i * 384 / strip.numPixels()) + j) % 384) );
    }  
    strip.show();   // write all the pixels out
    delay(wait);
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
      r = 127 - WheelPos % 128;   //Red down
      g = WheelPos % 128;      // Green up
      b = 0;                  //blue off
      break; 
    case 1:
      g = 127 - WheelPos % 128;  //green down
      b = WheelPos % 128;      //blue up
      r = 0;                  //red off
      break; 
    case 2:
      b = 127 - WheelPos % 128;  //blue down 
      r = WheelPos % 128;      //red up
      g = 0;                  //green off
      break; 
  }
  return(strip.Color(r,g,b));
}
