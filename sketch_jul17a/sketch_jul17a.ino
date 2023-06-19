const int in1 = 8;
const int in2 = 9;
const int in3 = 10;
const int in4 = 11;

void setup() {
  Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps
  Serial.setTimeout(10);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  idle();
}

void idle(){
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
}

void turnUp(){
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void turnDown(){
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void turnLeft(){
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void turnRight(){
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void readSerial(){
  // send data only when you receive data:
  if (Serial.available() > 0) {
    // read the incoming:
    String incoming = Serial.readString();
    
    Serial.println("Recieved : " + incoming);

    if(incoming.indexOf("LEFT") >= 0){
        turnLeft();
      }else if(incoming.indexOf("RIGHT") >= 0){
        turnRight();
      }else if(incoming.indexOf("UP") >= 0){
        turnUp();
      }else if(incoming.indexOf("DOWN") >= 0){
        turnDown();
      }else{
        idle();
      }
    
    Serial.flush(); 
  }
}

void loop() {
  readSerial();
}
