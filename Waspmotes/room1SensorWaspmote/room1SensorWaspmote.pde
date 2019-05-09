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

 packetXBee* paq_sent;
 int8_t state=0;
 long previous=0;
 long previouss=0;
 char* content="";
 char dataSens[160];
 char* data="Response from Router! \r\n";
 char* data2="";
 uint8_t destination[8];
 int sol;
 int reg = 0;
 int regGroup = 0;
 int ab = 0;
 int regWasp = 0;
 uint8_t sent = 0;
   
 long timeout = 4000; // for 10 seconds
  
 char * initial = "room1SensorWaspmote##iMuseumI##sensors";
 char * sender = "room1SensorWaspmote";
 char * receiver = "";
 int send = 0;
 
char sTem[12];
char sLum[12];
char sPir[12];

void setup()
{
  USB.begin();
  USB.println(freeMemory());
  
  ///////////////////////////////////////////
  //
  // INITIALIZE WIFI
  //
  ///////////////////////////////////////////
  // First, initialize the WIFI API and the connections with the waspmote.
  //WIFI.begin();
  // Then switch on the WIFI module.
  WIFI.ON(socket1);
  // If we don't know what configuration has the modules, reset it.
  WIFI.resetValues();
  
  // 1. Configure the transport protocol (UDP, TCP, FTP, HTTP...)
  WIFI.setConnectionOptions(CLIENT);
  // 2. Configure the way the modules will resolve the IP address.
  WIFI.setDHCPoptions(DHCP_ON);
  
  Utils.setLED(LED0,LED_OFF);
  Utils.setLED(LED1,LED_OFF);
  
  // Connection to AndroidAP
  // 3. Configure how to connect the AP (encryptation and name of the AP).
  WIFI.setAutojoinAuth(WPA1);
  WIFI.setAuthKey(WPA1,"androidap");
  WIFI.join("AndroidAP");
  Utils.setLED(LED1,LED_ON);
  // 4. Call the function to create a TCP connection to IP address
  WIFI.setTCPclient("150.214.108.58", 8080,2000);
  
  // Connection to Sol
  // 3. Configure how to connect the AP (encryptation and name of the AP).
  //WIFI.setAutojoinAuth(OPEN);
  // 4. Call the function to create a TCP connection to IP address
  //WIFI.setTCPclient("150.214.108.58", 8080,2000); //150.214.108.58 es la IP pública donde se encuentra Sol.
  ///////////////////////////////////////////
  
  SensorEvent.setBoardMode(SENS_ON);
  
  delay(5000);
}

void loop()
{        
  USB.println("Router enters loop");
  
  if(send == 0){
    sprintf(content,"@|HOST|%s|RegisterAgent|REG_ONTOLOGY|room1SensorWaspmote\n",initial);
    WIFI.send(content);
    send = 1;
  }
  
  Utils.blinkLED(LED1,500);
  
  WIFI.readAur(NOBLO);
  if(Utils.substring(WIFI.answer,"La conexion ha sido registrada correctamente")){
    send = 1;
  }else if(Utils.substring(WIFI.answer,"La conexion no pudo ser registrada correctamente")){
    send = 1;
  }else if(Utils.substring(WIFI.answer,"GetSensorsRoom1")){
        float temperature;
        float luminosity;
        float pir;
        
        temperature = SensorEvent.readValue(SENS_SOCKET5);
        Utils.float2String(((temperature-0.5)*100),sTem,2);
        USB.println("temperature:");
        USB.println(sTem);
        
        luminosity = SensorEvent.readValue(SENS_SOCKET6);
        Utils.float2String(((luminosity*100)/3.3),sLum,2);
        USB.println("luminiosity:");
        USB.println(sLum);
        
        pir = SensorEvent.readValue(SENS_SOCKET7);
        Utils.float2String(pir,sPir,2);
        USB.println("PIR:");
        USB.println(sPir);
        
        sprintf(content,"@|security|%s&%s&%s&%d|RoomoneProtocol|SensorOntology|room1SensorWaspmote\n",sTem,sLum,sPir,PWR.getBatteryLevel());
        WIFI.send(content);
  }
  
  delay(4000);
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
