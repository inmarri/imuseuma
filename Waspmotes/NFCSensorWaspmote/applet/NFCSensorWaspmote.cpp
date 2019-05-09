#include "WProgram.h"
void setup();
void loop();
packetXBee* paq_sent;
int8_t state=0;
long previous=0;
char* initial = "NFCSensorWaspmote";
char* content = "";
uint8_t panid[8] = { 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

// number of possible cards
const int nCards = 9;
                    
// stores the 16 bytes data read from a block:
blockData readData;
// stores the key or password:
uint8_t keyAccess[] = {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF };
// stores the block address:
uint8_t address = 0x04;
// block address with the info:
uint8_t addressData = 0x07;
// stores the UID (unique identifier) of a card:
CardIdentifier CardID;
// stores the answer to request:
uint8_t ATQ[2];
// stores auxiliar information from the card:
uint8_t aux[16];

// vector with the UIDs of all the cards
uint8_t vCards [nCards*4] = {0x2E, 0xA2, 0xEB, 0xEB, 
0x4E, 0x74, 0xEC, 0xEB, 
0xDE, 0x97, 0xE9, 0xEB, 
0x1E, 0x76, 0xE9, 0xEB, 
0x82, 0x8C, 0x29, 0x13, 
0x72, 0xE6, 0x8E, 0x5D, 
0x12, 0x57, 0x2A, 0x13, 
0xD2, 0x16, 0x92, 0x5D, 
0x5D, 0x79, 0x9E, 0x77};

int card = -1; // stores the index of the present card

// define if the agent is coming in or going out. 0 is ready to enter, 1 is ready to exit.
uint8_t ioCards [nCards];


void setup()
{
  // switchs on the RFID/NFC @ 13.56 MHz module, type B, and asigns the socket
  RFID13.ON(SOCKET1, B);
  delay(1000);
 
  USB.begin();
  
  USB.print("Battery Level: ");
  USB.print(PWR.getBatteryLevel(),DEC);
  
  USB.print("\n");
  USB.println("RFID/NFC @ 13.56 MHz module started");
  
  
  for(int i=0; i<nCards; i++){
     ioCards[i] = 0; 
  }
  
  // Inits the XBee ZigBee library  
  xbeeZB.init(ZIGBEE,FREQ2_4G,NORMAL);

  // it is supposed that XBee SM=1 (Pin hibernate)
  xbeeZB.wake();
  delay(2000);
}



void loop()
{
  USB.print("\n");
  USB.println("Ready to read...");
  
    
  // get the UID
  RFID13.init(CardID, ATQ);
  USB.print("\n");
  USB.print("the Card UID: ");
  RFID13.print(CardID, 4);
  
  card = RFID13.searchUID(vCards, CardID, nCards); // looks for the read card inside the data base. If so, it returns its index in the vector. If not, -1.
  
  if(card == -1){
     USB.print("\nUnauthorized personnel ! -> UID: ");
     RFID13.print(CardID,4);
     Utils.blinkLEDs(2000);
  }else{
    // authenticate block number 4 with its access key (2nd sector)
    if (!RFID13.authenticate(CardID, address, keyAccess))
    {
      USB.print("\n");
      USB.println("Authentication failed");
      Utils.setLED(LED0,LED_ON);
    }
    else  // success
    {
      USB.print("\n");
      USB.println("Authentication OK");
      
      if(!ioCards[card]){
        USB.print("GuideAgent with card ");
        USB.print(card+1);
        USB.println(" entering in the Museum");
        sprintf(content,"%s$GuideAgent with card %i entering in the room 2&%d!",initial,card+1,PWR.getBatteryLevel());
        ioCards[card] = 1;
      }else{
        USB.print("GuideAgent with card ");
        USB.print(card+1);
        USB.println(" going out from the Museum");
        sprintf(content,"%s$GuideAgent with card %i going out from the room 2&%d!",initial,card+1,PWR.getBatteryLevel());
        ioCards[card] = 0;
      }
      Utils.setLED(LED1,LED_ON);
    }
    delay(4000); // wait some time each loop
    Utils.setLED(LED0,LED_OFF);
    Utils.setLED(LED1,LED_OFF);
  }
  
    // Powers XBee
    xbeeZB.ON();
  
      //Send the info via ZigBee to Meshlium to resend it via WiFi to Sol
      paq_sent=(packetXBee*) calloc(1,sizeof(packetXBee)); 
      paq_sent->mode=UNICAST;
      paq_sent->MY_known=0;
      paq_sent->packetID=0x52;
      paq_sent->opt=0; 
      xbeeZB.hops=0;
      xbeeZB.setOriginParams(paq_sent, "5678", MY_TYPE);
      xbeeZB.setDestinationParams(paq_sent, "0013A2004081D390", content, MAC_TYPE,DATA_ABSOLUTE); //router mac address: 0013A20040779FE7
      xbeeZB.sendXBee(paq_sent);
      
      USB.print("\n");
      USB.print("start printing xbeeZB.error_TX:");
      USB.println(xbeeZB.error_TX);
      if( !xbeeZB.error_TX )
      {
        //XBee.println("ok");
        USB.println("NFC device sends out the info");
      }
      else USB.println("Transmission error\n\n");
      
      free(paq_sent);
      paq_sent=NULL;
  
  delay(2000);
  
  USB.print("\n");
}

int main(void)
{
	init();

	setup();
    
	for (;;)
		loop();
        
	return 0;
}

