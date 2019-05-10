// Global variables
float audio_val;
float temp_val;
float ultra_val;
float ldr_val;
float hall_val;

char audio [7];
char temp [7];
char ultra [7];
char ldr [7];
char hall [7];

//char db[10];

uint8_t panid[8] = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
char * content;
packetXBee* paq_sent;
int previous=0;
int previouss=0;
int previous3=0;
char * initial = "room2SensorWaspmote";
char aux[120];
int sent=0;
uint8_t reg=0;
uint8_t ready=0;
char dataSens[200];
char solProtocol[160];
//char data[200];
uint8_t destination[8];
uint8_t i=0;

int8_t state=0;
char*  data="Challenge from End Device!\r\n";
char * bat;
//gateway mac address:0013A2004081D390

void setup()
{
  // setup for Serial port over USB
  USB.begin();
  USB.println("USB port started...");
  // Inits the XBee ZigBee library
  xbeeZB.init(ZIGBEE,FREQ2_4G,NORMAL);
  //xbeeZB.init(ZIGBEE,FREQ2_4G,NORMAL,UART1);
  
  // Powers XBee
  xbeeZB.ON();
  // it is supposed that XBee SM=1 (Pin hibernate)
  xbeeZB.wake();
  
  if(!xbeeZB.setPAN(panid)) USB.println("setPAN ok");
  else USB.println("setPAN error");

  if(!xbeeZB.setScanningChannels(0xFF,0xFF)) USB.println("setScanningChannels ok");
  else USB.println("setScanningChannels error");

  if(!xbeeZB.setDurationEnergyChannels(3)) USB.println("setDurationEnergyChannels ok");
  else USB.println("setDurationEnergyChannels error");

  if(!xbeeZB.getAssociationIndication()) USB.println("getAssociationIndication ok");
  else USB.println("getAssociationIndication error");

  xbeeZB.writeValues();
 
  // wait until XBee module is associated
  while(xbeeZB.associationIndication != 0)
  {
    USB.println("\n\n-----> not associated <----------");
    USB.print("operatingPAN: ");
    USB.print(xbeeZB.operatingPAN[0],HEX);
    USB.println(xbeeZB.operatingPAN[1],HEX);

    USB.print("extendedPAN: ");
    USB.print(xbeeZB.extendedPAN[0],HEX);
    USB.print(xbeeZB.extendedPAN[1],HEX);
    USB.print(xbeeZB.extendedPAN[2],HEX);
    USB.print(xbeeZB.extendedPAN[3],HEX);
    USB.print(xbeeZB.extendedPAN[4],HEX);
    USB.print(xbeeZB.extendedPAN[5],HEX);
    USB.print(xbeeZB.extendedPAN[6],HEX);
    USB.println(xbeeZB.extendedPAN[7],HEX);

    USB.print("channel: ");
    USB.println(xbeeZB.channel,HEX);
         
    delay(5000);
    xbeeZB.getAssociationIndication();
    printAssociationState();
  }

  reg = 0;
  
  // Starting Radiation Board
  SensorCities.setBoardMode(SENS_ON);
  
  SensorCities.setSensorMode(SENS_ON, SENS_CITIES_AUDIO);

  SensorCities.setSensorMode(SENS_ON, SENS_CITIES_TEMPERATURE);
 
  SensorCities.setSensorMode(SENS_ON, SENS_CITIES_ULTRASOUND);
  
  SensorCities.setSensorMode(SENS_ON, SENS_CITIES_LDR);
  
  SensorCities.setSensorMode(SENS_ON, SENS_CITIES_HE);
  
  delay(2000);
}

void loop()
{
  // Set params to send
  //XBee.println("End Device enters loop");
  USB.println("End Device enters loop");
  xbeeZB.getOperatingPAN();
  xbeeZB.getExtendedPAN();
  xbeeZB.getChannel();

  USB.print("operatingPAN: ");
  USB.print(xbeeZB.operatingPAN[0],HEX);
  USB.println(xbeeZB.operatingPAN[1],HEX);

  USB.print("extendedPAN: ");
  USB.print(xbeeZB.extendedPAN[0],HEX);
  USB.print(xbeeZB.extendedPAN[1],HEX);
  USB.print(xbeeZB.extendedPAN[2],HEX);
  USB.print(xbeeZB.extendedPAN[3],HEX);
  USB.print(xbeeZB.extendedPAN[4],HEX);
  USB.print(xbeeZB.extendedPAN[5],HEX);
  USB.print(xbeeZB.extendedPAN[6],HEX);
  USB.println(xbeeZB.extendedPAN[7],HEX);

  USB.print("channel: ");
  USB.println(xbeeZB.channel,HEX);
  
  USB.println("Measuring audio levels");
  audio_val = SensorCities.readValue(SENS_CITIES_AUDIO);
  USB.print("Audio[dBSPLA]: ");
  USB.print(audio_val);
  //sprintf(db," (%i dB) ",audio_val*26); //1dBSPLA = 26dB
  //USB.print(db);
  if(audio_val>=50 && audio_val<=70){
    USB.println(" -Normal-");
  }else if(audio_val>70 && audio_val<90){
    USB.println(" -Medium-");
  }else if(audio_val>=90){
    USB.println(" -High-"); 
  }
  //audio = ftoa(audio, audio_val,3);
  float2string(audio_val,audio,2);
  USB.println();
  
  USB.println("Measuring temperature levels");
  temp_val = SensorCities.readValue(SENS_CITIES_TEMPERATURE);
  USB.print("Temperature[C]: ");
  USB.println(temp_val);
  //temp = ftoa(temp, temp_val,3);
  float2string(temp_val,temp,2); 
  USB.println(); 
  
  USB.println("Measuring presence levels");
  ultra_val = SensorCities.readValue(SENS_CITIES_ULTRASOUND);
  USB.print("Presence: ");
  USB.println(ultra_val);
  //ultra = ftoa(ultra, ultra_val,3);
  float2string(ultra_val,ultra,3);
  USB.println();
  
  USB.println("Measuring luminosity levels");
  ldr_val = SensorCities.readValue(SENS_CITIES_LDR);
  USB.print("Luminosity[Lux]: ");
  USB.println(ldr_val);
  //ldr = ftoa(ldr, ldr_val,3);
  float2string(((ldr_val*100)/3.3),ldr,2);
  USB.println();

  USB.println("Measuring Hall Effect");
  hall_val = SensorCities.readValue(SENS_CITIES_HE);
  USB.print("Hall Effect: ");
  if(hall_val > 0){
    USB.println("Closed");
  }else{
    USB.println("Opened");
  }
  //hall = ftoa(hall, hall_val,3);
  float2string(hall_val,hall,2);
  USB.println();
  
  delay(1000);
  USB.println(PWR.getBatteryLevel(),DEC);
  sprintf(data,"%s$%s&%s&%s&%s&%s&%d!\r\n",initial,audio,temp,ldr,ultra,hall,PWR.getBatteryLevel());
  USB.println(data);
  //sprintf(dataSens,"@|securityAgent|SPANISH|%s&%s&%s&0&%s|15|UTF-8|PERF|GroupProtocol|SendMessage|sensorWaspmote",audio,temp,ldr,hall);
  
  if(reg==0){
    paq_sent=(packetXBee*) calloc(1,sizeof(packetXBee)); 
    paq_sent->mode=UNICAST;
    paq_sent->MY_known=0;
    paq_sent->packetID=0x52;
    paq_sent->opt=0;
    xbeeZB.hops=0;
    xbeeZB.setOriginParams(paq_sent, "5678", MY_TYPE);
    xbeeZB.setDestinationParams(paq_sent, "0013A2004081D390", data, MAC_TYPE,DATA_ABSOLUTE);
    xbeeZB.sendXBee(paq_sent);
    USB.print("start printing xbeeZB.error_TX:");
    USB.println(xbeeZB.error_TX);// print xbeeZB.error_TX
    if( !xbeeZB.error_TX )
    {
      //XBee.println("ok");
      USB.println("End device sends out a challenge ok");
    }
    else USB.println("challenge transmission error\n\n");
    free(paq_sent);
    paq_sent=NULL;
    
    // Waiting the answer
    /*previous=millis();
    while( (millis()-previous) < 5000 )
    {
      if( XBee2.available() )//if( XBee.available() )
      {
        xbeeZB.treatData();
        USB.println("waiting for the reply and printing xbeeZB.error_RX:");
        USB.println(xbeeZB.error_RX);// print xbeeZB.error_RX
        if( !xbeeZB.error_RX ) 
        {
          // Writing the parameters of the packet received
          while(xbeeZB.pos>0)
          {
            USB.println("Enter xbeeZB.pos ");
            USB.print("MAC Address Source: ");        
            for(int b=0;b<4;b++)
            {
              //XBee.print(xbeeZB.packet_finished[xbeeZB.pos-1]->macSH[b],HEX);
              USB.print(xbeeZB.packet_finished[xbeeZB.pos-1]->macSH[b],HEX);
            }
            for(int c=0;c<4;c++)
            {
              //XBee.print(xbeeZB.packet_finished[xbeeZB.pos-1]->macSL[c],HEX);
              USB.print(xbeeZB.packet_finished[xbeeZB.pos-1]->macSL[c],HEX);
            }
            USB.println("");
            USB.print("MAC Address Origin: ");            
            for(int d=0;d<4;d++)
            {
              //XBee.print(xbeeZB.packet_finished[xbeeZB.pos-1]->macOH[d],HEX);
              USB.print(xbeeZB.packet_finished[xbeeZB.pos-1]->macOH[d],HEX);
            }
            for(int e=0;e<4;e++)
            {
              //XBee.print(xbeeZB.packet_finished[xbeeZB.pos-1]->macOL[e],HEX);
              USB.print(xbeeZB.packet_finished[xbeeZB.pos-1]->macOL[e],HEX);
            }
            USB.println("");
            USB.print("Data: ");          
            for(int f=0;f<xbeeZB.packet_finished[xbeeZB.pos-1]->data_length;f++)
            {
              //XBee.print(xbeeZB.packet_finished[xbeeZB.pos-1]->data[f],BYTE);
              USB.print(xbeeZB.packet_finished[xbeeZB.pos-1]->data[f],BYTE);
            }
            //XBee.println("");
            USB.println("");
            free(xbeeZB.packet_finished[xbeeZB.pos-1]);
            xbeeZB.packet_finished[xbeeZB.pos-1]=NULL;
            xbeeZB.pos--;
          }
          previous=millis();
        }
      }
    }*/
  }
  
  delay(2000);
}

void float2string(float f, char* c, uint8_t prec)
{
  int p = f;
  f -= p;
  while (prec > 0) 
  {
    f *= 10;
    prec--;
  }
  int q = f;
  sprintf(c, "%i.%i\0",p,q);
}  

void printAssociationState()
{
  switch(xbeeZB.associationIndication)
  {
    case 0x00  :  USB.println("Successfully formed or joined a network");
                  break;
    case 0x21  :  USB.println("Scan found no PANs");
                  break;   
    case 0x22  :  USB.println("Scan found no valid PANs based on current SC and ID settings");
                  break;   
    case 0x23  :  USB.println("Valid Coordinator or Routers found, but they are not allowing joining (NJ expired)");
                  break;   
    case 0x24  :  USB.println("No joinable beacons were found");
                  break;   
    case 0x25  :  USB.println("Unexpected state, node should not be attempting to join at this time");
                  break;
    case 0x27  :  USB.println("Node Joining attempt failed");
                  break;
    case 0x2A  :  USB.println("Coordinator Start attempt failed");
                  break;
    case 0x2B  :  USB.println("Checking for an existing coordinator");
                  break;
    case 0x2C  :  USB.println("Attempt to leave the network failed");
                  break;
    case 0xAB  :  USB.println("Attempted to join a device that did not respond.");
                  break;
    case 0xAC  :  USB.println("Secure join error  :  network security key received unsecured");
                  break;
    case 0xAD  :  USB.println("Secure join error  :  network security key not received");
                  break;
    case 0xAF  :  USB.println("Secure join error  :  joining device does not have the right preconfigured link key");
                  break;
    case 0xFF  :  USB.println("Scanning for a ZigBee network (routers and end devices)");
                  break;
    default    :  USB.println("Unkown associationIndication");
                  break;
  }
}

